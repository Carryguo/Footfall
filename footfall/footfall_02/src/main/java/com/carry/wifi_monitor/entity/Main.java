package com.carry.wifi_monitor.entity;

import lombok.Data;

import java.sql.Timestamp;


@Data
public class Main {
	private Integer id;
	private String mac;
	private Integer rssi;
	private Timestamp latest_time;
	private String historical_time;
	private Integer machine_id;
	private Integer visit_times;
	private Integer stay_time;
	private Integer deep_visit_times;
	private Timestamp first_in_time;
	private Integer rein_judge;
}
