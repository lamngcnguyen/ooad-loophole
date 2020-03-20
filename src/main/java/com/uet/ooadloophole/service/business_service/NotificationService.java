package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.business.Notification;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;

import java.util.List;

public interface NotificationService {
    public Notification create(Notification notification);

    public List<Notification> getAllByReceiverId(String receiverId);

    public void markAsSeen(String notificationId) throws BusinessServiceException;
}
