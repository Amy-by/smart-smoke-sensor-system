package com.iot.netty.server;

/**
 * Netty服务器接口
 * 
 * 说明：定义Netty服务器的统一接口，用于传感器数据接入
 * 
 * @author IoT架构组
 * @date 2025-01-27
 */
public interface NettyServer {
    
    /**
     * 启动Netty服务器
     * 
     * @param port 监听端口
     * @throws Exception 启动异常
     */
    void start(int port) throws Exception;
    
    /**
     * 停止Netty服务器
     * 
     * @throws Exception 停止异常
     */
    void stop() throws Exception;
    
    /**
     * 检查服务器是否运行中
     * 
     * @return true-运行中，false-已停止
     */
    boolean isRunning();
}

