package com.uet.ooadloophole.config;

public class Constants {
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_TEACHER = "TEACHER";
    public static final String ROLE_STUDENT = "STUDENT";
    public static final String ROLE_LEADER = "GROUP_LEADER";
    public static final String ROLE_MEMBER = "GROUP_MEMBER";
    public static final String EMAIL_SUFFIX = "@vnu.edu.vn";
    public static final String CONFIRMATION_URL = "http://ooad-loophole.herokuapp.com/activate-account?token=";
    public static final String RESET_LINK = "http://ooad-loophole.herokuapp.com/reset-password?token=";
    public static final String AVATAR_FOLDER = "avatar/";
    public static final String DEFAULT_AVATAR = "default.png";
    public static final String SPEC_FOLDER = "topic/";
    public static final String REQ_FOLDER = "requirement/";
    public static final String REPO_FOLDER = "repo/";
    public static final String WORK_ITEM_FOLDER = "work_item/";
    public static final String TOKEN_ACTIVATE = "activate";
    public static final String TOKEN_RESET = "reset";
    public static final String INVITATION = "invitation";
    public static final String REQUEST = "request";
    public static final int TOKEN_EXPIRATION = 60 * 24;
}
