package com.lqw.srtp_java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SrtpJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SrtpJavaApplication.class, args);
    }

}
