package com.iot.protocol.model;

import java.time.LocalDateTime;

/**
 * 传感器数据模型
 * 
 * 说明：标准化的传感器数据模型，用于统一不同协议的数据格式
 * 
 * @author IoT架构组
 * @date 2025-01-27
 */
public class SensorData {
    
    /** 设备ID（IMEI） */
    private String deviceId;
    
    /** 协议类型 */
    private String protocolType;
    
    /** 烟雾浓度（%） */
    private Integer smokeConcentration;
    
    /** 温度（℃） */
    private Integer temperature;
    
    /** 电池电量（%） */
    private Integer batteryLevel;
    
    /** 数据上报时间 */
    private LocalDateTime reportTime;
    
    /** 原始数据 */
    private String rawData;
    
    // Getters and Setters
    public String getDeviceId() {
        return deviceId;
    }
    
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    
    public String getProtocolType() {
        return protocolType;
    }
    
    public void setProtocolType(String protocolType) {
        this.protocolType = protocolType;
    }
    
    public Integer getSmokeConcentration() {
        return smokeConcentration;
    }
    
    public void setSmokeConcentration(Integer smokeConcentration) {
        this.smokeConcentration = smokeConcentration;
    }
    
    public Integer getTemperature() {
        return temperature;
    }
    
    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }
    
    public Integer getBatteryLevel() {
        return batteryLevel;
    }
    
    public void setBatteryLevel(Integer batteryLevel) {
        this.batteryLevel = batteryLevel;
    }
    
    public LocalDateTime getReportTime() {
        return reportTime;
    }
    
    public void setReportTime(LocalDateTime reportTime) {
        this.reportTime = reportTime;
    }
    
    public String getRawData() {
        return rawData;
    }
    
    public void setRawData(String rawData) {
        this.rawData = rawData;
    }
}

