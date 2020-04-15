package com.uet.ooadloophole.controller;

import com.uet.ooadloophole.controller.interface_model.BodyFragment;
import com.uet.ooadloophole.model.business.LoopholeUser;
import com.uet.ooadloophole.service.MasterPageService;
import com.uet.ooadloophole.service.SecureUserService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/home")
public class HomeController {
    @Autowired
    private SecureUserService secureUserService;

    @Autowired
    private MasterPageService masterPageService;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getHomePage() {
        try {
            LoopholeUser currentUser = secureUserService.getCurrentUser();
            ModelAndView modelAndView = masterPageService.getMasterPage("Nhà", new BodyFragment("index", "body-content"), currentUser);
            modelAndView.addObject("notifications", notificationService.getAllByReceiverId(currentUser.get_id()));
            return modelAndView;
        } catch (BusinessServiceException e) {
            return new ModelAndView("error");
        }
    }
}
