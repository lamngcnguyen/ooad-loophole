package com.uet.ooadloophole.model.business.requirement_elements;

import com.uet.ooadloophole.model.business.system_elements.UserFile;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

public class RequirementSpecFile extends UserFile {
    @Id
    private String _id;
    private String requirementId;
    @DBRef
    private List<RequirementSpecFile> previousVersions;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getRequirementId() {
        return requirementId;
    }

    public void setRequirementId(String requirementId) {
        this.requirementId = requirementId;
    }

    public List<RequirementSpecFile> getPreviousVersions() {
        return previousVersions;
    }

    public void setPreviousVersions(List<RequirementSpecFile> previousVersions) {
        this.previousVersions = previousVersions;
    }
}
