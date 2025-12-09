package com.iot.device.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 设备实体类
 * 对应数据库表：sensor_extend
 * 
 * @author 开发人员
 * @date 2025-12-10
 */
public class Device extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 传感器IMEI号（主键） */
    private String sensorId;

    /** 物料编码（固定JTY-GD-TCN2010） */
    private String materialCode;

    /** 安装位置 */
    private String installationLocation;

    /** 所属区域ID */
    private Long areaId;

    /** 所属区域名称 */
    private String areaName;

    /** SIM卡ID（Tag0x70，BCD[10]） */
    private String iccid;

    /** 通信模组版本（Tag0x67） */
    private String moduleVersion;

    /** 模组厂商（Tag0x68，如利尔达=03） */
    private String moduleVendor;

    /** 终端类型（固定0x98，Tag0x03） */
    private Integer terminalType;

    /** 正常上报间隔（秒，Tag0x06） */
    private Integer normalWorkInterval;

    /** 紧急报警间隔（秒，Tag0x08） */
    private Integer emergencyAlarmInterval;

    /** 烟雾阈值（%，Tag0x14） */
    private Integer smokeThreshold;

    /** 温度上限（℃，Tag0x0B） */
    private Integer tempUpperLimit;

    /** 温度下限（℃，Tag0x21） */
    private Integer tempLowerLimit;

    /** 电池寿命（固定3年） */
    private String batteryLife;

    /** 防护等级（固定IP30） */
    private String protectionLevel;

    /** 设备状态（0：未激活，1：已激活） */
    private String status;

    /** 绑定的用户ID */
    private Long userId;

    // getter和setter方法
    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getInstallationLocation() {
        return installationLocation;
    }

    public void setInstallationLocation(String installationLocation) {
        this.installationLocation = installationLocation;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getModuleVersion() {
        return moduleVersion;
    }

    public void setModuleVersion(String moduleVersion) {
        this.moduleVersion = moduleVersion;
    }

    public String getModuleVendor() {
        return moduleVendor;
    }

    public void setModuleVendor(String moduleVendor) {
        this.moduleVendor = moduleVendor;
    }

    public Integer getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(Integer terminalType) {
        this.terminalType = terminalType;
    }

    public Integer getNormalWorkInterval() {
        return normalWorkInterval;
    }

    public void setNormalWorkInterval(Integer normalWorkInterval) {
        this.normalWorkInterval = normalWorkInterval;
    }

    public Integer getEmergencyAlarmInterval() {
        return emergencyAlarmInterval;
    }

    public void setEmergencyAlarmInterval(Integer emergencyAlarmInterval) {
        this.emergencyAlarmInterval = emergencyAlarmInterval;
    }

    public Integer getSmokeThreshold() {
        return smokeThreshold;
    }

    public void setSmokeThreshold(Integer smokeThreshold) {
        this.smokeThreshold = smokeThreshold;
    }

    public Integer getTempUpperLimit() {
        return tempUpperLimit;
    }

    public void setTempUpperLimit(Integer tempUpperLimit) {
        this.tempUpperLimit = tempUpperLimit;
    }

    public Integer getTempLowerLimit() {
        return tempLowerLimit;
    }

    public void setTempLowerLimit(Integer tempLowerLimit) {
        this.tempLowerLimit = tempLowerLimit;
    }

    public String getBatteryLife() {
        return batteryLife;
    }

    public void setBatteryLife(String batteryLife) {
        this.batteryLife = batteryLife;
    }

    public String getProtectionLevel() {
        return protectionLevel;
    }

    public void setProtectionLevel(String protectionLevel) {
        this.protectionLevel = protectionLevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String toString() {
        return "Device{" +
                "sensorId='" + sensorId + '\'' +
                ", materialCode='" + materialCode + '\'' +
                ", installationLocation='" + installationLocation + '\'' +
                ", areaId=" + areaId +
                ", areaName='" + areaName + '\'' +
                ", iccid='" + iccid + '\'' +
                ", moduleVersion='" + moduleVersion + '\'' +
                ", moduleVendor='" + moduleVendor + '\'' +
                ", terminalType=" + terminalType +
                ", normalWorkInterval=" + normalWorkInterval +
                ", emergencyAlarmInterval=" + emergencyAlarmInterval +
                ", smokeThreshold=" + smokeThreshold +
                ", tempUpperLimit=" + tempUpperLimit +
                ", tempLowerLimit=" + tempLowerLimit +
                ", batteryLife='" + batteryLife + '\'' +
                ", protectionLevel='" + protectionLevel + '\'' +
                ", status='" + status + '\'' +
                ", userId=" + userId +
                '}';
    }
}