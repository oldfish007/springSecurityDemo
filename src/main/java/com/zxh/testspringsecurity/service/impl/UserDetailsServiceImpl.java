package com.zxh.testspringsecurity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zxh.testspringsecurity.domain.LoginUser;
import com.zxh.testspringsecurity.domain.User;
import com.zxh.testspringsecurity.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 实现了从数据查询的一个校验工作
 * 登录用的 byUsername
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        //查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,username);
        User user = userMapper.selectOne(queryWrapper);
        Optional.of(user).orElseThrow(() -> new UsernameNotFoundException("未找到用户名"));
        //TODO  查询数据库对应的权限集合信息返回userDetails
        List<String> permissions = new ArrayList<>(Arrays.asList("test","Admin"));
        return new LoginUser(user,permissions);
    }
}
