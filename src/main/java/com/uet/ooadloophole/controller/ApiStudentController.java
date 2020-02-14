package com.uet.ooadloophole.controller;

import com.uet.ooadloophole.model.business.Student;
import com.uet.ooadloophole.model.business.User;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.StudentService;
import com.uet.ooadloophole.service.business_service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/students")
public class ApiStudentController {
    @Autowired
    private StudentService studentService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getStudentById(@PathVariable String id) {
        try {
            Student result = studentService.getById(id);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/{id}/user", method = RequestMethod.GET)
    public ResponseEntity<Object> getUser(@PathVariable String id) {
        try {
            User user = userService.getById(id);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity<List<Student>> searchStudent(@RequestParam String keyword) {
        List<Student> results = studentService.searchByFullNameOrStudentId(keyword);
        HttpStatus httpStatus = (results.isEmpty()) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return ResponseEntity.status(httpStatus).body(results);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<Object> createStudent(@RequestBody Student student) {
        try {
            Student newStudent = studentService.create(student);
            return ResponseEntity.status(HttpStatus.OK).body(newStudent);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateStudent(@RequestBody Student student) {
        try {
            Student updatedStudent = studentService.update(student);
            return ResponseEntity.status(HttpStatus.OK).body(updatedStudent);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteStudent(@PathVariable String id) {
        try {
            studentService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public ResponseEntity<List<Student>> importStudents(@RequestBody List<Student> students) {
        List<Student> newStudents = studentService.importStudents(students);
        return ResponseEntity.status(HttpStatus.OK).body(newStudents);
    }
}
