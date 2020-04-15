package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.database.AssignmentRepository;
import com.uet.ooadloophole.model.business.Assignment;
import com.uet.ooadloophole.service.business_service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssignmentServiceImpl implements AssignmentService {
    @Autowired
    private AssignmentRepository assignmentRepository;

    @Override
    public Assignment getById(String id) {
        return assignmentRepository.findBy_id(id);
    }

    @Override
    public List<Assignment> getByClass(String classId) {
        return assignmentRepository.findByClassId(classId);
    }

    @Override
    public Assignment create(Assignment assignment) {
        return assignmentRepository.save(assignment);
    }
}
