package com.example.edcspring.entity;

public class TestData {
    private int Id;
    private String Cas;
    private String Smiles;
    private String Activity;
    private String NOAEL;
    private String UNIT;
    private String Assay;
    private String Source;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getCas() {
        return Cas;
    }

    public void setCas(String cas) {
        Cas = cas;
    }

    public String getSmiles() {
        return Smiles;
    }

    public void setSmiles(String smiles) {
        Smiles = smiles;
    }

    public String getActivity() {
        return Activity;
    }

    public void setActivity(String activity) {
        Activity = activity;
    }

    public String getNOAEL() {
        return NOAEL;
    }

    public void setNOAEL(String NOAEL) {
        this.NOAEL = NOAEL;
    }

    public String getUNIT() {
        return UNIT;
    }

    public void setUNIT(String UNIT) {
        this.UNIT = UNIT;
    }

    public String getAssay() {
        return Assay;
    }

    public void setAssay(String assay) {
        Assay = assay;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String source) {
        Source = source;
    }

    @Override
    public String toString() {
        return "tets_data{" +
                "Id=" + Id +
                ", Cas='" + Cas + '\'' +
                ", Smiles='" + Smiles + '\'' +
                ", Activity='" + Activity + '\'' +
                ", NOAEL='" + NOAEL + '\'' +
                ", UNIT='" + UNIT + '\'' +
                ", Assay='" + Assay + '\'' +
                ", Source='" + Source + '\'' +
                '}';
    }
}
