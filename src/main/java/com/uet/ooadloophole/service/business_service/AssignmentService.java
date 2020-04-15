package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.business.Assignment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AssignmentService {
    Assignment getById(String id);

    List<Assignment> getByClass(String classId);

    Assignment create(Assignment assignment);
}
