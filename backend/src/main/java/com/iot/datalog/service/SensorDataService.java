package com.iot.datalog.service;

import com.iot.datalog.domain.SensorDataLog;
import com.ruoyi.framework.web.page.TableDataInfo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 传感器数据服务接口
 * 
 * @author iot
 * @date 2025-01-27
 */
public interface SensorDataService
{
    /**
     * 保存传感器数据
     * 
     * @param sensorDataLog 传感器数据日志
     * @return 操作结果
     */
    public int saveSensorData(SensorDataLog sensorDataLog);

    /**
     * 查询传感器数据日志列表
     * 
     * @param sensorDataLog 传感器数据日志
     * @return 传感器数据日志集合
     */
    public List<SensorDataLog> selectSensorDataLogList(SensorDataLog sensorDataLog);

    /**
     * 查询传感器数据统计信息
     * 
     * @param sensorId 传感器ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param statisticsType 统计类型（AVG/MAX/MIN）
     * @return 统计结果
     */
    public Map<String, Object> getSensorDataStatistics(String sensorId, Date startTime, Date endTime, String statisticsType);

    /**
     * 查询传感器最新数据
     * 
     * @param sensorId 传感器ID
     * @return 最新传感器数据
     */
    public SensorDataLog getLatestSensorData(String sensorId);

    /**
     * 查询传感器数据按时间分组统计
     * 
     * @param sensorId 传感器ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计结果集合
     */
    public List<Map<String, Object>> getSensorDataByTimeGroup(String sensorId, Date startTime, Date endTime);

    /**
     * 查询传感器报警统计
     * 
     * @param sensorId 传感器ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 报警统计结果
     */
    public Map<String, Object> getSensorAlarmStatistics(String sensorId, Date startTime, Date endTime);
}
