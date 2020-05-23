package com.uet.ooadloophole.controller.api;

import com.google.gson.Gson;
import com.uet.ooadloophole.controller.interface_model.ResponseMessage;
import com.uet.ooadloophole.controller.interface_model.TableDataWrapper;
import com.uet.ooadloophole.controller.interface_model.dto.DTOIteration;
import com.uet.ooadloophole.controller.interface_model.interfaces.IIteration;
import com.uet.ooadloophole.model.business.rup_elements.Iteration;
import com.uet.ooadloophole.service.ConverterService;
import com.uet.ooadloophole.service.business_service.IterationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/iterations")
public class ApiIterationController {
    @Autowired
    private ConverterService converterService;
    @Autowired
    private IterationService iterationService;

    private Gson gson = new Gson();

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Iteration> createIteration(@RequestBody IIteration iIteration) {
        Iteration iteration = converterService.convertIterationInterface(iIteration);
        return ResponseEntity.status(HttpStatus.OK).body(iterationService.create(iteration));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<DTOIteration> getById(@PathVariable String id) {
        DTOIteration dtoIteration = converterService.convertToDTOIteration(iterationService.getById(id));
        return ResponseEntity.status(HttpStatus.OK).body(dtoIteration);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteById(@PathVariable String id) {
        iterationService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new ResponseMessage("deleted")));
    }

    @RequestMapping(value = "/{groupId}/group", method = RequestMethod.GET)
    public ResponseEntity<String> getByGroup(@PathVariable String groupId) {
        List<DTOIteration> dtoIterations = new ArrayList<>();
        List<Iteration> iterations = iterationService.getAllByGroup(groupId);
        for (Iteration iteration : iterations) {
            DTOIteration dtoIteration = converterService.convertToDTOIteration(iteration);
            dtoIterations.add(dtoIteration);
        }
        return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new TableDataWrapper(dtoIterations)));
    }
}
