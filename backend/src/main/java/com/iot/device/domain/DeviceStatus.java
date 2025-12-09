package com.iot.device.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 设备状态实体类
 * 用于表示设备的实时状态信息
 * 
 * @author 开发人员
 * @date 2025-12-10
 */
public class DeviceStatus extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 传感器IMEI号（主键） */
    private String sensorId;

    /** 设备在线状态（0-离线，1-在线） */
    private Integer onlineStatus;

    /** 电池电量（%） */
    private Integer batteryLevel;

    /** 信号强度（dBm） */
    private Integer signalStrength;

    /** 烟雾浓度（%） */
    private Integer smokeConcentration;

    /** 温度（℃） */
    private Integer temperature;

    /** 湿度（%） */
    private Integer humidity;

    /** 设备状态（0-正常，1-报警，2-故障） */
    private Integer deviceStatus;

    /** 最后上报时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastReportTime;

    /** 最后在线时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastOnlineTime;

    // getter和setter方法
    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public Integer getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(Integer onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public Integer getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(Integer batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public Integer getSignalStrength() {
        return signalStrength;
    }

    public void setSignalStrength(Integer signalStrength) {
        this.signalStrength = signalStrength;
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

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public Integer getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(Integer deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public Date getLastReportTime() {
        return lastReportTime;
    }

    public void setLastReportTime(Date lastReportTime) {
        this.lastReportTime = lastReportTime;
    }

    public Date getLastOnlineTime() {
        return lastOnlineTime;
    }

    public void setLastOnlineTime(Date lastOnlineTime) {
        this.lastOnlineTime = lastOnlineTime;
    }

    public String toString() {
        return "DeviceStatus{" +
                "sensorId='" + sensorId + '\'' +
                ", onlineStatus=" + onlineStatus +
                ", batteryLevel=" + batteryLevel +
                ", signalStrength=" + signalStrength +
                ", smokeConcentration=" + smokeConcentration +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", deviceStatus=" + deviceStatus +
                ", lastReportTime=" + lastReportTime +
                ", lastOnlineTime=" + lastOnlineTime +
                '}';
    }
}