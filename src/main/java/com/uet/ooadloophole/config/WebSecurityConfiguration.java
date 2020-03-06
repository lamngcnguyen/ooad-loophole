package com.uet.ooadloophole.config;

import com.uet.ooadloophole.service.business_service_impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableScheduling
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Value("${security.remember-me-key}")
    private String rememberMeKey;

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService mongoUserDetails() {
        return new UserServiceImpl();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                "Unauthorized");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        UserDetailsService userDetailsService = mongoUserDetails();
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder());
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()

                //Spring routing security rule set
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .antMatchers("/teacher/**").hasAuthority("TEACHER")
                .antMatchers("/student/**").hasAnyAuthority("STUDENT", "STUDENT_LEADER", "STUDENT_MEMBER")
                .antMatchers("/api/classes/**").hasAuthority("TEACHER")
                .antMatchers("/api/students/**").hasAuthority("TEACHER")
                .antMatchers("/api/semesters/**").hasAnyAuthority("ADMIN", "TEACHER")
                .antMatchers("/api/topics/**").hasAnyAuthority("ADMIN", "TEACHER", "STUDENT", "STUDENT_LEADER", "STUDENT_MEMBER")
                .antMatchers("/api/files/download").hasAnyAuthority("ADMIN", "TEACHER", "STUDENT", "STUDENT_LEADER", "STUDENT_MEMBER")
                .antMatchers("/api/files/upload/repo").hasAnyAuthority("STUDENT", "STUDENT_LEADER", "STUDENT_MEMBER")
                .antMatchers("/api/files/upload/spec").hasAuthority("TEACHER")
                .antMatchers("/api/groups/**").hasAnyAuthority("ADMIN", "TEACHER", "STUDENT", "STUDENT_LEADER", "STUDENT_MEMBER")
                .antMatchers("/register/**", "/", "/login").permitAll()
                .antMatchers("/reset", "/resetAccount**").permitAll()
                .antMatchers("/activate-account**").permitAll()
                .antMatchers("/api/users/resetPassword", "/api/users/activateAccount").permitAll()
                .anyRequest().authenticated()
                .and()

                //Spring security filters
//                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
//                .addFilter(new JWTAuthorizationFilter(authenticationManager()))
//                // this disables session creation on Spring Security
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                //Login form
                .formLogin()
                .loginPage("/login")
                .permitAll()
//                .loginProcessingUrl("/perform_login")
                .defaultSuccessUrl("/home")
//                .failureUrl("/login.html?error=true")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(customAuthenticationSuccessHandler)
////                .failureHandler(authenticationFailureHandler())
                .and()
                .logout().deleteCookies("JSESSIONID")
                .permitAll()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .and()
                .rememberMe().key(rememberMeKey).tokenValiditySeconds(7 * 24 * 60 * 60);
//                .logoutSuccessHandler(logoutSuccessHandler());
    }

    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**", "/js/libraries/**", "/font/**", "/font/roboto/**", "/webjars/**");
    }
}
