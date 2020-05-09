package com.uet.ooadloophole.controller.api;

import com.google.gson.Gson;
import com.uet.ooadloophole.model.business.GradingTemplate;
import com.uet.ooadloophole.model.business.User;
import com.uet.ooadloophole.service.SecureUserDetailService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.GradingTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/gradingTemplate")
public class ApiGradingTemplateController {
	@Autowired
	private SecureUserDetailService secureUserDetailService;
	@Autowired
	private GradingTemplateService gradingTemplateService;

	private Gson gson = new Gson();

	private boolean userCannotCreateGradingTemplate() throws BusinessServiceException {
		User user = secureUserDetailService.getCurrentUser();
		return (!user.hasRole("teacher") && !user.hasRole("admin"));
	}

	public ResponseEntity<Object> createGradingTemplate(@RequestBody GradingTemplate gradingTemplate) {
		try {
			if (userCannotCreateGradingTemplate())
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			GradingTemplate newGradingTemplate = gradingTemplateService.create(gradingTemplate);
			return ResponseEntity.status(HttpStatus.OK).body(newGradingTemplate);
		} catch (BusinessServiceException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	public ResponseEntity<Object> getGradingTemplateByTeacherId(@PathVariable String id) {
		try {


			if (userCannotCreateGradingTemplate()) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			} else {

			}
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); //Remove
		} catch (BusinessServiceException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
