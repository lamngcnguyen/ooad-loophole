package com.uet.ooadloophole.controller;

import com.uet.ooadloophole.model.User;
import com.uet.ooadloophole.security.CustomUserDetails;
import com.uet.ooadloophole.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {
    @Autowired
    private JwtTokenProvider tokenProvider;

    @GetMapping("/login")
    public String getToken() {
        User newUser = new User();
        newUser.setId((long) 1);
        return tokenProvider.generateToken(new CustomUserDetails(newUser));
    }

    @RequestMapping(value = "/user/registration", method = RequestMethod.GET)
    public ModelAndView showRegistrationForm() {
        ModelAndView model = new ModelAndView();
        User userDto = new User();
        model.addObject("user", userDto);
        model.setViewName("registration");
        return model;
    }
}
