package com.uet.ooadloophole.controllers;

import com.uet.ooadloophole.models.User;
import com.uet.ooadloophole.repositorys.UserRepository;
import com.uet.ooadloophole.security.CustomUserDetails;
import com.uet.ooadloophole.security.JwtTokenProvider;
import com.uet.ooadloophole.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private JwtTokenProvider tokenProvider;

    @GetMapping("/signin")
    public String getToken(){
        User a = new User();
        a.setId((long) 1);
        String jwt = tokenProvider.generateToken(new CustomUserDetails(a));
        return jwt;
    }
    @GetMapping("/api/resouce")
    public String hello() {
        return "Hello World";
    }
    @GetMapping("/api/createtestuser")
    public String createTestUser(){
        User user= new User();
        user.setId((long) 1 );
        user.setUsername("17020705");
        user.setPassword("12456789");
        try{
            userRepository.save(user);
        }catch (Exception e){
            return "that bai";
        }



        return "thanh công";
    }

}
