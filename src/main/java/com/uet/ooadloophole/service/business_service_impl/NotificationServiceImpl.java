package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.database.system_repositories.NotificationRepository;
import com.uet.ooadloophole.model.business.system_elements.Notification;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public Notification create(Notification notification) {
        notificationRepository.save(notification);
        return notification;
    }

    @Override
    public List<Notification> getAllByReceiverId(String receiverId) {
        return notificationRepository.findAllByReceiverId(receiverId);
    }

    @Override
    public void markAsSeen(String notificationId) throws BusinessServiceException {
        Notification notification = notificationRepository.findBy_id(notificationId);
        if(notification == null) {
            throw new BusinessServiceException("No notification found for this id: " + notificationId);
        }
        notification.setSeen(true);
        notificationRepository.save(notification);
    }
}
