package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.business.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {
    public List<Notification> findAllByReceiverId(String receiverId);

    public Notification findBy_id(String _id);
}
