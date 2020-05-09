package com.uet.ooadloophole.controller.api;

import com.google.gson.Gson;
import com.uet.ooadloophole.model.business.GradingTemplate;
import com.uet.ooadloophole.model.business.LoopholeUser;
import com.uet.ooadloophole.service.SecureUserService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.GradingTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/gradingTemplate")
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
	public ResponseEntity<Object> createGradingTemplate(@RequestBody GradingTemplate gradingTemplate) {
		GradingTemplate newGradingTemplate = gradingTemplateService.create(gradingTemplate);
		return ResponseEntity.status(HttpStatus.OK).body(newGradingTemplate);
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<Object> getGradingTemplateByTeacherId(String teacherId) {
		try {
			List<GradingTemplate> result = gradingTemplateService.getByTeacherId(teacherId);
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (BusinessServiceException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
