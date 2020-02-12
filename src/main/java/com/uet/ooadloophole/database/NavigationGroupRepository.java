package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.NavigationGroup;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NavigationGroupRepository extends MongoRepository<NavigationGroup, String> {
    NavigationGroup findByRoleId(String roleId);
}
