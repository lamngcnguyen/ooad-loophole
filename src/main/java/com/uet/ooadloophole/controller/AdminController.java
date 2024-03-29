package com.uet.ooadloophole.controller;

import com.uet.ooadloophole.controller.interface_model.BodyFragment;
import com.uet.ooadloophole.model.business.system_elements.LoopholeUser;
import com.uet.ooadloophole.service.MasterPageService;
import com.uet.ooadloophole.service.SecureUserService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private SecureUserService secureUserService;

    @Autowired
    private MasterPageService masterPageService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getAdminHome() {
        String pageTitle = "Admin";
        return getAdminPage(pageTitle, new BodyFragment("admin/home", "body-content"));
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ModelAndView getUserManagementView() {
        String pageTitle = "User Management";
        return getAdminPage(pageTitle, new BodyFragment("admin/user", "body-content"));
    }

    @RequestMapping(value = "/class", method = RequestMethod.GET)
    public ModelAndView getClassManagementView() {
        String pageTitle = "Class Management";
        return getAdminPage(pageTitle, new BodyFragment("admin/class", "body-content"));
    }

    @RequestMapping(value = "/semester", method = RequestMethod.GET)
    public ModelAndView getSemesterManagementView() {
        String pageTitle = "Semester Management";
        return getAdminPage(pageTitle, new BodyFragment("admin/semester", "body-content"));
    }

    private ModelAndView getAdminPage(String pageTitle, BodyFragment bodyFragment) {
        ModelAndView modelAndView;
        try {
            LoopholeUser currentUser = secureUserService.getCurrentUser();
            if (currentUser.hasRole("admin")) {
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
