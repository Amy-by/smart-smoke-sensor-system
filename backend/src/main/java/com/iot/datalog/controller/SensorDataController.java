package com.iot.datalog.controller;

import com.iot.datalog.domain.SensorDataLog;
import com.iot.datalog.service.SensorDataService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 传感器数据控制器
 * 
 * @author iot
 * @date 2025-01-27
 */
@RestController
@RequestMapping("/api/iot/sensor-data")
public class SensorDataController extends BaseController
{
    @Autowired
    private SensorDataService sensorDataService;

    /**
     * 查询传感器数据日志列表
     * 
     * @param sensorDataLog 传感器数据日志
     * @return 传感器数据日志集合
     */
    @GetMapping("/list")
    public TableDataInfo list(SensorDataLog sensorDataLog)
    {
        startPage();
        List<SensorDataLog> list = sensorDataService.selectSensorDataLogList(sensorDataLog);
        return getDataTable(list);
    }

    /**
     * 获取传感器数据统计信息
     * 
     * @param deviceId 设备ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param statisticsType 统计类型（AVG/MAX/MIN）
     * @return 统计结果
     */
    @GetMapping("/statistics")
    public AjaxResult statistics(@RequestParam String deviceId,
                                 @RequestParam Date startTime,
                                 @RequestParam Date endTime,
                                 @RequestParam String statisticsType)
    {
        Map<String, Object> statistics = sensorDataService.getSensorDataStatistics(deviceId, startTime, endTime, statisticsType);
        return AjaxResult.success(statistics);
    }

    /**
     * 获取传感器最新数据
     * 
     * @param deviceId 设备ID
     * @return 最新传感器数据
     */
    @GetMapping("/latest")
    public AjaxResult latest(@RequestParam String deviceId)
    {
        SensorDataLog sensorDataLog = sensorDataService.getLatestSensorData(deviceId);
        return AjaxResult.success(sensorDataLog);
    }

    /**
     * 获取传感器数据按时间分组统计
     * 
     * @param deviceId 设备ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计结果集合
     */
    @GetMapping("/time-group")
    public AjaxResult timeGroup(@RequestParam String deviceId,
                               @RequestParam Date startTime,
                               @RequestParam Date endTime)
    {
        List<Map<String, Object>> statistics = sensorDataService.getSensorDataByTimeGroup(deviceId, startTime, endTime);
        return AjaxResult.success(statistics);
    }

    /**
     * 获取传感器报警统计
     * 
     * @param deviceId 设备ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 报警统计结果
     */
    @GetMapping("/alarm-statistics")
    public AjaxResult alarmStatistics(@RequestParam String deviceId,
                                     @RequestParam Date startTime,
                                     @RequestParam Date endTime)
    {
        Map<String, Object> statistics = sensorDataService.getSensorAlarmStatistics(deviceId, startTime, endTime);
        return AjaxResult.success(statistics);
    }
}
