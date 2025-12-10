package com.iot.alarm.rule;

/**
 * 规则解析异常
 * 
 * 说明：用于表示规则解析过程中发生的异常
 * 
 * @author 开发人员
 * @date 2025-12-10
 */
public class RuleParseException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 构造方法
     * 
     * @param message 异常信息
     */
    public RuleParseException(String message) {
        super(message);
    }
    
    /**
     * 构造方法
     * 
     * @param message 异常信息
     * @param cause 异常原因
     */
    public RuleParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
