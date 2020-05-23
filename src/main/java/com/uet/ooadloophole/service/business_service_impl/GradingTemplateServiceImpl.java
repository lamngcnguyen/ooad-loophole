package com.uet.ooadloophole.service.business_service_impl;


import com.uet.ooadloophole.database.CriteriaRepository;
import com.uet.ooadloophole.database.GradingTemplateRepository;
import com.uet.ooadloophole.model.business.Criteria;
import com.uet.ooadloophole.model.business.GradingTemplate;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.GradingTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GradingTemplateServiceImpl implements GradingTemplateService {
    @Autowired
    private GradingTemplateRepository gradingTemplateRepository;
    @Autowired
    private CriteriaRepository criteriaRepository;

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
    public GradingTemplate getById(String id) {
        return gradingTemplateRepository.findBy_id(id);
    }

    @Override
    public GradingTemplate create(GradingTemplate gradingTemplate) {
        GradingTemplate newGradingTemplate = gradingTemplateRepository.save(gradingTemplate);
        return gradingTemplate;
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

    @Override
    public Criteria editCriteria(String id, Criteria criteria) {
        Criteria dbCriteria = criteriaRepository.findBy_id(id);
        dbCriteria.setName(criteria.getName());
        dbCriteria.setWeight(criteria.getWeight());
        dbCriteria.setDescription(criteria.getDescription());
        dbCriteria.setLevels(criteria.getLevels());
        return criteriaRepository.save(dbCriteria);
    }

    @Override
    public Criteria createCriteria(String templateId, Criteria criteria) {
        criteriaRepository.save(criteria);
        GradingTemplate template = gradingTemplateRepository.findBy_id(templateId);
        if (template.getCriteria() == null) {
            template.setCriteria(new ArrayList<>());
        }
        template.getCriteria().add(criteria);
        gradingTemplateRepository.save(template);
        return criteria;
    }

    @Override
    public List<Criteria> getAllCriteriaByTemplateId(String templateId) {
        GradingTemplate template = getById(templateId);
        return template.getCriteria();
    }

	@Override
	public void deleteCriteria(String criteriaId) {
		Criteria criteriaToDelete = criteriaRepository.findBy_id(criteriaId);
		criteriaRepository.delete(criteriaToDelete);
	}
}
