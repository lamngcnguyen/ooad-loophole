package com.uet.ooadloophole.model;

import org.springframework.data.annotation.Id;

import java.util.List;

public class Repository {
    @Id
    private String _id;
    private String classId;
    private String groupId;
    private List<String> files;

}
