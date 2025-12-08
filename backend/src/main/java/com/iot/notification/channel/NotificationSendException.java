package com.iot.notification.channel;

/**
 * 通知发送异常
 * 
 * @author IoT架构组
 * @date 2025-01-27
 */
public class NotificationSendException extends Exception {
    
    public NotificationSendException(String message) {
        super(message);
    }
    
    public NotificationSendException(String message, Throwable cause) {
        super(message, cause);
    }
}

