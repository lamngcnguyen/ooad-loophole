package com.uet.ooadloophole.controller;

import com.uet.ooadloophole.controller.interface_model.BodyFragment;
import com.uet.ooadloophole.model.business.User;
import com.uet.ooadloophole.service.MasterPageService;
import com.uet.ooadloophole.service.SecureUserDetailService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/teacher")
public class TeacherController {
    @Autowired
    private SecureUserDetailService secureUserDetailService;

    @Autowired
    private ClassService classService;

    @Autowired
    private MasterPageService masterPageService;

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
    public ModelAndView getClassView(@PathVariable String className) {
        return getTeacherView(className, new BodyFragment("teacher/class-setting", "body-content"));
    }

    @RequestMapping(value = "/evaluate", method = RequestMethod.GET)
    public ModelAndView getEvaluateView() {
        String pageTitle = "Chấm bài";
        return getTeacherView(pageTitle, new BodyFragment("teacher/evaluate", "body-content"));
    }

    @RequestMapping(value = "/process", method = RequestMethod.GET)
    public ModelAndView getProcessView() {
        String pageTitle = "Thiết lập quy trình phát triển";
        return getTeacherView(pageTitle, new BodyFragment("teacher/process", "body-content"));
    }

    private ModelAndView getTeacherView(String pageTitle, BodyFragment bodyFragment) {
        ModelAndView modelAndView;
        try {
            User currentUser = secureUserDetailService.getCurrentUser();
            if (currentUser.hasRole("teacher")) {
                modelAndView = masterPageService.getMasterPage(pageTitle, bodyFragment, currentUser);
                modelAndView.addObject("teacherId", currentUser.get_id());
            } else {
                modelAndView = new ModelAndView("unauthorized");
            }
        } catch (BusinessServiceException e) {
            modelAndView = new ModelAndView("error");
        }
        return modelAndView;
    }
}
