package com.uet.ooadloophole.controllers;

import com.uet.ooadloophole.models.User;
import com.uet.ooadloophole.repositorys.UserRepository;
import com.uet.ooadloophole.security.CustomUserDetails;
import com.uet.ooadloophole.security.JwtTokenProvider;
import com.uet.ooadloophole.services.FileStorageService;
import com.uet.ooadloophole.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/upload")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    FileStorageService fileStorageService ;

    @GetMapping
    public String getToken(){
        User a = new User();
        a.setId((long) 1);
        String jwt = tokenProvider.generateToken(new CustomUserDetails(a));
        return jwt;

    }
    @GetMapping("/api/resouce")
    public String hello() {
        return "Hello World";
    }
    @GetMapping("/api/createtestuser")
    public String createTestUser(){
        User user= new User();
        user.setId((long) 1 );
        user.setUsername("17020705");
        user.setPassword("12456789");
        try{
            userRepository.save(user);
        }catch (Exception e){
            return "that bai";
        }



        return "thanh công";
    }
    @PostMapping
    public  String upload(@RequestParam("file") MultipartFile file){
        String fileName = fileStorageService.storeFile(file);
        Resource resource = fileStorageService.loadFileAsResource(fileName);
        return  fileName ;
    }

}
