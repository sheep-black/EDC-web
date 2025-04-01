package com.example.edcspring.entity;

public class UserInfo {
    private int userId;
    private String userName;
    private String password;
    private String email;
    private String region;
    private int vip;
    private int predicttimes;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    public int getPredicttimes() {
        return predicttimes;
    }

    public void setPredicttimes(int predicttimes) {
        this.predicttimes = predicttimes;
    }
}
