package com.iot.alarm.service.impl;

import com.iot.alarm.domain.AlarmLog;
import com.iot.alarm.processor.Alarm;
import com.iot.alarm.processor.AlarmProcessor;
import com.iot.alarm.processor.AlarmProcessException;
import com.iot.alarm.service.mapper.AlarmMapper;
import com.iot.device.service.DeviceService;
import com.iot.protocol.model.SensorData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.mockito.stubbing.Answer;

/**
 * AlarmServiceImpl单元测试
 *
 * @author yourname
 * @date 2025-01-01
 */
public class AlarmServiceImplTest {

    private static final Logger log = LoggerFactory.getLogger(AlarmServiceImplTest.class);

    @Mock
    private AlarmMapper alarmMapper;

    @Mock
    private DeviceService deviceService;

    @Mock
    private AlarmProcessor alarmProcessor;

    @InjectMocks
    private AlarmServiceImpl alarmService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * 测试processAlarm方法 - 正常处理报警
     */
    @Test
    void testProcessAlarm_Success() throws Exception {
        // 准备测试数据
        SensorData sensorData = new SensorData();
        sensorData.setDeviceId("869149040980562");
        sensorData.setProtocolType("zigbee");
        sensorData.setSmokeConcentration(120);
        sensorData.setReportTime(LocalDateTime.now());
        sensorData.setRawData("0x01020304");

        List<Alarm> alarms = new ArrayList<>();
        Alarm alarm = new Alarm();
        alarm.setDeviceId("869149040980562");
        alarm.setAlarmType("火灾");
        alarm.setAlarmLevel("严重");
        alarm.setAlarmTime(LocalDateTime.now());
        alarm.setDescription("检测到烟雾浓度超标");
        alarms.add(alarm);

        // 模拟依赖行为
        when(alarmProcessor.process(sensorData)).thenReturn(alarms);
        doAnswer(invocation -> {
            AlarmLog alarmLog = invocation.getArgument(0);
            alarmLog.setId(1L); // 设置alarmLog的id
            return 1;
        }).when(alarmMapper).insertAlarm(any(AlarmLog.class));

        // 执行测试
        List<Long> alarmIds = alarmService.processAlarm(sensorData);

        // 验证结果
        assertNotNull(alarmIds);
        assertEquals(1, alarmIds.size());

        // 验证调用次数
        verify(alarmProcessor, times(1)).process(sensorData);
        verify(alarmMapper, times(1)).insertAlarm(any(AlarmLog.class));

        log.info("testProcessAlarm_Success 测试通过");
    }

    /**
     * 测试processAlarm方法 - 无报警情况
     */
    @Test
    void testProcessAlarm_NoAlarm() throws Exception {
        // 准备测试数据
        SensorData sensorData = new SensorData();
        sensorData.setDeviceId("869149040980562");
        sensorData.setProtocolType("zigbee");
        sensorData.setSmokeConcentration(50);
        sensorData.setReportTime(LocalDateTime.now());
        sensorData.setRawData("0x01020304");

        // 模拟依赖行为 - 返回空报警列表
        when(alarmProcessor.process(sensorData)).thenReturn(new ArrayList<>());

        // 执行测试
        List<Long> alarmIds = alarmService.processAlarm(sensorData);

        // 验证结果
        assertNotNull(alarmIds);
        assertTrue(alarmIds.isEmpty());

        // 验证调用次数
        verify(alarmProcessor, times(1)).process(sensorData);
        verify(alarmMapper, never()).insertAlarm(any(AlarmLog.class));

        log.info("testProcessAlarm_NoAlarm 测试通过");
    }

    /**
     * 测试processAlarm方法 - 报警处理异常
     */
    @Test
    void testProcessAlarm_Exception() throws Exception {
        // 准备测试数据
        SensorData sensorData = new SensorData();
        sensorData.setDeviceId("869149040980562");
        sensorData.setProtocolType("zigbee");
        sensorData.setSmokeConcentration(120);
        sensorData.setReportTime(LocalDateTime.now());
        sensorData.setRawData("0x01020304");

        // 模拟依赖行为 - 抛出异常
        String errorMsg = "报警处理失败";
        when(alarmProcessor.process(sensorData)).thenThrow(new AlarmProcessException(errorMsg));

        // 执行测试
        List<Long> alarmIds = alarmService.processAlarm(sensorData);

        // 验证结果 - 应该返回空列表，而不是抛出异常
        assertNotNull(alarmIds);
        assertTrue(alarmIds.isEmpty());

        // 验证调用次数
        verify(alarmProcessor, times(1)).process(sensorData);
        verify(alarmMapper, never()).insertAlarm(any(AlarmLog.class));

        log.info("testProcessAlarm_Exception 测试通过");
    }

    /**
     * 测试saveAlarm方法
     */
    @Test
    void testSaveAlarm() {
        // 准备测试数据
        AlarmLog alarmLog = new AlarmLog();
        alarmLog.setSensorId("869149040980562");
        alarmLog.setAlarmType(1); // 1=火灾
        alarmLog.setTriggerValue("烟雾浓度:120%");
        alarmLog.setAlarmTime(new Date());
        alarmLog.setHandleStatus(0);

        // 模拟依赖行为
        doAnswer(invocation -> {
            AlarmLog argAlarmLog = invocation.getArgument(0);
            argAlarmLog.setId(1L); // 设置alarmLog的id
            return 1;
        }).when(alarmMapper).insertAlarm(any(AlarmLog.class));

        // 执行测试
        Long alarmId = alarmService.saveAlarm(alarmLog);

        // 验证结果
        assertNotNull(alarmId);
        assertEquals(1L, alarmId);

        // 验证调用次数
        verify(alarmMapper, times(1)).insertAlarm(alarmLog);

        log.info("testSaveAlarm 测试通过");
    }
}
