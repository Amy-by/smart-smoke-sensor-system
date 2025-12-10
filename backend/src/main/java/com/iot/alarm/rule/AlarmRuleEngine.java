package com.iot.alarm.rule;

import com.iot.alarm.domain.AlarmRule;
import com.iot.alarm.processor.Alarm;
import com.iot.protocol.model.SensorData;

import java.util.List;

/**
 * 报警规则引擎接口
 * 
 * 说明：定义报警规则的解析和执行接口
 * 设计原则：开闭原则，支持不同类型的规则扩展
 * 
 * @author 开发人员
 * @date 2025-12-10
 */
public interface AlarmRuleEngine {
    
    /**
     * 解析报警规则
     * 
     * @param rule 报警规则实体
     * @return 解析后的规则对象
     * @throws RuleParseException 规则解析异常
     */
    Rule parseRule(AlarmRule rule) throws RuleParseException;
    
    /**
     * 根据传感器数据执行规则检测
     * 
     * @param sensorData 传感器数据
     * @param rules 报警规则列表
     * @return 检测到的报警列表
     */
    List<Alarm> executeRules(SensorData sensorData, List<AlarmRule> rules);
    
    /**
     * 根据传感器ID执行规则检测
     * 
     * @param sensorData 传感器数据
     * @return 检测到的报警列表
     */
    List<Alarm> executeRulesBySensorId(SensorData sensorData);
}
