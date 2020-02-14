package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.database.NavigationGroupRepository;
import com.uet.ooadloophole.model.frontend_element.NavigationGroup;
import com.uet.ooadloophole.model.business.Role;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.NavigationGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class NavigationGroupServiceImpl implements NavigationGroupService {
    @Autowired
    private NavigationGroupRepository navigationGroupRepository;

    @Override
    public NavigationGroup getByRole(Role role) throws BusinessServiceException {
        NavigationGroup group = navigationGroupRepository.findByRoleId(role.getId());
        if (group == null) {
            throw new BusinessServiceException("No navigation group found for role " + role.getRole());
        }
        return group;
    }

    @Override
    public List<NavigationGroup> getByRoles(Set<Role> roles) {
        List<NavigationGroup> navigationGroups = new ArrayList<>();
        roles.forEach(role -> {
            try {
                navigationGroups.add(getByRole(role));
            } catch (BusinessServiceException e) {
                System.out.println(e.getMessage());
            }
        });
        return navigationGroups;
    }
}
