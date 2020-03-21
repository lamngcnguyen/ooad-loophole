package com.uet.ooadloophole.controller.api;

import com.google.gson.Gson;
import com.uet.ooadloophole.config.Constants;
import com.uet.ooadloophole.controller.interface_model.DTOUser;
import com.uet.ooadloophole.controller.interface_model.IUser;
import com.uet.ooadloophole.controller.interface_model.ResponseMessage;
import com.uet.ooadloophole.controller.interface_model.TableDataWrapper;
import com.uet.ooadloophole.model.business.User;
import com.uet.ooadloophole.service.ConverterService;
import com.uet.ooadloophole.service.SecureUserDetailService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.TokenService;
import com.uet.ooadloophole.service.business_service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class ApiUserController {
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ConverterService converterService;
    @Autowired
    private SecureUserDetailService secureUserDetailService;

    private Gson gson = new Gson();

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getUsers() {
        List<DTOUser> dtoUsers = new ArrayList<>();
        for (User user : userService.getAll()) {
            dtoUsers.add(converterService.convertToDTOUser(user));
        }
        return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new TableDataWrapper(dtoUsers)));
    }

    @RequestMapping(value = "/role/{roleName}", method = RequestMethod.GET)
    public ResponseEntity<String> getUsersByRole(@PathVariable String roleName) {
        try {
            List<DTOUser> dtoUsers = new ArrayList<>();
            for (User user : userService.getAllByRole(roleName)) {
                dtoUsers.add(converterService.convertToDTOUser(user));
            }
            return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new TableDataWrapper(dtoUsers)));
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.PUT)
    public ResponseEntity<String> changePassword(String oldPassword, String password) {
        try {
            User user = secureUserDetailService.getCurrentUser();
            if (!userService.matchPassword(user, oldPassword)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Old Password doesn't match");
            } else {
                userService.changePassword(user, password);
                return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new ResponseMessage("success")));
            }
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to change password" + e.getMessage());
        }
    }

    @RequestMapping(value = "/reset-password", method = RequestMethod.POST)
    public ResponseEntity<String> resetPassword(String token, String email, String password) {
        try {
            if (tokenService.isValid(token)) {
                userService.setPassword(email, password);
                tokenService.deleteActiveToken(tokenService.getByTokenString(token));
                return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new ResponseMessage("success")));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Invalid token");
            }
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/activate-account", method = RequestMethod.POST)
    public ResponseEntity<String> activateAccount(String token, String userId, String password) {
        try {
            if (tokenService.isValid(token)) {
                userService.setPassword(userId, password);
                userService.setStatus(userId, true);
                tokenService.deleteActiveToken(tokenService.getByTokenString(token));
                return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new ResponseMessage("activated")));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Invalid token");
            }
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> createUser(@RequestBody IUser iUser) {
        try {
            User user = converterService.convertUserInterface(iUser);
            if (userService.emailExists(user.getEmail())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(gson.toJson(new ResponseMessage("Email already exists")));
            } else {
                User newUser = userService.create(user);
                //TODO: remove confirmation URL
                String token = tokenService.createToken(newUser.get_id());
                String confirmationUrl = Constants.CONFIRMATION_URL + token;
                return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new ResponseMessage(confirmationUrl)));
            }
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(new ResponseMessage(e.getMessage())));
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateUser(@PathVariable String id, @RequestBody IUser iUser) {
        try {
            User user = converterService.convertUserInterface(iUser);
            User updatedUser = userService.update(id, user);
            return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        try {
            userService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new ResponseMessage("success")));
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/{id}/status", method = RequestMethod.POST)
    public ResponseEntity<String> setStatus(@PathVariable String id, @RequestParam boolean status) {
        try {
            userService.setStatus(id, status);
            return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new ResponseMessage("success")));
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/avatar/{id}", method = RequestMethod.GET)
    public byte[] getAvatar(@PathVariable String id) throws IOException, BusinessServiceException {
        return userService.loadAvatar(id);
    }

    @RequestMapping(value = "/avatar", method = RequestMethod.POST)
    private ResponseEntity<String> uploadAvatar(@RequestParam("file") MultipartFile file, @CookieValue("userId") String id) {
        try {
            userService.uploadAvatar(file, id);
            return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new ResponseMessage("success")));
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(new ResponseMessage(e.getMessage())));
        }
    }
//    @PutMapping(value = "/{id}")
//    public ResponseEntity<Object> updateUser2(@PathVariable String id, @RequestBody IUser iUser){
//        try {
//            User user = converterService.convertUserInterface(iUser);
//
//        } catch (BusinessServiceException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//        }
//    }
}
