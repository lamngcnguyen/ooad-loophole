package com.uet.ooadloophole.controller.api;

import com.google.gson.Gson;
import com.uet.ooadloophole.controller.interface_model.dto.DTOSemester;
import com.uet.ooadloophole.controller.interface_model.interfaces.ISemester;
import com.uet.ooadloophole.controller.interface_model.ResponseMessage;
import com.uet.ooadloophole.controller.interface_model.TableDataWrapper;
import com.uet.ooadloophole.model.business.Semester;
import com.uet.ooadloophole.service.ConverterService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.SemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/semesters")
public class ApiSemesterController {
    @Autowired
    private SemesterService semesterService;
    @Autowired
    private ConverterService converterService;

    private Gson gson = new Gson();

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Semester> createSemester(@RequestBody ISemester iSemester) {
        Semester semester = semesterService.create(
                converterService.convertSemesterInterface(iSemester));
        return ResponseEntity.status(HttpStatus.OK).body(semester);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getSemesters() {
        List<DTOSemester> dtoSemesters = new ArrayList<>();
        semesterService.getAll().forEach(semester -> {
            dtoSemesters.add(converterService.convertToDTOSemester(semester));
        });
        return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new TableDataWrapper(dtoSemesters)));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateSemester(@PathVariable String id, @RequestBody ISemester iSemester) {
        try {
            Semester semester = converterService.convertSemesterInterface(iSemester);
            Semester updatedSemester = semesterService.update(id, semester);
            return ResponseEntity.status(HttpStatus.OK).body(updatedSemester);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(new ResponseMessage(e.getMessage())));
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteSemester(@PathVariable String id) {
        try {
            semesterService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new ResponseMessage("deleted")));
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(new ResponseMessage(e.getMessage())));
        }

    }
}
