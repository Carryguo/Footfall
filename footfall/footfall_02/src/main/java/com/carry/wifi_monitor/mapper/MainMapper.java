package com.carry.wifi_monitor.mapper;

import com.carry.wifi_monitor.entity.Main;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface MainMapper {
    List<String> selectAll();
    void insert(Main main);
    List<Object> checkExist(@Param("mac") String mac, @Param("machine_id") Integer machine_id);
    void update(Main main);
    Timestamp searchFirstTime(@Param("mac")String mac,@Param("machine_id")Integer machine_id);
    Timestamp searchTime(@Param("mac")String mac,@Param("machine_id")Integer machine_id);
    void reIn(@Param("mac")String mac,@Param("machine_id")Integer machine_id);
    List<String> searchDynamicConsumer(Integer rssi);
    List<Main> searchOutConsumer(Integer rssi);
    void updateStayTime(@Param("stay_time")Integer stay_time,@Param("mac")String mac,@Param("machine_id")Integer machine_id);
    void updateDeepVisitTimes(@Param("mac")String mac,@Param("machine_id")Integer machine_id);
    Integer justJumpOut();
    void reinJudgeUpdate(@Param("mac")String mac,@Param("machine_id")Integer machine_id);
    void reoutJudgeUpdate(@Param("mac")String mac,@Param("machine_id")Integer machine_id);
    Integer searchRssi(@Param("mac")String mac,@Param("machine_id")Integer machine_id);
    Integer searchVisitTimes(@Param("mac")String mac,@Param("machine_id")Integer machine_id);
    Integer searchRein_judge(@Param("mac")String mac,@Param("machine_id")Integer machine_id);
    void updateBeat(@Param("mac")String mac,@Param("machine_id")Integer machine_id);
    Timestamp searchBeat(@Param("mac")String mac,@Param("machine_id")Integer machine_id);
}