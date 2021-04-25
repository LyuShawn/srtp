package com.lqw.srtp_java.dao;

import com.lqw.srtp_java.Util.DbUtil;
import com.lqw.srtp_java.pojo.Machine;

import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MachineDao {
    private Map<String, Machine> machineMap=new HashMap<>();
    private List<Machine> machineList=new ArrayList<>();

    public MachineDao() {
    }

    public void getData() throws SQLException {
        String sql="select id,machine_model,time,warning from machine_info";
        ResultSet rs= DbUtil.executeQuery(sql);
        while(rs.next()){

            int timeID=rs.getInt("id");
            String machineName=rs.getString("machine_model");
            String time=rs.getString("time");
            int warn=rs.getInt("warning");
            if(!isAdd(machineName)){
                Machine machine=new Machine(machineName);
                machine.addTime(timeID,time,warn);
                machineList.add(machine);
                //machineMap.put(machineName,machine);
            }
            else {
                int index=findMachine(machineName);
                machineList.get(index).addTime(timeID,time,warn);
                //machineMap.get(machineName).addTimeNode(I,V,time,warn);
            }
        }
    }
    public  Map<String,Machine> getMap(){
        return machineMap;
    }
    public List<Machine> getList(){
        return machineList;
    }
    public List<Machine> getLatestList() throws SQLException {
        machineList.clear();
        getData();
        return machineList;
    }
    public int findMachine(String name){
        for (int i = 0; i < machineList.size(); i++) {
            if(machineList.get(i).machineName.equals(name))
                return i;
        }
        return -1;
    }
    public boolean isAdd(String name){
        for (Machine machine : machineList) {
            if(machine.machineName.equals(name)){
                return true;
            }
        }
        return false;
    }
}
