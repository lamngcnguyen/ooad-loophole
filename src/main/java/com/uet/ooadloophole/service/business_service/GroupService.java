package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.business.Group;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;

import java.util.List;

public interface GroupService {
    Group getById(String id) throws BusinessServiceException;

    List<Group> searchByName(String name);

    List<Group> getAllByClassId(String classId);

    Group findOneByName(String name);

    Group create(Group group) throws BusinessServiceException;

    Group update(Group group) throws BusinessServiceException;

    void deleteById(String id) throws BusinessServiceException;

    void deleteAllByClassId(String classId);
}
