package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.config.Constants;
import com.uet.ooadloophole.database.system_repositories.UserRepository;
import com.uet.ooadloophole.model.business.system_elements.LoopholeUser;
import com.uet.ooadloophole.model.business.system_elements.Role;
import com.uet.ooadloophole.model.business.system_elements.Student;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.*;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @Autowired
    private StudentService studentService;
    @Autowired
    private FileService fileService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LoopholeUser user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Username not found on database");
        }
        List<GrantedAuthority> authorities = getUserAuthority(user.getRoles());
        return buildUserForAuthentication(user, authorities);
    }

    private UserDetails buildUserForAuthentication(LoopholeUser user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), true, true, true, true, authorities);
    }

    private List<GrantedAuthority> getUserAuthority(Set<Role> userRoles) {
        Set<GrantedAuthority> roles = new HashSet<>();
        userRoles.forEach((role) -> roles.add(new SimpleGrantedAuthority(role.getRole())));
        return new ArrayList<>(roles);
    }

    @Override
    public List<LoopholeUser> getAll() {
        return userRepository.findAll();
    }

    @Override
    public LoopholeUser getById(String id) throws BusinessServiceException {
        LoopholeUser result = userRepository.findBy_id(id);
        if (result == null) {
            throw new BusinessServiceException("No user found for this id");
        }
        return result;
    }

    @Override
    public LoopholeUser getByEmail(String email) throws BusinessServiceException {
        LoopholeUser result = userRepository.findByEmail(email);
        if (result == null) {
            throw new BusinessServiceException("No user found for this email");
        }
        return result;
    }

    @Override
    public LoopholeUser getByUsername(String username) throws BusinessServiceException {
        LoopholeUser result = userRepository.findByUsername(username);
        if (result == null) {
            throw new BusinessServiceException("No user found for this username");
        }
        return result;
    }

    @Override
    public List<LoopholeUser> searchByFullName(String fullName) {
        return userRepository.findAllByFullNameIgnoreCase(fullName);
    }

    @Override
    public List<LoopholeUser> getAllByRole(String roleName) throws BusinessServiceException {
        try {
            Role role = roleService.getByName(roleName);
            return userRepository.findAllByRole(new ObjectId(role.getId()));
        } catch (BusinessServiceException e) {
            throw new BusinessServiceException("Unable to find users with role: " + e.getMessage());
        }
    }

    @Override
    public LoopholeUser createActivatedUser(LoopholeUser user, String[] roleNames) throws BusinessServiceException {
        try {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            Set<Role> userRoles = new HashSet<>();
            for (String r : roleNames) {
                userRoles.add(roleService.getByName(r));
            }
            user.setRoles(userRoles);
            user.setActive(true);
            userRepository.save(user);
//            emailService.sendActivationEmail(user);
            return user;
        } catch (BusinessServiceException e) {
            throw new BusinessServiceException("Unable to createInvitation user: " + e.getMessage());
        }
    }

    @Override
    public LoopholeUser create(LoopholeUser user) throws BusinessServiceException {
        try {
            try {
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            } catch (NullPointerException e) {
                user.setPassword(null);
            }
            user.setActive(false);
            userRepository.save(user);
            //emailService.sendActivationEmail(user);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessServiceException("Unable to createInvitation user: " + e.getMessage());
        }
    }

    @Override
    public LoopholeUser update(String userId, LoopholeUser user) throws BusinessServiceException {
        try {
            LoopholeUser dbUser = getById(userId);
            dbUser.setFullName(user.getFullName());
            dbUser.setUsername(user.getUsername());
            dbUser.setEmail(user.getEmail());
            dbUser.setRoles(user.getRoles());
            dbUser.setPhoneNumber(user.getPhoneNumber());
            return userRepository.save(dbUser);
        } catch (BusinessServiceException e) {
            throw new BusinessServiceException("Unable to update user" + e.getMessage());
        }
    }

    @Override
    public void delete(String userId) throws BusinessServiceException {
        try {
            LoopholeUser dbUser = getById(userId);
            userRepository.delete(dbUser);
        } catch (BusinessServiceException e) {
            throw new BusinessServiceException("Unable to delete user: " + e.getMessage());
        }
    }

    @Override
    public void setPassword(String userId, String password) throws BusinessServiceException {
        try {
            LoopholeUser user = getById(userId);
            user.setPassword(bCryptPasswordEncoder.encode(password));
            userRepository.save(user);
        } catch (BusinessServiceException e) {
            throw new BusinessServiceException("Unable to set password: " + e.getMessage());
        }

    }

    @Override
    public LoopholeUser resetAccount(String email) throws BusinessServiceException {
        LoopholeUser user = getByEmail(email);
        user.setPassword("");
        user.setActive(false);
//        emailService.sendResetPasswordEmail(user);
        userRepository.save(user);
        return user;
    }

    @Override
    public void assignRole(String userId, String roleName) throws BusinessServiceException {
        try {
            LoopholeUser dbUser = getById(userId);
            Role dbRole = roleService.getByName(roleName);
            dbUser.getRoles().add(dbRole);
            userRepository.save(dbUser);
        } catch (BusinessServiceException e) {
            throw new BusinessServiceException("Unable to assign role to user: " + e.getMessage());
        }
    }

    @Override
    public void assignRoles(String userId, String[] roleNames) throws BusinessServiceException {
        try {
            LoopholeUser dbUser = getById(userId);
            for (String roleName : roleNames) {
                Role dbRole = roleService.getByName(roleName);
                dbUser.getRoles().add(dbRole);
            }
            userRepository.save(dbUser);
        } catch (BusinessServiceException e) {
            throw new BusinessServiceException("Unable to assign role to user: " + e.getMessage());
        }
    }

    @Override
    public void removeRole(String userId, String roleName) throws BusinessServiceException {
        try {
            LoopholeUser dbUser = getById(userId);
            Role dbRole = roleService.getByName(roleName);
            if (dbUser.getRoles().removeIf(role -> role.getId().equals(dbRole.getId()))) {
                userRepository.save(dbUser);
            } else {
                throw new BusinessServiceException("Role not removed");
            }
        } catch (BusinessServiceException e) {
            throw new BusinessServiceException("Unable to remove role from user: " + e.getMessage());
        }
    }

    @Override
    public LoopholeUser setStatus(String userId, boolean status) throws BusinessServiceException {
        try {
            LoopholeUser user = getById(userId);
            user.setActive(status);
            userRepository.save(user);
            return user;
        } catch (BusinessServiceException e) {
            throw new BusinessServiceException("Unable to set status: " + e.getMessage());
        }
    }

    @Override
    public boolean getStatus(LoopholeUser user) {
        return user.isActive();
    }

    @Override
    public boolean matchPassword(LoopholeUser user, String password) {
        return bCryptPasswordEncoder.matches(password, user.getPassword());
    }

    @Override
    public void changePassword(LoopholeUser user, String password) {
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public byte[] loadAvatar(String id) throws IOException, BusinessServiceException {
        LoopholeUser user = getById(id);
        String fileName = user.getAvatar();
        String saveLocation = Constants.AVATAR_FOLDER;
        if (fileName == null) {
            fileName = Constants.DEFAULT_AVATAR;
        } else {
            if (user.hasRole(Constants.ROLE_TEACHER) || user.hasRole(Constants.ROLE_ADMIN)) {
                saveLocation += "/staff/";
            } else {
                Student student = studentService.getByUserId(id);
                saveLocation += student.getClassId() + "/";
            }
        }
        String imagePath = saveLocation + "/" + fileName;
        FileInputStream imageStream = new FileInputStream(imagePath);
        byte[] imageBytes = IOUtils.toByteArray(imageStream);
        imageStream.close();
        return imageBytes;
    }

    @Override
    public void uploadAvatar(MultipartFile file, String id) throws BusinessServiceException {
        LoopholeUser user = getById(id);
        String saveLocation = Constants.AVATAR_FOLDER;
        if (user.hasRole(Constants.ROLE_TEACHER) || user.hasRole(Constants.ROLE_ADMIN)) {
            saveLocation += "staff/";
        } else {
            Student student = studentService.getByUserId(id);
            saveLocation += student.getClassId() + "/";
        }
        String avatar = fileService.storeAvatar(file, saveLocation);
        user.setAvatar(avatar);
        userRepository.save(user);
    }

    @Override
    public boolean emailExists(String email) {
        try {
            getByEmail(email);
            return true;
        } catch (BusinessServiceException ignored) {
            return false;
        }
    }
}
