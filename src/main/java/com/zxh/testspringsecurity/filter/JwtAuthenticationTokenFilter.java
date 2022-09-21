package com.zxh.testspringsecurity.filter;

import com.zxh.testspringsecurity.common.AppProperties;
import com.zxh.testspringsecurity.domain.LoginUser;
import com.zxh.testspringsecurity.utils.JwtUtil;
import com.zxh.testspringsecurity.utils.RedisCache;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private AppProperties appProperties;
    @Autowired
    private RedisCache redisCache;
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

      //从请求中判断并校验token
       if(!checkJwtToken(httpServletRequest)){
      //没有token直接放行
           filterChain.doFilter(httpServletRequest,httpServletResponse);
           //责任链本质是递归
           return ;
       }
      //解析token 获取uid
        Optional<Claims> claims = ValidateToken(httpServletRequest);
       //TODO 取出authorities 转成集合
       String uid = claims.get().getSubject();
        //使用UID从redis中获取用户信息
        //拼接key
        String uPre = appProperties.getRedisPre().getREDIS_LOGIN_UID();
        uid = new StringBuilder(uPre).append(uid).toString();

        LoginUser loginUser = redisCache.getCacheObject(uid);
        Optional.of(loginUser).orElseThrow(()->new UsernameNotFoundException("用户未找到"));
        //获取springSecurityContextHolder存入LoginUser对象
        //todo 需要把权限集合封装到 UsernamePasswordAuthenticationToken 里面
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser,null,loginUser.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //放行
        filterChain.doFilter(httpServletRequest,httpServletResponse);

    }
//解析token
    private  Optional<Claims> ValidateToken(HttpServletRequest httpServletRequest) {

        String accessToke = httpServletRequest.getHeader(appProperties.getJwt().getHeader()).replace(appProperties.getJwt().getPrefix(),"");

        try {
            /**
             * return Jwts.parser()
             *                 .setSigningKey(secretKey)
             *                 .parseClaimsJws(jwt)
             *                 .getBody();
             */
           return  Optional.of(JwtUtil.parseJWT(accessToke));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private boolean checkJwtToken(HttpServletRequest request) {
        String authenticationHeader = request.getHeader(appProperties.getJwt().getHeader());
        return authenticationHeader!=null && authenticationHeader.startsWith(appProperties.getJwt().getPrefix());
    }

    /*public static void main(String[] args) {
        String str = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJmZmI3YzhlYjgyMWE0OTVmOTI0ZGI1NzBiNGFjNWJhZSIsInN1YiI6IjIiLCJpc3MiOiJzZyIsImlhdCI6MTY2MzcyODIyNSwiZXhwIjoxNjYzNzMxODI1fQ.DjMvxGowM6_Kgmww1Vyz_aKGSw6KR_3FBxHRDOUzSbk";
        String bearer_ = str.replace("Bearer ", "");
        System.out.println(bearer_);
    }*/
}
