package com.uet.ooadloophole.model;

import org.springframework.data.annotation.Id;

import java.util.List;

public class NavigationGroup {
    @Id
    private String _id;
    private String name;
    private String roleId;
    private List<NavigationItem> items;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public List<NavigationItem> getItems() {
        return items;
    }

    public void setItems(List<NavigationItem> items) {
        this.items = items;
    }
}
