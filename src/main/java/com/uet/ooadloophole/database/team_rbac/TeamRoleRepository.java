package com.uet.ooadloophole.database.team_rbac;

import com.uet.ooadloophole.model.business.student_rbac.TeamRole;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeamRoleRepository extends MongoRepository<TeamRole, String> {
    TeamRole findBy_id(String _id);

    TeamRole findByTeamRole(String role);
}
