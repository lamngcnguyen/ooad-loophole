package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.config.Constants;
import com.uet.ooadloophole.database.group_repositories.GroupRepository;
import com.uet.ooadloophole.database.system_repositories.StudentRepository;
import com.uet.ooadloophole.model.business.group_elements.Group;
import com.uet.ooadloophole.model.business.system_elements.Student;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.GroupService;
import com.uet.ooadloophole.service.business_service.StudentService;
import com.uet.ooadloophole.service.business_service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Group getById(String id) throws BusinessServiceException {
        Group result = groupRepository.findBy_id(id);
        if (result == null) {
            throw new BusinessServiceException("No group found for this id");
        }
        return result;
    }

    @Override
    public List<Group> searchByName(String name) {
        return groupRepository.findAllByGroupNameLikeIgnoreCase(name);
    }

    @Override
    public List<Group> getAllByClassId(String classId) {
        return groupRepository.findAllByClassId(classId);
    }

    @Override
    public Group findOneByName(String name) {
        return groupRepository.findByGroupName(name);
    }

    @Override
    public Group create(Group group) throws BusinessServiceException {
        try {
            if (groupNameExists(group.getGroupName())) {
                throw new BusinessServiceException("Group name already exists");
            }
            userService.assignRoles(group.getLeader().getUserId(), new String[]{Constants.ROLE_LEADER, Constants.ROLE_MEMBER});
            studentService.assignGroup(group.getLeader(), group.get_id());
            return group;
        } catch (BusinessServiceException e) {
            throw new BusinessServiceException("Unable to createInvitation group: " + e.getMessage());
        }
    }

    private boolean groupNameExists(String name) {
        return findOneByName(name) != null;
    }

    @Override
    public Group update(Group group) throws BusinessServiceException {
        try {
            if (groupNameExists(group.getGroupName(), group.get_id())) {
                throw new BusinessServiceException("Group name already exists");
            }
            Group dbGroup = getById(group.get_id());
            dbGroup.setGroupName(group.getGroupName());
            if (!dbGroup.getLeader().getUserId().equals(group.getLeader().getUserId())) {
                userService.removeRole(dbGroup.getLeader().getUserId(), Constants.ROLE_LEADER);
                userService.assignRole(group.getLeader().getUserId(), Constants.ROLE_LEADER);
            }
            groupRepository.save(dbGroup);
            return dbGroup;
        } catch (BusinessServiceException e) {
            throw new BusinessServiceException("Unable to update group: " + e.getMessage());
        }
    }

    private boolean groupNameExists(String name, String groupId) {
        return groupRepository.findBy_idNotAndGroupName(groupId, name) != null;
    }

    @Override
    public void deleteById(String id) throws BusinessServiceException {
        try {
            Group dbGroup = getById(id);
            groupRepository.delete(dbGroup);
        } catch (BusinessServiceException e) {
            throw new BusinessServiceException("Unable to delete group: " + e.getMessage());
        }
    }

    @Override
    public void deleteAllByClassId(String classId) {
        groupRepository.deleteAllByClassId(classId);
    }

    @Override
    public void removeMember(String groupId, String memberId) throws BusinessServiceException {
        Student student = studentRepository.findBy_id(memberId);
        if (student.getGroupId().equals(groupId)) {
            student.setGroupId(null);
            userService.removeRole(student.getUserId(), Constants.ROLE_MEMBER);
            studentRepository.save(student);
        } else {
            throw new BusinessServiceException("This student is not a member of this group");
        }
    }
}
