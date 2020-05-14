package com.uet.ooadloophole.database.team_rbac;

import com.uet.ooadloophole.model.business.student_rbac.TeamGroupRole;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TeamGroupRoleRepository extends MongoRepository<TeamGroupRole, String> {
    TeamGroupRole findBy_id(String _id);

    List<TeamGroupRole> findAllByTeamGroupId(String groupId);
    List<TeamGroupRole> findAllByTeamRoleId(String roleId);
    TeamGroupRole findByTeamGroupIdAndTeamRoleId(String groupId, String roleId);
}
