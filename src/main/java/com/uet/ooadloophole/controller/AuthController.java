package com.uet.ooadloophole.controller;

import com.uet.ooadloophole.model.business.User;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.TokenService;
import com.uet.ooadloophole.service.business_service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

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
            //TODO: remove confirmation URL
            User user = userService.createActivatedUser(newUser, "TEACHER");
            String token = tokenService.createToken(user.get_id());
            String confirmationUrl = "http://ooad-loophole.herokuapp.com/activate-account?token=" + token;
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(confirmationUrl);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/activate-account", method = RequestMethod.GET)
    private ModelAndView activateAccount(@RequestParam String token) {
        if (tokenService.isValid(token)) {
            //TODO: create activate account view
            ModelAndView model = new ModelAndView();
            model.addObject("userId", tokenService.getByTokenString(token).getUserId());
            model.setViewName("activate");
            return model;
        } else {
            //TODO: create error view
            ModelAndView errorView = new ModelAndView();
            errorView.setViewName("error");
            return errorView;
        }
    }

    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    private ResponseEntity<String> sendResetAccount(String email) {
        try {
            //TODO: remove confirmation URL
            User user = userService.resetAccount(email);
            String token = tokenService.createToken(user.get_id());
            String resetLink = "http://ooad-loophole.herokuapp.com/resetPassword?token=" + token;
            return ResponseEntity.status(HttpStatus.OK).body(resetLink);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/resetAccount", method = RequestMethod.GET)
    private ModelAndView resetAccount(@RequestParam String token) {
        if (tokenService.isValid(token)) {
            //TODO: create activate account view
            ModelAndView model = new ModelAndView();
            model.setViewName("reset");
            return model;
        } else {
            //TODO: create error view
            ModelAndView errorView = new ModelAndView();
            errorView.setViewName("error");
            return errorView;
        }
    }
}
