package com.iot.alarm.domain;

import java.util.Date;

/**
 * 报警查询条件DTO
 * 
 * 说明：用于封装报警查询的条件参数
 * 设计原则：单一职责原则 - 只负责查询条件的封装
 * 
 * @author IoT开发人员
 * @date 2025-12-11
 */
public class AlarmQueryDTO {
    
    /** 传感器ID */
    private String sensorId;
    
    /** 报警类型（1=火灾，2=温度，3=低电，4=防拆） */
    private Integer alarmType;
    
    /** 处理状态（0=未处理，1=已处理） */
    private Integer handleStatus;
    
    /** 开始时间 */
    private Date startTime;
    
    /** 结束时间 */
    private Date endTime;

    // getter和setter方法
    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public Integer getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(Integer alarmType) {
        this.alarmType = alarmType;
    }

    public Integer getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(Integer handleStatus) {
        this.handleStatus = handleStatus;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "AlarmQueryDTO{" + 
                "sensorId='" + sensorId + "'" +
                ", alarmType=" + alarmType +
                ", handleStatus=" + handleStatus +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
