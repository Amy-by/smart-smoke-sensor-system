package com.iot.alarm.processor;

import com.iot.alarm.rule.AlarmRuleEngine;
import com.iot.protocol.model.SensorData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 报警处理器实现类
 * 
 * 说明：实现报警检测和处理功能，负责调用报警规则引擎执行规则检测
 * 
 * @author 开发人员
 * @date 2025-12-10
 */
@Component
public class AlarmProcessorImpl implements AlarmProcessor {
    
    private static final Logger logger = LoggerFactory.getLogger(AlarmProcessorImpl.class);
    
    @Autowired
    private AlarmRuleEngine alarmRuleEngine;
    
    @Override
    public List<Alarm> process(SensorData sensorData) throws AlarmProcessException {
        try {
            if (sensorData == null) {
                throw new AlarmProcessException("传感器数据不能为空");
            }
            
            logger.info("开始处理传感器数据，设备ID：{}", sensorData.getDeviceId());
            
            // 调用报警规则引擎执行规则检测
            List<Alarm> alarms = alarmRuleEngine.executeRulesBySensorId(sensorData);
            
            if (alarms != null && !alarms.isEmpty()) {
                logger.info("检测到报警，设备ID：{}，报警数量：{}", sensorData.getDeviceId(), alarms.size());
                
                // 这里可以添加报警处理逻辑，比如：
                // 1. 保存报警记录
                // 2. 调用通知服务发送通知
                // 3. 更新设备状态
                
                // 暂时只返回检测到的报警
                return alarms;
            } else {
                logger.info("未检测到报警，设备ID：{}", sensorData.getDeviceId());
                return alarms;
            }
        } catch (Exception e) {
            logger.error("报警处理失败，设备ID：{}", sensorData != null ? sensorData.getDeviceId() : "未知", e);
            throw new AlarmProcessException("报警处理失败：" + e.getMessage(), e);
        }
    }
}
