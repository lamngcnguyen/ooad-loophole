package com.uet.ooadloophole.config;

import com.uet.ooadloophole.database.ClassRepository;
import com.uet.ooadloophole.database.StudentRepository;
import com.uet.ooadloophole.database.TeacherRepository;
import com.uet.ooadloophole.model.Student;
import com.uet.ooadloophole.model.Teacher;
import com.uet.ooadloophole.service.SecureUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private SecureUserDetailService userDetailService;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private ClassRepository classRepository;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.addCookie(new Cookie("userId", userDetailService.getCurrentUser().get_id()));
        if(userDetailService.getStudentId() != null) {
            Student currentStudent = studentRepository.findBy_id(userDetailService.getStudentId());
            httpServletResponse.addCookie(new Cookie("classId", currentStudent.getClassId()));
            httpServletResponse.addCookie(new Cookie("groupId", currentStudent.getGroupId()));
        }
//       else if(userDetailService.getTeacherId() != null){
//            Teacher currentTeacher = teacherRepository.findBy_id(userDetailService.getTeacherId());
//            httpServletResponse.addCookie(new Cookie("classId", classRepository.findByTeacherId(currentTeacher.get_id()).get_id()));
//        }
        for (GrantedAuthority auth : authentication.getAuthorities()) {
            if ("USER".equals(auth.getAuthority())) {
                httpServletResponse.sendRedirect("/userInfo");
            }
            if ("ADMIN".equals(auth.getAuthority())) {
                httpServletResponse.sendRedirect("/userInfo");
            }
            if ("TEACHER".equals(auth.getAuthority())) {
                httpServletResponse.sendRedirect("/userInfo");
            }
        }
    }
}
