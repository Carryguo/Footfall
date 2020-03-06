package com.carry.wifi_monitor.service;

import com.carry.wifi_monitor.entity.Main;

import java.util.List;

public interface MainService {
    List<String> selectAll();
    void insert(Main permission);
}
