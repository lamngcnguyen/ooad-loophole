package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.User;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;

import java.util.List;

public interface UserService {
    User getById(String id) throws BusinessServiceException;

    User getByEmail(String email) throws BusinessServiceException;

    List<User> searchByFullName(String fullName);

    User create(User user, String roleName) throws BusinessServiceException;

    User update(User user) throws BusinessServiceException;

    void delete(String userId) throws BusinessServiceException;

    void changePassword(String userId, String newPassword) throws BusinessServiceException;

    void assignRole(String userId, String roleName) throws BusinessServiceException;

    void removeRole(String userId, String roleName) throws BusinessServiceException;
}
