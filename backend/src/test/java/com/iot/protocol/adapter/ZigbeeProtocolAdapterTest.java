package com.iot.protocol.adapter;

import com.iot.protocol.model.SensorData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Zigbee协议适配器单元测试
 * 
 * @author IoT架构组
 * @date 2025-01-27
 */
class ZigbeeProtocolAdapterTest {
    
    private ZigbeeProtocolAdapter adapter;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @BeforeEach
    void setUp() {
        adapter = new ZigbeeProtocolAdapter();
    }
    
    /**
     * 测试正常情况 - 完整的Zigbee协议数据解析
     */
    @Test
    void testAdapt_NormalCase() throws DataParseException {
        // 准备测试数据
        String rawData = "123456789012345|50|25|80|2025-01-27 10:30:00";
        
        // 执行测试
        SensorData result = adapter.adapt(rawData);
        
        // 验证结果
        assertNotNull(result);
        assertEquals("123456789012345", result.getDeviceId());
        assertEquals("ZIGBEE", result.getProtocolType());
        assertEquals(50, result.getSmokeConcentration());
        assertEquals(25, result.getTemperature());
        assertEquals(80, result.getBatteryLevel());
        assertEquals(LocalDateTime.parse("2025-01-27 10:30:00", formatter), result.getReportTime());
        assertEquals(rawData, result.getRawData());
    }
    
    /**
     * 测试边界情况 - 字段中有空格
     */
    @Test
    void testAdapt_FieldsWithSpaces() throws DataParseException {
        // 准备测试数据 - 字段中有空格
        String rawData = " 123456789012345 | 50 | 25 | 80 | 2025-01-27 10:30:00 ";
        
        // 执行测试
        SensorData result = adapter.adapt(rawData);
        
        // 验证结果
        assertNotNull(result);
        assertEquals("123456789012345", result.getDeviceId()); // 应该自动去除空格
        assertEquals("ZIGBEE", result.getProtocolType());
        assertEquals(50, result.getSmokeConcentration());
        assertEquals(25, result.getTemperature());
        assertEquals(80, result.getBatteryLevel());
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
     * 测试异常情况 - 字段数量不足
     */
    @Test
    void testAdapt_InsufficientFields() {
        // 准备测试数据 - 缺少一个字段
        String rawData = "123456789012345|50|25|80";
        
        DataParseException exception = assertThrows(DataParseException.class, () -> {
            adapter.adapt(rawData);
        });
        assertTrue(exception.getMessage().contains("原始数据字段数量错误"));
    }
    
    /**
     * 测试异常情况 - 字段数量过多
     */
    @Test
    void testAdapt_ExcessiveFields() {
        // 准备测试数据 - 多一个字段
        String rawData = "123456789012345|50|25|80|2025-01-27 10:30:00|extra";
        
        DataParseException exception = assertThrows(DataParseException.class, () -> {
            adapter.adapt(rawData);
        });
        assertTrue(exception.getMessage().contains("原始数据字段数量错误"));
    }
    
    /**
     * 测试异常情况 - 格式错误的数字
     */
    @Test
    void testAdapt_InvalidNumber() {
        // 准备测试数据 - 烟雾浓度为非数字
        String rawData = "123456789012345|abc|25|80|2025-01-27 10:30:00";
        
        DataParseException exception = assertThrows(DataParseException.class, () -> {
            adapter.adapt(rawData);
        });
        System.out.println("实际异常消息: " + exception.getMessage());
        assertTrue(exception.getMessage().contains("Zigbee协议数据解析失败"));
    }
    
    /**
     * 测试异常情况 - 格式错误的时间
     */
    @Test
    void testAdapt_InvalidTimeFormat() {
        // 准备测试数据 - 格式错误的时间
        String rawData = "123456789012345|50|25|80|2025-01-27 10:30";
        
        DataParseException exception = assertThrows(DataParseException.class, () -> {
            adapter.adapt(rawData);
        });
        assertTrue(exception.getMessage().contains("Zigbee协议数据解析失败"));
    }
    
    /**
     * 测试获取协议类型
     */
    @Test
    void testGetProtocolType() {
        assertEquals("ZIGBEE", adapter.getProtocolType());
    }
}