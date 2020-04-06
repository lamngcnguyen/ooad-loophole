package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.database.InvitationRepository;
import com.uet.ooadloophole.model.business.Group;
import com.uet.ooadloophole.model.business.Invitation;
import com.uet.ooadloophole.model.business.Notification;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class InvitationServiceImpl implements InvitationService {
    @Autowired
    private InvitationRepository invitationRepository;
    @Autowired
    private StudentService studentService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private NotificationService notificationService;

    @Override
    public Invitation getById(String id) {
        return invitationRepository.findBy_id(id);
    }

    @Override
    public List<Invitation> create(String groupId, List<String> studentIdList, String message) throws BusinessServiceException {
        List<Invitation> invitations = new ArrayList<>();
        for (String studentId : studentIdList) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            Invitation invitation = new Invitation();
            invitation.setGroupId(groupId);
            invitation.setReceiverId(studentService.getById(studentId).getUserId());
            invitation.setMessage(message);
            invitations.add(invitation);
            Invitation createdInvitation = invitationRepository.save(invitation);

            Group group = groupService.getById(groupId);
            Notification notification = new Notification();
            notification.setSubject("Lời mời vào nhóm");
            notification.setContent("Bạn đã được mời vào nhóm " + group.getGroupName());
            notification.setTimeStamp(timeStamp);
            notification.setReceiverId(studentService.getById(studentId).getUserId());
            notification.setUrl("/student/invitation/" + createdInvitation.get_id());
            notification.setSeen(false);
            notificationService.create(notification);
        }
        return invitations;
    }
}
