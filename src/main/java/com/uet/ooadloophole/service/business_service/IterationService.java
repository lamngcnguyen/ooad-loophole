package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.business.Iteration;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IterationService {
    Iteration getById(String id);
    Iteration create(Iteration iteration);
    List<Iteration> getAllByGroup(String groupId);

    void delete(String id);
}
