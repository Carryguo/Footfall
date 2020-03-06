package com.carry.wifi_monitor.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Historical_data {
    private Integer walker_number;
    private Integer consumer_number;
    private Integer new_consumer_number;
    private Integer old_consumer_number;
    private Timestamp historical_time;
    private Integer machine_id;
}
