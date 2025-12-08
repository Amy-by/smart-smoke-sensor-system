package com.iot.alarm.processor;

/**
 * 报警处理异常
 * 
 * @author IoT架构组
 * @date 2025-01-27
 */
public class AlarmProcessException extends Exception {
    
    public AlarmProcessException(String message) {
        super(message);
    }
    
    public AlarmProcessException(String message, Throwable cause) {
        super(message, cause);
    }
}

