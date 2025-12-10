package com.iot.notification.channel;

/**
 * 语音通知渠道实现类
 * 
 * 说明：实现语音通知的发送逻辑，使用腾讯云语音服务API
 * 设计原则：单一职责原则 - 只负责语音通知的发送
 * 
 * @author IoT架构组
 * @date 2025-01-27
 */
public class VoiceNotificationChannel implements NotificationChannel {
    
    /**
     * 发送语音通知
     * 
     * @param recipient 接收人手机号
     * @param content 语音内容文本
     * @return 发送结果
     * @throws NotificationSendException 语音发送异常
     */
    @Override
    public NotificationResult send(String recipient, String content) throws NotificationSendException {
        try {
            // TODO: 实际项目中应调用腾讯云语音API
            // 这里为了演示，使用模拟实现
            System.out.println("【腾讯云语音API】发送语音通知：");
            System.out.println("  接收人：" + recipient);
            System.out.println("  内容：" + content);
            
            // 模拟API调用成功
            NotificationResult result = new NotificationResult(true, "语音通知发送成功");
            result.setSendId("VOICE_" + System.currentTimeMillis()); // 生成模拟的发送ID
            return result;
        } catch (Exception e) {
            // 捕获并转换为统一的异常类型
            throw new NotificationSendException("语音通知发送失败：" + e.getMessage(), e);
        }
    }
    
    /**
     * 获取渠道类型
     * 
     * @return 渠道类型：VOICE
     */
    @Override
    public String getChannelType() {
        return "VOICE";
    }
}
