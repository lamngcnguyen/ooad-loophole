package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.business.system_elements.Notification;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;

import java.util.List;

public interface NotificationService {
    Notification create(Notification notification);

    List<Notification> getAllByReceiverId(String receiverId);

    Notification markAsSeen(String notificationId) throws BusinessServiceException;

    List<Notification> markAllAsSeen(String userId);
}
