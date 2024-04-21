package com.example.edcspring.entity;

public class testdata {
    private int Id;
    private String Name;
    private String Type;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    @Override
    public String toString() {
        return "edc{" +
                "Id=" + Id +
                ", Name='" + Name + '\'' +
                ", Type='" + Type + '\'' +
                '}';
    }
}
