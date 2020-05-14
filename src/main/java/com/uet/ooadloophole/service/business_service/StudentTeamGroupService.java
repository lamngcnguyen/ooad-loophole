package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.controller.interface_model.interfaces.IStudentTeamGroup;
import com.uet.ooadloophole.controller.interface_model.interfaces.ITeamGroupRole;
import com.uet.ooadloophole.model.business.student_rbac.TeamGroup;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StudentTeamGroupService {
    void updateStudentTeamGroup(List<IStudentTeamGroup> studentTeamRoles);

    TeamGroup createTeamGroup(String teamGroupName, String description) throws BusinessServiceException;

    void deleteTeamGroup(String teamGroupId) throws BusinessServiceException;

    void updateTeamGroupRole(List<ITeamGroupRole> teamGroupRoles);

    boolean studentHasTeamRole(String studentId, String teamRoleName);
}
