package com.carry.wifi_monitor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.carry.wifi_monitor.entity.Historical_data;
import com.carry.wifi_monitor.entity.Main;
import com.carry.wifi_monitor.mapper.Historical_dataMapper;
import com.carry.wifi_monitor.mapper.MainMapper;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.sql.Timestamp;
import java.util.*;

public class SaveDataThreadMonitor implements Runnable {
    //监听器端口
    private final static int PORT = 8082;
    DatagramSocket ds;
    DatagramPacket dp;
    byte[] buf = null;
    String strReceive;
    //解析json数据

    public static JSONObject JSONOBJECT = null;




    @Override
    public void run() {
        try {
            ds = new DatagramSocket(PORT);
            System.out.println("等待链接");

            while (true) {
                synchronized (this) {
                    try {
                        //一秒钟获取一条数据
                        Thread.sleep(1000);
                        JSONOBJECT = null;
                        buf = new byte[1024];
                        dp = new DatagramPacket(buf, buf.length);
                        ds.receive(dp);
                        strReceive = new String(dp.getData(), 0, dp.getLength());
                        //有数据就存到JSONOBJECT,供给SaveDataTread处理
                        JSONOBJECT = JSONObject.parseObject(strReceive);

                    }catch(Exception e){

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ds.close();
            System.out.println("退出while循环.................");
        }
        System.out.println("退出while循环.................");
    }
}

