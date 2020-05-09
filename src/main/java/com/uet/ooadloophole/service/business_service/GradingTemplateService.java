package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.business.GradingTemplate;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GradingTemplateService {
	List<GradingTemplate> getAll();

	List<GradingTemplate> getByTeacherId(String teacherId) throws BusinessServiceException;

	//Cần trao đổi lại xem 1 sprint có thể có nhiều template ko
	List<GradingTemplate> getByTeacherIdAndSprintId(String teacherId, String sprintId) throws BusinessServiceException;

	GradingTemplate getById(String id) throws BusinessServiceException;

	GradingTemplate create(GradingTemplate gradingTemplate);

	void delete(String gradingTemplateId) throws BusinessServiceException;

	GradingTemplate update(String gradingTemplateId, GradingTemplate gradingTemplate) throws BusinessServiceException;


}

