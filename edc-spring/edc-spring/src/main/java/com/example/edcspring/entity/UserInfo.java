package com.example.edcspring.entity;

public class UserInfo {
    private int userId;
    private String userName;
    private String password;
    private String email;
    private String region;
    private int VIP;
    private int predictTimes;

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

    public int getVIP() {
        return VIP;
    }

    public void setVIP(int VIP) {
        this.VIP = VIP;
    }

    public int getPredictTimes() {
        return predictTimes;
    }

    public void setPredictTimes(int predictTimes) {
        this.predictTimes = predictTimes;
    }
}
