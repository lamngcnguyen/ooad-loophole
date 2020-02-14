package com.uet.ooadloophole.controller;

import com.uet.ooadloophole.model.frontend_element.BodyFragment;
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
@RequestMapping(value = "/home")
public class HomeController {
    @Autowired
    private SecureUserDetailService secureUserDetailService;

    @Autowired
    private MasterPageService masterPageService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getHomePage() {
        try {
            User currentUser = secureUserDetailService.getCurrentUser();
            return masterPageService.getMasterPage("Nhà", new BodyFragment("index", "body-content"), currentUser);
        } catch (BusinessServiceException e) {
            return new ModelAndView("error");
        }
    }
}
