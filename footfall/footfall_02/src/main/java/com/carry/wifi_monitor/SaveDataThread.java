package com.carry.wifi_monitor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.carry.wifi_monitor.controller.SearchController;
import com.carry.wifi_monitor.entity.Historical_data;
import com.carry.wifi_monitor.entity.Hour_ConsumerNumber;
import com.carry.wifi_monitor.entity.Hour_InConsumerNumber;
import com.carry.wifi_monitor.entity.Main;
import com.carry.wifi_monitor.mapper.Historical_dataMapper;
import com.carry.wifi_monitor.mapper.Machine_dataMapper;
import com.carry.wifi_monitor.mapper.MainMapper;
import com.carry.wifi_monitor.mapper.Shop_dataMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.carry.wifi_monitor.CountDataThread.DYNAMICCONSUMER;
import static com.carry.wifi_monitor.CountDataThread.JUMPOUT;
import static com.carry.wifi_monitor.controller.SearchController.BIGGESTRSSI;


@Component
public class SaveDataThread implements Runnable {
    //解析json数据
    JSONArray jsonArray = null;
    JSONObject jsonObject = null;
    JSONObject jsonObjecta = null;

    //助存
    Integer checkExist = 0;
    Integer checkMachineExist = 0;
    String mac;
    Integer rssi;
    Timestamp latest_time;
    Timestamp first_in_time;
    String historical_time;

    Timestamp exit_latest_time;

    Integer timeCount;

    public static Integer CONSUMER_NUMBER = 0;
    public static Integer WALKER_NUMBER = 0;
    public static Integer NEW_CONSUMER = 0;

    static Integer hour_consumer_number_sup_2 = 0;
    static Integer consumer_number_sup_2 = 0;
    static Integer walker_number_sup_2 = 0;
    Integer old_consumer_number = 0;

    static Integer hour_inconsumernumber_sup = 0;

    Calendar c;

    static Integer machine_id = 0;

    private MainMapper mainMapper;
    private Historical_dataMapper historical_dataMapper;
    private Shop_dataMapper shop_dataMapper;
    private Machine_dataMapper machine_dataMapper;



    private Integer rssiJ=0;

    SaveDataThread(MainMapper mainMapper, Historical_dataMapper historical_dataMapper,Shop_dataMapper shop_dataMapper,Machine_dataMapper machine_dataMapper) {
        this.mainMapper = mainMapper;
        this.historical_dataMapper = historical_dataMapper;
        this.shop_dataMapper = shop_dataMapper;
        this.machine_dataMapper = machine_dataMapper;
    }

    @Override
    public void run() {
        try {

            //正则表达式筛选数据
            String macM = "([a-f0-9A-F]{2}:){5}[a-f0-9A-F]{2}";
            String rssiM = "-[1-9]\\d*";
            String idM = "^[1-9]\\d*$";


            while (true) {
                synchronized (this) {
                    //如果jason有数据,则进行处理
                    jsonObject = SaveDataThreadMonitor.JSONOBJECT;
                    if (jsonObject != null) {

                        machine_id = jsonObject.getInteger("Id");
                        Main main = new Main();

                        //保存设备id
                        if (machine_id != null && machine_id.toString().matches(idM)) {
                            main.setMachine_id(machine_id);
                        } else
                            continue;

                        //保存mac rssi latest_time
                        String data = jsonObject.getString("Data");
                        if (data != null) {
                            jsonArray = JSONArray.parseArray(data);
                        } else
                            continue;

                        int size = jsonArray.size();
                        for (int i = 0; i < size; i++) {
                            jsonObjecta = jsonArray.getJSONObject(i);
                            mac = jsonObjecta.getString("mac");
                            rssi = jsonObjecta.getInteger("rssi");

                            rssiJ=rssi.intValue();
                            //筛选
                            if (mac != null && mac.matches(macM) && rssi != null && rssi.toString().matches(rssiM)&&!mac.startsWith("00:00")&&rssiJ>BIGGESTRSSI) {
                                first_in_time = new Timestamp(System.currentTimeMillis());
                                main.setMac(mac);
                                main.setRssi(rssi);
                                latest_time = new Timestamp(System.currentTimeMillis());

                                //查看设备是否存在
                                checkMachineExist = machine_dataMapper.checkMachine_dataExist(1,machine_id).size();
                                if (checkMachineExist>=1){
                                    machine_dataMapper.updateBeat(1,machine_id);
                                }else{
                                    machine_dataMapper.insertMachine_data(1,machine_id);
                                }

                                //检查mac是否已经存在
                                checkExist = mainMapper.checkExist(mac, machine_id).size();
                                if (checkExist >= 1) {
                                    exit_latest_time = mainMapper.searchBeat(mac, machine_id);
                                    timeCount = new Long((latest_time.getTime() - exit_latest_time.getTime()) / (60*1000)).intValue();

                                    //这里测试是测试大于2分钟时算再进的客人,到时候再改
                                    if (timeCount >= 15) {
                                        if (main.getRssi() > SearchController.RSSI) {
                                            main.setFirst_in_time(first_in_time);
                                            //增加流量数
                                            consumer_number_sup_2++;

                                            hour_consumer_number_sup_2++;

                                            walker_number_sup_2++;

                                            //更新reinJudge
                                            mainMapper.reinJudgeUpdate(mac, machine_id);
                                            //再进的时候小时客流量+1;
                                            hour_inconsumernumber_sup++;

                                            //更新再进店的客人信息
                                            mainMapper.reIn(mac, machine_id);
                                            main.setRein_judge(1);
                                            main.setLatest_time(latest_time);
                                        } else {
                                            mainMapper.updateBeat(mac, machine_id);
                                            main.setFirst_in_time(mainMapper.searchFirstTime(mac, machine_id));
                                            main.setLatest_time(mainMapper.searchTime(mac, machine_id));
                                            walker_number_sup_2++;
                                            main.setRein_judge(mainMapper.searchRein_judge(mac, machine_id));
                                        }

                                        //这里rssi之后再设置
                                    }else{
                                        if (main.getRssi() > SearchController.RSSI)
                                        {
                                            if (mainMapper.searchRein_judge(mac, machine_id)==0)
                                            {
                                                //增加流量数
                                                consumer_number_sup_2++;

                                                hour_consumer_number_sup_2++;

                                                hour_inconsumernumber_sup++;
                                                //如果还没有进过店的,视为新客人
                                                if (mainMapper.searchVisitTimes(mac, machine_id)==0)
                                                {
                                                    NEW_CONSUMER++;
                                                    hour_inconsumernumber_sup++;
                                                }
                                                main.setFirst_in_time(first_in_time);
                                                mainMapper.reIn(mac, machine_id);
                                            }
                                            main.setRein_judge(1);
                                            main.setFirst_in_time(mainMapper.searchFirstTime(mac, machine_id));
                                            main.setLatest_time(latest_time);
                                        }
                                        else{
                                            //客人已出去
                                            if (mainMapper.searchRein_judge(mac, machine_id)==1)
                                            {
                                                main.setRein_judge(0);
                                            }
                                            main.setRein_judge(mainMapper.searchRein_judge(mac, machine_id));
                                            main.setFirst_in_time(mainMapper.searchFirstTime(mac, machine_id));
                                            main.setLatest_time(mainMapper.searchTime(mac, machine_id));
                                        }
                                    }

                                    //判断从远走到近的客人
                                    if (mainMapper.searchRssi(mac, machine_id)<SearchController.RSSI&&rssiJ>SearchController.RSSI&&mainMapper.searchVisitTimes(mac, machine_id)==0&&mainMapper.searchRein_judge(mac, machine_id)==0) {
                                        main.setFirst_in_time(first_in_time);
                                        NEW_CONSUMER++;
                                        consumer_number_sup_2++;
                                        hour_consumer_number_sup_2++;
                                        hour_inconsumernumber_sup++;
                                        main.setLatest_time(latest_time);
                                        mainMapper.reIn(mac, machine_id);
                                        main.setRein_judge(1);
                                    }
                                    mainMapper.updateBeat(mac, machine_id);
                                    mainMapper.update(main);
                                } else {
                                    main.setFirst_in_time(first_in_time);
                                    historical_time = new Timestamp(System.currentTimeMillis()).toString();
                                    main.setHistorical_time(historical_time);

                                    if (main.getRssi() > SearchController.RSSI) {
                                        consumer_number_sup_2++;
                                        hour_consumer_number_sup_2++;
                                        NEW_CONSUMER++;
                                        main.setRein_judge(1);
                                        //新客人进店时小时客流量再+1
                                        hour_inconsumernumber_sup++;
                                        main.setLatest_time(latest_time);
                                        main.setVisit_times(1);

                                    } else {
                                        main.setLatest_time(latest_time);
                                        main.setRein_judge(0);
                                        main.setVisit_times(0);
                                    }
                                    walker_number_sup_2++;
                                    mainMapper.insert(main);
                                    mainMapper.updateBeat(mac, machine_id);
                                }
                            } else
                                continue;
                        }
                    }

                    c = Calendar.getInstance();
                    int hour = c.get(Calendar.HOUR_OF_DAY);

                    //这里用于存储实时数据
                    //客流量 人流量 新客人 小时客流量 小时 小时进店量 小时
                    shop_dataMapper.update_SaveDataThread(consumer_number_sup_2,walker_number_sup_2,NEW_CONSUMER,hour_consumer_number_sup_2,hour,hour_inconsumernumber_sup,hour);

                    Thread.sleep(500);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("退出循环...");
        }
    }

    //一小时刷新一次数据
    @Transactional
    @Async
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void savePer1Hour(){

           c = Calendar.getInstance();
       //一小时算插入一次新数据
        shop_dataMapper.insert_hour_consumernumber();

        //重置小时进店量
        hour_inconsumernumber_sup=0;

        //重置小时客流量
        hour_consumer_number_sup_2=DYNAMICCONSUMER;

        //重置新客人量
        NEW_CONSUMER = 0;

        //重置人流量
        walker_number_sup_2 = 0;

        //重置客流量
        consumer_number_sup_2 = 0;

        //重置跳出量
        JUMPOUT = 0;

        //存储历史数据
        Historical_data historical_data = new Historical_data();
        historical_data.setConsumer_number(CONSUMER_NUMBER);
        historical_data.setWalker_number(WALKER_NUMBER);
        latest_time = new Timestamp(System.currentTimeMillis());
        historical_data.setHistorical_time(latest_time);
        historical_data.setMachine_id(machine_id);
        historical_data.setNew_consumer_number(NEW_CONSUMER);
        old_consumer_number = mainMapper.selectAll().size() - NEW_CONSUMER;
        historical_data.setOld_consumer_number(old_consumer_number);
        historical_dataMapper.insert(historical_data);
    }
}