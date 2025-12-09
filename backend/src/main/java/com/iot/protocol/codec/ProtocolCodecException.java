package com.iot.protocol.codec;

/**
 * 协议编解码异常
 * 
 * 说明：用于表示协议数据编解码过程中的异常情况
 * 
 * @author IoT架构组
 * @date 2025-01-27
 */
public class ProtocolCodecException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 构造函数
     * 
     * @param message 异常信息
     */
    public ProtocolCodecException(String message) {
        super(message);
    }
    
    /**
     * 构造函数
     * 
     * @param message 异常信息
     * @param cause 异常原因
     */
    public ProtocolCodecException(String message, Throwable cause) {
        super(message, cause);
    }
}
