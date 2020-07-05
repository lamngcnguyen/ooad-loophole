package com.uet.ooadloophole.controller.api;

import com.uet.ooadloophole.controller.interface_model.interfaces.INotification;
import com.uet.ooadloophole.model.business.system_elements.Notification;
import com.uet.ooadloophole.service.ConverterService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/notifications")
public class ApiNotificationController {
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ConverterService converterService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Object> create(@RequestBody INotification iNotification) {
        Notification notification = converterService.convertToNotification(iNotification);
        Notification response = notificationService.create(notification);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/{id}/seen", method = RequestMethod.PUT)
    public ResponseEntity<Object> markAsSeen(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(notificationService.markAsSeen(id));
        } catch (BusinessServiceException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @RequestMapping(value = "/{userId}/seen/all", method = RequestMethod.PUT)
    public ResponseEntity<Object> markAllAsSeen(@PathVariable String userId) {
        return ResponseEntity.status(HttpStatus.OK).body(notificationService.markAllAsSeen(userId));
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<Notification>> getAllByReceiverId(String receiverId) {
        List<Notification> result = notificationService.getAllByReceiverId(receiverId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
