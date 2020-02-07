package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.database.UserRepository;
import com.uet.ooadloophole.model.Role;
import com.uet.ooadloophole.model.User;
import com.uet.ooadloophole.service.business_service.RoleService;
import com.uet.ooadloophole.service.business_service.UserService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        List<GrantedAuthority> authorities = getUserAuthority(user.getRole());
        return buildUserForAuthentication(user, authorities);
    }

    private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), true, true, true, true, authorities);
    }

    private List<GrantedAuthority> getUserAuthority(Set<Role> userRoles) {
        Set<GrantedAuthority> roles = new HashSet<>();
        userRoles.forEach((role) -> roles.add(new SimpleGrantedAuthority(role.getRole())));
        return new ArrayList<>(roles);
    }

    @Override
    public User getUserById(String id) throws BusinessServiceException {
        User result = userRepository.findBy_id(id);
        if (result == null) {
            throw new BusinessServiceException("No user found for this id");
        }
        return result;
    }

    @Override
    public User getUserByEmail(String email) throws BusinessServiceException {
        User result = userRepository.findByEmail(email);
        if (result == null) {
            throw new BusinessServiceException("No user found for this email");
        }
        return result;
    }

    @Override
    public List<User> searchUserByFullName(String fullName) {
        return userRepository.findAllByFullNameIgnoreCase(fullName);
    }

    @Override
    public User createUser(User user, String roleName) throws BusinessServiceException {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Role userRole = roleService.getRoleByName(roleName);
        user.setRole(new HashSet<>(Collections.singletonList(userRole)));
        userRepository.save(user);
        return user;
    }

    @Override
    public User updateUser(User user) throws BusinessServiceException {
        User dbUser = getUserById(user.get_id());
        dbUser.setFullName(user.getFullName());
        dbUser.setEmail(user.getEmail());
        userRepository.save(user);
        return user;
    }

    @Override
    public void deleteUser(String userId) throws BusinessServiceException {
        User dbUser = getUserById(userId);
        userRepository.delete(dbUser);
    }

    @Override
    public void changePassword(String userId, String newPassword) throws BusinessServiceException {
        User user = getUserById(userId);
        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void assignRole(String userId, String roleName) throws BusinessServiceException {
        User user = getUserById(userId);
        Role role = roleService.getRoleByName(roleName);
        user.getRole().add(role);
        userRepository.save(user);
    }

    @Override
    public void removeRole(String userId, String roleName) throws BusinessServiceException {
        User user = getUserById(userId);
        Role role = roleService.getRoleByName(roleName);
        user.getRole().remove(role);
        userRepository.save(user);
    }
}
