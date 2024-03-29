package com.uet.ooadloophole.controller;

import com.uet.ooadloophole.config.Constants;
import com.uet.ooadloophole.controller.interface_model.BodyFragment;
import com.uet.ooadloophole.model.business.system_elements.LoopholeUser;
import com.uet.ooadloophole.service.MasterPageService;
import com.uet.ooadloophole.service.SecureUserService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.NotificationService;
import com.uet.ooadloophole.service.business_service.TokenService;
import com.uet.ooadloophole.service.business_service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private MasterPageService masterPageService;
    @Autowired
    private SecureUserService secureUserService;
    @Autowired
    private NotificationService notificationService;

    @RequestMapping(method = RequestMethod.GET)
    public String indexView() {
        return "redirect:/home";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginView() {
        return "security/login";
    }

    @ResponseBody
    @RequestMapping(value = "/register/teacher", method = RequestMethod.POST)
    public ResponseEntity<Object> teacherRegistration(String username, String email, String password, String fullName) {
        LoopholeUser newUser = new LoopholeUser();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setFullName(fullName);
        try {
            //TODO: remove confirmation URL
            LoopholeUser user = userService.createActivatedUser(newUser, new String[]{"TEACHER"});
            String token = tokenService.createToken(user.get_id(), Constants.TOKEN_ACTIVATE);
            String confirmationUrl = Constants.CONFIRMATION_URL + token;
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(confirmationUrl);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/activate-account", method = RequestMethod.GET)
    private ModelAndView activateAccount(@RequestParam String token) {
        if (tokenService.isValid(token) && tokenService.isTypeValid(token, Constants.TOKEN_ACTIVATE)) {
            //TODO: createInvitation activate account view
            ModelAndView model = new ModelAndView();
            model.addObject("userId", tokenService.getByTokenString(token).getUserId());
            model.setViewName("security/activate");
            return model;
        } else {
            //TODO: createInvitation error view
            ModelAndView errorView = new ModelAndView();
            errorView.setViewName("error");
            return errorView;
        }
    }

    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    private ResponseEntity<String> sendResetAccount(String email) {
        try {
            //TODO: remove confirmation URL
            LoopholeUser user = userService.resetAccount(email);
            String token = tokenService.createToken(user.get_id(), Constants.TOKEN_RESET);
            String resetLink = Constants.RESET_LINK + token;
            return ResponseEntity.status(HttpStatus.OK).body(resetLink);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public ModelAndView getHomePage() {
        try {
            LoopholeUser currentUser = secureUserService.getCurrentUser();
            ModelAndView modelAndView = masterPageService.getMasterPage("Chỉnh sửa trang cá nhân", new BodyFragment("profile", "body-content"), currentUser);
            modelAndView.addObject("notifications", notificationService.getAllByReceiverId(currentUser.get_id()));
            return modelAndView;
        } catch (BusinessServiceException e) {
            return new ModelAndView("error");
        }
    }

    @RequestMapping(value = "/resetAccount", method = RequestMethod.GET)
    private ModelAndView resetAccount(@RequestParam String token) {
        if (tokenService.isValid(token) && tokenService.isTypeValid(token, Constants.TOKEN_RESET)) {
            //TODO: createInvitation activate account view
            ModelAndView model = new ModelAndView();
            model.addObject("userId", tokenService.getByTokenString(token).getUserId());
            model.setViewName("security/reset");
            return model;
        } else {
            //TODO: createInvitation error view
            ModelAndView errorView = new ModelAndView();
            errorView.setViewName("error");
            return errorView;
        }
    }
}
