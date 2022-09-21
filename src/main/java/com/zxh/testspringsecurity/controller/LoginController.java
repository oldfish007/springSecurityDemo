package com.zxh.testspringsecurity.controller;

import com.zxh.testspringsecurity.domain.ResponseResult;
import com.zxh.testspringsecurity.domain.User;
import com.zxh.testspringsecurity.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class LoginController {

    @Autowired
    private LoginService userService;

    @RequestMapping(value = "/user/login",method = RequestMethod.POST)
    public ResponseResult login(@RequestBody User user){
        //todo 请求体需要校验
        Optional.of(user).
                orElseThrow(()->new UsernameNotFoundException("用户名不存在"));
        //登录以后返回token
        return userService.login(user);
    }
    @RequestMapping("/user/logout")
    public ResponseResult logout(){
        return userService.logout();
    }

}
