package com.lqw.srtp_java.pojo;

import java.util.*;

public class Machine {
    public String machineName;
    public List<Time> timeList=new ArrayList<>();

    public Machine(String machineName){
        this.machineName=machineName;
    }
    //添加时间点，添加后排序
    public void addTime(int id,String time,int warning){
        Time time1=new Time(id,time,warning);
        timeList.add(time1);
        Collections.sort(timeList, new Comparator<Time>() {
            @Override
            public int compare(Time o1, Time o2) {
                return o1.getTimeStamp().compareTo(o2.getTimeStamp());
            }
        });
    }
}
