package com.lqw.srtp_java.controller;

import com.lqw.srtp_java.service.WebSocketServer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOError;
import java.io.IOException;

@Controller
public class MyController {

    @GetMapping("/socket/{cid}")
    public ModelAndView socket(@PathVariable String cid){
        ModelAndView mav=new ModelAndView("/socket");
        mav.addObject("cid",cid);
        return mav;
    }

    @ResponseBody
    @RequestMapping("/socket/push/{cid}")
    public String pushToWeb(@PathVariable String cid,String message){
        WebSocketServer.sendInfo(message,cid);

        return "发送成功";
    }
}
