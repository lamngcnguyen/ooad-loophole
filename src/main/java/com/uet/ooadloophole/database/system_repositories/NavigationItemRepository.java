package com.uet.ooadloophole.database.system_repositories;

import com.uet.ooadloophole.model.frontend_element.NavigationItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NavigationItemRepository extends MongoRepository<NavigationItem, String> {
    List<NavigationItem> getAllByRoleId(String roleId);

    NavigationItem findByName(String name);
}
