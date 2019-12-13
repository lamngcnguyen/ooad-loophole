package com.uet.ooadloophole.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/teacher")
public class TeacherController {
    @RequestMapping(value = "/class", method = RequestMethod.GET)
    public String classView() {
        return "teacher/class";
    }

    @RequestMapping(value = "/class/{id}")
    public ModelAndView classSettingView(@PathVariable String id) {
        System.out.println("Class ID: " + id);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("title", "Course " + id);
        modelAndView.setViewName("teacher/class_setting");
        return modelAndView;
    }
}
