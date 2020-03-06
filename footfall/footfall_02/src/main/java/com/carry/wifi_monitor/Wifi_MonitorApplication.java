package com.carry.wifi_monitor;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("com.carry.wifi_monitor.mapper")
public class Wifi_MonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(Wifi_MonitorApplication.class, args);
    }
}
