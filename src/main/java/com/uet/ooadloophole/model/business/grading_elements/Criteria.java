package com.uet.ooadloophole.model.business.grading_elements;

import org.springframework.data.annotation.Id;

import java.util.List;

public class Criteria {
	@Id
	private String _id;
	private String name;
	private float weight;
	private String description;
	private List<Level> levels;

	public Criteria() {

	}

	public Criteria(String name, float weight) {
		this.name = name;
		this.weight = weight;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public List<Level> getLevels() {
		return levels;
	}

	public void setLevels(List<Level> levels) {
		this.levels = levels;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
