package com.example.edcspring.entity;

public class ScreenData {
    private String[] endpoint;
    private String[] assay;
    private String[] activity;

    public String[] getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String[] endpoint) {
        this.endpoint = endpoint;
    }

    public String[] getAssay() {
        return assay;
    }

    public void setAssay(String[] assay) {
        this.assay = assay;
    }

    public String[] getActivity() {
        return activity;
    }

    public void setActivity(String[] activity) {
        this.activity = activity;
    }
// 省略 getter 和 setter 方法
}
