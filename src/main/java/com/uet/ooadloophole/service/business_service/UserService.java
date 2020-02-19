package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.business.User;
import com.uet.ooadloophole.model.business.Role;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<User> getAll();

    User getById(String id) throws BusinessServiceException;

    User getByEmail(String email) throws BusinessServiceException;

    User getByUsername(String username) throws BusinessServiceException;

    List<User> searchByFullName(String fullName);

    List<User> getAllByRole(String roleName) throws BusinessServiceException;

    User createActivatedUser(User user, String roleName) throws BusinessServiceException; //create pre-activated user

    User create(User user, String roleName) throws BusinessServiceException; //default create user method

    User update(User user) throws BusinessServiceException;

    void delete(String userId) throws BusinessServiceException;

    void setPassword(String userEmail, String password) throws BusinessServiceException;

//    void changePassword(String userId, String newPassword) throws BusinessServiceException;

    void resetAccount(String email) throws BusinessServiceException;

    void assignRole(String userId, String roleName) throws BusinessServiceException;

    void removeRole(String userId, String roleName) throws BusinessServiceException;

    User setStatus(User user, boolean status);

    boolean getStatus(User user);

    boolean matchPassword(User user, String password);

    void changePassword(User user, String password);
}
