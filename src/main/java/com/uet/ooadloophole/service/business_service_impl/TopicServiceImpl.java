package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.database.TopicRepository;
import com.uet.ooadloophole.model.Group;
import com.uet.ooadloophole.model.Topic;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.GroupService;
import com.uet.ooadloophole.service.business_service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicServiceImpl implements TopicService {
    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private GroupService groupService;

    @Override
    public Topic getById(String id) throws BusinessServiceException {
        Topic dbTopic = topicRepository.findBy_id(id);
        if (dbTopic == null) {
            throw new BusinessServiceException("No topic found for this id");
        }
        return dbTopic;
    }

    @Override
    public Topic getByGroupId(String groupId) throws BusinessServiceException {
        Topic dbTopic = topicRepository.findByGroupId(groupId);
        if(dbTopic == null) {
            throw new BusinessServiceException("No topic found for this group");
        }
        return dbTopic;
    }

    @Override
    public List<Topic> getAllByClassId(String classId) {
        return topicRepository.findAllByClassId(classId);
    }

    @Override
    public List<Topic> searchByNameOrDescription(String keyword) {
        return topicRepository.findAllByNameLikeOrDescriptionsLike(keyword, keyword);
    }

    @Override
    public Topic create(Topic topic) {
        topicRepository.save(topic);
        return topic;
    }

    @Override
    public Topic update(Topic topic) throws BusinessServiceException {
        try {
            Topic dbTopic = getById(topic.get_id());
            dbTopic.setName(topic.getName());
            dbTopic.setDescriptions(topic.getDescriptions());
            topicRepository.save(dbTopic);
            return dbTopic;
        } catch (BusinessServiceException e) {
            throw new BusinessServiceException("Unable to update topic: " + e.getMessage());
        }
    }

    @Override
    public void delete(String topicId) throws BusinessServiceException {
        try {
            Topic dbTopic = getById(topicId);
            topicRepository.delete(dbTopic);
        } catch (BusinessServiceException e) {
            throw new BusinessServiceException("Unable to delete topic: " + e.getMessage());
        }
    }

    @Override
    public void assignToGroup(String topicId, String groupId) throws BusinessServiceException {
        try {
            Topic dbTopic = getById(topicId);
            Group dbGroup = groupService.getById(groupId);
            dbTopic.setGroupId(dbGroup.get_id());
            topicRepository.save(dbTopic);
        } catch (BusinessServiceException e) {
            throw new BusinessServiceException("Unable to assign topic to group: " + e.getMessage());
        }
    }

    @Override
    public void removeFromGroup(String topicId) throws BusinessServiceException {
        try {
            Topic dbTopic = getById(topicId);
            dbTopic.setGroupId(null);
            topicRepository.save(dbTopic);
        } catch (BusinessServiceException e) {
            throw new BusinessServiceException("Unable to remove topic from group: " + e.getMessage());
        }
    }

    @Override
    public boolean isAlreadyAssigned(String topicId) throws BusinessServiceException {
        try {
            Topic dbTopic = getById(topicId);
            return dbTopic.getGroupId() != null;
        } catch (BusinessServiceException e) {
            throw new BusinessServiceException("Unable to get topic status: " + e.getMessage());
        }
    }
}
