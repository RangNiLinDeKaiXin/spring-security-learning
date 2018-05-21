package com.lcc.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import com.earthchen.security.app.social.AppSingUpUtils;


@RestController
@RequestMapping("/lcc")
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        return "hello spring security";
    }
}
