package com.uet.ooadloophole.controller;

import com.uet.ooadloophole.database.GroupRepository;
import com.uet.ooadloophole.database.TopicRepository;
import com.uet.ooadloophole.model.Group;
import com.uet.ooadloophole.model.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/topic")
public class TopicController {
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private GroupRepository groupRepository;

    @ResponseBody
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity createNewTopic(String classId, String topicName, String groupId) {
        Topic topic = new Topic();
        topic.setName(topicName);
        topic.setClassId(classId);
        topicRepository.save(topic);
        if(groupId != null) {
            Group group = groupRepository.findBy_id(groupId);
            group.setTopicId(topic.get_id());
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
    }
}
