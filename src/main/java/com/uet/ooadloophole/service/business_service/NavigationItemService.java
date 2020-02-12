package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.NavigationItem;
import com.uet.ooadloophole.model.Role;

import java.util.List;
import java.util.Set;

public interface NavigationItemService {
    List<NavigationItem> getByRoleId(String roleId);

    List<NavigationItem> getByRole(Role role);

    List<NavigationItem> getByRoles(Set<Role> roles);

    NavigationItem getByName(String name);
}
