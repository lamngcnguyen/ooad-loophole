package com.uet.ooadloophole.controller;

import com.google.gson.Gson;
import com.uet.ooadloophole.controller.interface_model.DTOClass;
import com.uet.ooadloophole.controller.interface_model.DTOStudent;
import com.uet.ooadloophole.controller.interface_model.SearchResultWrapper;
import com.uet.ooadloophole.controller.interface_model.TableDataWrapper;
import com.uet.ooadloophole.model.business.Class;
import com.uet.ooadloophole.model.business.Student;
import com.uet.ooadloophole.model.business.User;
import com.uet.ooadloophole.service.InterfaceModelConverterService;
import com.uet.ooadloophole.service.SecureUserDetailService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/classes")
public class ApiClassController {
    @Autowired
    private SecureUserDetailService secureUserDetailService;
    @Autowired
    private ClassService classService;
    @Autowired
    private InterfaceModelConverterService interfaceModelConverterService;

    private Gson gson = new Gson();

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<Object> createClass(@RequestBody Class ooadClass) {
        try {
            if (userIsNotTeacher())
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            Class newClass = classService.create(ooadClass);
            return ResponseEntity.status(HttpStatus.OK).body(newClass);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/teacher/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getClassesByTeacherId(@PathVariable String id) {
        try {
            if (userIsNotTeacher()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            } else {
                List<DTOClass> dtoClasses = new ArrayList<>();
                classService.getByTeacherId(id).forEach(ooadClass -> {
                    try {
                        dtoClasses.add(interfaceModelConverterService.convertToDTOClass(ooadClass));
                    } catch (BusinessServiceException e) {
                        e.printStackTrace();
                        //TODO: add logger here
                    }
                });
                return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new TableDataWrapper(dtoClasses)));
            }
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<Object> getClasses() {
        List<DTOClass> dtoClasses = new ArrayList<>();
        classService.getAll().forEach(ooadClass -> {
            try {
                dtoClasses.add(interfaceModelConverterService.convertToDTOClass(ooadClass));
            } catch (BusinessServiceException e) {
                e.printStackTrace();
                //TODO: add logger here
            }
        });
        return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new TableDataWrapper(dtoClasses)));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteClass(@PathVariable String id) {
        try {
            classService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/{classId}/students", method = RequestMethod.GET)
    public ResponseEntity<String> getAllStudentsByClass(@PathVariable String classId) {
        List<DTOStudent> dtoStudents = new ArrayList<>();
        classService.getAllStudents(classId).forEach(student -> {
            try {
                dtoStudents.add(interfaceModelConverterService.convertToDTOStudent(student));
            } catch (BusinessServiceException e) {
                //TODO: add logger here
                e.printStackTrace();
            }
        });
        return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new TableDataWrapper(dtoStudents)));
    }

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

    private boolean userIsNotTeacher() throws BusinessServiceException {
        User user = secureUserDetailService.getCurrentUser();
        return !user.hasRole("teacher");
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity<String> searchClass(@RequestParam String keyword) {
        List<Class> classes = classService.searchByName(keyword);
        HttpStatus httpStatus = (classes.isEmpty()) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return ResponseEntity.status(httpStatus).body(gson.toJson(new SearchResultWrapper(classes)));
    }
}
