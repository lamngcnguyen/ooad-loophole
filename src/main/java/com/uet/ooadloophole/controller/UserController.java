package com.uet.ooadloophole.controller;

import com.uet.ooadloophole.model.User;
import com.uet.ooadloophole.service.SecureUserDetailService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private SecureUserDetailService secureUserDetailService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String indexView() {
        return "redirect:/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginView() {
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
    @RequestMapping(value = "/register/teacher", method = RequestMethod.POST)
    public ResponseEntity<Object> teacherRegistration(String username, String email, String password, String fullName) {
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setFullName(fullName);
        try {
            userService.create(newUser, "TEACHER");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(newUser);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/user/info", method = RequestMethod.GET)
    public ModelAndView userInfo() {
        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.addObject("userInfo", secureUserDetailService.getUsername());
            modelAndView.setViewName("user_info");
        } catch (BusinessServiceException e) {
            modelAndView.setViewName("error");
        }
        return modelAndView;
    }
}
