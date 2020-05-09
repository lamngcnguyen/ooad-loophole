package com.uet.ooadloophole.model.business;

import org.springframework.data.annotation.Id;

import java.util.List;

public class Criteria {
	@Id
	private String _id;
	private String criteriaName;
	private float weight;
	private String templateId;
	private List<Level> levels;

	public Criteria() {

	}

	public Criteria(String criteriaName, float weight, String templateId) {
		this.criteriaName = criteriaName;
		this.weight = weight;
		this.templateId = templateId;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getCriteriaName() {
		return criteriaName;
	}

	public void setCriteriaName(String criteriaName) {
		this.criteriaName = criteriaName;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
}
