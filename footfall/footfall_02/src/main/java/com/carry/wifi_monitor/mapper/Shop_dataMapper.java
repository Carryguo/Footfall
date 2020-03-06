package com.carry.wifi_monitor.mapper;

import com.carry.wifi_monitor.entity.Detail_data;
import com.carry.wifi_monitor.entity.Shop_data;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Shop_dataMapper {
    void insert();
    Integer selectWithin1hour();
    void updateWithin1hour();
    void update_SaveDataThread(@Param("consumer_number")Integer consumer_number, @Param("walker_number")Integer walker_number, @Param("new_consumer")Integer new_consumer,@Param("hour_consumernumber")Integer hour_consumernumber,@Param("hourConsumerNumber_hours")Integer hourConsumerNumber_hours,@Param("hour_inconsumernumber")Integer hour_inconsumernumber,@Param("hourInconsumernumber_hours")Integer hourInconsumernumber_hours);
    void update_CountDataThread(@Param("jumpout")Integer jumpout, @Param("dynamicconsumer")Integer dynamicconsumer);
    void insert_hour_consumernumber();
    List<Shop_data> show_hour_consumernumber(@Param("shop")Integer shop,@Param("num")Integer num);
    List<Shop_data> show_hour_inconsumernumber(@Param("shop")Integer shop,@Param("num")Integer num);
    List<Shop_data> search_detialData1(@Param("shop")Integer shop, @Param("num")Integer num);
    List<Shop_data> search_detialData2(@Param("shop")Integer shop, @Param("num")Integer num);
    List<Shop_data> search_detialData3(@Param("shop")Integer shop, @Param("num")Integer num);
    List<Shop_data> search_detialData4(@Param("shop")Integer shop, @Param("num")Integer num);
    Integer show_realtimedata(@Param("shop")Integer shop);
    Integer show_NowData(@Param("shop")Integer shop);
    List<Shop_data> countRealTimeData(@Param("shop")Integer shop, @Param("num")Integer num);
//List<Shop_data> countRealTimeData(@Param("shop")Integer shop, @Param("time")String time);
}
