package com.uet.ooadloophole.config;

import com.uet.ooadloophole.database.NavigationGroupRepository;
import com.uet.ooadloophole.database.NavigationItemRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NavigationItemCleaner implements InitializingBean {
    @Autowired
    private NavigationGroupRepository navigationGroupRepository;
    @Autowired
    private NavigationItemRepository navigationItemRepository;

    //Cleanup old nav items and groups
    @Override
    public void afterPropertiesSet() throws Exception {
        navigationGroupRepository.deleteAll();
        navigationItemRepository.deleteAll();
    }
}
