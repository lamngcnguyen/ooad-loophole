package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.business.system_elements.Role;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;

public interface RoleService {
    Role create(String roleName);
    Role getByName(String roleName) throws BusinessServiceException;
    boolean checkRoleNotExists(String roleName);
}
