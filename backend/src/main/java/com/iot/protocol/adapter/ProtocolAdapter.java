package com.iot.protocol.adapter;

import com.iot.protocol.model.SensorData;

/**
 * 协议适配器接口
 * 
 * 说明：定义协议适配的统一接口，支持多种传感器协议（Cat.1、Zigbee、WiFi等）
 * 设计原则：开闭原则 - 新增协议支持时只需实现此接口，无需修改现有代码
 * 
 * @author IoT架构组
 * @date 2025-01-27
 */
public interface ProtocolAdapter {
    
    /**
     * 适配协议数据
     * 
     * @param rawData 原始协议数据
     * @return 标准化后的传感器数据
     * @throws DataParseException 数据解析异常
     */
    SensorData adapt(String rawData) throws DataParseException;
    
    /**
     * 获取协议类型
     * 
     * @return 协议类型（如：CAT1、ZIGBEE、WIFI）
     */
    String getProtocolType();
}

