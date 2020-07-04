package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.database.class_repositories.AssignmentRepository;
import com.uet.ooadloophole.database.group_repositories.RepoFileRepository;
import com.uet.ooadloophole.model.business.class_elements.Assignment;
import com.uet.ooadloophole.model.business.group_elements.RepoFile;
import com.uet.ooadloophole.service.business_service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AssignmentServiceImpl implements AssignmentService {
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private RepoFileRepository repoFileRepository;

    @Override
    public Assignment getById(String id) {
        return assignmentRepository.findBy_id(id);
    }

    @Override
    public List<Assignment> getAllByClass(String classId) {
        return assignmentRepository.findByClassId(classId);
    }

    @Override
    public Assignment create(Assignment assignment) {
        return assignmentRepository.save(assignment);
    }

    @Override
    public Assignment edit(String id, Assignment assignment) {
        Assignment dbAssignment = getById(id);
        dbAssignment.setName(assignment.getName());
        dbAssignment.setDescription(assignment.getDescription());
        dbAssignment.setDeadline(assignment.getDeadline());
        dbAssignment.setGradingTemplateId(assignment.getGradingTemplateId());
        return assignmentRepository.save(dbAssignment);
    }

    @Override
    public void delete(String id) {
        assignmentRepository.delete(getById(id));
    }

    @Override
    public List<RepoFile> getStudentAssignmentWork(String assignmentId, String groupId) {
        Assignment assignment = getById(assignmentId);
        LocalDate deadline = assignment.getDeadline();
        LocalDateTime dateTime = deadline.atTime(0, 0, 0);
        return repoFileRepository.findByGroupIdAndTimeStampBefore(groupId, dateTime);
    }
}
