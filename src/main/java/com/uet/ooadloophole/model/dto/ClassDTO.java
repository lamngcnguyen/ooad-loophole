package com.uet.ooadloophole.model.dto;

import com.uet.ooadloophole.model.business.Class;

public class ClassDTO {
    public String classId;
    public String className;
    public int numberOfStudents;
    public String startDate;
    public String endDate;
    public String dayOfWeek;

    public ClassDTO(Class data, int numberOfStudents) {
        this.classId = data.get_id();
        this.className = data.getClassName();
//        this.startDate = data.getStartDate().toString();
//        this.endDate = data.getEndDate().toString();
        this.numberOfStudents = numberOfStudents;
        this.dayOfWeek = getDayOfWeek(data.getScheduledDayOfWeek());
    }

    public static String getDayOfWeek(int value) {
        switch (value) {
            case 2:
                return "Monday";
            case 3:
                return "Tuesday";
            case 4:
                return "Wednesday";
            case 5:
                return  "Thursday";
            case 6:
                return "Friday";
            case 7:
                return "Saturday";
            default:
                return "???";
        }
    }
}
