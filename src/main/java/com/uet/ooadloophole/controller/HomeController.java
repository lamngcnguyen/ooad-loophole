package com.uet.ooadloophole.controller;

import com.uet.ooadloophole.model.NavigationGroup;
import com.uet.ooadloophole.model.ViewFragment;
import com.uet.ooadloophole.service.SecureUserDetailService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.NavigationGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(value = "/home")
public class HomeController {
    @Autowired
    private SecureUserDetailService secureUserDetailService;

    @Autowired
    private NavigationGroupService navigationGroupService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getHomePage() {
        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.setViewName("home");
            modelAndView.addObject("user", secureUserDetailService.getCurrentUser());
            modelAndView.addObject("navGroups", getNavigationGroups());
            modelAndView.addObject("viewFragment", new ViewFragment("index", "body-content"));
            return modelAndView;
        } catch (BusinessServiceException e) {
            modelAndView.setViewName("error");
        }
        return modelAndView;
    }

    private List<NavigationGroup> getNavigationGroups() throws BusinessServiceException {
        try {
            return navigationGroupService.getByRoles(
                    secureUserDetailService.getCurrentUser().getRole());
        } catch (BusinessServiceException e) {
            throw new BusinessServiceException("Unable to get user nav bars: " + e.getMessage());
        }
    }
}
