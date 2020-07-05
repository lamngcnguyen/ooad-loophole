package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.config.Constants;
import com.uet.ooadloophole.database.group_repositories.RequestRepository;
import com.uet.ooadloophole.model.business.group_elements.Group;
import com.uet.ooadloophole.model.business.group_elements.Request;
import com.uet.ooadloophole.model.business.system_elements.Notification;
import com.uet.ooadloophole.model.business.system_elements.Student;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.GroupService;
import com.uet.ooadloophole.service.business_service.NotificationService;
import com.uet.ooadloophole.service.business_service.RequestService;
import com.uet.ooadloophole.service.business_service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RequestServiceImpl implements RequestService {
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private StudentService studentService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private NotificationService notificationService;

    @Override
    public Request getById(String id) {
        return requestRepository.findBy_id(id);
    }

    @Override
    public List<Request> createInvitation(String groupId, List<String> studentIdList, String message) throws BusinessServiceException {
        List<Request> requests = new ArrayList<>();
        Group group = groupService.getById(groupId);
        for (String studentId : studentIdList) {
            Request request = new Request();
            request.setGroupId(groupId);
            request.setUserId(studentService.getById(studentId).getUserId());
            request.setMessage(message);
            request.setType(Constants.INVITATION);
            requests.add(request);
            Request createdRequest = requestRepository.save(request);

            Notification notification = new Notification();
            notification.setSubject("New invitation");
            notification.setContent("You have been invited to join " + group.getGroupName());
            notification.setTimeStamp(LocalDateTime.now());
            notification.setReceiverId(studentService.getById(studentId).getUserId());
            notification.setUrl("/student/invitation/" + createdRequest.get_id());
            notification.setSeen(false);
            notificationService.create(notification);
        }
        return requests;
    }

    @Override
    public void acceptInvitation(String userId, String invitationId) throws BusinessServiceException {
        Request request = getById(invitationId);
        if (request.getType().equals(Constants.INVITATION)) {
            if (userId.equals(request.getUserId())) {
                Group group = groupService.getById(request.getGroupId());
                studentService.assignGroup(studentService.getByUserId(userId), group.get_id());
                requestRepository.delete(request);
            } else {
                throw new BusinessServiceException("Invalid invitation");
            }
        } else {
            throw new BusinessServiceException("Invalid invitation");
        }
    }

    @Override
    public void denyInvitation(String userId, String invitationId) throws BusinessServiceException {
        Request request = getById(invitationId);
        if (request.getType().equals(Constants.INVITATION)) {
            if (userId.equals(request.getUserId())) {
                requestRepository.delete(request);
            } else {
                throw new BusinessServiceException("Invalid invitation");
            }
        } else {
            throw new BusinessServiceException("Invalid invitation");
        }
    }

    /**
     * Student creating request to join group
     *
     * @param groupId   request target group
     * @param studentId student making the request
     * @return Created request
     * @throws BusinessServiceException if previously thrown in other methods
     */
    @Override
    public Request createRequest(String groupId, String studentId) throws BusinessServiceException {
        Student student = studentService.getById(studentId);
        Request request = new Request();
        request.setGroupId(groupId);
        request.setUserId(student.getUserId()); //Student making the request
        request.setType(Constants.REQUEST);
        Request createdRequest = requestRepository.save(request);

        Group group = groupService.getById(groupId);
        Notification notification = new Notification();
        notification.setSubject("New request");
        notification.setContent(student.getFullName() + " has sent request to join " + group.getGroupName());
        notification.setTimeStamp(LocalDateTime.now());
        notification.setReceiverId(group.getLeader().getUserId()); //Leader receiving the request
        notification.setUrl("/student/request/" + createdRequest.get_id());
        notification.setSeen(false);
        notificationService.create(notification);
        return request;
    }

    /**
     * Accept student request to join group
     *
     * @param userId    leader's user id
     * @param requestId request id
     * @throws BusinessServiceException if request invalid or the user accepting the request is not authorized
     */
    @Override
    public void acceptRequest(String userId, String requestId) throws BusinessServiceException {
        Request request = getById(requestId);
        Group group = groupService.getById(request.getGroupId());
        if (request.getType().equals(Constants.REQUEST)) {
            Student requester = studentService.getByUserId(request.getUserId());
            if (userId.equals(group.getLeader().getUserId())) {
                studentService.assignGroup(requester, group.get_id());
                requestRepository.delete(request);
            } else {
                throw new BusinessServiceException("Not authorized");
            }
        } else {
            throw new BusinessServiceException("Invalid invitation");
        }
    }

    @Override
    public void denyRequest(String userId, String requestId) throws BusinessServiceException {
        Request request = getById(requestId);
        Group group = groupService.getById(request.getGroupId());
        if (request.getType().equals(Constants.REQUEST)) {
            if (userId.equals(group.getLeader().getUserId())) {
                requestRepository.delete(request);
            } else {
                throw new BusinessServiceException("Not authorized");
            }
        } else {
            throw new BusinessServiceException("Invalid invitation");
        }
    }
}
