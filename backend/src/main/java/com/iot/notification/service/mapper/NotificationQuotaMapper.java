package com.iot.notification.service.mapper;

import com.iot.notification.entity.NotificationQuota;

/**
 * 通知额度管理Mapper接口
 * 
 * @author ruoyi
 * @date 2025-12-09
 */
public interface NotificationQuotaMapper
{
    /**
     * 通过传感器ID查询通知额度
     * 
     * @param sensorId 传感器ID
     * @return 通知额度信息
     */
    public NotificationQuota selectNotificationQuotaBySensorId(String sensorId);

    /**
     * 新增通知额度
     * 
     * @param notificationQuota 通知额度信息
     * @return 结果
     */
    public int insertNotificationQuota(NotificationQuota notificationQuota);

    /**
     * 修改通知额度
     * 
     * @param notificationQuota 通知额度信息
     * @return 结果
     */
    public int updateNotificationQuota(NotificationQuota notificationQuota);

    /**
     * 扣减免费语音通知额度
     * 
     * @param sensorId 传感器ID
     * @return 结果
     */
    public int deductFreeVoiceQuota(String sensorId);

    /**
     * 扣减免费短信通知额度
     * 
     * @param sensorId 传感器ID
     * @return 结果
     */
    public int deductFreeSmsQuota(String sensorId);

    /**
     * 扣减余额
     * 
     * @param notificationQuota 通知额度信息，包含sensorId和扣款金额
     * @return 结果
     */
    public int deductBalance(NotificationQuota notificationQuota);

    /**
     * 充值余额
     * 
     * @param notificationQuota 通知额度信息，包含sensorId和充值金额
     * @return 结果
     */
    public int rechargeBalance(NotificationQuota notificationQuota);

    /**
     * 增加免费语音通知额度
     * 
     * @param notificationQuota 通知额度信息，包含sensorId和增加数量
     * @return 结果
     */
    public int addFreeVoiceQuota(NotificationQuota notificationQuota);

    /**
     * 增加免费短信通知额度
     * 
     * @param notificationQuota 通知额度信息，包含sensorId和增加数量
     * @return 结果
     */
    public int addFreeSmsQuota(NotificationQuota notificationQuota);
}
