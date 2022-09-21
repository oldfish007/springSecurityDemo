package com.zxh.testspringsecurity.service;

import com.zxh.testspringsecurity.common.AppProperties;
import com.zxh.testspringsecurity.domain.LoginUser;
import com.zxh.testspringsecurity.domain.ResponseResult;
import com.zxh.testspringsecurity.domain.User;
import com.zxh.testspringsecurity.response.TokenResponse;
import com.zxh.testspringsecurity.utils.JwtUtil;
import com.zxh.testspringsecurity.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private AppProperties appProperties;


    public ResponseResult login(User user) {
        //通过AuthenticationManager的authenticate方法来认证
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        //调用providerManger去认证
        Authentication authenticate = authenticationManager.authenticate(authRequest);
        //认证过以后LoginUser(user=User(id=2, userName=sangen, nickName=sangeng2, password=$2a$10$Q/PfdBjBtsxRNPO9llIcmuzGnvB.yxvuXxI8JBikCsDaM3RzEfA7m, status=0, email=466403678@qq.com, phonenumber=13438040105, sex=1, avatar=null, userType=1, createBy=null, createTime=Wed Sep 21 02:32:02 GMT+08:00 2022, updateBy=null, updateTime=Wed Sep 21 02:31:56 GMT+08:00 2022, delFlag=0))
        Optional.of(authenticate).orElseThrow(()->new UsernameNotFoundException("用户名或者密码错误"));
        //认证成功后返回jwt
        LoginUser principal = (LoginUser) authenticate.getPrincipal();
        String uid = principal.getUser().getId().toString();
        //生成JWT
        String jwt = JwtUtil.createJWT(uid);
        //用户ID作为key 存储redis
        redisCache.setCacheObject(appProperties.getRedisPre().getREDIS_LOGIN_UID()+uid,principal);
        //token响应出去 一般是要在定义一个Respondomin
        TokenResponse tokenResponse = TokenResponse.builder().accessToken(jwt).build();
        return new ResponseResult(200,"success",tokenResponse);
    }

    /**
     * logout
     * 清空redis
     */
     public ResponseResult logout(){
         //取出认证用主体
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         LoginUser loginUser = (LoginUser) authentication.getPrincipal();
         Long uid = loginUser.getUser().getId();
         redisCache.deleteObject(appProperties.getRedisPre().getREDIS_LOGIN_UID()+uid);
         return  new ResponseResult(200,"登出成功");
     }


}

