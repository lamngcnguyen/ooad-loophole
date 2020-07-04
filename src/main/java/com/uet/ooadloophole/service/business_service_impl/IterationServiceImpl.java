package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.database.rup_repositories.IterationRepository;
import com.uet.ooadloophole.model.business.rup_elements.Iteration;
import com.uet.ooadloophole.service.business_service.IterationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    public Iteration edit(String id, Iteration iteration) {
        Iteration dbIteration = iterationRepository.findBy_id(id);
        dbIteration.setName(iteration.getName());
        dbIteration.setObjective(iteration.getObjective());
        dbIteration.setStartDateTime(iteration.getStartDateTime());
        dbIteration.setEndDateTime(iteration.getEndDateTime());
        return iterationRepository.save(dbIteration);
    }

    @Override
    public void delete(String id) {
        iterationRepository.delete(getById(id));
    }

    @Override
    public Iteration getCurrentIteration(String groupId) {
        return iterationRepository.findByStartDateTimeBeforeAndEndDateTimeAfterAndGroupId(LocalDate.now(), LocalDate.now(), groupId);
    }
}
