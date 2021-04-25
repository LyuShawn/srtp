package com.lqw.srtp_java.pojo;

public class Time {
    private int id;
    private String timeStamp;
    private int warn;

    public String getTimeStamp() {
        return timeStamp;
    }

    public Time(int id,String time,int warn){
        this.id=id;
        this.timeStamp=time;
        this.warn=warn;
    }
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getWarn() {
        return warn;
    }

    public void setWarn(int warn) {
        this.warn = warn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
