package com.zxh.testspringsecurity.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class LoginUser implements UserDetails {

    private User user;
    private List<String> permissions;

    public LoginUser(User user, List<String> permissions) {
        this.user = user;
        this.permissions = permissions;
    }
    //第一次取的时候 只处理一遍
    @JSONField(serialize = false)
    private List<GrantedAuthority> authorities;
    /**
     * 用于返回权限信息的
     * 框架会调用这个方法返回权限集合
     * @return
     */
    @Override
    public  Collection<? extends GrantedAuthority> getAuthorities() {
        if(authorities!=null){
            return authorities;
        }
        //把permissions中字符串类型的权限信息转换成GrantedAuthority对象存入authorities中
        authorities = permissions.stream().
                map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return authorities;
    }


    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }
//账号没有过期
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
//账号没有被锁
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
//密码没有过期
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
//是否可用
    @Override
    public boolean isEnabled() {
        return true;
    }
}
