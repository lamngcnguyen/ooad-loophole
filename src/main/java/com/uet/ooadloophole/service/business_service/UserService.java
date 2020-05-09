package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.business.LoopholeUser;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface UserService {
    List<LoopholeUser> getAll();

    LoopholeUser getById(String id) throws BusinessServiceException;

    LoopholeUser getByEmail(String email) throws BusinessServiceException;

    LoopholeUser getByUsername(String username) throws BusinessServiceException;

    List<LoopholeUser> searchByFullName(String fullName);

    List<LoopholeUser> getAllByRole(String roleName) throws BusinessServiceException;

    LoopholeUser createActivatedUser(LoopholeUser user, String[] roleNames) throws BusinessServiceException; //createInvitation pre-activated user

    LoopholeUser create(LoopholeUser user) throws BusinessServiceException; //default createInvitation user method

    LoopholeUser update(String userId, LoopholeUser user) throws BusinessServiceException;

    void delete(String userId) throws BusinessServiceException;

    void setPassword(String userId, String password) throws BusinessServiceException;

//    void changePassword(String userId, String newPassword) throws BusinessServiceException;

    LoopholeUser resetAccount(String email) throws BusinessServiceException;

    void assignRole(String userId, String roleName) throws BusinessServiceException;

    void assignRoles(String userId, String[] roleNames) throws BusinessServiceException;

    void removeRole(String userId, String roleName) throws BusinessServiceException;

    LoopholeUser setStatus(String userId, boolean status) throws BusinessServiceException;

    boolean getStatus(LoopholeUser user);

    boolean matchPassword(LoopholeUser user, String password);

    void changePassword(LoopholeUser user, String password);

    byte[] loadAvatar(String id) throws IOException, BusinessServiceException;

    void uploadAvatar(MultipartFile file, String id) throws BusinessServiceException;

    boolean emailExists(String email);
}
