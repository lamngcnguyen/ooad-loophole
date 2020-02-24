package com.uet.ooadloophole.controller;

import com.google.gson.Gson;
import com.uet.ooadloophole.controller.interface_model.ISemester;
import com.uet.ooadloophole.controller.interface_model.TableDataWrapper;
import com.uet.ooadloophole.model.business.Semester;
import com.uet.ooadloophole.service.InterfaceModelConverterService;
import com.uet.ooadloophole.service.business_service.SemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/semesters")
public class ApiSemesterController {
    @Autowired
    private SemesterService semesterService;
    @Autowired
    private InterfaceModelConverterService interfaceModelConverterService;

    private Gson gson = new Gson();

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<Semester> createSemester(@RequestBody ISemester iSemester) {
        Semester semester = semesterService.create(
                interfaceModelConverterService.convertSemesterInterface(iSemester));
        return ResponseEntity.status(HttpStatus.OK).body(semester);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<String> getClasses() {
        return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new TableDataWrapper(semesterService.getAll())));
    }
}
