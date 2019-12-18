package com.uet.ooadloophole.controller;

import com.uet.ooadloophole.database.GroupRepository;
import com.uet.ooadloophole.model.Group;
import com.uet.ooadloophole.model.User;
import com.uet.ooadloophole.service.SecureUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private SecureUserDetailService userDetailService;

    @RequestMapping(value = "/topic", method = RequestMethod.GET)
    public ModelAndView topicView(@CookieValue String groupId, @CookieValue String userId) {
        User user = userDetailService.getCurrentUser();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/student/topic");
        modelAndView.addObject("userFullName", user.getFullName());
        return modelAndView;
    }

    @RequestMapping(value = "/repo", method = RequestMethod.GET)
    public ModelAndView repoView(@CookieValue String groupId, @CookieValue String userId) {
        Group group = groupRepository.findBy_id(groupId);
        ModelAndView modelAndView = new ModelAndView();
        if (group.getTopicId() == null) modelAndView.setViewName("/student/unassigned");
        else {
            modelAndView.setViewName("/student/repo");
            modelAndView.addObject("groupName", group.getGroupName());
        }
        User user = userDetailService.getCurrentUser();
        modelAndView.addObject("userFullName", user.getFullName());
        return modelAndView;
    }
}
