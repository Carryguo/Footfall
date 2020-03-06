package com.carry.wifi_monitor.mapper;

import com.carry.wifi_monitor.entity.Historical_data;
import org.springframework.stereotype.Repository;

@Repository
public interface Historical_dataMapper {
    void insert(Historical_data historical_data);
}
