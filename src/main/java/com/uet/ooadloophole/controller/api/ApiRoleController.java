package com.uet.ooadloophole.controller.api;

import com.google.gson.Gson;
import com.uet.ooadloophole.controller.interface_model.ResponseMessage;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.RoleService;
import com.uet.ooadloophole.service.business_service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
public class ApiRoleController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    private Gson gson = new Gson();

    @RequestMapping(value = "/assign/{userId}", method = RequestMethod.POST)
    public ResponseEntity<String> assignRole(@PathVariable String userId, @RequestParam String roleName) {
        try {
            userService.assignRole(userId, roleName);
            return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new ResponseMessage("success")));
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<String> createRole(String roleName) {
        roleService.create(roleName);
        return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new ResponseMessage("success")));
    }
}
