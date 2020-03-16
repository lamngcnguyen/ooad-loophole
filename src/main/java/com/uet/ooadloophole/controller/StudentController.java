package com.uet.ooadloophole.controller;

import com.uet.ooadloophole.controller.interface_model.BodyFragment;
import com.uet.ooadloophole.model.business.Student;
import com.uet.ooadloophole.model.business.User;
import com.uet.ooadloophole.service.MasterPageService;
import com.uet.ooadloophole.service.SecureUserDetailService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/student")
public class StudentController {
    @Autowired
    private SecureUserDetailService secureUserDetailService;
    @Autowired
    private MasterPageService masterPageService;
    @Autowired
    private StudentService studentService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getHomeView() {
        String pageTitle = "Sinh viên";
        try {
            User currentUser = secureUserDetailService.getCurrentUser();
            return masterPageService.getMasterPage(pageTitle, new BodyFragment("student/home", "body-content"), currentUser);
        } catch (BusinessServiceException e) {
            return new ModelAndView("unauthorized");
        }
    }

    @RequestMapping(value = "/group", method = RequestMethod.GET)
    public ModelAndView getGroupView() {
        String pageTitle = "Nhóm";
        return getStudentView(pageTitle, new BodyFragment("group", "body-content"));
    }

    @RequestMapping(value = "/iteration", method = RequestMethod.GET)
    public ModelAndView getIterationView() {
        String pageTitle = "Vòng lặp phát triển";
        return getStudentView(pageTitle, new BodyFragment("iteration", "body-content"));
    }

    @RequestMapping(value = "/evaluation", method = RequestMethod.GET)
    public ModelAndView getEvaluationView() {
        String pageTitle = "Chấm điểm";
        return getStudentView(pageTitle, new BodyFragment("evaluation", "body-content"));
    }

    @RequestMapping(value = "/requirement", method = RequestMethod.GET)
    public ModelAndView getRequirementView() {
        String pageTitle = "Yêu cầu";
        return getStudentView(pageTitle, new BodyFragment("requirement", "body-content"));
    }

    private ModelAndView getStudentView(String pageTitle, BodyFragment bodyFragment) {
        ModelAndView modelAndView;
        try {
            User currentUser = secureUserDetailService.getCurrentUser();
            if (currentUser.hasRole("student")) {
                Student student = studentService.getByUserId(currentUser.get_id());
                if (student.getGroupId() == null) {
                    modelAndView = masterPageService.getMasterPage(pageTitle, new BodyFragment("student/unassigned", "body-content"), currentUser);
                }
                else {
                    String roleFolder = currentUser.hasRole("group_leader") ? "leader" : "member";
                    bodyFragment.setView("student/" + roleFolder + "/" + bodyFragment.getView());
                    modelAndView = masterPageService.getMasterPage(pageTitle, bodyFragment, currentUser);
                }
                modelAndView.addObject("studentId", student.get_id());
            } else {
                modelAndView = new ModelAndView("unauthorized");
            }
        } catch (BusinessServiceException e) {
            modelAndView = new ModelAndView("error");
        }
        return modelAndView;
    }
}
