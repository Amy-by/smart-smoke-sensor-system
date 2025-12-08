package com.iot.protocol.adapter;

/**
 * 数据解析异常
 * 
 * @author IoT架构组
 * @date 2025-01-27
 */
public class DataParseException extends Exception {
    
    public DataParseException(String message) {
        super(message);
    }
    
    public DataParseException(String message, Throwable cause) {
        super(message, cause);
    }
}

