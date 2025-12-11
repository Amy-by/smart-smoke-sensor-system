package com.iot.datalog.service.impl;

import com.iot.datalog.domain.SensorDataLog;
import com.iot.datalog.mapper.SensorDataMapper;
import com.iot.datalog.service.SensorDataService;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 传感器数据服务实现类
 * 
 * @author iot
 * @date 2025-01-27
 */
@Service
public class SensorDataServiceImpl implements SensorDataService
{
    @Autowired
    private SensorDataMapper sensorDataMapper;

    /**
     * 保存传感器数据
     * 
     * @param sensorDataLog 传感器数据日志
     * @return 操作结果
     */
    @Override
    public int saveSensorData(SensorDataLog sensorDataLog)
    {
        if (sensorDataLog.getCollectionTime() == null)
        {
            sensorDataLog.setCollectionTime(new Date());
        }
        return sensorDataMapper.insertSensorDataLog(sensorDataLog);
    }

    /**
     * 查询传感器数据日志列表
     * 
     * @param sensorDataLog 传感器数据日志
     * @return 传感器数据日志集合
     */
    @Override
    public List<SensorDataLog> selectSensorDataLogList(SensorDataLog sensorDataLog)
    {
        return sensorDataMapper.selectSensorDataLogList(sensorDataLog);
    }

    /**
     * 查询传感器数据统计信息
     * 
     * @param sensorId 传感器ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param statisticsType 统计类型（AVG/MAX/MIN）
     * @return 统计结果
     */
    @Override
    public Map<String, Object> getSensorDataStatistics(String sensorId, Date startTime, Date endTime, String statisticsType)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("sensorId", sensorId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("statisticsType", statisticsType);
        return sensorDataMapper.selectSensorDataStatistics(params);
    }

    /**
     * 查询传感器最新数据
     * 
     * @param sensorId 传感器ID
     * @return 最新传感器数据
     */
    @Override
    public SensorDataLog getLatestSensorData(String sensorId)
    {
        return sensorDataMapper.selectLatestSensorData(sensorId);
    }

    /**
     * 查询传感器数据按时间分组统计
     * 
     * @param sensorId 传感器ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计结果集合
     */
    @Override
    public List<Map<String, Object>> getSensorDataByTimeGroup(String sensorId, Date startTime, Date endTime)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("sensorId", sensorId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        return sensorDataMapper.selectSensorDataByTimeGroup(params);
    }

    /**
     * 查询传感器报警统计
     * 
     * @param sensorId 传感器ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 报警统计结果
     */
    @Override
    public Map<String, Object> getSensorAlarmStatistics(String sensorId, Date startTime, Date endTime)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("sensorId", sensorId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        return sensorDataMapper.selectSensorAlarmStatistics(params);
    }
}
