package com.iot.alarm.domain;

import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 传感器联系人实体类
 * 对应数据库表：sensor_contact
 * 
 * @author 开发人员
 * @date 2025-12-10
 */
public class SensorContact extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 传感器IMEI号 */
    private String sensorId;

    /** 联系人用户ID */
    private Long userId;

    /** 联系电话 */
    private String phone;

    /** 通知类型（1-语音，2-短信） */
    private Integer notifyType;

    /** 通知优先级（1-最高） */
    private Integer priority;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(Integer notifyType) {
        this.notifyType = notifyType;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "SensorContact{" +
                "id=" + id +
                ", sensorId='" + sensorId + '\'' +
                ", userId=" + userId +
                ", phone='" + phone + '\'' +
                ", notifyType=" + notifyType +
                ", priority=" + priority +
                '}';
    }
}
