package com.uet.ooadloophole.controller;

import com.uet.ooadloophole.database.TopicRepository;
import com.uet.ooadloophole.model.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/topic")
public class TopicController {
    @Autowired
    private TopicRepository topicRepository;

    @ResponseBody
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createNewTopic(String classId, String topicName) {
        Topic topic = new Topic();
        topic.setName(topicName);
        topic.setClassId(classId);
        topicRepository.save(topic);
        return "created";
    }
}
