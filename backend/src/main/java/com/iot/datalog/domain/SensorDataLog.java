package com.iot.datalog.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

/**
 * 传感器数据日志实体类
 * 
 * @author iot
 * @date 2025-01-27
 */
public class SensorDataLog
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 传感器IMEI号 */
    private String sensorId;

    /** 烟雾浓度 */
    private Double smokeConcentration;

    /** 温度 */
    private Double temperature;

    /** 电池电量 */
    private Integer batteryLevel;

    /** 设备状态 */
    private String deviceStatus;

    /** 采集时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date collectionTime;

    /** 获取主键ID */
    public Long getId()
    {
        return id;
    }

    /** 设置主键ID */
    public void setId(Long id)
    {
        this.id = id;
    }

    /** 获取传感器IMEI号 */
    public String getSensorId()
    {
        return sensorId;
    }

    /** 设置传感器IMEI号 */
    public void setSensorId(String sensorId)
    {
        this.sensorId = sensorId;
    }

    /** 获取烟雾浓度 */
    public Double getSmokeConcentration()
    {
        return smokeConcentration;
    }

    /** 设置烟雾浓度 */
    public void setSmokeConcentration(Double smokeConcentration)
    {
        this.smokeConcentration = smokeConcentration;
    }

    /** 获取温度 */
    public Double getTemperature()
    {
        return temperature;
    }

    /** 设置温度 */
    public void setTemperature(Double temperature)
    {
        this.temperature = temperature;
    }

    /** 获取电池电量 */
    public Integer getBatteryLevel()
    {
        return batteryLevel;
    }

    /** 设置电池电量 */
    public void setBatteryLevel(Integer batteryLevel)
    {
        this.batteryLevel = batteryLevel;
    }

    /** 获取设备状态 */
    public String getDeviceStatus()
    {
        return deviceStatus;
    }

    /** 设置设备状态 */
    public void setDeviceStatus(String deviceStatus)
    {
        this.deviceStatus = deviceStatus;
    }

    /** 获取采集时间 */
    public Date getCollectionTime()
    {
        return collectionTime;
    }

    /** 设置采集时间 */
    public void setCollectionTime(Date collectionTime)
    {
        this.collectionTime = collectionTime;
    }
}