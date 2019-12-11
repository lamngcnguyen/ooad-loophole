package com.uet.ooadloophole.model;

import org.springframework.data.annotation.Id;

public class Iteration {
    @Id
    private String _id;
    private String name;
    private String file; //schema
    private String groupScore; //schema
    private String teacherScore;
}
