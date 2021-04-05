package com.lqw.srtp_java;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lqw.srtp_java.dao.MachineDao;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api")
public class APIController {

    @ResponseBody
    @RequestMapping("getInfo")
    public String getInfo() throws SQLException {
        MachineDao machineDao=new MachineDao();
        System.out.println(machineDao.getList());
        String j = JSON.toJSONString(machineDao.getList());
        return j;
    }

    public JSONObject test(){
        JSONObject object = new JSONObject();
        //string
        object.put("string","string");
        //int
        object.put("int",2);
        //boolean
        object.put("boolean",true);
        //array
        List<Integer> integers = Arrays.asList(1,2,3);
        object.put("list",integers);
        //null
        object.put("null",null);
        return object;
    }
}
