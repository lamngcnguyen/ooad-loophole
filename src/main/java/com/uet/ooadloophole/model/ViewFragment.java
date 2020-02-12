package com.uet.ooadloophole.model;

public class ViewFragment {
    private String view;
    private String fragment;

    public ViewFragment(String view, String fragment) {
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
