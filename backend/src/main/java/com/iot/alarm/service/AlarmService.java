package com.iot.alarm.service;

import com.iot.alarm.domain.AlarmLog;
import com.iot.alarm.domain.AlarmQueryDTO;
import com.iot.alarm.domain.AlarmVO;
import com.iot.protocol.model.SensorData;
import java.util.List;

/**
 * 报警服务接口
 * 
 * 说明：定义报警的管理和查询功能
 * 设计原则：单一职责原则 - 只负责报警业务逻辑
 * 
 * @author IoT开发人员
 * @date 2025-12-11
 */
public interface AlarmService {
    
    /**
     * 处理传感器报警数据
     * 
     * @param sensorData 传感器数据
     * @return 处理结果（报警日志ID列表）
     */
    List<Long> processAlarm(SensorData sensorData);
    
    /**
     * 保存报警记录
     * 
     * @param alarmLog 报警日志
     * @return 报警日志ID
     */
    Long saveAlarm(AlarmLog alarmLog);
    
    /**
     * 分页查询报警列表
     * 
     * @param queryDTO 查询条件
     * @return 报警列表
     */
    List<AlarmVO> getAlarmList(AlarmQueryDTO queryDTO);
    
    /**
     * 根据ID查询报警详情
     * 
     * @param alarmId 报警ID
     * @return 报警详情
     */
    AlarmVO getAlarmById(Long alarmId);
    
    /**
     * 处理报警（如消音）
     * 
     * @param alarmId 报警ID
     * @param handleUser 处理人
     * @return 处理结果
     */
    Boolean handleAlarm(Long alarmId, String handleUser);
    
    /**
     * 根据传感器ID查询报警记录
     * 
     * @param sensorId 传感器ID
     * @param limit 查询数量
     * @return 报警记录列表
     */
    List<AlarmLog> getAlarmsBySensorId(String sensorId, Integer limit);
}
