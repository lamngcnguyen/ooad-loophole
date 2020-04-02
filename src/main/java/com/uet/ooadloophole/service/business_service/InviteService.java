package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.business.Invitation;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InviteService {
    List<Invitation> create(String groupId, List<String> studentIdList, String content) throws BusinessServiceException;
}
