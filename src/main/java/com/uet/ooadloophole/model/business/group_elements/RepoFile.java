package com.uet.ooadloophole.model.business.group_elements;

import com.uet.ooadloophole.model.business.system_elements.UserFile;
import org.springframework.data.annotation.Id;

public class RepoFile extends UserFile {
    @Id
    private String _id;
    private String iterationId;
    private String classId;
    private String groupId;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getIterationId() {
        return iterationId;
    }

    public void setIterationId(String iterationId) {
        this.iterationId = iterationId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
