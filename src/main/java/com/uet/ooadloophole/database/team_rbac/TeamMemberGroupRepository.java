package com.uet.ooadloophole.database.team_rbac;

import com.uet.ooadloophole.model.business.student_rbac.TeamMemberGroup;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TeamMemberGroupRepository extends MongoRepository<TeamMemberGroup, String> {
    TeamMemberGroup findBy_id(String _id);

    List<TeamMemberGroup> findAllByTeamMemberId(String memberId);
    List<TeamMemberGroup> findAllByTeamGroupId(String groupId);
    TeamMemberGroup findByTeamMemberIdAndTeamGroupId(String memberId, String groupId);
}
