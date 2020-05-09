package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.database.IterationRepository;
import com.uet.ooadloophole.model.business.Iteration;
import com.uet.ooadloophole.service.business_service.IterationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IterationServiceImpl implements IterationService {
    @Autowired
    private IterationRepository iterationRepository;

    @Override
    public Iteration getById(String id) {
        return iterationRepository.findBy_id(id);
    }

    @Override
    public Iteration create(Iteration iteration) {
        return iterationRepository.save(iteration);
    }

    @Override
    public List<Iteration> getAllByGroup(String groupId) {
        return iterationRepository.findAllByGroupId(groupId);
    }

    @Override
    public void delete(String id) {
        iterationRepository.delete(getById(id));
    }
}
