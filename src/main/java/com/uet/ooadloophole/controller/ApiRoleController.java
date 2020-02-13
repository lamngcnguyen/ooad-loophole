package com.uet.ooadloophole.controller;

import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.RoleService;
import com.uet.ooadloophole.service.business_service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/role")
public class ApiRoleController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/assign/{userId}", method = RequestMethod.POST)
    public ResponseEntity assignRole(@PathVariable String userId, @RequestParam String roleName) {
        try {
            userService.assignRole(userId, roleName);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
