package com.uet.ooadloophole.controller;

import com.uet.ooadloophole.model.Student;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/student")
public class ApiStudentController {
    @Autowired
    private StudentService studentService;

    @RequestMapping(value = "/id", method = RequestMethod.GET)
    public ResponseEntity getStudentById(@RequestParam String id) {
        try {
            Student result = studentService.getStudentById(id);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/student-id", method = RequestMethod.GET)
    public ResponseEntity getStudentByStudentId(@RequestParam String studentId) {
        try {
            Student result = studentService.getStudentByStudentId(studentId);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/user-id", method = RequestMethod.GET)
    public ResponseEntity getStudentByUserId(@RequestParam String userId) {
        try {
            Student result = studentService.getStudentByUserId(userId);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/class", method = RequestMethod.GET)
    public ResponseEntity getStudentByClass(@RequestParam String classId) {
        List<Student> results = studentService.getStudentByClass(classId);
        HttpStatus httpStatus = (results.isEmpty()) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return ResponseEntity.status(httpStatus).body(results);
    }

    @RequestMapping(value = "/group", method = RequestMethod.GET)
    public ResponseEntity getStudentByGroup(@RequestParam String groupId) {
        List<Student> results = studentService.getStudentByGroup(groupId);
        HttpStatus httpStatus = (results.isEmpty()) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return ResponseEntity.status(httpStatus).body(results);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity searchStudentByFullName(@RequestParam String keyword) {
        List<Student> results = studentService.searchStudentByFullName(keyword);
        HttpStatus httpStatus = (results.isEmpty()) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return ResponseEntity.status(httpStatus).body(results);
    }
}
