package com.uet.ooadloophole.controller;

import com.uet.ooadloophole.model.Class;
import com.uet.ooadloophole.model.implement_model.IClass;
import com.uet.ooadloophole.service.ClassService;
import com.uet.ooadloophole.service.SecureUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping(value = "/api/class")
public class APIClassController {
    @Autowired
    private SecureUserDetailService secureUserDetailService;
    @Autowired
    private ClassService classService;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity createClass(IClass iClass) {
        if (secureUserDetailService.isTeacher()) {
            Class ooadClass = classService.createClass(iClass);
            return ResponseEntity.status(HttpStatus.OK).body(ooadClass);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public ResponseEntity deleteClass(@CookieValue String classId) {
        try {
            classService.deleteClass(classId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } finally {
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/students/import", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity importStudents(@CookieValue String classId, @RequestBody Map<String, Object> payload) {
        try {
            classService.importStudents(classId, payload);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } finally {
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }
    }
}
