package com.uet.ooadloophole.model.dto;

import com.uet.ooadloophole.model.Topic;
import com.uet.ooadloophole.model.UserFile;

import java.util.List;

public class TopicDTO {
    public String id;
    public String name;
    public String descriptions;
    public List<UserFile> specificationFiles;
    public String groupName;
    public String groupId;

    public TopicDTO(Topic data, String groupId, String groupName) {
        this.id = data.get_id();
        this.name = data.getName();
        this.descriptions = data.getDescriptions();
        this.specificationFiles = data.getSpecificationFiles();
        this.groupId = groupId;
        this.groupName = groupName;
    }
}
