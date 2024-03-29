package com.uet.ooadloophole.controller;

import com.uet.ooadloophole.controller.interface_model.BodyFragment;
import com.uet.ooadloophole.controller.interface_model.dto.DTOGradingTemplate;
import com.uet.ooadloophole.model.business.class_elements.Assignment;
import com.uet.ooadloophole.model.business.group_elements.Group;
import com.uet.ooadloophole.model.business.group_elements.RepoFile;
import com.uet.ooadloophole.model.business.system_elements.LoopholeUser;
import com.uet.ooadloophole.service.ConverterService;
import com.uet.ooadloophole.service.MasterPageService;
import com.uet.ooadloophole.service.SecureUserService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.AssignmentService;
import com.uet.ooadloophole.service.business_service.ClassService;
import com.uet.ooadloophole.service.business_service.GradingTemplateService;
import com.uet.ooadloophole.service.business_service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/teacher")
public class TeacherController {
    @Autowired
    private SecureUserService secureUserService;
    @Autowired
    private MasterPageService masterPageService;
    @Autowired
    private ConverterService converterService;
    @Autowired
    private ClassService classService;
    @Autowired
    private GradingTemplateService gradingTemplateService;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private GroupService groupService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getHomeView() {
        String pageTitle = "Teacher";
        return getTeacherView(pageTitle, new BodyFragment("teacher/home", "body-content"));
    }

    @RequestMapping(value = "/class", method = RequestMethod.GET)
    public ModelAndView getClassView() {
        String pageTitle = "My Classes";
        return getTeacherView(pageTitle, new BodyFragment("teacher/class", "body-content"));
    }

    @RequestMapping(value = "/class/{className}", method = RequestMethod.GET)
    public ModelAndView getClassView(@PathVariable String className, @CookieValue String userId) {
        ModelAndView modelAndView;
        try {
            modelAndView = getTeacherView(className, new BodyFragment("teacher/class-details", "body-content"));
            modelAndView.addObject("class",
                    converterService.convertToDTOClass(classService.getByTeacherIdAndClassName(userId, className)));
            return modelAndView;
        } catch (BusinessServiceException e) {
            modelAndView = new ModelAndView("error");
        }
        return modelAndView;
    }

    @RequestMapping(value = "/class/{className}/settings", method = RequestMethod.GET)
    public ModelAndView getClassSettingView(@PathVariable String className, @CookieValue String userId) {
        ModelAndView modelAndView;
        try {
            modelAndView = getTeacherView(className, new BodyFragment("teacher/class-settings", "body-content"));
            modelAndView.addObject("class", converterService.convertToDTOClass(classService.getByTeacherIdAndClassName(userId, className)));
            return modelAndView;
        } catch (BusinessServiceException e) {
            modelAndView = new ModelAndView("error");
        }
        return modelAndView;
    }

    @RequestMapping(value = "/evaluate", method = RequestMethod.GET)
    public ModelAndView getEvaluateView() {
        String pageTitle = "Evaluate";
        return getTeacherView(pageTitle, new BodyFragment("teacher/evaluate", "body-content"));
    }

    @RequestMapping(value = "/template", method = RequestMethod.GET)
    public ModelAndView getGradingTemplateListView() {
        ModelAndView modelAndView;
        try {
            modelAndView = getTeacherView("Grading Template Management", new BodyFragment("teacher/grading-template-list", "body-content"));
            List<DTOGradingTemplate> templateList = gradingTemplateService.getByTeacherId(secureUserService.getCurrentUser().get_id())
                    .stream()
                    .map(DTOGradingTemplate::new).collect(Collectors.toList());
            //templateList.forEach(x -> System.out.println(x.get_id() + "/" + x.getGradingTemplateName()));
            modelAndView.addObject("templates", templateList);
        } catch (Exception e) {
            modelAndView = new ModelAndView("error");
        }
        return modelAndView;
    }

    @RequestMapping(value = "/template/{templateId}", method = RequestMethod.GET)
    public ModelAndView getGradingTemplateView(@PathVariable String templateId) {
        ModelAndView modelAndView;
        try {
            String pageTitle = "Edit grading template";
            modelAndView = getTeacherView(pageTitle, new BodyFragment("teacher/grading-template", "body-content-teacher"));
            modelAndView.addObject("template", gradingTemplateService.getById(templateId));
        } catch (BusinessServiceException e) {
            modelAndView = new ModelAndView("error");
        }
        return modelAndView;
    }

    @RequestMapping(value = "/assignment/{assignmentId}/results", method = RequestMethod.GET)
    public ModelAndView getAssignmentResult(@PathVariable String assignmentId) {
        Assignment assignment = assignmentService.getById(assignmentId);
        List<Group> groups = groupService.getAllByClassId(assignment.getClassId());
        String pageTitle = "Assignment " + assignment.getName() + " results";
        ModelAndView modelAndView = getTeacherView(pageTitle, new BodyFragment("teacher/assignment-results", "body-content"));
        modelAndView.addObject("groups", groups);
        modelAndView.addObject("assignment", assignment);

        return modelAndView;
    }

    @RequestMapping(value = "/grading/{assignmentId}", method = RequestMethod.GET)
    public ModelAndView getGradingView(@PathVariable String assignmentId) {
        ModelAndView modelAndView;
        try {
            String pageTitle = "Grading";
            String templateId = assignmentService.getById(assignmentId).getGradingTemplateId();
            modelAndView = getTeacherView(pageTitle, new BodyFragment("teacher/grading", "body-content-teacher"));
            modelAndView.addObject("template", gradingTemplateService.getById(templateId));
        } catch (Exception e) {
            modelAndView = new ModelAndView(("error"));
        }
        return modelAndView;
    }

    private ModelAndView getTeacherView(String pageTitle, BodyFragment bodyFragment) {
        ModelAndView modelAndView;
        try {
            LoopholeUser currentUser = secureUserService.getCurrentUser();
            if (currentUser.hasRole("teacher")) {
                modelAndView = masterPageService.getMasterPage(pageTitle, bodyFragment, currentUser);
            } else {
                modelAndView = new ModelAndView("forbidden");
            }
        } catch (BusinessServiceException e) {
            modelAndView = new ModelAndView("error");
        }
        return modelAndView;
    }
}
