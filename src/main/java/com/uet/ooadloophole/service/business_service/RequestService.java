package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.business.Request;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RequestService {
    Request getById(String id);

    List<Request> createInvitation(String groupId, List<String> studentIdList, String content) throws BusinessServiceException;

    void acceptInvitation(String student_id, String invitationId) throws BusinessServiceException;

    void denyInvitation(String student_id, String invitationId) throws BusinessServiceException;
}
