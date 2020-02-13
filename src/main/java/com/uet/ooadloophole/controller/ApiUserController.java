package com.uet.ooadloophole.controller;

import com.google.gson.Gson;
import com.uet.ooadloophole.model.User;
import com.uet.ooadloophole.model.ListJsonWrapper;
import com.uet.ooadloophole.service.business_service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class ApiUserController {
    @Autowired
    private UserService userService;

    private Gson gson = new Gson();

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getUsers() {
        List users = userService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new ListJsonWrapper(users)));
    }
}
