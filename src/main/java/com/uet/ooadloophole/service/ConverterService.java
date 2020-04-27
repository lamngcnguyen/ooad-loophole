package com.uet.ooadloophole.service;

import com.uet.ooadloophole.config.Constants;
import com.uet.ooadloophole.controller.interface_model.dto.*;
import com.uet.ooadloophole.controller.interface_model.interfaces.*;
import com.uet.ooadloophole.model.business.Class;
import com.uet.ooadloophole.model.business.*;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Converts Interface models to business models and business models to DTO models
 *
 */

@Service
public class ConverterService {
    @Autowired
    private RoleService roleService;
    @Autowired
    private ClassService classService;
    @Autowired
    private UserService userService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private SemesterService semesterService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private TopicSpecFileService topicSpecFileService;

    public LoopholeUser convertUserInterface(IUser iUser) throws BusinessServiceException {
        Set<Role> roles = new HashSet<>();
        LoopholeUser user = new LoopholeUser();
        user.setUsername(iUser.getUsername());
        user.setFullName(iUser.getFullName());
        user.setEmail(iUser.getEmail());
        user.setPhoneNumber(iUser.getPhoneNumber());
        if (iUser.isAdmin()) {
            Role role = roleService.getByName(Constants.ROLE_ADMIN);
            roles.add(role);
        }
        if (iUser.isTeacher()) {
            Role role = roleService.getByName(Constants.ROLE_TEACHER);
            roles.add(role);
        }
        user.setRoles(roles);
        return user;
    }

    public Student convertStudentInterface(IStudent iStudent) {
        Student student = new Student();
        student.setFullName(iStudent.getFullName());
        student.setStudentId(iStudent.getStudentId());
        student.setClassId(iStudent.getClassId());
        return student;
    }

    public Semester convertSemesterInterface(ISemester iSemester) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        Semester semester = new Semester();
        semester.setName(iSemester.getName());
        semester.setStartDate(LocalDate.parse(iSemester.getStartDate(), formatter));
        semester.setEndDate(LocalDate.parse(iSemester.getEndDate(), formatter));
        return semester;
    }

    public DTOStudent convertToDTOStudent(Student student) throws BusinessServiceException {
        DTOStudent dtoStudent = new DTOStudent();
        Class ooadClass = classService.getById(student.getClassId());
        LoopholeUser user = userService.getById(student.getUserId());
        dtoStudent.set_id(student.get_id());
        dtoStudent.setStudentId(student.getStudentId());
        dtoStudent.setClassId(student.getClassId());
        dtoStudent.setClassName(ooadClass.getClassName());
        dtoStudent.setUserId(user.get_id());
        dtoStudent.setFullName(user.getFullName());
        dtoStudent.setEmail(user.getEmail());
        dtoStudent.setActive(user.isActive());
        return dtoStudent;
    }

    public DTOClass convertToDTOClass(Class ooadClass) throws BusinessServiceException {
        DTOClass dtoClass = new DTOClass();
        LoopholeUser user = userService.getById(ooadClass.getTeacherId());
        Semester semester = semesterService.getById(ooadClass.getSemesterId());

        dtoClass.set_id(ooadClass.get_id());
        dtoClass.setClassName(ooadClass.getClassName());
        dtoClass.setTeacherName(user.getFullName());
        dtoClass.setTeacherId(user.get_id());
        dtoClass.setSemesterName(semester.getName());
        dtoClass.setSemesterId(semester.get_id());
        dtoClass.setScheduledDayOfWeek(ooadClass.getScheduledDayOfWeek());
        dtoClass.setStudentCount(studentService.countByClassId(ooadClass.get_id()));
        dtoClass.setActive(ooadClass.isActive());
        return dtoClass;
    }

    public Student convertToStudent(IStudent iStudent) {
        Student student = new Student();
        student.setStudentId(iStudent.getStudentId());
        student.setFullName(iStudent.getFullName());
        student.setClassId(iStudent.getClassId());
        return student;
    }

    public DTOSemester convertToDTOSemester(Semester semester) {
        DTOSemester dtoSemester = new DTOSemester();
        dtoSemester.set_id(semester.get_id());
        dtoSemester.setName(semester.getName());
        dtoSemester.setStartDate(semester.getStartDate().toString());
        dtoSemester.setEndDate(semester.getEndDate().toString());
        return dtoSemester;
    }

    public DTOUser convertToDTOUser(LoopholeUser user) {
        DTOUser dtoUser = new DTOUser();
        dtoUser.set_id(user.get_id());
        dtoUser.setUsername(user.getUsername());
        dtoUser.setEmail(user.getEmail());
        dtoUser.setFullName(user.getFullName());
        dtoUser.setAvatar(user.getAvatar());
        dtoUser.setRoles(user.getRoles());
        dtoUser.setActive(user.isActive());
        dtoUser.setPhoneNumber(user.getPhoneNumber());
        return dtoUser;
    }

    public DTOTopic convertToDTOTopic(Topic topic) throws BusinessServiceException {
        DTOTopic dtoTopic = new DTOTopic();
        dtoTopic.set_id(topic.get_id());
        dtoTopic.setName(topic.getName());
        dtoTopic.setDescriptions(topic.getDescriptions());
        dtoTopic.setClassId(topic.getClassId());
        dtoTopic.setGroupId(topic.getGroupId());
        if (topic.getGroupId() != null) {
            try {
                Group group = groupService.getById(topic.getGroupId());
                if (group != null) {
                    dtoTopic.setGroupName(group.getGroupName());
                }
            } catch (BusinessServiceException e) {
                System.out.println(e.getMessage());
            }
        }
        List<TopicSpecFile> topicSpecFiles = topicSpecFileService.getByTopicId(topic.get_id());
        dtoTopic.setFiles(topicSpecFiles);

        return dtoTopic;
    }

    public DTOGroup convertToDTOGroup(Group group) {
        DTOGroup dtoGroup = new DTOGroup();
        dtoGroup.set_id(group.get_id());
        dtoGroup.setGroupName(group.getGroupName());
        dtoGroup.setLeader(group.getLeader());
        List<Student> studentList = studentService.getByGroupIdExcludingLeader(group.getLeader().get_id(), group.get_id());
        dtoGroup.setMembers(studentList);
        return dtoGroup;
    }

    public Group convertToGroup(IGroup iGroup) throws BusinessServiceException {
        Group group = new Group();
        group.set_id(iGroup.get_id());
        group.setGroupName(iGroup.getGroupName());
        group.setClassId(iGroup.getClassId());
        group.setLeader(studentService.getById(iGroup.getLeaderId()));
        return group;
    }

    public String formatFileName(String fileName, String timeStamp, String extension) {
        return fileName.substring(0, fileName.lastIndexOf(".")) + "_" + timeStamp + "." + extension;
    }

    public Notification convertToNotification(INotification iNotification) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        Notification notification = new Notification();
        notification.setSeen(false);
        notification.setSubject(iNotification.getSubject());
        notification.setContent(NotificationTemplateFactory.getContent(iNotification.getType(), iNotification.getTemplateValues()));
        notification.setReceiverId(iNotification.getReceiverId());
        notification.setTimeStamp(timeStamp);
        notification.setUrl("/home");
        return notification;
    }

    public Assignment convertAssignmentInterface(IAssignment iAssignment) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        Assignment assignment = new Assignment();
        assignment.setName(iAssignment.getName());
        assignment.setDescription(iAssignment.getDescription());
        assignment.setClassId(iAssignment.getClassId());
        assignment.setDeadline(LocalDate.parse(iAssignment.getDeadline(), formatter));
        return assignment;
    }

    public DTOAssignment convertToDTOAssignment(Assignment assignment) {
        DTOAssignment dtoAssignment = new DTOAssignment();
        dtoAssignment.set_id(assignment.get_id());
        dtoAssignment.setName(assignment.getName());
        dtoAssignment.setClassId(assignment.getClassId());
        dtoAssignment.setDescription(assignment.getDescription());
        dtoAssignment.setDeadline(assignment.getDeadline().toString());
        return dtoAssignment;
    }

    public Iteration convertIterationInterface(IIteration iIteration) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        Iteration iteration = new Iteration();
        iteration.setName(iIteration.getName());
        iteration.setGroupId(iIteration.getGroupId());
        iteration.setObjective(iIteration.getObjective());
        iteration.setStartDateTime(LocalDate.parse(iIteration.getStartDateTime(), formatter));
        iteration.setEndDateTime(LocalDate.parse(iIteration.getEndDateTime(), formatter));
        return iteration;
    }

    public DTOIteration convertToDTOIteration(Iteration iteration) {
        DTOIteration dtoIteration = new DTOIteration();
        dtoIteration.setName(iteration.getName());
        dtoIteration.setGroupId(iteration.getGroupId());
        dtoIteration.setObjective(iteration.getObjective());
        dtoIteration.setStartDateTime(iteration.getStartDateTime().toString());
        dtoIteration.setEndDateTime(iteration.getEndDateTime().toString());
        return dtoIteration;
    }
}
