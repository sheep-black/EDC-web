package com.example.edcspring.entity;

public class ResultDL {
    private String SMILES;
    private String sAOP;
    private String sAO;
    private float sNOAEL;
    private int id;

    public String getSMILES() {
        return SMILES;
    }

    public void setSMILES(String SMILES) {
        this.SMILES = SMILES;
    }

    public String getsAOP() {
        return sAOP;
    }

    public void setsAOP(String sAOP) {
        this.sAOP = sAOP;
    }

    public String getsAO() {
        return sAO;
    }

    public void setsAO(String sAO) {
        this.sAO = sAO;
    }

    public float getsNOAEL() {
        return sNOAEL;
    }

    public void setsNOAEL(float sNOAEL) {
        this.sNOAEL = sNOAEL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
