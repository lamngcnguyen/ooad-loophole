package com.uet.ooadloophole.config;

import com.uet.ooadloophole.model.business.system_elements.Student;
import com.uet.ooadloophole.model.business.system_elements.LoopholeUser;
import com.uet.ooadloophole.service.SecureUserService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private SecureUserService userDetailService;
    @Autowired
    private StudentService studentService;

    public CustomAuthenticationSuccessHandler() {
        super();
        setUseReferer(true);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        try {
            LoopholeUser currentUser = userDetailService.getCurrentUser();
            httpServletResponse.addCookie(new Cookie("userId", currentUser.get_id()));
            if (userDetailService.getCurrentUser().hasRole("student")) {
                Student currentStudent = studentService.getByUserId(currentUser.get_id());
                httpServletResponse.addCookie(new Cookie("studentId", currentStudent.get_id()));
            }
            httpServletResponse.sendRedirect("/home");
        } catch (BusinessServiceException e) {
            httpServletResponse.sendRedirect("/error");
            e.printStackTrace();
        }
    }
}
