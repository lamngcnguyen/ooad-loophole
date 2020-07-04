package com.uet.ooadloophole.controller.api;

import com.google.gson.Gson;
import com.uet.ooadloophole.controller.interface_model.TableDataWrapper;
import com.uet.ooadloophole.model.business.grading_elements.Criteria;
import com.uet.ooadloophole.model.business.grading_elements.GradingTemplate;
import com.uet.ooadloophole.model.business.system_elements.LoopholeUser;
import com.uet.ooadloophole.controller.interface_model.ResponseMessage;
import com.uet.ooadloophole.service.SecureUserService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.GradingTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/grading-template")
public class ApiGradingTemplateController {
    @Autowired
    private SecureUserService secureUserService;
    @Autowired
    private GradingTemplateService gradingTemplateService;

    private Gson gson = new Gson();

    private boolean userCannotCreateGradingTemplate() throws BusinessServiceException {
        LoopholeUser user = secureUserService.getCurrentUser();
        return (!user.hasRole("teacher") && !user.hasRole("admin"));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> createGradingTemplate(@RequestBody GradingTemplate gradingTemplate) throws BusinessServiceException {
        gradingTemplate.setTeacherId(secureUserService.getCurrentUser().get_id());
        GradingTemplate newGradingTemplate = gradingTemplateService.create(gradingTemplate);
        return ResponseEntity.status(HttpStatus.OK).body(newGradingTemplate);
    }

    @RequestMapping(value = "/teacher/{teacherId}",method = RequestMethod.GET)
    public ResponseEntity<Object> getGradingTemplateByTeacherId(@PathVariable String teacherId) {
        try {
            List<GradingTemplate> result = gradingTemplateService.getByTeacherId(teacherId);
            return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new TableDataWrapper(result)));
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/{templateId}/criteria", method = RequestMethod.GET)
    public ResponseEntity<List<Criteria>> createCriteria(@PathVariable String templateId) {
        return ResponseEntity.status(HttpStatus.OK).body(gradingTemplateService.getAllCriteriaByTemplateId(templateId));
    }

    @RequestMapping(value = "/{templateId}/criteria", method = RequestMethod.POST)
    public ResponseEntity<Criteria> createCriteria(@PathVariable String templateId, @RequestBody Criteria criteria) {
        return ResponseEntity.status(HttpStatus.OK).body(gradingTemplateService.createCriteria(templateId, criteria));
    }

    @RequestMapping(value = "/criteria/{criteriaId}", method = RequestMethod.PUT)
    public ResponseEntity<Criteria> editCriteria(@PathVariable String criteriaId, @RequestBody Criteria criteria) {
        return ResponseEntity.status(HttpStatus.OK).body(gradingTemplateService.editCriteria(criteriaId, criteria));
    }

    @RequestMapping(value = "/criteria/{criteriaId}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteCriteria(@PathVariable String criteriaId) {
		try {
			gradingTemplateService.deleteCriteria(criteriaId);
			return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new ResponseMessage("success")));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
