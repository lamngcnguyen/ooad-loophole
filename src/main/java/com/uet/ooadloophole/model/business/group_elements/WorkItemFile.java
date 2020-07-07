package com.uet.ooadloophole.model.business.group_elements;

import com.uet.ooadloophole.model.business.system_elements.UserFile;
import org.springframework.data.annotation.Id;

public class WorkItemFile extends UserFile {
    @Id
    private String _id;
    private String workItemId;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getWorkItemId() {
        return workItemId;
    }

    public void setWorkItemId(String workItemId) {
        this.workItemId = workItemId;
    }
}
