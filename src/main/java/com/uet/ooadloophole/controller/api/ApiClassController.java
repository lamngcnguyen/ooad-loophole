package com.uet.ooadloophole.controller.api;

import com.google.gson.Gson;
import com.uet.ooadloophole.config.Constants;
import com.uet.ooadloophole.controller.interface_model.*;
import com.uet.ooadloophole.controller.interface_model.dto.*;
import com.uet.ooadloophole.controller.interface_model.interfaces.IClassDisciplineConfig;
import com.uet.ooadloophole.model.business.class_elements.*;
import com.uet.ooadloophole.model.business.class_elements.Class;
import com.uet.ooadloophole.model.business.group_elements.Group;
import com.uet.ooadloophole.model.business.system_elements.LoopholeUser;
import com.uet.ooadloophole.model.business.system_elements.Student;
import com.uet.ooadloophole.service.ConverterService;
import com.uet.ooadloophole.service.SecureUserService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/classes")
public class ApiClassController {
    @Autowired
    private SecureUserService secureUserService;
    @Autowired
    private ClassService classService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private TopicService topicService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private ConverterService converterService;

    private final Gson gson = new Gson();

    private boolean userCanNotCreateClass() throws BusinessServiceException {
        LoopholeUser user = secureUserService.getCurrentUser();
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

    @RequestMapping(value = "/{classId}/topics/unassigned", method = RequestMethod.GET)
    public ResponseEntity<String> getUnassignedTopics(@PathVariable String classId) {
        List<Topic> topics = topicService.getUnassignedByClassId(classId);
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

    @RequestMapping(value = "/{classId}/assignments", method = RequestMethod.GET)
    public ResponseEntity<String> getAssignments(@PathVariable String classId) {
        List<Assignment> assignments = assignmentService.getAllByClass(classId);
        List<DTOAssignment> dtoAssignments = new ArrayList<>();
        assignments.forEach(assignment -> dtoAssignments.add(converterService.convertToDTOAssignment(assignment)));
        return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new TableDataWrapper(dtoAssignments)));
    }

    @RequestMapping(value = "/{classId}/settings", method = RequestMethod.GET)
    public ResponseEntity<DTOClassConfig> getClassConfig(@PathVariable String classId) {
        DTOClassConfig classConfig = converterService.convertToDTOClassConfig(classService.getClassConfig(classId));
        return ResponseEntity.status(HttpStatus.OK).body(classConfig);
    }

    @RequestMapping(value = "/{classId}/settings/disciplines", method = RequestMethod.GET)
    public ResponseEntity<String> getClassDisciplineConfig(@PathVariable String classId) {
        List<ClassDisciplineConfig> classDisciplineConfigs = classService.getClassDisciplineConfig(classId);
        List<DTOClassDisciplineConfig> dtoClassDisciplineConfigs = new ArrayList<>();
        for(ClassDisciplineConfig classDisciplineConfig : classDisciplineConfigs) {
            DTOClassDisciplineConfig dtoClassDisciplineConfig = converterService.convertToDTOClassDisciplineConfig(classDisciplineConfig);
            dtoClassDisciplineConfigs.add(dtoClassDisciplineConfig);
        }
        return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new TableDataWrapper(dtoClassDisciplineConfigs)));
    }

    @RequestMapping(value = "/{classId}/settings/group", method = RequestMethod.POST)
    public ResponseEntity<Object> groupSetting(@PathVariable String classId, int groupMin, int groupMax, String deadline) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        if (groupMax < groupMin) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(gson.toJson(new ResponseMessage("Minimum group size must not be greater than maximum group size")));
        } else {
            try {
                ClassConfig classConfig = classService.groupSetting(classService.getById(classId), groupMin, groupMax, LocalDate.parse(deadline, formatter));
                return ResponseEntity.status(HttpStatus.OK).body(classConfig);
            } catch (BusinessServiceException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
        }
    }

    @RequestMapping(value = "/{classId}/settings/iteration", method = RequestMethod.POST)
    public ResponseEntity<Object> iterationSetting(@PathVariable String classId, int defaultLength, int maxLength, String deadline) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        if (maxLength < defaultLength) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(gson.toJson(new ResponseMessage("Default iteration length must not be greater than minimum iteration length")));
        } else {
            try {
                ClassConfig classConfig = classService.iterationSetting(classService.getById(classId), defaultLength, maxLength, LocalDate.parse(deadline, formatter));
                return ResponseEntity.status(HttpStatus.OK).body(classConfig);
            } catch (BusinessServiceException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
        }
    }

    @RequestMapping(value = "/settings/disciplines", method = RequestMethod.POST)
    public ResponseEntity<String> classDisciplineConfig(@RequestBody List<IClassDisciplineConfig> iClassDisciplineConfigs) {
        List<ClassDisciplineConfig> classDisciplineConfigs = new ArrayList<>();
        for(IClassDisciplineConfig iClassDisciplineConfig : iClassDisciplineConfigs) {
            classDisciplineConfigs.add(classService.disciplineSetting(iClassDisciplineConfig));
        }
        return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new TableDataWrapper(classDisciplineConfigs)));
    }
}
