package com.carry.wifi_monitor.service.impl;

import com.carry.wifi_monitor.entity.Main;
import com.carry.wifi_monitor.mapper.MainMapper;
import com.carry.wifi_monitor.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MainServiceImpl implements MainService {
    @Autowired
    private MainMapper MainMapper;
    @Override
    public List<String> selectAll() {
        return MainMapper.selectAll();
    }

    @Override
    public void insert(Main main) {
        MainMapper.insert(main);
    }
}
