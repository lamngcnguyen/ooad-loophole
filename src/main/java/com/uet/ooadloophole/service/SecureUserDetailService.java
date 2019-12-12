package com.uet.ooadloophole.service;

import com.uet.ooadloophole.database.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service

public class SecureUserDetailService {
    @Autowired
    private UserRepository userRepository;

    public User getCurrentSecureUser() throws ClassCastException {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public String getUsername() {
        User loggedInUser = getCurrentSecureUser();
        return loggedInUser.getUsername();
    }

    public com.uet.ooadloophole.model.User getCurrentUser() {
        return userRepository.findByEmail(getUsername());
    }
}
