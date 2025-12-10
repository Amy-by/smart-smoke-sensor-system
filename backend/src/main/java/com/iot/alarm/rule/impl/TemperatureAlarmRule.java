package com.iot.alarm.rule.impl;

import com.iot.alarm.domain.AlarmRule;
import com.iot.alarm.processor.Alarm;
import com.iot.alarm.rule.Rule;
import com.iot.protocol.model.SensorData;

import java.time.LocalDateTime;

/**
 * 温度报警规则实现
 * 
 * @author 开发人员
 * @date 2025-12-10
 */
public class TemperatureAlarmRule implements Rule {
    
    private Integer threshold;
    private String ruleName;
    private Integer priority;
    
    public TemperatureAlarmRule(AlarmRule rule) {
        this.threshold = rule.getThreshold();
        this.ruleName = rule.getRuleName();
        this.priority = rule.getPriority();
    }
    
    @Override
    public Alarm execute(SensorData sensorData) {
        Integer temperature = sensorData.getTemperature();
        if (temperature != null && temperature >= threshold) {
            Alarm alarm = new Alarm();
            alarm.setDeviceId(sensorData.getDeviceId());
            alarm.setAlarmType("温度");
            alarm.setAlarmLevel(getAlarmLevel(priority));
            alarm.setAlarmTime(LocalDateTime.now());
            alarm.setDescription(String.format("温度超过阈值：%d℃（阈值：%d℃）", 
                temperature, threshold));
            return alarm;
        }
        return null;
    }
    
    @Override
    public Integer getRuleType() {
        return 2; // 2=温度报警
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
