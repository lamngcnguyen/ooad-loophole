package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.NavigationGroup;
import com.uet.ooadloophole.model.Role;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;

import java.util.List;
import java.util.Set;

public interface NavigationGroupService {
    NavigationGroup getByRole(Role role) throws BusinessServiceException;

    List<NavigationGroup> getByRoles(Set<Role> roles) throws BusinessServiceException;
}
