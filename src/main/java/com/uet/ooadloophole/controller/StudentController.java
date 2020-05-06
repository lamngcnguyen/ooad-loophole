package com.uet.ooadloophole.controller;

import com.uet.ooadloophole.controller.interface_model.BodyFragment;
import com.uet.ooadloophole.model.business.*;
import com.uet.ooadloophole.service.MasterPageService;
import com.uet.ooadloophole.service.SecureUserService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Controller
@RequestMapping(value = "/student")
public class StudentController {
    @Autowired
    private SecureUserService secureUserService;
    @Autowired
    private MasterPageService masterPageService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private RequestService requestService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private IterationService iterationService;
    @Autowired
    private ClassService classService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getHomeView() {
        String pageTitle = "Sinh viên";
        try {
            LoopholeUser currentUser = secureUserService.getCurrentUser();
            return masterPageService.getMasterPage(pageTitle, new BodyFragment("student/home", "body-content"), currentUser);
        } catch (BusinessServiceException e) {
            return new ModelAndView("forbidden");
        }
    }

    @RequestMapping(value = "/invitation/{id}", method = RequestMethod.GET)
    public ModelAndView getInvitationView(@PathVariable String id) {
        try {
            Request request = requestService.getById(id);
            ModelAndView modelAndView;
            LoopholeUser currentUser = secureUserService.getCurrentUser();
            Student student = studentService.getByUserId(currentUser.get_id());
            if (request != null) {
                Group group = groupService.getById(request.getGroupId());
                String pageTitle = "Bạn đã được mời vào nhóm " + group.getGroupName();
                modelAndView = masterPageService.getMasterPage(pageTitle, new BodyFragment("student/invitation", "body-content"), currentUser);
                modelAndView.addObject("invitation", request);
                modelAndView.addObject("group", group);
                modelAndView.addObject("student", student);
            } else {
                String pageTitle = "Lời mời không hợp lệ";
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
        try {
            Date date = new Date();
            LocalDate today = LocalDate.from(date.toInstant().atZone(ZoneId.of("GMT+7")));
            String pageTitle = "Vòng lặp phát triển";
            LoopholeUser currentUser = secureUserService.getCurrentUser();
            Student student = studentService.getByUserId(currentUser.get_id());
            ClassConfig classConfig = classService.getClassConfig(student.getClassId());
            boolean iterationDeadlineMet = today.compareTo(classConfig.getIterationSetupDeadline()) > 0;

            ModelAndView iterationView = getStudentView(pageTitle, new BodyFragment("student/iteration", "content"));
            iterationView.addObject("isSetupPhase", !iterationDeadlineMet);
            iterationView.addObject("groupId", student.getGroupId());
            iterationView.addObject("classId", student.getClassId());
            return iterationView;
        } catch (BusinessServiceException e) {
            return new ModelAndView("error");
        }
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

    @RequestMapping(value = "/assignment", method = RequestMethod.GET)
    public ModelAndView getAssignmentView() {
        String pageTitle = "Bài tập";
        return getStudentView(pageTitle, new BodyFragment("student/assignment", "content"));
    }

    private ModelAndView getStudentView(String pageTitle, BodyFragment bodyFragment) {
        ModelAndView modelAndView;
        try {
            LoopholeUser currentUser = secureUserService.getCurrentUser();
            if (currentUser.hasRole("student")) {
                Student student = studentService.getByUserId(currentUser.get_id());
                if (student.getGroupId() == null) {
                    modelAndView = masterPageService.getMasterPage(pageTitle, new BodyFragment("student/unassigned", "body-content"), currentUser);
                } else {
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
