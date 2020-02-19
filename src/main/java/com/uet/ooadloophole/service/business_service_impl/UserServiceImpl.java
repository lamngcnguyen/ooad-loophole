package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.database.UserRepository;
import com.uet.ooadloophole.model.business.Role;
import com.uet.ooadloophole.model.business.User;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.EmailService;
import com.uet.ooadloophole.service.business_service.RoleService;
import com.uet.ooadloophole.service.business_service.UserService;
import org.bson.types.ObjectId;
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
    @Autowired
    private EmailService emailService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Username not found on database");
        }
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
    public List<User> getAll() {
        return userRepository.findAll();
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
    public List<User> getAllByRole(String roleName) throws BusinessServiceException {
        try {
            Role role = roleService.getByName(roleName);
            return userRepository.findAllByRole(new ObjectId(role.getId()));
        } catch (BusinessServiceException e) {
            throw new BusinessServiceException("Unable to find users with role: " + e.getMessage());
        }
    }

    @Override
    public User createActivatedUser(User user, String roleName) throws BusinessServiceException {
        try {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            Role dbRole = roleService.getByName(roleName);
            user.setRoles(new HashSet<>(Collections.singletonList(dbRole)));
            user.setActive(false);
            userRepository.save(user);
            emailService.sendActivationEmail(user);
            return user;
        } catch (BusinessServiceException e) {
            throw new BusinessServiceException("Unable to create user: " + e.getMessage());
        }
    }

    @Override
    public User create(User user, String roleName) throws BusinessServiceException {
        try {
            Role dbRole = roleService.getByName(roleName);
            user.setRoles(new HashSet<>(Collections.singletonList(dbRole)));
            user.setActive(false);
            userRepository.save(user);
            emailService.sendActivationEmail(user);
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
            return userRepository.save(dbUser);
        } catch (BusinessServiceException e) {
            throw new BusinessServiceException("Unable to update user" + e.getMessage());
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
    public void setPassword(String userEmail, String password) throws BusinessServiceException {
        try {
            User user = getByEmail(userEmail);
            user.setPassword(bCryptPasswordEncoder.encode(password));
            userRepository.save(user);
        } catch (BusinessServiceException e) {
            throw new BusinessServiceException("Unable to set password: " + e.getMessage());
        }

    }

//    @Override
//    public void changePassword(User user, String newPassword) {
//        //            User dbUser = getById(userId);
//        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
//        userRepository.save(user);
//    }

    @Override
    public void resetAccount(String email) throws BusinessServiceException {
        User user = getByEmail(email);
        user.setPassword("");
        user.setActive(false);
        emailService.sendResetPasswordEmail(user);
        userRepository.save(user);
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

    @Override
    public User setStatus(User user, boolean status) {
        user.setActive(status);
        return user;
    }

    @Override
    public boolean getStatus(User user) {
        return user.isActive();
    }

    @Override
    public boolean matchPassword(User user, String password) {
        return bCryptPasswordEncoder.matches(password, user.getPassword());
    }

    @Override
    public void changePassword(User user, String password) {
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
    }
}
