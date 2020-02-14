package com.uet.ooadloophole.controller;

import com.uet.ooadloophole.model.business.Semester;
import com.uet.ooadloophole.service.business_service.SemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping(value = "/api/semesters")
public class ApiSemesterController {
    @Autowired
    private SemesterService semesterService;

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<Semester> createSemester(String name, String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MM-yyyy");

        Semester semester = new Semester();
        semester.setName(name);
        semester.setStartDate(LocalDate.parse(startDate, formatter));
        semester.setEndDate(LocalDate.parse(endDate, formatter));
        semesterService.create(semester);

        return ResponseEntity.status(HttpStatus.OK).body(semester);
    }
}
