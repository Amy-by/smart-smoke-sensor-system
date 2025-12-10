package com.iot.alarm.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 传感器报警日志实体类
 * 对应数据库表：sensor_alarm_log
 * 
 * @author 开发人员
 * @date 2025-12-10
 */
public class AlarmLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 传感器IMEI号 */
    private String sensorId;

    /** 报警类型（1=火灾，2=温度，3=低电，4=防拆） */
    private Integer alarmType;

    /** 触发报警的协议Tag（0x01/0x02） */
    private Integer triggerTag;

    /** 触发值（如"烟雾浓度:120%"） */
    private String triggerValue;

    /** 报警时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date alarmTime;

    /** 处理状态（0=未处理，1=已处理） */
    private Integer handleStatus;

    /** 处理时间（如消音时间） */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date handleTime;

    /** 处理人（用户名） */
    private String handleUser;

    // getter和setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Integer getTriggerTag() {
        return triggerTag;
    }

    public void setTriggerTag(Integer triggerTag) {
        this.triggerTag = triggerTag;
    }

    public String getTriggerValue() {
        return triggerValue;
    }

    public void setTriggerValue(String triggerValue) {
        this.triggerValue = triggerValue;
    }

    public Date getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(Date alarmTime) {
        this.alarmTime = alarmTime;
    }

    public Integer getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(Integer handleStatus) {
        this.handleStatus = handleStatus;
    }

    public Date getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
    }

    public String getHandleUser() {
        return handleUser;
    }

    public void setHandleUser(String handleUser) {
        this.handleUser = handleUser;
    }

    @Override
    public String toString() {
        return "AlarmLog{" +
                "id=" + id +
                ", sensorId='" + sensorId + '\'' +
                ", alarmType=" + alarmType +
                ", triggerTag=" + triggerTag +
                ", triggerValue='" + triggerValue + '\'' +
                ", alarmTime=" + alarmTime +
                ", handleStatus=" + handleStatus +
                ", handleTime=" + handleTime +
                ", handleUser='" + handleUser + '\'' +
                '}';
    }
}
