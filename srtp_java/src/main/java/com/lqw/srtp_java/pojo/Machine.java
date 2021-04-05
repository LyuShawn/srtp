package com.lqw.srtp_java.pojo;

import java.util.ArrayList;
import java.util.List;

public class Machine {
    public String machineName;
    public List<TimeNode> timeNodeList=new ArrayList<>();

    public Machine(String machineName){
        this.machineName=machineName;
    }
    public void addTimeNode(String i,String v,String time,int warnning){
        TimeNode timeNode=new TimeNode(time,i,v,warnning);
        timeNodeList.add(timeNode);
    }
}
