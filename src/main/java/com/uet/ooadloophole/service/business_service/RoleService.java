package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.Role;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;

public interface RoleService {
    Role getRoleByName(String roleName) throws BusinessServiceException;
}
