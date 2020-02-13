package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.database.UserRepository;
import com.uet.ooadloophole.model.Role;
import com.uet.ooadloophole.model.User;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.RoleService;
import com.uet.ooadloophole.service.business_service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        List<GrantedAuthority> authorities = getUserAuthority(user.getRoles());
        return buildUserForAuthentication(user, authorities);
    }

    private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), true, true, true, true, authorities);
    }

    private List<GrantedAuthority> getUserAuthority(Set<Role> userRoles) {
        Set<GrantedAuthority> roles = new HashSet<>();
        userRoles.forEach((role) -> roles.add(new SimpleGrantedAuthority(role.getRole())));
        return new ArrayList<>(roles);
    }

    @Override
    public User getById(String id) throws BusinessServiceException {
        User result = userRepository.findBy_id(id);
        if (result == null) {
            throw new BusinessServiceException("No user found for this id");
        }
        return result;
    }

    @Override
    public User getByEmail(String email) throws BusinessServiceException {
        User result = userRepository.findByEmail(email);
        if (result == null) {
            throw new BusinessServiceException("No user found for this email");
        }
        return result;
    }

    @Override
    public User getByUsername(String username) throws BusinessServiceException {
        User result = userRepository.findByUsername(username);
        if (result == null) {
            throw new BusinessServiceException("No user found for this username");
        }
        return result;
    }

    @Override
    public List<User> searchByFullName(String fullName) {
        return userRepository.findAllByFullNameIgnoreCase(fullName);
    }

    @Override
    public User create(User user, String roleName) throws BusinessServiceException {
        try {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            Role dbRole = roleService.getByName(roleName);
            user.setRoles(new HashSet<>(Collections.singletonList(dbRole)));
            userRepository.save(user);
            return user;
        } catch (BusinessServiceException e) {
            throw new BusinessServiceException("Unable to create user: " + e.getMessage());
        }
    }

    @Override
    public User update(User user) throws BusinessServiceException {
        try {
            User dbUser = getById(user.get_id());
            dbUser.setFullName(user.getFullName());
            dbUser.setUsername(user.getUsername());
            dbUser.setEmail(user.getEmail());
            userRepository.save(dbUser);
            return dbUser;
        } catch (BusinessServiceException e) {
            throw new BusinessServiceException("Unable to update user: " + e.getMessage());
        }
    }

    @Override
    public void delete(String userId) throws BusinessServiceException {
        try {
            User dbUser = getById(userId);
            userRepository.delete(dbUser);
        } catch (BusinessServiceException e) {
            throw new BusinessServiceException("Unable to delete user: " + e.getMessage());
        }
    }

    @Override
    public void changePassword(String userId, String newPassword) throws BusinessServiceException {
        try {
            User dbUser = getById(userId);
            dbUser.setPassword(bCryptPasswordEncoder.encode(newPassword));
            userRepository.save(dbUser);
        } catch (BusinessServiceException e) {
            throw new BusinessServiceException("Unable to change password: " + e.getMessage());
        }
    }

    @Override
    public void assignRole(String userId, String roleName) throws BusinessServiceException {
        try {
            User dbUser = getById(userId);
            Role dbRole = roleService.getByName(roleName);
            dbUser.getRoles().add(dbRole);
            userRepository.save(dbUser);
        } catch (BusinessServiceException e) {
            throw new BusinessServiceException("Unable to assign role to user: " + e.getMessage());
        }
    }

    @Override
    public void removeRole(String userId, String roleName) throws BusinessServiceException {
        try {
            User dbUser = getById(userId);
            Role dbRole = roleService.getByName(roleName);
            dbUser.getRoles().remove(dbRole);
            userRepository.save(dbUser);
        } catch (BusinessServiceException e) {
            throw new BusinessServiceException("Unable to remove role from user: " + e.getMessage());
        }
    }
}
