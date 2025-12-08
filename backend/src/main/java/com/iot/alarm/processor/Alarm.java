package com.iot.alarm.processor;

import java.time.LocalDateTime;

/**
 * 报警信息
 * 
 * @author IoT架构组
 * @date 2025-01-27
 */
public class Alarm {
    
    /** 设备ID */
    private String deviceId;
    
    /** 报警类型（火灾/温度/低电/防拆） */
    private String alarmType;
    
    /** 报警等级（轻微/一般/严重/紧急） */
    private String alarmLevel;
    
    /** 报警时间 */
    private LocalDateTime alarmTime;
    
    /** 报警描述 */
    private String description;
    
    // Getters and Setters
    public String getDeviceId() {
        return deviceId;
    }
    
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    
    public String getAlarmType() {
        return alarmType;
    }
    
    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }
    
    public String getAlarmLevel() {
        return alarmLevel;
    }
    
    public void setAlarmLevel(String alarmLevel) {
        this.alarmLevel = alarmLevel;
    }
    
    public LocalDateTime getAlarmTime() {
        return alarmTime;
    }
    
    public void setAlarmTime(LocalDateTime alarmTime) {
        this.alarmTime = alarmTime;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
}

