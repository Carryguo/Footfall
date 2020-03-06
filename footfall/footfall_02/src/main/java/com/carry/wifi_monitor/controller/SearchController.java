package com.carry.wifi_monitor.controller;

import ch.qos.logback.core.joran.spi.ElementSelector;
import com.alibaba.fastjson.JSONArray;
import com.carry.wifi_monitor.CountDataThread;
import com.carry.wifi_monitor.SaveDataThread;
import com.carry.wifi_monitor.entity.*;
import com.carry.wifi_monitor.mapper.Machine_dataMapper;
import com.carry.wifi_monitor.mapper.Shop_dataMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class SearchController {

    @Resource
    private Shop_dataMapper shop_dataMapper;

    @Resource
    private Machine_dataMapper machine_dataMapper;

    //RSSI和BIGGESTRSSI初始值
    public static Integer RSSI = -50;
    public static Integer BIGGESTRSSI = -82;
    private Calendar c;

    @PostMapping("/update")
    @ResponseBody
    public Map<String,Integer> update(@RequestParam("shop")String shop){
        Integer walker_number = 0;
        Integer consumer_number = 0;
        Integer new_consumer = 0;
        Integer jumpout = 0;
        c = Calendar.getInstance();
        int num = c.get(Calendar.HOUR_OF_DAY)+1;
        Date t = new Date();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        //System.out.println(df.format(t));
        List<Shop_data> shop_dataList = shop_dataMapper.countRealTimeData(Integer.parseInt(shop),num);
//        List<Shop_data> shop_dataList = shop_dataMapper.countRealTimeData(Integer.parseInt(shop),"2019-04-12");
        //System.out.println(shop_dataList.size());

        for (int i = 0;i<shop_dataList.size();i++)
        {
            walker_number += shop_dataList.get(i).getWalker_number();
            consumer_number += shop_dataList.get(i).getConsumer_number();
            new_consumer += shop_dataList.get(i).getNew_consumer();
            jumpout += shop_dataList.get(i).getJumpout();
        }

        Integer dynamicconsumer = shop_dataMapper.show_realtimedata(Integer.parseInt(shop));

        Map<String,Integer> map=new HashMap<>();
        map.put("walkerNumber",walker_number);
        map.put("consumerNumber",consumer_number);
        map.put("newConsumer",new_consumer);
        map.put("jmpOut",jumpout);
        map.put("dynamicConsumer",dynamicconsumer);
        return map;
    }

    @PostMapping("/searchDetail")
    @ResponseBody
    public List<Detail_data> searchDetail(@RequestParam("shop")String shop,@RequestParam("type")String type){
        Integer getShop = Integer.parseInt(shop);
        Integer getType = Integer.parseInt(type);

        List<Shop_data> shop_dataList;
        List<Detail_data> detail_datalist = new ArrayList<>();;
        c = Calendar.getInstance();
        int num = c.get(Calendar.HOUR_OF_DAY)+1;

        if (getType==1)
        {
            shop_dataList =shop_dataMapper.search_detialData1(Integer.parseInt(shop),num);

            Detail_data detail_data;
            for(int i = 0;i<shop_dataList.size();i++)
            {
                detail_data = new Detail_data();
                detail_data.setDetailData(shop_dataList.get(i).getWalker_number());
                detail_data.setHours(shop_dataList.get(i).getHourConsumerNumber_hours());
                detail_data.setDescription("人流量每小时详细情况");
                detail_datalist.add(detail_data);
            }
            Collections.reverse(detail_datalist);
            return detail_datalist;
        }
        if (getType==2)
        {
            shop_dataList =shop_dataMapper.search_detialData2(Integer.parseInt(shop),num);

            Detail_data detail_data;
            for(int i = 0;i<shop_dataList.size();i++)
            {
                detail_data = new Detail_data();
                detail_data.setDetailData(shop_dataList.get(i).getConsumer_number());
                detail_data.setHours(shop_dataList.get(i).getHourConsumerNumber_hours());
                detail_data.setDescription("客流量每小时详细情况");
                detail_datalist.add(detail_data);
            }

            Collections.reverse(detail_datalist);
            return detail_datalist;
        }
        if (getType==3)
        {
            shop_dataList =shop_dataMapper.search_detialData3(Integer.parseInt(shop),num);

            Detail_data detail_data;
            for(int i = 0;i<shop_dataList.size();i++)
            {
                detail_data = new Detail_data();
                detail_data.setDetailData(shop_dataList.get(i).getNew_consumer());
                detail_data.setHours(shop_dataList.get(i).getHourConsumerNumber_hours());
                detail_data.setDescription("新客户每小时详细情况");
                detail_datalist.add(detail_data);
            }

            Collections.reverse(detail_datalist);
            return detail_datalist;
        }
        if (getType==4)
        {
            shop_dataList =shop_dataMapper.search_detialData4(Integer.parseInt(shop),num);

            Detail_data detail_data;
            for(int i = 0;i<shop_dataList.size();i++)
            {
                detail_data = new Detail_data();
                detail_data.setDetailData(shop_dataList.get(i).getJumpout());
                detail_data.setHours(shop_dataList.get(i).getHourConsumerNumber_hours());
                detail_data.setDescription("跳出量每小时详细情况");
                detail_datalist.add(detail_data);
            }

            Collections.reverse(detail_datalist);
            return detail_datalist;
        }

        return null;
    }

    @PostMapping("/showNowData")
    @ResponseBody
    public Integer showNowData(@RequestParam("shop")String shop){
        return shop_dataMapper.show_NowData(Integer.parseInt(shop));
    }

    @PostMapping("/setrssi")
    @ResponseBody
    public Integer setrssi(@RequestParam("rssi")String rssi) {
        String rssiM = rssi;
        Integer shopRssi;
        try {
            if (rssiM.matches("-[1-9]\\d*")) {
                shopRssi=Integer.parseInt(rssiM);
                if (shopRssi<-100)
                {
                    return 2;
                }else if (shopRssi<BIGGESTRSSI)
                {
                    return 3;
                }else if (shopRssi>0){
                    return 4;
                }else{
                    RSSI = shopRssi;
                    return 0;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("转换失败");
        }
        return 1;
    }

    @GetMapping("/showRssi")
    @ResponseBody
    public Map<String,Integer> showRssi(){
        Map<String,Integer> rssiMap =new HashMap<>();
        rssiMap.put("shopRssi",RSSI);
        rssiMap.put("biggestRssi",BIGGESTRSSI);
        return rssiMap;
    }

    @PostMapping("/setBiggestrssi")
    @ResponseBody
    public Integer setBiggestrssi(@RequestParam("bRssi")String bRssi) {
        String rssiM = bRssi;
        //System.out.println(bRssi);
        Integer biggestRssi;
        try {
            if (rssiM.matches("-[1-9]\\d*")) {
                biggestRssi=Integer.parseInt(rssiM);
                if (biggestRssi<-100)
                {
                    return 2;
                }else if (biggestRssi>RSSI)
                {
                    return 3;
                }else if (biggestRssi>0){
                    return 4;
                }else{
                    BIGGESTRSSI = biggestRssi;
                    return 0;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("转换失败");
        }
        return 1;
    }



    @PostMapping("/hourconsumer")
    @ResponseBody
    public List<Hour_ConsumerNumber> hourconsumer(@RequestParam("shop")String shop){
        List<Hour_ConsumerNumber> hour_consumerNumberList  = new ArrayList<Hour_ConsumerNumber>();
        c = Calendar.getInstance();
        int num = c.get(Calendar.HOUR_OF_DAY)+1;
        List<Shop_data> shop_dataList =shop_dataMapper.show_hour_consumernumber(Integer.parseInt(shop),num);
        Hour_ConsumerNumber hour_consumerNumber;
        for(int i = 0;i<shop_dataList.size();i++)
        {
            hour_consumerNumber = new Hour_ConsumerNumber();
            hour_consumerNumber.setHourConsumerNumber(shop_dataList.get(i).getHour_consumernumber());
            hour_consumerNumber.setHours(shop_dataList.get(i).getHourConsumerNumber_hours());
            hour_consumerNumberList.add(hour_consumerNumber);
        }
        Collections.reverse(hour_consumerNumberList);
        return hour_consumerNumberList;
    }

    @PostMapping("/hourinconsumer")
    @ResponseBody
    public List<Hour_InConsumerNumber> hourinconsumer(@RequestParam("shop")String shop){


        List<Hour_InConsumerNumber> hour_inconsumerNumberList  = new ArrayList<>();
        c = Calendar.getInstance();
        int num = c.get(Calendar.HOUR_OF_DAY)+1;
        List<Shop_data> shop_dataList =shop_dataMapper.show_hour_inconsumernumber(Integer.parseInt(shop),num);
        Hour_InConsumerNumber hour_inConsumerNumber;
        for(int i = 0;i<shop_dataList.size();i++)
        {
            hour_inConsumerNumber = new Hour_InConsumerNumber();
            hour_inConsumerNumber.setHourInConsumerNumber(shop_dataList.get(i).getHour_inconsumernumber());
            hour_inConsumerNumber.setHours(shop_dataList.get(i).getHourInconsumernumber_hours());
            hour_inconsumerNumberList.add(hour_inConsumerNumber);
        }
        Collections.reverse(hour_inconsumerNumberList);
        return hour_inconsumerNumberList;
    }

    @PostMapping("/searchMachineData")
    @ResponseBody
    public List<Machine_data> searchMachineData(@RequestParam("shop")String shop){

        List<Machine_data> machine_dataList = machine_dataMapper.searchMachine_data(Integer.parseInt(shop));
        Timestamp current_time = new Timestamp(System.currentTimeMillis());
        Integer timeCount;
        for (int i=0;i<machine_dataList.size();i++)
        {
            timeCount = new Long((current_time.getTime() - machine_dataList.get(i).getBeat().getTime()) / (60*1000)).intValue();
            if (timeCount>2){
                machine_dataList.get(i).setState("离线");
            }else{
                machine_dataList.get(i).setState("在线");
            }
        }
        Collections.reverse(machine_dataList);
        return machine_dataList;
    }
}
