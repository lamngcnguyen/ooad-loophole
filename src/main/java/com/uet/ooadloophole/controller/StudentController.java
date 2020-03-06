package com.uet.ooadloophole.controller;

import com.uet.ooadloophole.controller.interface_model.BodyFragment;
import com.uet.ooadloophole.model.business.User;
import com.uet.ooadloophole.service.MasterPageService;
import com.uet.ooadloophole.service.SecureUserDetailService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
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
    @RequestMapping(value = "/group", method = RequestMethod.GET)
    public ModelAndView getGroupView(){
        String pageTitle = "Nhóm";
        return getStudentView(pageTitle, new BodyFragment("student/group", "body-content"));
    }

    private ModelAndView getStudentView(String pageTitle, BodyFragment bodyFragment) {
        ModelAndView modelAndView;
        try {
            User currentUser = secureUserDetailService.getCurrentUser();
            if (currentUser.hasRole("student")) {
                modelAndView = masterPageService.getMasterPage(pageTitle, bodyFragment, currentUser);
                modelAndView.addObject("studentId", currentUser.get_id());
            } else {
                modelAndView = new ModelAndView("unauthorized");
            }
        } catch (BusinessServiceException e) {
            modelAndView = new ModelAndView("error");
        }
        return modelAndView;
    }
}
