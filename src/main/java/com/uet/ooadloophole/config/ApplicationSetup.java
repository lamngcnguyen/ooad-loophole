package com.uet.ooadloophole.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.uet.ooadloophole.database.DisciplineFileTypeRepository;
import com.uet.ooadloophole.database.DisciplineRepository;
import com.uet.ooadloophole.database.team_rbac.TeamRoleRepository;
import com.uet.ooadloophole.model.Token;
import com.uet.ooadloophole.model.business.LoopholeUser;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.NavigationGroupService;
import com.uet.ooadloophole.service.business_service.RoleService;
import com.uet.ooadloophole.service.business_service.TokenService;
import com.uet.ooadloophole.service.business_service.UserService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

@Component
public class ApplicationSetup implements InitializingBean {
    @Value("${navigation.config}")
    private String navConfigFile;
    @Value("${roles.config}")
    private String roleConfigFile;
    @Value("${phases.config}")
    private String phaseConfigFile;
    @Value("${team_roles.config}")
    private String teamRoleConfigFile;
    @Autowired
    private RoleService roleService;
    @Autowired
    private NavigationGroupService navigationGroupService;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private DisciplineRepository disciplineRepository;
    @Autowired
    private DisciplineFileTypeRepository disciplineFileTypeRepository;
    @Autowired
    private TeamRoleRepository teamRoleRepository;

    //Cleanup old nav items and groups
    @Override
    public void afterPropertiesSet() throws FileNotFoundException {
        createRole();
        createNavigations();
        createAdmin();
        clearExpiredTokens();
        createDisciplines();
        createTeamRoles();
    }

    private void createNavigations() throws FileNotFoundException {
        navigationGroupService.deleteAll();
        new NavigationSetup(navConfigFile, roleService, navigationGroupService).initNavGroups();
    }

    private void createDisciplines() throws FileNotFoundException {
        new DisciplineSetup(phaseConfigFile, disciplineRepository, disciplineFileTypeRepository).createDisciplines();
    }

    private void createRole() throws FileNotFoundException {
        if (roleService.checkRoleNotExists("ADMIN")) {
            roleService.create("ADMIN");
        }
        JsonObject rolesJson = JsonParser.parseReader(new FileReader(roleConfigFile)).getAsJsonObject();
        JsonArray dataArray = rolesJson.getAsJsonArray("roles");
        for (JsonElement data : dataArray) {
            if (roleService.checkRoleNotExists(data.getAsString())) {
                roleService.create(data.getAsString());
            }
        }
    }

    private void createAdmin() {
        try {
            LoopholeUser user;
            try {
                userService.getByUsername("admin-loophole");
                return;
            } catch (BusinessServiceException ignored) {
                System.out.println("Default admin does not exists, creating one...");
            }
            user = new LoopholeUser();
            user.setFullName("Loophole Admin");
            user.setUsername("admin-loophole");
            user.setPassword("a@123456");
            user.setEmail("phonghatuan1998@gmail.com");
            user.setPhoneNumber("0915141031");
            userService.createActivatedUser(user, new String[]{"admin"});
        } catch (BusinessServiceException e) {
            System.out.println("Error createInvitation default admin account: " + e.getMessage());
        }
    }

    private void clearExpiredTokens() {
        List<Token> tokenList = tokenService.getAll();
        for (Token token : tokenList) {
            if (!tokenService.isValid(token.getTokenString())) {
                tokenService.deleteToken(token);
            }
        }
    }

    private void createTeamRoles() throws FileNotFoundException {
        new TeamRoleSetup(teamRoleConfigFile, teamRoleRepository).createTeamRoles();
    }
}
