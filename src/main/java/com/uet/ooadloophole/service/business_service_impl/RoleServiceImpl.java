package com.uet.ooadloophole.service.business_service_impl;


import com.uet.ooadloophole.database.system_repositories.RoleRepository;
import com.uet.ooadloophole.model.business.system_elements.Role;
import com.uet.ooadloophole.service.business_service.RoleService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role create(String roleName) {
        Role role = new Role();
        role.setRole(roleName);
        return roleRepository.save(role);
    }

    @Override
    public Role getByName(String roleName) throws BusinessServiceException {
        Role result = roleRepository.findByRole(roleName.toUpperCase());
        if(result == null) {
            throw new BusinessServiceException("No role found");
        }
        return result;
    }

    @Override
    public boolean checkRoleNotExists(String roleName) {
        Role role = roleRepository.findByRole(roleName.toUpperCase());
        return role == null;
    }
}
