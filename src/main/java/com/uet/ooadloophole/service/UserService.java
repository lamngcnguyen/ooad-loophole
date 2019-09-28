package com.uet.ooadloophole.service;

import com.uet.ooadloophole.model.User;
import com.uet.ooadloophole.repository.UserRepository;
import com.uet.ooadloophole.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        // Kiểm tra xem user có tồn tại trong database không?
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new CustomUserDetails(user);
    }

    public UserDetails loadUserById(long userId){
        // Kiểm tra xem user có tồn tại trong database không?
        User user = userRepository.findById(userId);
        if (user == null) {

        }
        return new CustomUserDetails(user);
    }
}
