package com.iot.datalog.service;

import com.iot.datalog.domain.SensorDataLog;
import com.iot.datalog.mapper.SensorDataMapper;
import com.iot.datalog.service.impl.SensorDataServiceImpl;
import com.ruoyi.framework.web.page.TableDataInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * 传感器数据服务单元测试
 * 
 * @author iot
 * @date 2025-01-27
 */
class SensorDataServiceTest {

    @Mock
    private SensorDataMapper sensorDataMapper;

    @InjectMocks
    private SensorDataServiceImpl sensorDataService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveSensorData() {
        // 准备测试数据
        SensorDataLog sensorDataLog = new SensorDataLog();
        sensorDataLog.setSensorId("test-sensor-id");
        sensorDataLog.setSmokeConcentration(50.0);
        sensorDataLog.setTemperature(25.5);
        sensorDataLog.setBatteryLevel(80);
        sensorDataLog.setDeviceStatus("ONLINE");

        // 模拟mapper方法
        when(sensorDataMapper.insertSensorDataLog(sensorDataLog)).thenReturn(1);

        // 执行测试方法
        int result = sensorDataService.saveSensorData(sensorDataLog);

        // 验证结果
        assertEquals(1, result);
        verify(sensorDataMapper, times(1)).insertSensorDataLog(sensorDataLog);
    }

    @Test
    void testSelectSensorDataLogList() {
        // 准备测试数据
        SensorDataLog sensorDataLog = new SensorDataLog();
        sensorDataLog.setSensorId("test-sensor-id");

        List<SensorDataLog> mockList = new ArrayList<>();
        SensorDataLog data1 = new SensorDataLog();
        data1.setId(1L);
        data1.setSensorId("test-sensor-id");
        data1.setSmokeConcentration(50.0);
        mockList.add(data1);

        // 模拟mapper方法
        when(sensorDataMapper.selectSensorDataLogList(sensorDataLog)).thenReturn(mockList);

        // 执行测试方法
        List<SensorDataLog> result = sensorDataService.selectSensorDataLogList(sensorDataLog);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(sensorDataMapper, times(1)).selectSensorDataLogList(sensorDataLog);
    }

    @Test
    void testGetSensorDataStatistics() {
        // 准备测试数据
        String sensorId = "test-sensor-id";
        Date startTime = new Date();
        Date endTime = new Date();
        String statisticsType = "AVG";

        Map<String, Object> mockStats = new HashMap<>();
        mockStats.put("avgSmoke", 45.5);
        mockStats.put("avgTemp", 24.5);
        mockStats.put("avgBattery", 75.0);

        // 模拟mapper方法
        when(sensorDataMapper.selectSensorDataStatistics(anyMap())).thenReturn(mockStats);

        // 执行测试方法
        Map<String, Object> result = sensorDataService.getSensorDataStatistics(sensorId, startTime, endTime, statisticsType);

        // 验证结果
        assertNotNull(result);
        assertEquals(45.5, result.get("avgSmoke"));
        assertEquals(24.5, result.get("avgTemp"));
        assertEquals(75.0, result.get("avgBattery"));
        verify(sensorDataMapper, times(1)).selectSensorDataStatistics(anyMap());
    }

    @Test
    void testGetLatestSensorData() {
        // 准备测试数据
        String sensorId = "test-sensor-id";

        SensorDataLog mockData = new SensorDataLog();
        mockData.setId(1L);
        mockData.setSensorId(sensorId);
        mockData.setSmokeConcentration(50.0);
        mockData.setTemperature(25.5);
        mockData.setCollectionTime(new Date());

        // 模拟mapper方法
        when(sensorDataMapper.selectLatestSensorData(sensorId)).thenReturn(mockData);

        // 执行测试方法
        SensorDataLog result = sensorDataService.getLatestSensorData(sensorId);

        // 验证结果
        assertNotNull(result);
        assertEquals(sensorId, result.getSensorId());
        assertEquals(50.0, result.getSmokeConcentration());
        verify(sensorDataMapper, times(1)).selectLatestSensorData(sensorId);
    }

    @Test
    void testGetSensorDataByTimeGroup() {
        // 准备测试数据
        String sensorId = "test-sensor-id";
        Date startTime = new Date();
        Date endTime = new Date();

        List<Map<String, Object>> mockList = new ArrayList<>();
        Map<String, Object> data1 = new HashMap<>();
        data1.put("timeGroup", "2025-12-10 10:00:00");
        data1.put("avgSmoke", 45.5);
        data1.put("avgTemp", 24.5);
        data1.put("avgBattery", 75.0);
        mockList.add(data1);

        Map<String, Object> data2 = new HashMap<>();
        data2.put("timeGroup", "2025-12-10 11:00:00");
        data2.put("avgSmoke", 48.0);
        data2.put("avgTemp", 25.0);
        data2.put("avgBattery", 74.0);
        mockList.add(data2);

        // 模拟mapper方法
        when(sensorDataMapper.selectSensorDataByTimeGroup(anyMap())).thenReturn(mockList);

        // 执行测试方法
        List<Map<String, Object>> result = sensorDataService.getSensorDataByTimeGroup(sensorId, startTime, endTime);

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("2025-12-10 10:00:00", result.get(0).get("timeGroup"));
        assertEquals(45.5, result.get(0).get("avgSmoke"));
        verify(sensorDataMapper, times(1)).selectSensorDataByTimeGroup(anyMap());
    }

    @Test
    void testGetSensorAlarmStatistics() {
        // 准备测试数据
        String sensorId = "test-sensor-id";
        Date startTime = new Date();
        Date endTime = new Date();

        Map<String, Object> mockStats = new HashMap<>();
        mockStats.put("totalAlarmCount", 5);
        mockStats.put("smokeAlarmCount", 3);
        mockStats.put("temperatureAlarmCount", 2);
        mockStats.put("avgAlarmInterval", 1200); // 平均报警间隔（秒）
        mockStats.put("maxConsecutiveAlarms", 2); // 最大连续报警次数

        // 模拟mapper方法
        when(sensorDataMapper.selectSensorAlarmStatistics(anyMap())).thenReturn(mockStats);

        // 执行测试方法
        Map<String, Object> result = sensorDataService.getSensorAlarmStatistics(sensorId, startTime, endTime);

        // 验证结果
        assertNotNull(result);
        assertEquals(5, result.get("totalAlarmCount"));
        assertEquals(3, result.get("smokeAlarmCount"));
        assertEquals(2, result.get("temperatureAlarmCount"));
        verify(sensorDataMapper, times(1)).selectSensorAlarmStatistics(anyMap());
    }
}
