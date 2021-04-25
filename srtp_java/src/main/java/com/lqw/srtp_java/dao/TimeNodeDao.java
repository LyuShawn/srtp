package com.lqw.srtp_java.dao;

import com.lqw.srtp_java.Util.DbUtil;
import com.lqw.srtp_java.pojo.Machine;
import com.lqw.srtp_java.pojo.TimeNode;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TimeNodeDao {
    private TimeNode timeNode;


    public TimeNodeDao(int id) throws SQLException {
        timeNode=new TimeNode();
        getData(id);
    }

    private void getData(int id) throws SQLException {
        String sql="select time,current,voltage,warning from machine_info where id=?";
        ResultSet rs= DbUtil.executeQuery(sql,id);
        while(rs.next()){
            String timeStamp=rs.getString("time");
            String I=rs.getString("current");
            String V=rs.getString("voltage");
            int warn=rs.getInt("warning");
            timeNode.timeStamp=timeStamp;
            timeNode.warning=warn;
            timeNode.SetI(I);
            timeNode.SetV(V);
        }
    }
    public TimeNode getTimeNode(){
        return timeNode;
    }
}
