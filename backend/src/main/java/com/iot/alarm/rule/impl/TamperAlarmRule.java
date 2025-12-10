package com.iot.alarm.rule.impl;

import com.iot.alarm.domain.AlarmRule;
import com.iot.alarm.processor.Alarm;
import com.iot.alarm.rule.Rule;
import com.iot.protocol.model.SensorData;

import java.time.LocalDateTime;

/**
 * 防拆报警规则实现
 * 
 * @author 开发人员
 * @date 2025-12-10
 */
public class TamperAlarmRule implements Rule {
    
    private String ruleName;
    private Integer priority;
    
    public TamperAlarmRule(AlarmRule rule) {
        this.ruleName = rule.getRuleName();
        this.priority = rule.getPriority();
    }
    
    @Override
    public Alarm execute(SensorData sensorData) {
        // 防拆报警通常由设备直接触发，这里简化处理
        // 实际应用中可能需要从原始数据中解析防拆状态
        Alarm alarm = new Alarm();
        alarm.setDeviceId(sensorData.getDeviceId());
        alarm.setAlarmType("防拆");
        alarm.setAlarmLevel(getAlarmLevel(priority));
        alarm.setAlarmTime(LocalDateTime.now());
        alarm.setDescription("设备被非法拆卸");
        return alarm;
    }
    
    @Override
    public Integer getRuleType() {
        return 4; // 4=防拆报警
    }
    
    /**
     * 根据优先级获取报警等级
     * 
     * @param priority 优先级（1-最高，2-高，3-中，4-低）
     * @return 报警等级
     */
    private String getAlarmLevel(Integer priority) {
        switch (priority) {
            case 1:
                return "紧急";
            case 2:
                return "严重";
            case 3:
                return "一般";
            case 4:
                return "轻微";
            default:
                return "一般";
        }
    }
}
