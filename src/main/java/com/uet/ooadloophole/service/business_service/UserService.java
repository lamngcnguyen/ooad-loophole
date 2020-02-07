package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.User;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    User getUserById(String id) throws BusinessServiceException;

    User getUserByEmail(String email) throws BusinessServiceException;

    List<User> searchUserByFullName(String fullName);

    User createUser(User user, String roleName) throws BusinessServiceException;

    User updateUser(User user) throws BusinessServiceException;

    void deleteUser(String userId) throws BusinessServiceException;

    void changePassword(String userId, String newPassword) throws BusinessServiceException;

    void assignRole(String userId, String roleName) throws BusinessServiceException;

    void removeRole(String userId, String roleName) throws BusinessServiceException;
}
