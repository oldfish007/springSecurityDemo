package com.zxh.testspringsecurity;

import com.zxh.testspringsecurity.domain.User;
import com.zxh.testspringsecurity.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.stream.Collectors;

@SpringBootTest
class TestSpringSecurityApplicationTests {

    @Autowired
    private UserMapper userMapper;
 //从容器注入
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void contextLoads() {
        ArrayList<User> userList = userMapper.selectList(null).stream().collect(Collectors.toCollection(ArrayList::new));
        System.out.println(userList);
    }

    @Test
    public void test1(){

//$2a$10$Q/PfdBjBtsxRNPO9llIcmuzGnvB.yxvuXxI8JBikCsDaM3RzEfA7m
        System.out.println(passwordEncoder
                .matches("1234",
                "$2a$10$Q/PfdBjBtsxRNPO9llIcmuzGnvB.yxvuXxI8JBikCsDaM3RzEfA7m"));

    }




}
