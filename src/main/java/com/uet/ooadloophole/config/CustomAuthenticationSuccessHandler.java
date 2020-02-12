package com.uet.ooadloophole.config;

import com.uet.ooadloophole.model.Student;
import com.uet.ooadloophole.model.User;
import com.uet.ooadloophole.service.SecureUserDetailService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.ClassService;
import com.uet.ooadloophole.service.business_service.RoleService;
import com.uet.ooadloophole.service.business_service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private SecureUserDetailService userDetailService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private ClassService classService;
    @Autowired
    private RoleService roleService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        try {
            User currentUser = userDetailService.getCurrentUser();
            httpServletResponse.addCookie(new Cookie("userId", currentUser.get_id()));
            if (userDetailService.isStudent()) {
                Student currentStudent = studentService.getByUserId(currentUser.get_id());
                httpServletResponse.addCookie(new Cookie("classId", currentStudent.getClassId()));
                httpServletResponse.addCookie(new Cookie("groupId", currentStudent.getGroupId()));
            }
            httpServletResponse.sendRedirect("/home");
        } catch (BusinessServiceException e) {
            httpServletResponse.sendRedirect("/error");
            e.printStackTrace();
        }
    }
}
