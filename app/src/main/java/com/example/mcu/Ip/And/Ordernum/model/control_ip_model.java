package com.example.mcu.Ip.And.Ordernum.model;

public class control_ip_model {

    private String id, ip, startTime, endTime;
    private int hour, order, speed;

    public control_ip_model(String id, String ip, String startTime, String endTime, int hour, int order, int speed) {
        this.id = id;
        this.ip = ip;
        this.startTime = startTime;
        this.endTime = endTime;
        this.hour = hour;
        this.order = order;
        this.speed = speed;
    }

    public control_ip_model() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
