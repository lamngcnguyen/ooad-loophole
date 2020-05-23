package com.uet.ooadloophole.model.business.grading_elements;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;
import java.util.List;

public class GradingTemplate {
	@Id
	private String _id;
	private String teacherId;
	private String sprintId;
	private String gradingTemplateName;
	@DBRef
	private List<Criteria> criteria;
	private LocalDateTime timestamp;

	GradingTemplate() {

	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getSprintId() {
		return sprintId;
	}

	public void setSprintId(String sprintId) {
		this.sprintId = sprintId;
	}

	public String getGradingTemplateName() {
		return gradingTemplateName;
	}

	public void setGradingTemplateName(String gradingTemplateName) {
		this.gradingTemplateName = gradingTemplateName;
	}

	public List<Criteria> getCriteria() {
		return criteria;
	}

	public void setCriteria(List<Criteria> criteria) {
		this.criteria = criteria;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
}
