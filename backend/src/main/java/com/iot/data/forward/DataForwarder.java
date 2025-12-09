package com.iot.data.forward;

import com.iot.protocol.model.SensorData;

/**
 * 数据转发器接口
 * 
 * 说明：定义传感器数据的转发功能，将解析后的传感器数据转发到相应的业务模块
 * 设计原则：依赖倒置原则 - 业务模块依赖抽象而非具体实现
 * 
 * @author IoT架构组
 * @date 2025-01-27
 */
public interface DataForwarder {
    
    /**
     * 转发传感器数据
     * 
     * @param sensorData 解析后的传感器数据
     * @throws DataForwardException 数据转发异常
     */
    void forward(SensorData sensorData) throws DataForwardException;
}
