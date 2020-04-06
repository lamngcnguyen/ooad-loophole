package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.business.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findAllByReceiverId(String receiverId);

    Notification findBy_id(String _id);
}
