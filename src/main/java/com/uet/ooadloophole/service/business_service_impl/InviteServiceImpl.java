package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.database.InvitationRepository;
import com.uet.ooadloophole.model.business.Invitation;
import com.uet.ooadloophole.model.business.Notification;
import com.uet.ooadloophole.service.business_service.InviteService;
import com.uet.ooadloophole.service.business_service.NotificationService;
import com.uet.ooadloophole.service.business_service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InviteServiceImpl implements InviteService {
    @Autowired
    private InvitationRepository invitationRepository;
    @Autowired
    private StudentService studentService;
    @Autowired
    private NotificationService notificationService;

    @Override
    public List<Invitation> create(String groupId, List<String> studentIdList, String message) {
        List<Invitation> invitations = new ArrayList<>();
        for (String studentId : studentIdList) {
            Invitation invitation = new Invitation();
            invitation.setGroupId(groupId);
            invitation.setReceiverId(studentId);
            invitation.setMessage(message);
            invitations.add(invitation);

            Notification notification = new Notification();
            notification.setContent("Đã mời vào nhóm");
            invitationRepository.save(invitation);
        }
        return invitations;
    }
}
