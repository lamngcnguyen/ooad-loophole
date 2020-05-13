package com.uet.ooadloophole.controller.api;

import com.google.gson.Gson;
import com.uet.ooadloophole.controller.interface_model.ResponseMessage;
import com.uet.ooadloophole.controller.interface_model.TableDataWrapper;
import com.uet.ooadloophole.controller.interface_model.dto.DTODiscipline;
import com.uet.ooadloophole.model.business.Discipline;
import com.uet.ooadloophole.model.business.DisciplineFileType;
import com.uet.ooadloophole.service.ConverterService;
import com.uet.ooadloophole.service.business_service.DisciplineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/disciplines")
public class ApiDisciplineController {
    @Autowired
    private DisciplineService disciplineService;
    @Autowired
    private ConverterService converterService;

    private Gson gson = new Gson();

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getDisciplines() {
        List<Discipline> disciplineList = disciplineService.getAllDisciplines();
        List<DTODiscipline> dtoDisciplines = new ArrayList<>();
        for (Discipline discipline : disciplineList) {
            dtoDisciplines.add(converterService.convertToDTODiscipline(discipline));
        }
        return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new TableDataWrapper(dtoDisciplines)));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<DTODiscipline> getDiscipline(@PathVariable String id) {
        DTODiscipline dtoDiscipline = converterService.convertToDTODiscipline(disciplineService.getById(id));
        return ResponseEntity.status(HttpStatus.OK).body(dtoDiscipline);
    }

    @RequestMapping(value = "/fileType/{id}", method = RequestMethod.GET)
    public ResponseEntity<DisciplineFileType> getFileType(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(disciplineService.getFileType(id));
    }

    @RequestMapping(value = "/fileType", method = RequestMethod.POST)
    public ResponseEntity<DisciplineFileType> createNewFileType(@RequestBody DisciplineFileType disciplineFileType) {
        return ResponseEntity.status(HttpStatus.OK).body(disciplineService.createFileType(disciplineFileType));
    }

    @RequestMapping(value = "/fileType/{id}", method = RequestMethod.PUT)
    public ResponseEntity<DisciplineFileType> editNewFileType(@PathVariable String id, @RequestBody DisciplineFileType disciplineFileType) {
        return ResponseEntity.status(HttpStatus.OK).body(disciplineService.editFileType(id, disciplineFileType));
    }

    @RequestMapping(value = "/fileType/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteFileType(@PathVariable String id) {
        disciplineService.deleteFileType(id);
        return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new ResponseMessage("deleted")));
    }
}
