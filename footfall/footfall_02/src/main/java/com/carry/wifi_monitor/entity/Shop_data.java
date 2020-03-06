package com.carry.wifi_monitor.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Shop_data {
    private Integer shop;
    private Integer walker_number;
    private Integer consumer_number;
    private Integer new_consumer;
    private Integer jumpout;
    private Integer dynamicconsumer;
    private Integer hour_consumernumber;
    private Integer hour_inconsumernumber;
    private Timestamp updatetime;
    private Integer hourConsumerNumber_hours;
    private Integer hourInconsumernumber_hours;
}
