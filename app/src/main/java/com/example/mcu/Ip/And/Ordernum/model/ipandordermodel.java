package com.example.mcu.Ip.And.Ordernum.model;

import androidx.fragment.app.FragmentActivity;

import java.util.List;

public class ipandordermodel {


    private String ip;
    private int order;


    public ipandordermodel ( String ip, int order ) {
        this.ip = ip;
        this.order = order;
    }


    public ipandordermodel(){

    }

    public String getIp () {
        return ip;
    }

    public void setIp ( String ip ) {
        this.ip = ip;
    }

    public int getOrder () {
        return order;
    }

    public void setOrder ( int order ) {
        this.order = order;
    }
}
