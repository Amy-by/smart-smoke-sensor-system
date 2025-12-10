package com.iot.alarm.domain;

import java.math.BigDecimal;

import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 通知额度实体类
 * 对应数据库表：notification_quota
 * 
 * @author 开发人员
 * @date 2025-12-10
 */
public class NotificationQuota extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 传感器IMEI号（主键） */
    private String sensorId;

    /** 剩余免费语音条数 */
    private Integer freeVoiceCount;

    /** 剩余免费短信条数 */
    private Integer freeSmsCount;

    /** 账户余额 */
    private BigDecimal balance;

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

    public Integer getFreeVoiceCount() {
        return freeVoiceCount;
    }

    public void setFreeVoiceCount(Integer freeVoiceCount) {
        this.freeVoiceCount = freeVoiceCount;
    }

    public Integer getFreeSmsCount() {
        return freeSmsCount;
    }

    public void setFreeSmsCount(Integer freeSmsCount) {
        this.freeSmsCount = freeSmsCount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "NotificationQuota{" +
                "id=" + id +
                ", sensorId='" + sensorId + '\'' +
                ", freeVoiceCount=" + freeVoiceCount +
                ", freeSmsCount=" + freeSmsCount +
                ", balance=" + balance +
                '}';
    }
}
