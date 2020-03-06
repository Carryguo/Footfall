package com.carry.wifi_monitor;

import com.carry.wifi_monitor.controller.SearchController;
import com.carry.wifi_monitor.entity.Hour_ConsumerNumber;
import com.carry.wifi_monitor.entity.Main;
import com.carry.wifi_monitor.mapper.MainMapper;
import com.carry.wifi_monitor.mapper.Shop_dataMapper;
import org.springframework.stereotype.Component;

import java.util.*;

public class CountDataThread implements Runnable {

    private MainMapper mainMapper;
    private Shop_dataMapper shop_dataMapper;

    CountDataThread(MainMapper mainMapper,Shop_dataMapper shop_dataMapper){
        this.mainMapper = mainMapper;
        this.shop_dataMapper = shop_dataMapper;
    }
   public static Integer DYNAMICCONSUMER;

    private List<Main> outConsumer;
    private Integer stayTime =0;
    private String mac;
    private Integer machine_id;
    public static Integer JUMPOUT=0;
    private Calendar c;
    @Override
    public void run() {

            while (true) {
                synchronized (this) {
                    try {
                //实时店内的人数
                DYNAMICCONSUMER = mainMapper.searchDynamicConsumer(SearchController.RSSI).size();

                outConsumer = mainMapper.searchOutConsumer(SearchController.RSSI);
                Main main;
                //计算跳出量
                for (int i = 0; i < outConsumer.size(); i++) {
                    main = outConsumer.get(i);
                    stayTime = new Long((main.getLatest_time().getTime() - main.getFirst_in_time().getTime()) / 1000).intValue();
                    mac = main.getMac();
                    machine_id = main.getMachine_id();

                    mainMapper.updateStayTime((stayTime / 60), mac, machine_id);

                    if (stayTime >= 600) {
                        mainMapper.updateDeepVisitTimes(mac, machine_id);
                    }
                    if (stayTime<50){
                        JUMPOUT++;
                    }
                    mainMapper.reoutJudgeUpdate(mac, machine_id);
                }


                //跟新跳出量和动态客流量
                        shop_dataMapper.update_CountDataThread(JUMPOUT,DYNAMICCONSUMER);

                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
