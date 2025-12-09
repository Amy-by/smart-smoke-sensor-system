package com.iot.protocol.codec;

import com.iot.protocol.model.SensorData;

/**
 * 协议编解码器接口
 * 
 * 说明：定义协议数据的编码和解码功能，支持多种传感器协议（Cat.1、Zigbee、WiFi等）
 * 设计原则：开闭原则 - 新增协议支持时只需实现此接口，无需修改现有代码
 * 
 * @author IoT架构组
 * @date 2025-01-27
 */
public interface ProtocolCodec {
    
    /**
     * 解码协议数据
     * 
     * @param rawData 原始协议数据
     * @return 标准化后的传感器数据
     * @throws ProtocolCodecException 协议编解码异常
     */
    SensorData decode(byte[] rawData) throws ProtocolCodecException;
    
    /**
     * 编码传感器数据
     * 
     * @param sensorData 传感器数据
     * @return 编码后的协议数据
     * @throws ProtocolCodecException 协议编解码异常
     */
    byte[] encode(SensorData sensorData) throws ProtocolCodecException;
    
    /**
     * 获取协议类型
     * 
     * @return 协议类型（如：CAT1、ZIGBEE、WIFI）
     */
    String getProtocolType();
}
