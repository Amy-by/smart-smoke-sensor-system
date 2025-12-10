package com.iot.alarm.rule.impl;

import com.iot.alarm.domain.AlarmRule;
import com.iot.alarm.processor.Alarm;
import com.iot.alarm.rule.AlarmRuleEngine;
import com.iot.alarm.rule.Rule;
import com.iot.alarm.rule.RuleParseException;
import com.iot.protocol.model.SensorData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 报警规则引擎实现
 * 
 * @author 开发人员
 * @date 2025-12-10
 */
public class AlarmRuleEngineImpl implements AlarmRuleEngine {
    
    private static final Logger logger = LoggerFactory.getLogger(AlarmRuleEngineImpl.class);
    
    @Override
    public Rule parseRule(AlarmRule rule) throws RuleParseException {
        if (rule == null) {
            throw new RuleParseException("规则不能为空");
        }
        
        Integer ruleType = rule.getRuleType();
        if (ruleType == null) {
            throw new RuleParseException("规则类型不能为空");
        }
        
        switch (ruleType) {
            case 1:
                return new SmokeAlarmRule(rule);
            case 2:
                return new TemperatureAlarmRule(rule);
            case 3:
                return new LowBatteryAlarmRule(rule);
            case 4:
                return new TamperAlarmRule(rule);
            default:
                throw new RuleParseException("不支持的规则类型：" + ruleType);
        }
    }
    
    @Override
    public List<Alarm> executeRules(SensorData sensorData, List<AlarmRule> rules) {
        List<Alarm> alarms = new ArrayList<>();
        
        if (sensorData == null || rules == null || rules.isEmpty()) {
            return alarms;
        }
        
        for (AlarmRule rule : rules) {
            // 只执行启用状态的规则
            if (rule.getStatus() != null && rule.getStatus() == 1) {
                try {
                    Rule parsedRule = parseRule(rule);
                    Alarm alarm = parsedRule.execute(sensorData);
                    if (alarm != null) {
                        alarms.add(alarm);
                    }
                } catch (RuleParseException e) {
                    logger.error("解析规则失败：{}", e.getMessage(), e);
                }
            }
        }
        
        return alarms;
    }
    
    @Override
    public List<Alarm> executeRulesBySensorId(SensorData sensorData) {
        // 这里应该从数据库中获取该传感器的所有规则
        // 由于暂时没有Mapper/Service，这里返回空列表
        return new ArrayList<>();
    }
}
