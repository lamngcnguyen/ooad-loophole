package com.uet.ooadloophole.controller;

import com.uet.ooadloophole.controller.interface_model.BodyFragment;
import com.uet.ooadloophole.model.business.system_elements.LoopholeUser;
import com.uet.ooadloophole.model.business.system_elements.Notification;
import com.uet.ooadloophole.service.MasterPageService;
import com.uet.ooadloophole.service.SecureUserService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.NotificationService;
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
    private SecureUserService secureUserService;

    @Autowired
    private MasterPageService masterPageService;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getHomePage() {
        try {
            LoopholeUser currentUser = secureUserService.getCurrentUser();
            List<Notification> notifications = notificationService.getAllByReceiverId(currentUser.get_id());
            int unreadCount;
            ModelAndView modelAndView = masterPageService.getMasterPage("Nhà", new BodyFragment("index", "body-content"), currentUser);
            unreadCount = (int) notifications.stream().filter(notification -> !notification.getSeen()).count();
            modelAndView.addObject("unreadCount", unreadCount);
            modelAndView.addObject("notifications", notifications);
            return modelAndView;
        } catch (BusinessServiceException e) {
            return new ModelAndView("error");
        }
    }
}
