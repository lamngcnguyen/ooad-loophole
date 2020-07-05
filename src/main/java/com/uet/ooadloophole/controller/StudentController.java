package com.uet.ooadloophole.controller;

import com.uet.ooadloophole.controller.interface_model.BodyFragment;
import com.uet.ooadloophole.controller.interface_model.dto.DTOAssignment;
import com.uet.ooadloophole.controller.interface_model.dto.DTOTopic;
import com.uet.ooadloophole.model.business.class_elements.Assignment;
import com.uet.ooadloophole.model.business.class_elements.Class;
import com.uet.ooadloophole.model.business.class_elements.ClassConfig;
import com.uet.ooadloophole.model.business.group_elements.Group;
import com.uet.ooadloophole.model.business.group_elements.Request;
import com.uet.ooadloophole.model.business.group_elements.WorkItem;
import com.uet.ooadloophole.model.business.group_elements.WorkItemLog;
import com.uet.ooadloophole.model.business.system_elements.LoopholeUser;
import com.uet.ooadloophole.model.business.system_elements.Student;
import com.uet.ooadloophole.service.ConverterService;
import com.uet.ooadloophole.service.MasterPageService;
import com.uet.ooadloophole.service.SecureUserService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/student")
public class StudentController {
    @Autowired
    private SecureUserService secureUserService;
    @Autowired
    private MasterPageService masterPageService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private RequestService requestService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private TopicService topicService;
    @Autowired
    private WorkItemService workItemService;
    @Autowired
    private ClassService classService;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private ConverterService converterService;
    @Autowired
    private WorkItemLogService workItemLogService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getHomeView() {
        String pageTitle = "Student";
        try {
            LoopholeUser currentUser = secureUserService.getCurrentUser();
            return masterPageService.getMasterPage(pageTitle, new BodyFragment("student/home", "body-content"), currentUser);
        } catch (BusinessServiceException e) {
            return new ModelAndView("forbidden");
        }
    }

    @RequestMapping(value = "/invitation/{id}", method = RequestMethod.GET)
    public ModelAndView getInvitationView(@PathVariable String id) {
        try {
            Request request = requestService.getById(id);
            ModelAndView modelAndView;
            LoopholeUser currentUser = secureUserService.getCurrentUser();
            Student student = studentService.getByUserId(currentUser.get_id());
            if (request != null) {
                Group group = groupService.getById(request.getGroupId());
                String pageTitle = "You have been invited to " + group.getGroupName();
                modelAndView = masterPageService.getMasterPage(pageTitle, new BodyFragment("student/invitation", "body-content"), currentUser);
                modelAndView.addObject("invitation", request);
                modelAndView.addObject("group", group);
                modelAndView.addObject("student", student);
            } else {
                String pageTitle = "Invalid Invitation";
                modelAndView = masterPageService.getMasterPage(pageTitle, new BodyFragment("student/invitation", "invalid-content"), currentUser);
            }
            return modelAndView;
        } catch (BusinessServiceException e) {
            return new ModelAndView("forbidden");
        }
    }

    @RequestMapping(value = "/request/{id}", method = RequestMethod.GET)
    public ModelAndView getRequestView(@PathVariable String id) {
        try {
            Request request = requestService.getById(id);
            ModelAndView modelAndView;
            LoopholeUser currentUser = secureUserService.getCurrentUser();
            Student student = studentService.getByUserId(currentUser.get_id());
            if (request != null) {
                Student requester = studentService.getByUserId(request.getUserId());
                Group group = groupService.getById(request.getGroupId());
                String pageTitle = requester.getFullName() + " have requested to join " + group.getGroupName();
                modelAndView = masterPageService.getMasterPage(pageTitle, new BodyFragment("student/request", "body-content"), currentUser);
                modelAndView.addObject("request", request);
                modelAndView.addObject("group", group);
                modelAndView.addObject("student", student);
                modelAndView.addObject("requester", requester);
            } else {
                String pageTitle = "Invalid Invitation";
                modelAndView = masterPageService.getMasterPage(pageTitle, new BodyFragment("student/invitation", "invalid-content"), currentUser);
            }
            return modelAndView;
        } catch (BusinessServiceException e) {
            return new ModelAndView("forbidden");
        }
    }

    @RequestMapping(value = "/group", method = RequestMethod.GET)
    public ModelAndView getGroupView() throws BusinessServiceException {
        String pageTitle = "Group Setting";
        ModelAndView modelAndView = getStudentView(pageTitle, new BodyFragment("student/group", "content"));

        Date date = new Date();
        LocalDate today = LocalDate.from(date.toInstant().atZone(ZoneId.of("GMT+7")));
        Student student = studentService.getByUserId(secureUserService.getCurrentUser().get_id());
        ClassConfig classConfig = classService.getById(student.getClassId()).getConfig();
        boolean groupSetupDeadlineMet;
        try {
            groupSetupDeadlineMet = today.compareTo(classConfig.getIterationSetupDeadline()) > 0;
            modelAndView.addObject("config", classConfig);
        } catch (Exception e) {
            //TODO: Show class not configured page
            return new ModelAndView("error");
        }
        modelAndView.addObject("isSetupPhase", !groupSetupDeadlineMet);
        try {
            DTOTopic topic = converterService.convertToDTOTopic(topicService.getByGroupId(student.getGroupId()));
            modelAndView.addObject("topic", topic);
        } catch (Exception e) {
            modelAndView.addObject("topic", null);
        }
        return modelAndView;
    }

    @RequestMapping(value = "/iteration", method = RequestMethod.GET)
    public ModelAndView getIterationView() {
        try {
            Date date = new Date();
            LocalDate today = LocalDate.from(date.toInstant().atZone(ZoneId.of("GMT+7")));
            String pageTitle = "Iterations";
            LoopholeUser currentUser = secureUserService.getCurrentUser();
            Student student = studentService.getByUserId(currentUser.get_id());
            ClassConfig classConfig = classService.getClassConfig(student.getClassId());
            boolean iterationDeadlineMet;
            try {
                iterationDeadlineMet = today.compareTo(classConfig.getIterationSetupDeadline()) > 0;
            } catch (Exception e) {
                //TODO: Show class not configured page
                return new ModelAndView("error");
            }

            ModelAndView iterationView = getStudentView(pageTitle, new BodyFragment("student/iteration", "content"));
            iterationView.addObject("isSetupPhase", !iterationDeadlineMet);
            iterationView.addObject("groupId", student.getGroupId());
            iterationView.addObject("classId", student.getClassId());
            return iterationView;
        } catch (BusinessServiceException e) {
            return new ModelAndView("error");
        }
    }

    @RequestMapping(value = "/evaluation", method = RequestMethod.GET)
    public ModelAndView getEvaluationView() {
        String pageTitle = "Grading";
        return getStudentView(pageTitle, new BodyFragment("student/evaluation", "content"));
    }

    @RequestMapping(value = "/requirement", method = RequestMethod.GET)
    public ModelAndView getRequirementView() {
        String pageTitle = "Requirement Management";
        return getStudentView(pageTitle, new BodyFragment("student/requirement", "content"));
    }

    @RequestMapping(value = "/assignment", method = RequestMethod.GET)
    public ModelAndView getAssignmentView() {
        try {
            Student student = studentService.getByUserId(secureUserService.getCurrentUser().get_id());
            Class ooadClass = classService.getById(student.getClassId());
            String pageTitle = "Assignments";
            List<Assignment> assignments = assignmentService.getAllByClass(ooadClass.get_id());
            List<DTOAssignment> dtoAssignments = assignments.stream().map(assignment -> converterService.convertToDTOAssignment(assignment)).collect(Collectors.toList());
            ModelAndView modelAndView = getStudentView(pageTitle, new BodyFragment("student/assignment", "content"));
            modelAndView.addObject("assignments", dtoAssignments);
            return modelAndView;
        } catch (BusinessServiceException e) {
            return new ModelAndView("error");
        }
    }

    @RequestMapping(value = "/boards", method = RequestMethod.GET)
    public ModelAndView getBoardsView() {
        ModelAndView modelAndView;
        try {
            String pageTitle = "Boards";
            Student student = studentService.getByUserId(secureUserService.getCurrentUser().get_id());
            List<WorkItem> newItems = workItemService.getByGroupAndStatus(student.getGroupId(), "New");
            List<WorkItem> approvedItems = workItemService.getByGroupAndStatus(student.getGroupId(), "Approved");
            List<WorkItem> committedItems = workItemService.getByGroupAndStatus(student.getGroupId(), "Committed");
            List<WorkItem> domeItems = workItemService.getByGroupAndStatus(student.getGroupId(), "Done");
            modelAndView = getStudentView(pageTitle, new BodyFragment("student/boards", "content"));
            modelAndView.addObject("newItems", newItems);
            modelAndView.addObject("approvedItems", approvedItems);
            modelAndView.addObject("committedItems", committedItems);
            modelAndView.addObject("doneItems", domeItems);
            newItems.forEach(System.out::println);
            return modelAndView;
        } catch (BusinessServiceException e) {
            modelAndView = new ModelAndView("error");
            return modelAndView;
        }
    }

    @RequestMapping(value = "/work-item/{id}", method = RequestMethod.GET)
    public ModelAndView getWorkItemView(@PathVariable String id) {
        WorkItem workItem = workItemService.getById(id);
        ModelAndView modelAndView = getStudentView("Work Item", new BodyFragment("student/work-item", "content"));
        List<WorkItemLog> logs = workItemLogService.getByTask(workItem);
        Collections.reverse(logs);
        modelAndView.addObject("workItem", workItem);
        modelAndView.addObject("logs", logs);
        return modelAndView;
    }

    private ModelAndView getStudentView(String pageTitle, BodyFragment bodyFragment) {
        ModelAndView modelAndView;
        try {
            LoopholeUser currentUser = secureUserService.getCurrentUser();
            if (currentUser.hasRole("student")) {
                Student student = studentService.getByUserId(currentUser.get_id());
                if (student.getGroupId() == null) {
                    modelAndView = masterPageService.getMasterPage(pageTitle, new BodyFragment("student/unassigned", "body-content"), currentUser);
                } else {
                    Group group = groupService.getById(student.getGroupId());
                    String roleFolder = currentUser.hasRole("group_leader") ? "leader" : "member";
                    bodyFragment.setFragment(roleFolder + "-" + bodyFragment.getFragment());
                    modelAndView = masterPageService.getMasterPage(pageTitle, bodyFragment, currentUser);
                    modelAndView.addObject("group", group);
                }
                modelAndView.addObject("student", student);
            } else {
                modelAndView = new ModelAndView("forbidden");
            }
        } catch (BusinessServiceException e) {
            modelAndView = new ModelAndView("error");
        }
        return modelAndView;
    }
}
