package com.uet.ooadloophole.service.business_service_impl;


import com.uet.ooadloophole.database.RoleRepository;
import com.uet.ooadloophole.model.Role;
import com.uet.ooadloophole.service.business_service.RoleService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role getByName(String roleName) throws BusinessServiceException {
        Role result = roleRepository.findByRole(roleName.toUpperCase());
        if(result == null) {
            throw new BusinessServiceException("No role found");
        }
        return result;
    }
}
