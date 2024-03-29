package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.database.system_repositories.NavigationItemRepository;
import com.uet.ooadloophole.model.frontend_element.NavigationItem;
import com.uet.ooadloophole.model.business.system_elements.Role;
import com.uet.ooadloophole.service.business_service.NavigationItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class NavigationItemServiceImpl implements NavigationItemService {
    @Autowired
    private NavigationItemRepository navigationItemRepository;

    @Override
    public NavigationItem create(String name, String url, Role role, String description) {
        NavigationItem navigationItem = new NavigationItem();
        navigationItem.setName(name);
        navigationItem.setUrl(url);
        navigationItem.setRoleId(role.getId());
        navigationItem.setDescription(description);
        navigationItemRepository.save(navigationItem);
        return navigationItem;
    }

    @Override
    public List<NavigationItem> getByRoleId(String roleId) {
        return navigationItemRepository.getAllByRoleId(roleId);
    }

    @Override
    public List<NavigationItem> getByRole(Role role) {
        return navigationItemRepository.getAllByRoleId(role.getId());
    }

    @Override
    public List<NavigationItem> getByRoles(Set<Role> roles) {
        List<NavigationItem> items = new ArrayList<>();
        roles.forEach(role -> items.addAll(getByRole(role)));
        return items;
    }

    @Override
    public NavigationItem getByName(String name) {
        return navigationItemRepository.findByName(name);
    }
}
