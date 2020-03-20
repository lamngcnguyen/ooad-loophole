package com.uet.ooadloophole.service;

import java.util.HashMap;

public class NotificationTemplateFactory {
    public static String getContent(String type, HashMap<String, String> values) {
        switch (type) {
            case "group_invitation":
                return createGroupInvitation(values.get("leaderName"), values.get("groupName"));
            case "iteration_deadline":
                return createIterationDeadlineNotice(values.get("iterationId"));
            default:
                return "";
        }
    }

    private static String createGroupInvitation(String leaderName, String groupName) {
        return NotificationTemplateStore.groupInvitation.replace("*leaderName*", leaderName)
            .replace("*groupName*", groupName);
    }

    private static String createIterationDeadlineNotice(String iterationId) {
        return null;
    }
}
