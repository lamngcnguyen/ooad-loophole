package com.uet.ooadloophole.controller;

import com.uet.ooadloophole.model.Class;
import com.uet.ooadloophole.model.Student;
import com.uet.ooadloophole.service.SecureUserDetailService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.ClassService;
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

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<Object> createClass(@RequestBody Class ooadClass) {
        try {
            if (!secureUserDetailService.isTeacher())
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            Class newClass = classService.create(ooadClass);
            return ResponseEntity.status(HttpStatus.OK).body(newClass);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

//    @ResponseBody
//    @RequestMapping(value = "/", method = RequestMethod.POST)
//    public ResponseEntity createClass(String name, String teacherId, String semesterId, int scheduledDayOfWeek) {
//        try {
//            if (!secureUserDetailService.isTeacher())
//                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//            Class newClass = classService.create(name, teacherId, semesterId, scheduledDayOfWeek);
//            return ResponseEntity.status(HttpStatus.OK).body(newClass);
//        } catch (BusinessServiceException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//        }
//    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<Object> getClassesByTeacherId(@CookieValue String userId) {
        try {
            if (!secureUserDetailService.isTeacher())
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            return ResponseEntity.status(HttpStatus.OK).body(classService.getByTeacherId(userId));
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteClass(@PathVariable String id) {
        try {
            classService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/{id}/students/import", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Object> importStudents(@PathVariable String id, @RequestBody List<Student> students) {
        try {
            for (Student student : students) {
                System.out.println(student.getFullName());
            }
            List<Student> studentList = classService.importStudents(id, students);
            return ResponseEntity.status(HttpStatus.OK).body(studentList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
