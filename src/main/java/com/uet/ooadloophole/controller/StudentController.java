package com.uet.ooadloophole.controller;

import com.uet.ooadloophole.controller.interface_model.BodyFragment;
import com.uet.ooadloophole.model.business.Invitation;
import com.uet.ooadloophole.model.business.Student;
import com.uet.ooadloophole.model.business.User;
import com.uet.ooadloophole.service.MasterPageService;
import com.uet.ooadloophole.service.SecureUserDetailService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.InvitationService;
import com.uet.ooadloophole.service.business_service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
    @Autowired
    private InvitationService invitationService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getHomeView() {
        String pageTitle = "Sinh viên";
        try {
            User currentUser = secureUserDetailService.getCurrentUser();
            return masterPageService.getMasterPage(pageTitle, new BodyFragment("student/home", "body-content"), currentUser);
        } catch (BusinessServiceException e) {
            return new ModelAndView("forbidden");
        }
    }

    @RequestMapping(value = "/invitation/{id}", method = RequestMethod.GET)
    public ModelAndView getInvitationView(@PathVariable String id) {
        String pageTitle = "Mời vào nhóm";
        try {
            ModelAndView modelAndView;
            User currentUser = secureUserDetailService.getCurrentUser();
            Invitation invitation = invitationService.getById(id);
            if (invitation != null) {
                modelAndView = masterPageService.getMasterPage(pageTitle, new BodyFragment("student/invitation", "body-content"), currentUser);
                modelAndView.addObject("invitation", invitation);
            } else {
                modelAndView = masterPageService.getMasterPage(pageTitle, new BodyFragment("student/invitation", "invalid-content"), currentUser);
            }
            return modelAndView;
        } catch (BusinessServiceException e) {
            return new ModelAndView("forbidden");
        }
    }

    @RequestMapping(value = "/group", method = RequestMethod.GET)
    public ModelAndView getGroupView() {
        String pageTitle = "Nhóm";
        return getStudentView(pageTitle, new BodyFragment("student/group", "content"));
    }

    @RequestMapping(value = "/iteration", method = RequestMethod.GET)
    public ModelAndView getIterationView() {
        String pageTitle = "Vòng lặp phát triển";
        return getStudentView(pageTitle, new BodyFragment("student/iteration", "content"));
    }

    @RequestMapping(value = "/evaluation", method = RequestMethod.GET)
    public ModelAndView getEvaluationView() {
        String pageTitle = "Chấm điểm";
        return getStudentView(pageTitle, new BodyFragment("student/evaluation", "content"));
    }

    @RequestMapping(value = "/requirement", method = RequestMethod.GET)
    public ModelAndView getRequirementView() {
        String pageTitle = "Yêu cầu";
        return getStudentView(pageTitle, new BodyFragment("student/requirement", "content"));
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
                    bodyFragment.setFragment(roleFolder + "-" + bodyFragment.getFragment());
                    modelAndView = masterPageService.getMasterPage(pageTitle, bodyFragment, currentUser);
                }
                modelAndView.addObject("student", student);
            } else {
                modelAndView = new ModelAndView("forbidden");
            }
        } catch (BusinessServiceException e) {
            modelAndView = new ModelAndView("error");
        }
        return modelAndView;
    }
}
