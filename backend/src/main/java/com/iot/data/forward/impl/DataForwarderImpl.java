package com.iot.data.forward.impl;

import com.iot.alarm.processor.Alarm;
import com.iot.alarm.processor.AlarmProcessor;
import com.iot.data.forward.DataForwardException;
import com.iot.data.forward.DataForwarder;
import com.iot.protocol.model.SensorData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 数据转发器实现类
 * 
 * 说明：实现数据转发功能，将解析后的传感器数据转发到各个业务模块
 * 设计原则：单一职责原则 - 专注于数据转发逻辑
 * 
 * @author IoT架构组
 * @date 2025-01-27
 */
@Component
public class DataForwarderImpl implements DataForwarder {
    
    private static final Logger logger = LoggerFactory.getLogger(DataForwarderImpl.class);
    
    /**
     * 报警处理器映射
     * 使用Map注入所有实现了AlarmProcessor接口的Bean
     */
    @Autowired
    private Map<String, AlarmProcessor> alarmProcessorMap;
    
    @Override
    public void forward(SensorData sensorData) throws DataForwardException {
        try {
            if (sensorData == null) {
                throw new DataForwardException("转发失败：传感器数据不能为空");
            }
            
            logger.info("开始转发传感器数据，设备ID：{}", sensorData.getDeviceId());
            
            // 1. 转发到报警处理模块
            forwardToAlarmModule(sensorData);
            
            // 2. 转发到数据日志模块（待实现）
            // forwardToDataLogModule(sensorData);
            
            // 3. 转发到设备状态更新模块（待实现）
            // forwardToDeviceStatusModule(sensorData);
            
            logger.info("传感器数据转发完成，设备ID：{}", sensorData.getDeviceId());
        } catch (Exception e) {
            logger.error("转发传感器数据失败：{}", e.getMessage(), e);
            throw new DataForwardException("数据转发失败：" + e.getMessage(), e);
        }
    }
    
    /**
     * 将传感器数据转发到报警处理模块
     * 
     * @param sensorData 传感器数据
     */
    private void forwardToAlarmModule(SensorData sensorData) {
        if (alarmProcessorMap.isEmpty()) {
            logger.warn("没有可用的报警处理器，跳过报警检测");
            return;
        }
        
        // 遍历所有报警处理器进行处理
        for (Map.Entry<String, AlarmProcessor> entry : alarmProcessorMap.entrySet()) {
            try {
                AlarmProcessor processor = entry.getValue();
                logger.info("使用报警处理器：{} 处理传感器数据", entry.getKey());
                
                // 调用报警处理器处理传感器数据
                List<Alarm> alarms = processor.process(sensorData);
                
                if (alarms != null && !alarms.isEmpty()) {
                    logger.info("检测到报警：{} 条", alarms.size());
                    // 这里可以添加报警后续处理逻辑（如存储、通知等）
                } else {
                    logger.info("未检测到报警");
                }
            } catch (Exception e) {
                logger.error("使用报警处理器 {} 处理传感器数据失败：{}", entry.getKey(), e.getMessage(), e);
                // 单个报警处理器处理失败不应影响其他处理器
            }
        }
    }
    
    /**
     * 将传感器数据转发到数据日志模块（待实现）
     * 
     * @param sensorData 传感器数据
     */
    private void forwardToDataLogModule(SensorData sensorData) {
        // 待实现：将传感器数据保存到数据日志
        logger.info("转发到数据日志模块（待实现），设备ID：{}", sensorData.getDeviceId());
    }
    
    /**
     * 将传感器数据转发到设备状态更新模块（待实现）
     * 
     * @param sensorData 传感器数据
     */
    private void forwardToDeviceStatusModule(SensorData sensorData) {
        // 待实现：更新设备状态
        logger.info("转发到设备状态更新模块（待实现），设备ID：{}", sensorData.getDeviceId());
    }
}
