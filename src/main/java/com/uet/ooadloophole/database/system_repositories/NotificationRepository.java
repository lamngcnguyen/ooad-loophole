package com.uet.ooadloophole.database.system_repositories;

import com.uet.ooadloophole.model.business.system_elements.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findAllByReceiverId(String receiverId);
    Notification findBy_id(String _id);
}
