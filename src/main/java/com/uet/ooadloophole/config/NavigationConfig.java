package com.uet.ooadloophole.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.uet.ooadloophole.model.frontend_element.NavigationGroup;
import com.uet.ooadloophole.model.frontend_element.NavigationItem;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.NavigationGroupService;
import com.uet.ooadloophole.service.business_service.RoleService;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class NavigationConfig {
    private String jsonPath;
    private RoleService roleService;
    private NavigationGroupService navigationGroupService;

    public NavigationConfig(String jsonPath, RoleService roleService, NavigationGroupService navigationGroupService) {
        this.jsonPath = jsonPath;
        this.roleService = roleService;
        this.navigationGroupService = navigationGroupService;
    }

    public void initNavGroups() throws FileNotFoundException {
        JsonArray navGroupData = JsonParser.parseReader(new FileReader(jsonPath)).getAsJsonArray();
        navGroupData.forEach(g -> {
            try {
                NavigationGroup group = createGroup(g.getAsJsonObject());
                navigationGroupService.create(group);
            } catch (BusinessServiceException e) {
                e.printStackTrace();
            }
        });
    }

    private NavigationGroup createGroup(JsonObject data) throws BusinessServiceException {
        NavigationGroup group = new NavigationGroup();
        group.setName(data.get("name").getAsString());
        group.setUrl(data.get("url").getAsString());
        group.setRoleId(roleService.getByName(data.get("role").getAsString()).getId());
        List<NavigationItem> items = new ArrayList<>();
        data.get("navigationItems").getAsJsonArray().forEach(i -> items.add(createItem(i.getAsJsonObject())));
        group.setItems(items);
        return group;
    }

    private NavigationItem createItem(JsonObject data) {
        NavigationItem item = new NavigationItem();
        item.setName(data.get("name").getAsString());
        item.setUrl(data.get("url").getAsString());
        try {
            item.setDescription(data.get("description").getAsString());
        } catch (UnsupportedOperationException ignored) {
            item.setDescription(null);
        }
        return item;
    }

    private void createRole(JsonObject data) {

    }
}
