package com.uet.ooadloophole.service;

import com.uet.ooadloophole.controller.interface_model.BodyFragment;
import com.uet.ooadloophole.model.business.User;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.NavigationGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service
public class MasterPageService {
    @Autowired
    private NavigationGroupService navigationGroupService;

    public ModelAndView getMasterPage(String pageTitle, BodyFragment bodyFragment, User user) throws BusinessServiceException {
        try {
            ModelAndView modelAndView = new ModelAndView("master");
            modelAndView.addObject("pageTitle", pageTitle);
            modelAndView.addObject("user", user);
            modelAndView.addObject("bodyFragment", bodyFragment);
            modelAndView.addObject("navGroups", navigationGroupService.getByRoles(user.getRoles()));
            return modelAndView;
        } catch (BusinessServiceException e) {
            throw new BusinessServiceException("Error creating master-components page: " + e.getMessage());
        }
    }
}
