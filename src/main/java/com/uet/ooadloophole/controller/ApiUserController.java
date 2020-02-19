package com.uet.ooadloophole.controller;

import com.google.gson.Gson;
import com.uet.ooadloophole.controller.interface_model.IUser;
import com.uet.ooadloophole.controller.interface_model.ListJsonWrapper;
import com.uet.ooadloophole.model.business.User;
import com.uet.ooadloophole.service.InterfaceModelConverterService;
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
    private InterfaceModelConverterService interfaceModelConverterService;
    @Autowired
    private SecureUserDetailService secureUserDetailService;

    private Gson gson = new Gson();

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getUsers() {
        List<User> users = userService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new ListJsonWrapper(users)));
    }

    @RequestMapping(value = "/role/{roleName}", method = RequestMethod.GET)
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

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.POST)
    private ResponseEntity<Object> createUser(@RequestBody IUser iUser) {
        try {
            User user = interfaceModelConverterService.convertUserInterface(iUser);
            User newUser = userService.create(user);
            return ResponseEntity.status(HttpStatus.OK).body(newUser);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    private ResponseEntity<Object> updateUser(@PathVariable String id, @RequestBody IUser iUser) {
        try {
            User user = interfaceModelConverterService.convertUserInterface(iUser);
            User updatedUser = userService.update(id, user);
            return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    private ResponseEntity<String> deleteUser(@PathVariable String id) {
        try {
            userService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/{id}/status", method = RequestMethod.POST)
    private ResponseEntity<String> setStatus(@PathVariable String id, @RequestParam boolean status) {
        try {
            userService.setStatus(id, status);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
