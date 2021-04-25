package com.lqw.srtp_java.pojo;

import java.util.ArrayList;
import java.util.List;

public class TimeNode {
    public String timeStamp;//时间戳
    public List<Double> I=new ArrayList<>();
    public List<Double> V=new ArrayList<>();
    public int warning;


    public void SetI(String I){
        IVTransform(I,0);
    }
    public void SetV(String V){
        IVTransform(V,1);
    }

    private void IVTransform(String list,int d){         //0表示I,1表示V
        String[] arr=list.split("_");
        for (String s : arr) {
            if(d==0){
                I.add(Double.parseDouble(s));
            }
            else if(d==1){
                V.add(Double.parseDouble(s));
            }
        }
    }
}
