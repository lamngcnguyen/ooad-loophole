package com.uet.ooadloophole.controller;

import com.uet.ooadloophole.database.StudentRepository;
import com.uet.ooadloophole.database.TeacherRepository;
import com.uet.ooadloophole.model.Student;
import com.uet.ooadloophole.model.Teacher;
import com.uet.ooadloophole.model.User;
import com.uet.ooadloophole.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String indexView(){
        return "redirect:/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginView(){
        return "login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView showRegistrationForm() {
        ModelAndView model = new ModelAndView();
        User userDto = new User();
        model.addObject("user", userDto);
        model.setViewName("registration");
        return model;
    }

    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity register(String fullName, String email, String password) {
        User newUser = new User();
        Student student = new Student();

        newUser.setFullName(fullName);
        newUser.setEmail(email);
        newUser.setPassword(password);
        userService.saveUser(newUser, "USER");

        student.setUserId(newUser.get_id());
        studentRepository.save(student);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
    }

    @ResponseBody
    @RequestMapping(value = "/register/teacher", method = RequestMethod.POST)
    public ResponseEntity teacherRegistration(String fullName, String email, String password) {
        User newUser = new User();
        Teacher teacher = new Teacher();

        newUser.setFullName(fullName);
        newUser.setEmail(email);
        newUser.setPassword(password);
        userService.saveUser(newUser, "TEACHER");

        teacher.setUserId(newUser.get_id());
        teacherRepository.save(teacher);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
    }

    @RequestMapping(value = "/user/info", method = RequestMethod.GET)
    public String userInfo(Model model) {
        org.springframework.security.core.userdetails.User loggedInUser =
                (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userInfo = loggedInUser.getUsername();
        model.addAttribute("userInfo", userInfo);
        return "userInfoPage";
    }
}
