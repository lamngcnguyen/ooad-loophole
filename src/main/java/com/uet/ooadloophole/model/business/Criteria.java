package com.uet.ooadloophole.model.business;

import org.springframework.data.annotation.Id;

import java.util.List;

public class Criteria {
	private String criteriaName;
	private float weight;
	private List<Level> levels;

	public Criteria() {

	}

	public Criteria(String criteriaName, float weight) {
		this.criteriaName = criteriaName;
		this.weight = weight;
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

	public void addLevel(Level level) {
		levels.add(level);
	}

	public void removeLevel(Level level) {
		levels.remove(level);
	}

	public float getPoint() {
		float result = 0;
		for (Level lv : levels) {
			if (lv.isSelected()) {
				result = lv.getWeight() * this.weight;
			}
		}
		return result;
	}
}
