package com.uet.ooadloophole.database.team_rbac;

import com.uet.ooadloophole.model.business.student_rbac.TeamGroup;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeamGroupRepository extends MongoRepository<TeamGroup, String> {
    TeamGroup findBy_id(String _id);
    TeamGroup findByTeamGroup(String group);
}
