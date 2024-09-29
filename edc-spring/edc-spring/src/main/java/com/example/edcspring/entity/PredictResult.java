package com.example.edcspring.entity;

public class PredictResult {
    private String smile;

    private int id;

    public String getSource() {
        return smile;
    }

    public void setSource(String source) {
        this.smile = source;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
