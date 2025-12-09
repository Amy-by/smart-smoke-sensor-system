package com.iot.data.forward;

/**
 * 数据转发异常
 * 
 * @author IoT架构组
 * @date 2025-01-27
 */
public class DataForwardException extends Exception {
    
    public DataForwardException(String message) {
        super(message);
    }
    
    public DataForwardException(String message, Throwable cause) {
        super(message, cause);
    }
}
