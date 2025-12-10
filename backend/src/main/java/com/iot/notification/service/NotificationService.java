package com.iot.notification.service;

import com.iot.notification.channel.NotificationResult;
import com.iot.notification.entity.NotificationQuota;
import com.iot.notification.template.NotificationTemplate;
import com.iot.alarm.domain.AlarmLog;
import java.util.List;

/**
 * 通知服务接口
 * 
 * 说明：定义通知的管理和发送功能
 * 设计原则：单一职责原则 - 只负责通知业务逻辑
 * 
 * @author IoT架构组
 * @date 2025-01-27
 */
public interface NotificationService {
    
    /**
     * 触发通知（根据报警信息）
     * 
     * @param sensorId 传感器ID
     * @param alarmType 报警类型
     * @return 发送结果列表
     */
    List<NotificationResult> triggerNotification(String sensorId, String alarmType);
    
    /**
     * 发送通知
     * 
     * @param deviceId 设备ID
     * @param channelType 通知渠道（SMS/VOICE）
     * @param recipient 接收人
     * @param templateId 模板ID
     * @param params 模板参数
     * @return 发送结果
     */
    NotificationResult sendNotification(String deviceId, String channelType, String recipient, String templateId, Object params);
    
    /**
     * 根据报警日志发送通知
     * 
     * @param alarmLog 报警日志
     * @return 发送结果列表
     */
    List<NotificationResult> sendNotificationByAlarm(AlarmLog alarmLog);
    
    /**
     * 获取通知模板
     * 
     * @param templateId 模板ID
     * @return 通知模板
     */
    NotificationTemplate getTemplate(String templateId);
    
    /**
     * 获取指定类型的模板
     * 
     * @param templateType 模板类型（SMS/VOICE）
     * @param alarmType 报警类型
     * @return 通知模板
     */
    NotificationTemplate getTemplateByType(String templateType, String alarmType);
    
    /**
     * 渲染模板内容
     * 
     * @param template 通知模板
     * @param params 模板参数
     * @return 渲染后的内容
     */
    String renderTemplate(NotificationTemplate template, Object params);
    
    /**
     * 查询通知额度
     * 
     * @param sensorId 传感器ID
     * @return 通知额度信息
     */
    NotificationQuota queryNotificationQuota(String sensorId);
    
    /**
     * 充值余额
     * 
     * @param sensorId 传感器ID
     * @param amount 充值金额
     * @return 充值结果
     */
    boolean rechargeBalance(String sensorId, double amount);
    
    /**
     * 充值免费语音额度
     * 
     * @param sensorId 传感器ID
     * @param count 充值数量
     * @return 充值结果
     */
    boolean rechargeFreeVoiceQuota(String sensorId, int count);
    
    /**
     * 充值免费短信额度
     * 
     * @param sensorId 传感器ID
     * @param count 充值数量
     * @return 充值结果
     */
    boolean rechargeFreeSmsQuota(String sensorId, int count);
    
    /**
     * 扣减通知额度
     * 
     * @param sensorId 传感器ID
     * @param channelType 通知渠道（SMS/VOICE）
     * @return 是否成功扣减
     */
    boolean deductNotificationQuota(String sensorId, String channelType);
}
