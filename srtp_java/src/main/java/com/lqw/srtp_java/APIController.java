package com.lqw.srtp_java;

import com.alibaba.fastjson.JSON;
import com.lqw.srtp_java.dao.MachineDao;
import com.lqw.srtp_java.dao.TimeNodeDao;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@CrossOrigin
@RequestMapping("api")
public class APIController {

    @ResponseBody
    @RequestMapping("getInfo")
    public String getInfo() throws SQLException {
        MachineDao machineDao=new MachineDao();
        String j = JSON.toJSONString(machineDao.getLatestList());
        return j;
    }

    @ResponseBody
    @RequestMapping(value="getIV",method = RequestMethod.GET)
    public String getIV(@RequestParam Integer id) throws SQLException {
        TimeNodeDao timeNodeDao=new TimeNodeDao(id);
        String IV=JSON.toJSONString(timeNodeDao.getTimeNode());
        return IV;
    }

}
