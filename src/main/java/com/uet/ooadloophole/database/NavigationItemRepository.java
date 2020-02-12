package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.NavigationItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NavigationItemRepository extends MongoRepository<NavigationItem, String> {
    List<NavigationItem> getAllByRoleId(String roleId);

    NavigationItem findByName(String name);
}
