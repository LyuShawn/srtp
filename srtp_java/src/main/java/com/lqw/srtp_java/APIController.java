package com.lqw.srtp_java;

import com.alibaba.fastjson.JSON;
import com.lqw.srtp_java.dao.MachineDao;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@CrossOrigin
@RequestMapping("api")
public class APIController {

    @ResponseBody
    @RequestMapping("getInfo")
    public String getInfo() throws SQLException {
        MachineDao machineDao=new MachineDao();
        String j = JSON.toJSONString(machineDao.getList());
        return j;
    }

}
