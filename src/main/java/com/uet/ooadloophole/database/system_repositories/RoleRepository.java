package com.uet.ooadloophole.database.system_repositories;

import com.uet.ooadloophole.model.business.system_elements.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {
    Role findByRole(String role);
}
