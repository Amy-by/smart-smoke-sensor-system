package com.iot.data.forward.impl;

import com.iot.alarm.processor.Alarm;
import com.iot.alarm.processor.AlarmProcessor;
import com.iot.alarm.processor.AlarmProcessException;
import com.iot.data.forward.DataForwardException;
import com.iot.data.forward.DataForwarder;
import com.iot.protocol.model.SensorData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * DataForwarderImpl 单元测试
 * 
 * @author IoT架构组
 * @date 2025-01-27
 */
@ExtendWith(MockitoExtension.class)
public class DataForwarderImplTest {
    
    private static final Logger logger = LoggerFactory.getLogger(DataForwarderImplTest.class);
    
    @Mock
    private Map<String, AlarmProcessor> alarmProcessorMap;
    
    @Mock
    private AlarmProcessor mockAlarmProcessor;
    
    @InjectMocks
    private DataForwarderImpl dataForwarder;
    
    private SensorData testSensorData;
    
    @BeforeEach
    public void setUp() {
        // 初始化测试数据
        testSensorData = new SensorData();
        testSensorData.setDeviceId("123456789012345");
        testSensorData.setProtocolType("CAT1");
        testSensorData.setSmokeConcentration(50);
        testSensorData.setTemperature(25);
        testSensorData.setBatteryLevel(80);
        testSensorData.setReportTime(LocalDateTime.now());
        testSensorData.setRawData("{\"deviceId\":\"123456789012345\",\"data\":{\"smokeConcentration\":50,\"temperature\":25,\"batteryLevel\":80},\"reportTime\":\"2025-01-27 10:00:00\"}");
        
        // 配置报警处理器映射
        Map<String, AlarmProcessor> processorMap = new HashMap<>();
        processorMap.put("mockAlarmProcessor", mockAlarmProcessor);
        
        // 使用反射将mock的processorMap注入到dataForwarder
        try {
            java.lang.reflect.Field field = DataForwarderImpl.class.getDeclaredField("alarmProcessorMap");
            field.setAccessible(true);
            field.set(dataForwarder, processorMap);
        } catch (Exception e) {
            logger.error("注入mock对象失败：{}", e.getMessage(), e);
        }
    }
    
    /**
     * 测试转发正常的传感器数据
     */
    @Test
    public void testForwardNormalData() throws Exception {
        // 模拟报警处理器返回空报警列表（表示没有报警）
        when(mockAlarmProcessor.process(any(SensorData.class))).thenReturn(new ArrayList<>());
        
        // 执行测试
        assertDoesNotThrow(() -> dataForwarder.forward(testSensorData));
        
        // 验证报警处理器被调用
        verify(mockAlarmProcessor, times(1)).process(testSensorData);
    }
    
    /**
     * 测试转发异常数据（null数据）
     */
    @Test
    public void testForwardNullData() {
        // 执行测试，预期抛出异常
        DataForwardException exception = assertThrows(DataForwardException.class, () -> {
            dataForwarder.forward(null);
        });
        
        // 验证异常信息
        assertTrue(exception.getMessage().contains("传感器数据不能为空"));
    }
    
    /**
     * 测试转发数据时报警处理器抛出异常的情况
     */
    @Test
    public void testForwardWithAlarmProcessorException() throws Exception {
        // 模拟报警处理器抛出异常
        when(mockAlarmProcessor.process(any(SensorData.class)))
                .thenThrow(new AlarmProcessException("报警处理异常测试"));
        
        // 执行测试，验证是否能正常处理异常
        assertDoesNotThrow(() -> dataForwarder.forward(testSensorData));
        
        // 验证报警处理器仍然被调用
        verify(mockAlarmProcessor, times(1)).process(testSensorData);
    }
    
    /**
     * 测试转发数据时报警处理器检测到报警的情况
     */
    @Test
    public void testForwardWithAlarms() throws Exception {
        // 创建测试报警
        List<Alarm> testAlarms = new ArrayList<>();
        Alarm alarm = new Alarm();
        alarm.setDeviceId("123456789012345");
        alarm.setAlarmType("火灾报警");
        alarm.setAlarmLevel("严重");
        alarm.setAlarmTime(LocalDateTime.now());
        alarm.setDescription("烟雾浓度超过阈值");
        testAlarms.add(alarm);
        
        // 模拟报警处理器返回报警列表
        when(mockAlarmProcessor.process(any(SensorData.class))).thenReturn(testAlarms);
        
        // 执行测试
        assertDoesNotThrow(() -> dataForwarder.forward(testSensorData));
        
        // 验证报警处理器被调用
        verify(mockAlarmProcessor, times(1)).process(testSensorData);
    }
    
    /**
     * 测试没有报警处理器的情况
     */
    @Test
    public void testForwardWithoutAlarmProcessors() throws Exception {
        // 清空报警处理器映射
        try {
            java.lang.reflect.Field field = DataForwarderImpl.class.getDeclaredField("alarmProcessorMap");
            field.setAccessible(true);
            field.set(dataForwarder, new HashMap<>());
        } catch (Exception e) {
            logger.error("清空报警处理器映射失败：{}", e.getMessage(), e);
        }
        
        // 执行测试
        assertDoesNotThrow(() -> dataForwarder.forward(testSensorData));
    }
}
