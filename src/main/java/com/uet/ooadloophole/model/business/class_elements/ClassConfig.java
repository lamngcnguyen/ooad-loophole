package com.uet.ooadloophole.model.business.class_elements;


import java.time.LocalDate;

public class ClassConfig {
    private int groupLimitMax;
    private int groupLimitMin;
    private LocalDate groupRegistrationDeadline;
    private int defaultIterationLength;
    private int maxIterationLength;
    private LocalDate iterationSetupDeadline;

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
