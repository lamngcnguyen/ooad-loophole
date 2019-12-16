package com.uet.ooadloophole.model.dto;

import com.uet.ooadloophole.model.Iteration;

import java.time.format.DateTimeFormatter;

public class IterationDTO {
    public String id;
    public String startDateTime;
    public String endDateTime;
    public String message;

    public IterationDTO(Iteration data) {
        this.id = data.get_id();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm MM/dd/yyyy");
        this.startDateTime = data.getStartDateTime().format(dateTimeFormatter);
        this.endDateTime = data.getEndDateTime().format(dateTimeFormatter);
        this.message = data.getMessage();
    }
}
