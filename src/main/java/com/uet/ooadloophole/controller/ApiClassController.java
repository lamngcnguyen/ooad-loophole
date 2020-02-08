package com.uet.ooadloophole.controller;

import com.uet.ooadloophole.model.Class;
import com.uet.ooadloophole.model.Student;
import com.uet.ooadloophole.model.interface_model.IClass;
import com.uet.ooadloophole.service.SecureUserDetailService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.ClassService;
import com.uet.ooadloophole.service.business_service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/api/classes")
public class ApiClassController {
    @Autowired
    private SecureUserDetailService secureUserDetailService;
    @Autowired
    private ClassService classService;
    @Autowired
    private StudentService studentService;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity createClass(IClass iClass) {
        try {
            if (!secureUserDetailService.isTeacher())
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            Class newClass = classService.create(iClass);
            return ResponseEntity.status(HttpStatus.OK).body(newClass);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteClass(@PathVariable String id) {
        try {
            classService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/{id}/students/import", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity importStudents(@PathVariable String id, @RequestBody List<Student> students) {
        try {
            students = studentService.importStudents(students);
            return ResponseEntity.status(HttpStatus.OK).body(students);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
