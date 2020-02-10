package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.Topic;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;

import java.util.List;

public interface TopicService {
    Topic getById(String id) throws BusinessServiceException;

    Topic getByGroupId(String groupId) throws BusinessServiceException;

    List<Topic> getAllByClassId(String classId);

    List<Topic> searchByNameOrDescription(String keyword);

    Topic create(Topic topic);

    Topic update(Topic topic) throws BusinessServiceException;

    void delete(String topicId) throws BusinessServiceException;

    void assignToGroup(String topicId, String groupId) throws BusinessServiceException;

    void removeFromGroup(String topicId) throws BusinessServiceException;

    boolean isAlreadyAssigned(String topicId) throws BusinessServiceException;
}