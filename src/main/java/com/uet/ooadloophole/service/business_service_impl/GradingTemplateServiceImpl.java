package com.uet.ooadloophole.service.business_service_impl;


import com.uet.ooadloophole.database.GradingTemplateRepository;
import com.uet.ooadloophole.model.business.GradingTemplate;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.GradingTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GradingTemplateServiceImpl implements GradingTemplateService {
    @Autowired
    private GradingTemplateRepository gradingTemplateRepository;

    @Override
    public List<GradingTemplate> getAll() {
        return gradingTemplateRepository.findAll();
    }

    @Override
    public List<GradingTemplate> getByTeacherId(String teacherId) throws BusinessServiceException {
        return gradingTemplateRepository.findAllByTeacherId(teacherId);
    }

    @Override
    public List<GradingTemplate> getBySprintId(String sprintId) throws BusinessServiceException {
        return gradingTemplateRepository.findAllBySprintId(sprintId);
    }

    @Override
    public GradingTemplate getById(String id) throws BusinessServiceException {
        return gradingTemplateRepository.findBy_id(id);
    }

    @Override
    public GradingTemplate create(GradingTemplate gradingTemplate) {
        GradingTemplate newGradingTemplate = gradingTemplateRepository.save(gradingTemplate);
        return gradingTemplate;
    }

    @Override
    public void delete(String gradingTemplateId) throws BusinessServiceException {
        GradingTemplate gradingTemplateToDelete = getById(gradingTemplateId);
        gradingTemplateRepository.delete(gradingTemplateToDelete);
    }

    @Override
    public GradingTemplate update(String gradingTemplateId, GradingTemplate gradingTemplate) throws BusinessServiceException {
        GradingTemplate dbGradingTemplate = gradingTemplateRepository.findBy_id(gradingTemplateId);
        dbGradingTemplate.setCriteria(gradingTemplate.getCriteria());
        dbGradingTemplate.setGradingTemplateName(gradingTemplate.getGradingTemplateName());
        dbGradingTemplate.setSprintId(gradingTemplate.getSprintId());
        dbGradingTemplate.setTeacherId(gradingTemplate.getTeacherId());
        gradingTemplateRepository.save(dbGradingTemplate);
        return dbGradingTemplate;
    }


}
