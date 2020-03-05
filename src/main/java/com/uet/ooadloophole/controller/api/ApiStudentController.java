package com.uet.ooadloophole.controller.api;

import com.google.gson.Gson;
import com.uet.ooadloophole.config.Constants;
import com.uet.ooadloophole.controller.interface_model.DTOStudent;
import com.uet.ooadloophole.controller.interface_model.IStudent;
import com.uet.ooadloophole.controller.interface_model.ResponseMessage;
import com.uet.ooadloophole.controller.interface_model.TableDataWrapper;
import com.uet.ooadloophole.model.business.Student;
import com.uet.ooadloophole.model.business.User;
import com.uet.ooadloophole.service.ConverterService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.StudentService;
import com.uet.ooadloophole.service.business_service.TokenService;
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
    private ConverterService converterService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;

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

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> createStudent(@RequestBody IStudent iStudent) {
        try {
            Student student = converterService.convertStudentInterface(iStudent);
            //TODO: remove confirmation URL
            if (studentService.studentIdExists(student.getStudentId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(gson.toJson(new ResponseMessage("Student already exists")));
            } else {
                Student newStudent = studentService.create(student);
                String token = tokenService.createToken(newStudent.getUserId());
                String confirmationUrl = Constants.CONFIRMATION_URL + token;
                return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new ResponseMessage(confirmationUrl)));
            }
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateStudent(@PathVariable String id, @RequestBody IStudent iStudent) {
        try {
            Student student = converterService.convertStudentInterface(iStudent);
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
            return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new ResponseMessage("success")));
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public ResponseEntity<List<Student>> importStudents(@RequestBody List<IStudent> students) {
        List<Student> convertedStudents = students.stream().map(s -> converterService.convertToStudent(s))
                .collect(Collectors.toList());
        List<Student> newStudents = studentService.importStudents(convertedStudents);
        return ResponseEntity.status(HttpStatus.OK).body(newStudents);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getAllStudents() {
        List<DTOStudent> dtoStudents = new ArrayList<>();
        studentService.getAll().forEach(student -> {
            try {
                dtoStudents.add(converterService.convertToDTOStudent(student));
            } catch (BusinessServiceException e) {
                e.printStackTrace();
                //TODO: add logger here
            }
        });
        return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new TableDataWrapper(dtoStudents)));
    }
}
