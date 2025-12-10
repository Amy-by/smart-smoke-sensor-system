package com.iot.alarm.domain;

import java.util.Date;

/**
 * 报警视图对象
 * 
 * 说明：用于封装返回给前端的报警数据
 * 设计原则：单一职责原则 - 只负责视图数据的封装
 * 
 * @author IoT开发人员
 * @date 2025-12-11
 */
public class AlarmVO {
    
    /** 报警ID */
    private Long id;
    
    /** 传感器ID */
    private String sensorId;
    
    /** 传感器名称 */
    private String sensorName;
    
    /** 安装位置 */
    private String installationLocation;
    
    /** 报警类型（1=火灾，2=温度，3=低电，4=防拆） */
    private Integer alarmType;
    
    /** 报警类型名称 */
    private String alarmTypeName;
    
    /** 触发值 */
    private String triggerValue;
    
    /** 报警时间 */
    private Date alarmTime;
    
    /** 处理状态（0=未处理，1=已处理） */
    private Integer handleStatus;
    
    /** 处理状态名称 */
    private String handleStatusName;
    
    /** 处理时间 */
    private Date handleTime;
    
    /** 处理人 */
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

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public String getInstallationLocation() {
        return installationLocation;
    }

    public void setInstallationLocation(String installationLocation) {
        this.installationLocation = installationLocation;
    }

    public Integer getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(Integer alarmType) {
        this.alarmType = alarmType;
        // 设置报警类型名称
        if (alarmType != null) {
            switch (alarmType) {
                case 1:
                    this.alarmTypeName = "火灾报警";
                    break;
                case 2:
                    this.alarmTypeName = "温度报警";
                    break;
                case 3:
                    this.alarmTypeName = "低电报警";
                    break;
                case 4:
                    this.alarmTypeName = "防拆报警";
                    break;
                default:
                    this.alarmTypeName = "未知报警";
            }
        }
    }

    public String getAlarmTypeName() {
        return alarmTypeName;
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
        // 设置处理状态名称
        if (handleStatus != null) {
            this.handleStatusName = handleStatus == 0 ? "未处理" : "已处理";
        }
    }

    public String getHandleStatusName() {
        return handleStatusName;
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
        return "AlarmVO{" + 
                "id=" + id +
                ", sensorId='" + sensorId + "'" +
                ", sensorName='" + sensorName + "'" +
                ", installationLocation='" + installationLocation + "'" +
                ", alarmType=" + alarmType +
                ", alarmTypeName='" + alarmTypeName + "'" +
                ", triggerValue='" + triggerValue + "'" +
                ", alarmTime=" + alarmTime +
                ", handleStatus=" + handleStatus +
                ", handleStatusName='" + handleStatusName + "'" +
                ", handleTime=" + handleTime +
                ", handleUser='" + handleUser + "'" +
                '}';
    }
}
