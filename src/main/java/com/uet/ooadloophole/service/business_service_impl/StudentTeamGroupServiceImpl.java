package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.controller.interface_model.interfaces.IStudentTeamGroup;
import com.uet.ooadloophole.controller.interface_model.interfaces.ITeamGroupRole;
import com.uet.ooadloophole.database.team_rbac.TeamGroupRepository;
import com.uet.ooadloophole.database.team_rbac.TeamGroupRoleRepository;
import com.uet.ooadloophole.database.team_rbac.TeamMemberGroupRepository;
import com.uet.ooadloophole.database.team_rbac.TeamRoleRepository;
import com.uet.ooadloophole.model.business.student_rbac.TeamGroup;
import com.uet.ooadloophole.model.business.student_rbac.TeamGroupRole;
import com.uet.ooadloophole.model.business.student_rbac.TeamMemberGroup;
import com.uet.ooadloophole.model.business.student_rbac.TeamRole;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.StudentTeamGroupService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class StudentTeamGroupServiceImpl implements StudentTeamGroupService {
    @Autowired
    private TeamMemberGroupRepository teamMemberGroupRepository;
    @Autowired
    private TeamGroupRoleRepository teamGroupRoleRepository;
    @Autowired
    private TeamRoleRepository teamRoleRepository;
    @Autowired
    private TeamGroupRepository teamGroupRepository;

    @Override
    public void updateStudentTeamGroup(List<IStudentTeamGroup> studentTeamRoles) {
        teamMemberGroupRepository.saveAll(studentTeamRoles
                .stream()
                .filter(IStudentTeamGroup::belongsTo)
                .map(data -> new TeamMemberGroup(data.getStudentId(), data.getTeamGroupId()))
                .collect(Collectors.toList())
        );
        teamMemberGroupRepository.deleteAll(studentTeamRoles
                .stream()
                .filter(data -> !data.belongsTo())
                .map(data -> teamMemberGroupRepository.findByTeamMemberIdAndTeamGroupId(data.getStudentId(), data.getTeamGroupId()))
                .collect(Collectors.toList())
        );
    }

    @Override
    public TeamGroup createTeamGroup(String teamGroupName, String description) throws BusinessServiceException {
        TeamGroup teamGroup = teamGroupRepository.findByTeamGroup(teamGroupName);
        if (teamGroup != null) {
            throw new BusinessServiceException("member group already exists!");
        }
        teamGroup = new TeamGroup();
        teamGroup.setTeamGroup(teamGroupName);
        teamGroup.setDescription(description);
        teamGroupRepository.save(teamGroup);
        return teamGroup;
    }

    @Override
    public void deleteTeamGroup(String teamGroupId) throws BusinessServiceException {
        TeamGroup teamGroup = teamGroupRepository.findBy_id(teamGroupId);
        if (teamGroup == null) {
            throw new BusinessServiceException("member group not found!");
        }
        teamGroupRepository.delete(teamGroup);
    }

    @Override
    public void updateTeamGroupRole(List<ITeamGroupRole> teamGroupRoles) {
        teamGroupRoleRepository.saveAll(teamGroupRoles
                .stream()
                .filter(ITeamGroupRole::hasAccess)
                .map(data -> new TeamGroupRole(data.getGroupId(), data.getRoleId()))
                .collect(Collectors.toList())
        );
        teamGroupRoleRepository.deleteAll(teamGroupRoles
                .stream()
                .filter(data -> !data.hasAccess())
                .map(data -> teamGroupRoleRepository.findByTeamGroupIdAndTeamRoleId(data.getGroupId(), data.getRoleId()))
                .collect(Collectors.toList())
        );
    }

    @Override
    public boolean studentHasTeamRole(String studentId, String teamRoleName) {
        List<TeamMemberGroup> studentGroups = teamMemberGroupRepository.findAllByTeamMemberId(studentId);
        for (TeamMemberGroup group : studentGroups) {
            List<TeamGroupRole> groupRoles = teamGroupRoleRepository.findAllByTeamGroupId(group.getTeamGroupId());
            boolean hasAccess = groupRoles.stream().anyMatch(item -> {
                TeamRole role = teamRoleRepository.findBy_id(item.getTeamRoleId());
                if (role != null) {
                    return role.getTeamRole().equals(teamRoleName);
                }
                return false;
            });
            if (hasAccess) return true;
        }
        return true;
    }
}

