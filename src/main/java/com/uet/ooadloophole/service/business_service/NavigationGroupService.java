package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.frontend_element.NavigationGroup;
import com.uet.ooadloophole.model.business.system_elements.Role;
import com.uet.ooadloophole.model.frontend_element.NavigationItem;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;

import java.util.List;
import java.util.Set;

public interface NavigationGroupService {
    NavigationGroup create(String name, List<NavigationItem> items, Role role, String url);

    NavigationGroup create(NavigationGroup group);

    NavigationGroup getByRole(Role role) throws BusinessServiceException;

    List<NavigationGroup> getByRoles(Set<Role> roles) throws BusinessServiceException;

    void deleteAll();
}
