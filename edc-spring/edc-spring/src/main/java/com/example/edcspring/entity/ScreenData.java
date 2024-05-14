package com.example.edcspring.entity;

import java.util.List;

public class ScreenData {
    private List<String> endpoint;
    private List<String> assay;
    private String activity;

    public List<String> getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(List<String> endpoint) {
        this.endpoint = endpoint;
    }

    public List<String> getAssay() {
        return assay;
    }

    public void setAssay(List<String> assay) {
        this.assay = assay;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }
}
