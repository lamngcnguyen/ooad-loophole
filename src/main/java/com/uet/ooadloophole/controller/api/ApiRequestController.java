package com.uet.ooadloophole.controller.api;

import com.google.gson.Gson;
import com.uet.ooadloophole.controller.interface_model.ResponseMessage;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class ApiRequestController {
    @Autowired
    private RequestService requestService;

    Gson gson = new Gson();

    @RequestMapping(value = "/invitation/accept", method = RequestMethod.POST)
    public ResponseEntity<String> acceptInvitation(String student_id, String invitationId) {
        try {
            requestService.acceptInvitation(student_id, invitationId);
            return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new ResponseMessage("accepted")));
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/invitation/deny", method = RequestMethod.POST)
    public ResponseEntity<String> denyInvitation(String student_id, String invitationId) {
        try {
            requestService.denyInvitation(student_id, invitationId);
            return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new ResponseMessage("denied")));
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
