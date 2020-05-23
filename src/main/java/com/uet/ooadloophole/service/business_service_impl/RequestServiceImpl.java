package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.config.Constants;
import com.uet.ooadloophole.database.group_repositories.InvitationRepository;
import com.uet.ooadloophole.model.business.group_elements.Group;
import com.uet.ooadloophole.model.business.group_elements.Request;
import com.uet.ooadloophole.model.business.system_elements.Notification;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.GroupService;
import com.uet.ooadloophole.service.business_service.RequestService;
import com.uet.ooadloophole.service.business_service.NotificationService;
import com.uet.ooadloophole.service.business_service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class RequestServiceImpl implements RequestService {
    @Autowired
    private InvitationRepository invitationRepository;
    @Autowired
    private StudentService studentService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private NotificationService notificationService;

    @Override
    public Request getById(String id) {
        return invitationRepository.findBy_id(id);
    }

    @Override
    public List<Request> createInvitation(String groupId, List<String> studentIdList, String message) throws BusinessServiceException {
        List<Request> requests = new ArrayList<>();
        for (String studentId : studentIdList) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            Request request = new Request();
            request.setGroupId(groupId);
            request.setReceiverId(studentService.getById(studentId).get_id());
            request.setMessage(message);
            request.setType(Constants.INVITATION);
            requests.add(request);
            Request createdRequest = invitationRepository.save(request);

            Group group = groupService.getById(groupId);
            Notification notification = new Notification();
            notification.setSubject("Lời mời vào nhóm");
            notification.setContent("Bạn đã được mời vào nhóm " + group.getGroupName());
            notification.setTimeStamp(timeStamp);
            notification.setReceiverId(studentService.getById(studentId).getUserId());
            notification.setUrl("/student/invitation/" + createdRequest.get_id());
            notification.setSeen(false);
            notificationService.create(notification);
        }
        return requests;
    }

    @Override
    public void acceptInvitation(String student_id, String invitationId) throws BusinessServiceException {
        Request request = getById(invitationId);
        if (request.getType().equals(Constants.INVITATION)) {
            if (student_id.equals(request.getReceiverId())) {
                Group group = groupService.getById(request.getGroupId());
                studentService.assignGroup(student_id, group.get_id());
                invitationRepository.delete(request);
            } else {
                throw new BusinessServiceException("Invalid invitation");
            }
        } else {
            throw new BusinessServiceException("Invalid invitation");
        }
    }

    @Override
    public void denyInvitation(String student_id, String invitationId) throws BusinessServiceException {
        Request request = getById(invitationId);
        if (request.getType().equals(Constants.INVITATION)) {
            if (student_id.equals(request.getReceiverId())) {
                invitationRepository.delete(request);
            } else {
                throw new BusinessServiceException("Invalid invitation");
            }
        } else {
            throw new BusinessServiceException("Invalid invitation");
        }
    }
}
