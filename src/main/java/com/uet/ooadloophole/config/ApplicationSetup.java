package com.uet.ooadloophole.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.uet.ooadloophole.service.business_service.NavigationGroupService;
import com.uet.ooadloophole.service.business_service.RoleService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;

@Component
public class ApplicationSetup implements InitializingBean {
    @Value("${navigation.config}")
    private String navConfigFile;
    @Value("${roles.config}")
    private String roleConfigFile;
    @Autowired
    private RoleService roleService;
    @Autowired
    private NavigationGroupService navigationGroupService;

    //Cleanup old nav items and groups
    @Override
    public void afterPropertiesSet() throws FileNotFoundException {
        createNavigations();
        createRole();
    }

    private void createNavigations() throws FileNotFoundException {
        navigationGroupService.deleteAll();
        new NavigationSetup(navConfigFile, roleService, navigationGroupService).initNavGroups();
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
}
