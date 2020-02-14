package com.uet.ooadloophole.model.frontend_element;

import com.uet.ooadloophole.model.business.User;

import java.util.List;

public class ListJsonWrapper {
    private List<User> data;

    public ListJsonWrapper(List data) {
        this.data = data;
    }
}
