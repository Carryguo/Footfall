package com.carry.wifi_monitor.service;

import com.carry.wifi_monitor.entity.Main;
import com.carry.wifi_monitor.service.impl.MainServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.sql.Timestamp;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MainServiceTest {
    @Autowired
    private MainServiceImpl mainService;
    @Test
    public void insert() {
        Main main = new Main();
        main.setId(1);
        main.setDeep_visit_times(1);
        main.setHistorical_time("1313");

        Timestamp time= new Timestamp(System.currentTimeMillis());//获取系统当前时间
         main.setLatest_time(time);
        main.setMac("1313");
        main.setMachine_id(1324132);
        main.setRssi(31313);
        main.setVisit_times(1);
        main.setStay_time(2);

       // System.out.println(main.getLatestTime());
       mainService.insert(main);
    }
}