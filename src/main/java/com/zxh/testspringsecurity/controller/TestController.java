package com.zxh.testspringsecurity.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {
    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('system:dept:list')")
    public String test(){
        System.out.println();
        return "helloworld";
    }
}
