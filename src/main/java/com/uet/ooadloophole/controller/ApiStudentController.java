package com.uet.ooadloophole.controller;

import com.google.gson.Gson;
import com.uet.ooadloophole.controller.interface_model.DTOStudent;
import com.uet.ooadloophole.controller.interface_model.IStudent;
import com.uet.ooadloophole.controller.interface_model.TableDataWrapper;
import com.uet.ooadloophole.model.business.Student;
import com.uet.ooadloophole.model.business.User;
import com.uet.ooadloophole.service.InterfaceModelConverterService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.StudentService;
import com.uet.ooadloophole.service.business_service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/students")
public class ApiStudentController {
    @Autowired
    private InterfaceModelConverterService interfaceModelConverterService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private UserService userService;

    private Gson gson = new Gson();

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
    public ResponseEntity<Object> createStudent(@RequestBody IStudent iStudent) {
        try {
            Student student = interfaceModelConverterService.convertStudentInterface(iStudent);
            Student newStudent = studentService.create(student);
            return ResponseEntity.status(HttpStatus.OK).body(newStudent);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateStudent(@PathVariable String id, @RequestBody IStudent iStudent) {
        try {
            Student student = interfaceModelConverterService.convertStudentInterface(iStudent);
            Student updatedStudent = studentService.update(id, student);
            return ResponseEntity.status(HttpStatus.OK).body(updatedStudent);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteStudent(@PathVariable String id) {
        try {
            studentService.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public ResponseEntity<List<Student>> importStudents(@RequestBody List<IStudent> students) {
        List<Student> convertedStudents = students.stream().map(s -> interfaceModelConverterService.convertToStudent(s))
                .collect(Collectors.toList());
        List<Student> newStudents = studentService.importStudents(convertedStudents);
        return ResponseEntity.status(HttpStatus.OK).body(newStudents);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<String> getAllStudents() {
        List<DTOStudent> dtoStudents = new ArrayList<>();
        studentService.getAll().forEach(student -> {
            try {
                dtoStudents.add(interfaceModelConverterService.convertToDTOStudent(student));
            } catch (BusinessServiceException e) {
                e.printStackTrace();
                //TODO: add logger here
            }
        });
        return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new TableDataWrapper(dtoStudents)));
    }
}
