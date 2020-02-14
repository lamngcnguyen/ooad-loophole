package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.business.Role;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;

public interface RoleService {
    Role getByName(String roleName) throws BusinessServiceException;
}
