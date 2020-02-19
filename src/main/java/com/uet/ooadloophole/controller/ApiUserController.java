package com.uet.ooadloophole.controller;

import com.google.gson.Gson;
import com.uet.ooadloophole.model.business.User;
import com.uet.ooadloophole.model.frontend_element.ListJsonWrapper;
import com.uet.ooadloophole.service.SecureUserDetailService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.TokenService;
import com.uet.ooadloophole.service.business_service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class ApiUserController {
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private SecureUserDetailService secureUserDetailService;

    private Gson gson = new Gson();

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getUsers() {
        List<User> users = userService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new ListJsonWrapper(users)));
    }

    @RequestMapping(value = "/role/{roleName}",method = RequestMethod.GET)
    public ResponseEntity<String> getUsers(@PathVariable String roleName) {
        try {
            List<User> users = userService.getAllByRole(roleName);
            return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new ListJsonWrapper(users)));
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

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
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    private ResponseEntity<String> resetPassword(String token, String email, String password) {
        try {
            if (tokenService.isValid(token)) {
                userService.setPassword(email, password);
                tokenService.deleteActiveToken(tokenService.getByTokenString(token));
                return ResponseEntity.status(HttpStatus.OK).build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Invalid token");
            }
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/activateAccount", method = RequestMethod.POST)
    private ResponseEntity<String> activateAccount(String token, String email, String password) {
        try {
            if (tokenService.isValid(token)) {
                userService.setPassword(email, password);
                tokenService.deleteActiveToken(tokenService.getByTokenString(token));
                return ResponseEntity.status(HttpStatus.OK).build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Invalid token");
            }
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
