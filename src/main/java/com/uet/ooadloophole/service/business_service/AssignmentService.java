package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.business.class_elements.Assignment;
import com.uet.ooadloophole.model.business.group_elements.RepoFile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AssignmentService {
    Assignment getById(String id);

    List<Assignment> getAllByClass(String classId);

    Assignment create(Assignment assignment);

    Assignment edit(String id, Assignment assignment);

    void delete(String id);

    List<RepoFile> getStudentAssignmentWork(String assignmentId, String groupId);
}
