package com.uet.ooadloophole.database.grading_repositories;

import com.uet.ooadloophole.model.business.grading_elements.GradingTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GradingTemplateRepository extends MongoRepository<GradingTemplate, String> {
	GradingTemplate findBy_id(String _id);

	List<GradingTemplate> findAllByTeacherId(String teacherId);

	List<GradingTemplate> findAllBySprintId(String sprintId);
}
