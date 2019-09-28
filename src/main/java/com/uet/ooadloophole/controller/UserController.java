package com.uet.ooadloophole.controller;

import com.uet.ooadloophole.model.User;
import com.uet.ooadloophole.repository.UserRepository;
import com.uet.ooadloophole.security.CustomUserDetails;
import com.uet.ooadloophole.security.JwtTokenProvider;
import com.uet.ooadloophole.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
    public String getToken() {
        User a = new User();
        a.setId((long) 1);
        return tokenProvider.generateToken(new CustomUserDetails(a));
    }

    @GetMapping("/api/resource")
    public String hello() {
        return "Hello World";
    }

    @GetMapping("/api/createTestUser")
    public String createTestUser() {
        User user = new User();
        user.setId((long) 1);
        user.setUsername("17020705");
        user.setPassword("12456789");
        try {
            userRepository.save(user);
        } catch (Exception e) {
            return "that bai";
        }
        return "thanh công";
    }

}
