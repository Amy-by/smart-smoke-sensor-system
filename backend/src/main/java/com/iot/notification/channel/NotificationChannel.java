package com.iot.notification.channel;

/**
 * 通知渠道接口
 * 
 * 说明：定义通知发送的统一接口，支持多种通知渠道（短信、语音、APP推送等）
 * 设计原则：开闭原则 - 新增通知渠道时只需实现此接口，无需修改现有代码
 * 
 * @author IoT架构组
 * @date 2025-01-27
 */
public interface NotificationChannel {
    
    /**
     * 发送通知
     * 
     * @param recipient 接收人（手机号、用户ID等）
     * @param content 通知内容
     * @return 发送结果
     * @throws NotificationSendException 通知发送异常
     */
    NotificationResult send(String recipient, String content) throws NotificationSendException;
    
    /**
     * 获取渠道类型
     * 
     * @return 渠道类型（如：SMS、VOICE、APP）
     */
    String getChannelType();
}

