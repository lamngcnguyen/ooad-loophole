package com.uet.ooadloophole.controller;

import com.uet.ooadloophole.database.ClassRepository;
import com.uet.ooadloophole.model.Class;
import com.uet.ooadloophole.model.User;
import com.uet.ooadloophole.model.dto.ClassDTO;
import com.uet.ooadloophole.service.CookieService;
import com.uet.ooadloophole.service.SecureUserDetailService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/teacher")
public class TeacherController {
    @Autowired
    private SecureUserDetailService userDetailService;

    @Autowired
    private ClassRepository classRepository;

    @RequestMapping(value = "/class", method = RequestMethod.GET)
    public ModelAndView classView() {
        ModelAndView modelAndView = new ModelAndView();
        try {
            User user = userDetailService.getCurrentUser();
            modelAndView.addObject("userFullName", user.getFullName());
            modelAndView.addObject("userEmail", user.getEmail());
            modelAndView.setViewName("teacher/class");
        } catch (BusinessServiceException e) {
            modelAndView.setViewName("error");
        }
        return modelAndView;
    }

    @RequestMapping(value = "/class/{className}")
    public ModelAndView classSettingView(@PathVariable String className, HttpServletResponse response, HttpServletRequest request, @CookieValue(name = "classId", defaultValue = "none") String classId) {
        ModelAndView modelAndView = new ModelAndView();
        Class myClass = classRepository.findClassByClassName(className);
        try {
            User user = userDetailService.getCurrentUser();
            if (classId == null) {
                response.addCookie(new Cookie("classId", myClass.get_id()) {{
                    setPath("/class");
                }});
            } else response.addCookie(CookieService.updateCookie(request, "classId", myClass.get_id(), "/class"));
            modelAndView.addObject("className", myClass.getClassName());
            modelAndView.addObject("classId", myClass.get_id());
//            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            modelAndView.addObject("dayOfWeek", ClassDTO.getDayOfWeek(myClass.getScheduledDayOfWeek()));
            modelAndView.addObject("userFullName", user.getFullName());
            modelAndView.addObject("userEmail", user.getEmail());
            modelAndView.setViewName("teacher/class_setting");
        } catch (BusinessServiceException e) {
            modelAndView.setViewName("error");
        }
        return modelAndView;
    }
}
