package com.uet.ooadloophole.controller;

import com.uet.ooadloophole.controller.interface_model.BodyFragment;
import com.uet.ooadloophole.model.business.system_elements.LoopholeUser;
import com.uet.ooadloophole.service.ConverterService;
import com.uet.ooadloophole.service.MasterPageService;
import com.uet.ooadloophole.service.SecureUserService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.ClassService;
import com.uet.ooadloophole.service.business_service.GradingTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

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

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getHomeView() {
        String pageTitle = "Giảng viên";
        return getTeacherView(pageTitle, new BodyFragment("teacher/home", "body-content"));
    }

    @RequestMapping(value = "/class", method = RequestMethod.GET)
    public ModelAndView getClassView() {
        String pageTitle = "Lớp của tôi";
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
        String pageTitle = "Chấm bài";
        return getTeacherView(pageTitle, new BodyFragment("teacher/evaluate", "body-content"));
    }

    @RequestMapping(value = "/edit-template/{templateId}", method = RequestMethod.GET)
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

    private ModelAndView getTeacherView(String pageTitle, BodyFragment bodyFragment) {
        ModelAndView modelAndView;
        try {
            LoopholeUser currentUser = secureUserService.getCurrentUser();
            if (currentUser.hasRole("teacher")) {
                modelAndView = masterPageService.getMasterPage(pageTitle, bodyFragment, currentUser);
            } else {
                modelAndView = new ModelAndView("unauthorized");
            }
        } catch (BusinessServiceException e) {
            modelAndView = new ModelAndView("error");
        }
        return modelAndView;
    }
}
