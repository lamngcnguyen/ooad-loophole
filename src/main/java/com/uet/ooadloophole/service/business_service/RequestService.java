package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.business.group_elements.Request;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RequestService {
    Request getById(String id);

    List<Request> createInvitation(String groupId, List<String> studentIdList, String content) throws BusinessServiceException;

    void acceptInvitation(String userId, String invitationId) throws BusinessServiceException;

    void denyInvitation(String userId, String invitationId) throws BusinessServiceException;

    Request createRequest(String groupId, String studentId) throws BusinessServiceException;

    void acceptRequest(String userId, String requestId) throws BusinessServiceException;

    void denyRequest(String userId, String requestId) throws BusinessServiceException;
}
