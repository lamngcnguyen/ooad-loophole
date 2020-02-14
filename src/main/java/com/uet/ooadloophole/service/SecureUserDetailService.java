package com.uet.ooadloophole.service;

import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service

public class SecureUserDetailService {
    @Autowired
    private UserService userService;

    private User getCurrentSecureUser() throws BusinessServiceException {
        try {
            return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (ClassCastException e) {
            throw new BusinessServiceException("Unable to get security user: " + e.getMessage());
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

    public com.uet.ooadloophole.model.business.User getCurrentUser() throws BusinessServiceException {
        try {
            return userService.getByUsername(getUsername());
        } catch (BusinessServiceException e) {
            throw new BusinessServiceException("Unable to get current user: " + e.getMessage());
        }
    }
}
