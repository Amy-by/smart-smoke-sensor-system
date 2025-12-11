package com.iot.datalog.mapper;

import com.iot.datalog.domain.SensorDataLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 传感器数据日志Mapper接口
 * 
 * @author iot
 * @date 2025-01-27
 */
@Mapper
public interface SensorDataMapper
{
    /**
     * 新增传感器数据日志
     * 
     * @param sensorDataLog 传感器数据日志
     * @return 结果
     */
    public int insertSensorDataLog(SensorDataLog sensorDataLog);

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
     * @param params 查询参数
     * @return 统计结果
     */
    public Map<String, Object> selectSensorDataStatistics(Map<String, Object> params);

    /**
     * 查询传感器最新数据
     * 
     * @param sensorId 传感器ID
     * @return 最新传感器数据
     */
    public SensorDataLog selectLatestSensorData(String sensorId);

    /**
     * 查询传感器数据按时间分组统计
     * 
     * @param params 查询参数
     * @return 统计结果集合
     */
    public List<Map<String, Object>> selectSensorDataByTimeGroup(Map<String, Object> params);

    /**
     * 查询传感器报警统计
     * 
     * @param params 查询参数
     * @return 报警统计结果
     */
    public Map<String, Object> selectSensorAlarmStatistics(Map<String, Object> params);
}
