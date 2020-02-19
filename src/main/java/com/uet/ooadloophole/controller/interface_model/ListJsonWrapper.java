package com.uet.ooadloophole.controller.interface_model;

import com.uet.ooadloophole.model.business.User;

import java.util.Collection;

public class ListJsonWrapper {
    private Collection data;

    public ListJsonWrapper(Collection data) {
        this.data = data;
    }
}
