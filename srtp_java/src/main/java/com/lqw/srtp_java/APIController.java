package com.lqw.srtp_java;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class APIController {

    @RequestMapping("getInfo")
    public String getInfo(){
        return "";
    }
}
