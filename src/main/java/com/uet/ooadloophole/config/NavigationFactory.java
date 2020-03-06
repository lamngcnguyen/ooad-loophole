package com.uet.ooadloophole.config;

import com.uet.ooadloophole.service.business_service.NavigationGroupService;
import com.uet.ooadloophole.service.business_service.RoleService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;

@Component
public class NavigationFactory implements InitializingBean {
    @Value("${navigation.config}")
    private String configFile;
    @Autowired
    private RoleService roleService;
    @Autowired
    private NavigationGroupService navigationGroupService;

    //Cleanup old nav items and groups
    @Override
    public void afterPropertiesSet() throws FileNotFoundException {
        createNavigations();
    }

    private void createNavigations() throws FileNotFoundException {
        navigationGroupService.deleteAll();
        new NavigationConfig(configFile, roleService, navigationGroupService).initNavGroups();
    }
}
