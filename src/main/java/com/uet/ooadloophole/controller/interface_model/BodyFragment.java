package com.uet.ooadloophole.controller.interface_model;

public class BodyFragment {
    private String view;
    private String fragment;

    public BodyFragment(String view, String fragment) {
        this.view = view;
        this.fragment = fragment;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getFragment() {
        return fragment;
    }

    public void setFragment(String fragment) {
        this.fragment = fragment;
    }
}
