package com.iot.protocol.adapter;

import com.iot.protocol.model.SensorData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Cat.1协议适配器单元测试
 * 
 * @author IoT架构组
 * @date 2025-01-27
 */
class Cat1ProtocolAdapterTest {
    
    private Cat1ProtocolAdapter adapter;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @BeforeEach
    void setUp() {
        adapter = new Cat1ProtocolAdapter();
    }
    
    /**
     * 测试正常情况 - 完整的Cat.1协议数据解析
     */
    @Test
    void testAdapt_NormalCase() throws DataParseException {
        // 准备测试数据
        String rawData = "{\"deviceId\":\"123456789012345\",\"data\":{\"smokeConcentration\":50,\"temperature\":25,\"batteryLevel\":80},\"reportTime\":\"2025-01-27 10:30:00\"}";
        
        // 执行测试
        SensorData result = adapter.adapt(rawData);
        
        // 验证结果
        assertNotNull(result);
        assertEquals("123456789012345", result.getDeviceId());
        assertEquals("CAT1", result.getProtocolType());
        assertEquals(50, result.getSmokeConcentration());
        assertEquals(25, result.getTemperature());
        assertEquals(80, result.getBatteryLevel());
        assertEquals(LocalDateTime.parse("2025-01-27 10:30:00", formatter), result.getReportTime());
        assertEquals(rawData, result.getRawData());
    }
    
    /**
     * 测试边界情况 - 部分数据缺失
     */
    @Test
    void testAdapt_PartialData() throws DataParseException {
        // 准备测试数据 - 缺少电池电量
        String rawData = "{\"deviceId\":\"123456789012345\",\"data\":{\"smokeConcentration\":50,\"temperature\":25},\"reportTime\":\"2025-01-27 10:30:00\"}";
        
        // 执行测试
        SensorData result = adapter.adapt(rawData);
        
        // 验证结果
        assertNotNull(result);
        assertEquals("123456789012345", result.getDeviceId());
        assertEquals("CAT1", result.getProtocolType());
        assertEquals(50, result.getSmokeConcentration());
        assertEquals(25, result.getTemperature());
        assertNull(result.getBatteryLevel()); // 电池电量应为null
        assertEquals(LocalDateTime.parse("2025-01-27 10:30:00", formatter), result.getReportTime());
        assertEquals(rawData, result.getRawData());
    }
    
    /**
     * 测试异常情况 - 空数据
     */
    @Test
    void testAdapt_EmptyData() {
        DataParseException exception = assertThrows(DataParseException.class, () -> {
            adapter.adapt("");
        });
        assertEquals("原始数据不能为空", exception.getMessage());
    }
    
    /**
     * 测试异常情况 - null数据
     */
    @Test
    void testAdapt_NullData() {
        DataParseException exception = assertThrows(DataParseException.class, () -> {
            adapter.adapt(null);
        });
        assertEquals("原始数据不能为空", exception.getMessage());
    }
    
    /**
     * 测试异常情况 - 缺少必要字段
     */
    @Test
    void testAdapt_MissingRequiredField() {
        // 准备测试数据 - 缺少reportTime字段
        String rawData = "{\"deviceId\":\"123456789012345\",\"data\":{\"smokeConcentration\":50,\"temperature\":25}}".replace("\\", "");
        
        DataParseException exception = assertThrows(DataParseException.class, () -> {
            adapter.adapt(rawData);
        });
        assertTrue(exception.getMessage().contains("原始数据缺少必要字段"));
    }
    
    /**
     * 测试异常情况 - 格式错误的JSON
     */
    @Test
    void testAdapt_InvalidJson() {
        // 准备测试数据 - 格式错误的JSON
        String rawData = "{\"deviceId\":\"123456789012345\",\"data\":{\"smokeConcentration\":50,\"temperature\":25},\"reportTime\":\"2025-01-27 10:30:00\"";
        
        DataParseException exception = assertThrows(DataParseException.class, () -> {
            adapter.adapt(rawData);
        });
        assertTrue(exception.getMessage().contains("Cat.1协议数据解析失败"));
    }
    
    /**
     * 测试异常情况 - 格式错误的时间
     */
    @Test
    void testAdapt_InvalidTimeFormat() {
        // 准备测试数据 - 格式错误的时间
        String rawData = "{\"deviceId\":\"123456789012345\",\"data\":{\"smokeConcentration\":50,\"temperature\":25},\"reportTime\":\"2025-01-27 10:30\"}".replace("\\", "");
        
        DataParseException exception = assertThrows(DataParseException.class, () -> {
            adapter.adapt(rawData);
        });
        assertTrue(exception.getMessage().contains("Cat.1协议数据解析失败"));
    }
    
    /**
     * 测试获取协议类型
     */
    @Test
    void testGetProtocolType() {
        assertEquals("CAT1", adapter.getProtocolType());
    }
}