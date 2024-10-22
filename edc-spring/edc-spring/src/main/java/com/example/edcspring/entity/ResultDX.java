package com.example.edcspring.entity;

public class ResultDX {
    private String SMILES;
    private String EDC;
    private int id;

    public String getSMILES() {
        return SMILES;
    }

    public void setSMILES(String SMILES) {
        this.SMILES = SMILES;
    }

    public String getEDC() {
        return EDC;
    }

    public void setEDC(String EDC) {
        this.EDC = EDC;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
