package com.iot.alarm.processor;

import com.iot.protocol.model.SensorData;
import java.util.List;

/**
 * 报警处理器接口
 * 
 * 说明：定义报警处理的统一接口，负责报警检测和处理
 * 设计原则：依赖倒置原则 - 依赖抽象而非具体实现
 * 
 * @author IoT架构组
 * @date 2025-01-27
 */
public interface AlarmProcessor {
    
    /**
     * 处理传感器数据，检测并生成报警
     * 
     * @param sensorData 传感器数据
     * @return 报警列表
     * @throws AlarmProcessException 报警处理异常
     */
    List<Alarm> process(SensorData sensorData) throws AlarmProcessException;
}

