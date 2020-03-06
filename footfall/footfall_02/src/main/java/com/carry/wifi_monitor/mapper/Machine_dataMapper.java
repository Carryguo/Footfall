package com.carry.wifi_monitor.mapper;

import com.carry.wifi_monitor.entity.Machine_data;
import com.carry.wifi_monitor.entity.Shop_data;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Machine_dataMapper {
    void insertMachine_data(@Param("shop")Integer shop,@Param("machine_id")Integer machine_id);
    void updateBeat(@Param("shop")Integer shop,@Param("machine_id")Integer machine_id);
    List<Machine_data> searchMachine_data(@Param("shop")Integer shop);
    List<Object> checkMachine_dataExist(@Param("shop")Integer shop,@Param("machine_id")Integer machine_id);
}
