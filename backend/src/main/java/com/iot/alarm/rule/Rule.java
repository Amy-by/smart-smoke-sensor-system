package com.iot.alarm.rule;

import com.iot.alarm.processor.Alarm;
import com.iot.protocol.model.SensorData;

/**
 * 规则接口
 * 
 * 说明：定义规则的执行接口，用于表示解析后的可执行规则
 * 设计原则：策略模式，支持不同类型的规则实现
 * 
 * @author 开发人员
 * @date 2025-12-10
 */
public interface Rule {
    
    /**
     * 执行规则检测
     * 
     * @param sensorData 传感器数据
     * @return 检测到的报警，如果没有报警则返回null
     */
    Alarm execute(SensorData sensorData);
    
    /**
     * 获取规则类型
     * 
     * @return 规则类型
     */
    Integer getRuleType();
}
