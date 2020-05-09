package com.uet.ooadloophole.model.business;

import org.springframework.data.annotation.Id;

import java.time.LocalDate;

public class ClassConfig {
    @Id
    private String _id;
    private String classId;
    private int groupLimitMax;
    private int groupLimitMin;
    private LocalDate groupRegistrationDeadline;
    private int defaultIterationLength;
    private int maxIterationLength;
    private LocalDate iterationSetupDeadline;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public int getGroupLimitMax() {
        return groupLimitMax;
    }

    public void setGroupLimitMax(int groupLimitMax) {
        this.groupLimitMax = groupLimitMax;
    }

    public int getGroupLimitMin() {
        return groupLimitMin;
    }

    public void setGroupLimitMin(int groupLimitMin) {
        this.groupLimitMin = groupLimitMin;
    }

    public LocalDate getGroupRegistrationDeadline() {
        return groupRegistrationDeadline;
    }

    public void setGroupRegistrationDeadline(LocalDate groupRegistrationDeadline) {
        this.groupRegistrationDeadline = groupRegistrationDeadline;
    }

    public int getDefaultIterationLength() {
        return defaultIterationLength;
    }

    public void setDefaultIterationLength(int defaultIterationLength) {
        this.defaultIterationLength = defaultIterationLength;
    }

    public int getMaxIterationLength() {
        return maxIterationLength;
    }

    public void setMaxIterationLength(int maxIterationLength) {
        this.maxIterationLength = maxIterationLength;
    }

    public LocalDate getIterationSetupDeadline() {
        return iterationSetupDeadline;
    }

    public void setIterationSetupDeadline(LocalDate iterationSetupDeadline) {
        this.iterationSetupDeadline = iterationSetupDeadline;
    }
}
