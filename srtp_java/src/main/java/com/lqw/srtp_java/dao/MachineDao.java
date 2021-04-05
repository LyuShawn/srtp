package com.lqw.srtp_java.dao;

import com.lqw.srtp_java.Util.DbUtil;
import com.lqw.srtp_java.pojo.Machine;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MachineDao {
    private Map<String, Machine> machineMap=new HashMap<>();
    private List<Machine> machineList=new ArrayList<>();

    public MachineDao() throws SQLException {
        getData();
    }

    public void getData() throws SQLException {
        String sql="select * from machine_info";
        ResultSet rs= DbUtil.executeQuery(sql);
        while(rs.next()){
            String machineName=rs.getString("machine_model");
            String I=rs.getString("current");
            String V=rs.getString("voltage");
            String time=rs.getString("time");
            int warn=rs.getInt("warning");
            if(!isAdd(machineName)){
                Machine machine=new Machine(machineName);
                machine.addTimeNode(I,V,time,warn);
                machineList.add(machine);
                //machineMap.put(machineName,machine);
            }
            else {
                int index=findMachine(machineName);
                machineList.get(index).addTimeNode(I,V,time,warn);
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
