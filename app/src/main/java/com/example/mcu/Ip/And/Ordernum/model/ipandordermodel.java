package com.example.mcu.Ip.And.Ordernum.model;


import com.google.gson.annotations.SerializedName;


public class ipandordermodel {

    @SerializedName("id")
    private String id;
    private String ip;
    private int order;


    public ipandordermodel(String id, String ip, int order) {
        this.id = id;
        this.ip = ip;
        this.order = order;
    }

    public ipandordermodel() {
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

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
