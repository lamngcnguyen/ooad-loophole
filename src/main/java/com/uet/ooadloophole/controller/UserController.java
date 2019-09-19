package com.uet.ooadloophole.controller;

import com.uet.ooadloophole.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {
    @RequestMapping(value = "/user/registration", method = RequestMethod.GET)
    public ModelAndView showRegistrationForm() {
        ModelAndView model = new ModelAndView();
        User userDto = new User();
        model.addObject("user", userDto);
        model.setViewName("registration");
        return model;
    }

}
