package com.uet.ooadloophole.controller;

import com.uet.ooadloophole.model.User;
import com.uet.ooadloophole.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String indexView(){
        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginView(){
        return "login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView showRegistrationForm() {
        ModelAndView model = new ModelAndView();
        User userDto = new User();
        model.addObject("user", userDto);
        model.setViewName("registration");
        return model;
    }

    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(String email, String password, String role) {
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(password);
        userService.saveUser(newUser, role);
        return "created";
    }

    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    public String userInfo(Model model, Principal principal) {
        String userName = principal.getName();
        System.out.println("User Name: " + userName);
        User loggedUser = (User) ((Authentication) principal).getPrincipal();
        String userInfo = loggedUser.getEmail();
        model.addAttribute("userInfo", userInfo);
        return "userInfoPage";
    }
}
