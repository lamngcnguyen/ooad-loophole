package com.uet.ooadloophole.controller.api;

import com.google.gson.Gson;
import com.uet.ooadloophole.config.Constants;
import com.uet.ooadloophole.controller.interface_model.*;
import com.uet.ooadloophole.model.business.*;
import com.uet.ooadloophole.model.business.Class;
import com.uet.ooadloophole.service.ConverterService;
import com.uet.ooadloophole.service.SecureUserDetailService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.ClassService;
import com.uet.ooadloophole.service.business_service.GroupService;
import com.uet.ooadloophole.service.business_service.TokenService;
import com.uet.ooadloophole.service.business_service.TopicService;
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
    private TokenService tokenService;
    @Autowired
    private TopicService topicService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private ConverterService converterService;

    private Gson gson = new Gson();

    private boolean userCanNotCreateClass() throws BusinessServiceException {
        User user = secureUserDetailService.getCurrentUser();
        return (!user.hasRole("teacher") && !user.hasRole("admin"));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> createClass(@RequestBody Class ooadClass) {
        try {
            if (userCanNotCreateClass())
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            if (classService.classNameExists(ooadClass.getTeacherId(), ooadClass.getClassName()))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(gson.toJson(
                        new ResponseMessage("Class name already exists for this teacher")));
            Class newClass = classService.create(ooadClass);
            return ResponseEntity.status(HttpStatus.OK).body(newClass);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/teacher/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getClassesByTeacherId(@PathVariable String id) {
        try {
            if (userCanNotCreateClass()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            } else {
                List<DTOClass> dtoClasses = new ArrayList<>();
                classService.getByTeacherId(id).forEach(ooadClass -> {
                    try {
                        dtoClasses.add(converterService.convertToDTOClass(ooadClass));
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

    @RequestMapping(value = "/teacher/{id}/semester/{semesterId}", method = RequestMethod.GET)
    public ResponseEntity<String> getClassesBySemester(@PathVariable String id, @PathVariable String semesterId) {
        try {
            if (userCanNotCreateClass()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            } else {
                List<DTOClass> dtoClasses = new ArrayList<>();
                classService.getByTeacherIdAndSemesterId(id, semesterId).forEach(ooadClass -> {
                    try {
                        dtoClasses.add(converterService.convertToDTOClass(ooadClass));
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

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getClasses() {
        List<DTOClass> dtoClasses = new ArrayList<>();
        classService.getAll().forEach(ooadClass -> {
            try {
                dtoClasses.add(converterService.convertToDTOClass(ooadClass));
            } catch (BusinessServiceException e) {
                //e.printStackTrace();
                //TODO: add logger here
            }
        });
        return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new TableDataWrapper(dtoClasses)));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteClass(@PathVariable String id) {
        try {
            if (userCanNotCreateClass())
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            classService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new ResponseMessage("success")));
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/{classId}/students", method = RequestMethod.GET)
    public ResponseEntity<String> getAllStudentsByClass(@PathVariable String classId) {
        List<DTOStudent> dtoStudents = new ArrayList<>();
        classService.getAllStudents(classId).forEach(student -> {
            try {
                dtoStudents.add(converterService.convertToDTOStudent(student));
            } catch (BusinessServiceException e) {
                //TODO: add logger here
                e.printStackTrace();
            }
        });
        return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new TableDataWrapper(dtoStudents)));
    }

    @RequestMapping(value = "/{classId}/students/withoutGroup", method = RequestMethod.GET)
    public ResponseEntity<String> getStudentsWithoutGroup(@PathVariable String classId) {
        List<DTOStudent> dtoStudents = new ArrayList<>();
        classService.getStudentsWithoutGroup(classId).forEach(student -> {
            try {
                dtoStudents.add(converterService.convertToDTOStudent(student));
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
            List<String> confirmationLinks = new ArrayList<>();
            for (Student s : studentList) {
                //TODO: remove confirmation URL
                String token = tokenService.createToken(s.getUserId(), Constants.TOKEN_ACTIVATE);
                String confirmationUrl = Constants.CONFIRMATION_URL + token;
                confirmationLinks.add(confirmationUrl);
            }
            return ResponseEntity.status(HttpStatus.OK).body(new Gson().toJson(new TableDataWrapper(confirmationLinks)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateClass(@PathVariable String id, @RequestBody Class ooadClass) {
        try {
            if (userCanNotCreateClass())
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            if (classService.classNameExists(ooadClass.getTeacherId(), ooadClass.getClassName()))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(gson.toJson(
                        new ResponseMessage("Class name already exists for this teacher")));
            Class updatedClass = classService.update(id, ooadClass);
            return ResponseEntity.status(HttpStatus.OK).body(updatedClass);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity<String> searchClass(@RequestParam String keyword) {
        List<Class> classes = classService.searchByName(keyword);
        HttpStatus httpStatus = (classes.isEmpty()) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return ResponseEntity.status(httpStatus).body(gson.toJson(new SearchResultWrapper(classes)));
    }

    @RequestMapping(value = "/{classId}/topics", method = RequestMethod.GET)
    public ResponseEntity<String> getTopics(@PathVariable String classId) {
        List<Topic> topics = topicService.getAllByClassId(classId);
        List<DTOTopic> dtoTopics = new ArrayList<>();
        topics.forEach(topic -> {
            try {
                dtoTopics.add(converterService.convertToDTOTopic(topic));
            } catch (BusinessServiceException e) {
                System.out.println(e.getMessage());
            }
        });
        return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new TableDataWrapper(dtoTopics)));
    }

    @RequestMapping(value = "/{classId}/groups", method = RequestMethod.GET)
    public ResponseEntity<String> getGroups(@PathVariable String classId) {
        List<Group> groups = groupService.getAllByClassId(classId);
        List<DTOGroup> dtoGroups = new ArrayList<>();
        groups.forEach(group -> dtoGroups.add(converterService.convertToDTOGroup(group)));
        return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new TableDataWrapper(dtoGroups)));
    }
}
