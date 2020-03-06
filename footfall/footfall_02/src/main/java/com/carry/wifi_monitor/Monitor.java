package com.carry.wifi_monitor;

import com.carry.wifi_monitor.mapper.Historical_dataMapper;
import com.carry.wifi_monitor.mapper.Machine_dataMapper;
import com.carry.wifi_monitor.mapper.MainMapper;
import com.carry.wifi_monitor.mapper.Shop_dataMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class Monitor implements CommandLineRunner {

    @Resource
    private MainMapper mainMapper;

    @Resource
    private Historical_dataMapper historical_dataMapper;

    @Resource
    private Shop_dataMapper shop_dataMapper;

    @Resource
    private Machine_dataMapper machine_dataMapper;


    @Override
    public void run(String... strings) throws Exception {

        //一开始插入一条原始新数据
        if(shop_dataMapper.selectWithin1hour()==0)
            shop_dataMapper.insert();
        else {
            shop_dataMapper.updateWithin1hour();
        }

        //开始线程和传入map
        SaveDataThreadMonitor saveDataThreadMonitor = new SaveDataThreadMonitor();
        CountDataThread countDataThread = new CountDataThread(mainMapper,shop_dataMapper);
        SaveDataThread saveDataThread = new SaveDataThread(mainMapper,historical_dataMapper,shop_dataMapper,machine_dataMapper);
        new Thread(saveDataThreadMonitor).start();
        new Thread(countDataThread).start();
        new Thread(saveDataThread).start();
    }

}
