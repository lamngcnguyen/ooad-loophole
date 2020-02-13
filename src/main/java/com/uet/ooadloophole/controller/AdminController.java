package com.uet.ooadloophole.controller;

import com.uet.ooadloophole.model.BodyFragment;
import com.uet.ooadloophole.model.User;
import com.uet.ooadloophole.service.MasterPageService;
import com.uet.ooadloophole.service.SecureUserDetailService;
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
    private SecureUserDetailService secureUserDetailService;

    @Autowired
    private MasterPageService masterPageService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getAdminHome() {
        String pageTitle = "Quản trị";
        return getAdminPage(pageTitle, new BodyFragment("admin/home", "body-content"));
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ModelAndView getUserManagementView() {
        String pageTitle = "Quản lý người dùng";
        return getAdminPage(pageTitle, new BodyFragment("admin/user", "body-content"));
    }

    private ModelAndView getAdminPage(String pageTitle, BodyFragment bodyFragment) {
        ModelAndView modelAndView;
        try {
            User currentUser = secureUserDetailService.getCurrentUser();
            if (currentUser.hasRole("admin")) {
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
