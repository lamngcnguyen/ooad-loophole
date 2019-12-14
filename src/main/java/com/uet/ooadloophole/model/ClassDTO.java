package com.uet.ooadloophole.model;

public class ClassDTO {
    public String classId;
    public String className;
    public int numberOfStudents;
    public String startDate;
    public String endDate;

    public ClassDTO(Class data, int numberOfStudents) {
        this.classId = data.get_id();
        this.className = data.getClassName();
        this.startDate = data.getStartDate();
        this.endDate = data.getEndDate();
        this.numberOfStudents = numberOfStudents;
    }
}
