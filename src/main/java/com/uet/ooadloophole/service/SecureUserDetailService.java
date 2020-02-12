package com.uet.ooadloophole.service;

import com.uet.ooadloophole.model.Role;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.RoleService;
import com.uet.ooadloophole.service.business_service.StudentService;
import com.uet.ooadloophole.service.business_service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service

public class SecureUserDetailService {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private StudentService studentService;

    private static final String ROLE_TEACHER = "TEACHER";
    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_STUDENT = "USER";

    private User getCurrentSecureUser() throws BusinessServiceException {
        try {
            return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (ClassCastException e) {
            throw new BusinessServiceException("Unable to get current user: " + e.getMessage());
        }
    }

    public String getUsername() throws BusinessServiceException {
        try {
            User loggedInUser = getCurrentSecureUser();
            return loggedInUser.getUsername();
        } catch (BusinessServiceException e) {
            throw new BusinessServiceException("Unable to get username: " + e.getMessage());
        }
    }

    public com.uet.ooadloophole.model.User getCurrentUser() throws BusinessServiceException {
        try {
            return userService.getByUsername(getUsername());
        } catch (BusinessServiceException e) {
            throw new BusinessServiceException("Unable to get current user: " + e.getMessage());
        }
    }

    public boolean isTeacher() throws BusinessServiceException {
        try {
            ArrayList<String> roleList = new ArrayList<>();
            com.uet.ooadloophole.model.User currentUser = getCurrentUser();
            for (Role role : currentUser.getRole()) {
                roleList.add(role.getRole());
            }
            return roleList.contains(ROLE_TEACHER);
        } catch (BusinessServiceException e) {
            throw new BusinessServiceException("Unable to check user role:" + e.getMessage());
        }
    }

    public boolean isStudent() throws BusinessServiceException {
        try {
            ArrayList<String> roleList = new ArrayList<>();
            com.uet.ooadloophole.model.User currentUser = getCurrentUser();
            for (Role role : currentUser.getRole()) {
                roleList.add(role.getRole());
            }
            return roleList.contains(ROLE_STUDENT);
        } catch (BusinessServiceException e) {
            throw new BusinessServiceException("Unable to check user role: " + e.getMessage());
        }
    }
}
