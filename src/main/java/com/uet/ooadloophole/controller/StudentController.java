package com.uet.ooadloophole.controller;

import com.uet.ooadloophole.service.SecureUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/student")
public class StudentController {
    @RequestMapping(value = "/repo", method = RequestMethod.GET)
    public ModelAndView groupView() {
        ModelAndView modelAndView = new ModelAndView();
//        if (!new SecureUserDetailService().isStudent()) modelAndView.setViewName("/forbidden");
        modelAndView.setViewName("/student/repo");
        return modelAndView;
    }
}
