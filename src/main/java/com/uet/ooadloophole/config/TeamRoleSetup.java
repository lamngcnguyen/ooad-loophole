package com.uet.ooadloophole.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.uet.ooadloophole.database.team_rbac.TeamRoleRepository;
import com.uet.ooadloophole.model.business.student_rbac.TeamRole;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class TeamRoleSetup {
    private String jsonPath;
    private TeamRoleRepository teamRoleRepository;

    public TeamRoleSetup(String jsonPath, TeamRoleRepository teamRoleRepository) {
        this.jsonPath = jsonPath;
        this.teamRoleRepository = teamRoleRepository;
    }

    public void createTeamRoles() throws FileNotFoundException {
        JsonArray rolesArray = JsonParser.parseReader(new FileReader(jsonPath)).getAsJsonArray();
        rolesArray.forEach(r -> {
            JsonObject jsonObject = r.getAsJsonObject();
            TeamRole teamRole = teamRoleRepository.findByTeamRole(jsonObject.get("name").getAsString());
            if(teamRole == null) {
                teamRole = new TeamRole();
                teamRole.setTeamRole(jsonObject.get("name").getAsString());
                teamRole.setDescription(jsonObject.get("description").getAsString());
                teamRoleRepository.save(teamRole);
            }
        });
    }
}
