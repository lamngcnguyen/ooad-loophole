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

@RequestMapping("/api/user")
@Controller
public class ApiUserController {
    @Autowired
    private UserService userService;
    @Autowired
    private SecureUserDetailService secureUserDetailService;

    @ResponseBody
    @RequestMapping(value = "/changePassword", method = RequestMethod.PUT)
    private ResponseEntity<String> changePassword(String oldPassword, String password) {
        try {
            User user = secureUserDetailService.getCurrentUser();
            if (!userService.matchPassword(user, oldPassword)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Old Password doesn't match");
            } else {
                userService.changePassword(user, password);
                return ResponseEntity.status(HttpStatus.OK).build();
            }
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to change password" + e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping("/resetPassword")
    private ResponseEntity<String> resetPassword(String password) {
        try {
            User user = secureUserDetailService.getCurrentUser();
            user.setPassword(password);
            userService.update(user);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
