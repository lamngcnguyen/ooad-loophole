package com.uet.ooadloophole.database.system_repositories;

import com.uet.ooadloophole.model.frontend_element.NavigationGroup;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NavigationGroupRepository extends MongoRepository<NavigationGroup, String> {
    NavigationGroup findByRoleId(String roleId);
}
