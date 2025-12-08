package com.iot.notification.channel;

/**
 * 通知发送结果
 * 
 * @author IoT架构组
 * @date 2025-01-27
 */
public class NotificationResult {
    
    /** 是否成功 */
    private boolean success;
    
    /** 结果消息 */
    private String message;
    
    /** 发送ID（用于追踪） */
    private String sendId;
    
    public NotificationResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getSendId() {
        return sendId;
    }
    
    public void setSendId(String sendId) {
        this.sendId = sendId;
    }
}

