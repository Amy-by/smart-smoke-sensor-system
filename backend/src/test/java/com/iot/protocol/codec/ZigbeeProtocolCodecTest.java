package com.iot.protocol.codec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Test;

import com.iot.protocol.model.SensorData;

/**
 * Zigbee协议编解码器测试类
 * 
 * @author IoT架构组
 * @date 2025-01-27
 */
public class ZigbeeProtocolCodecTest {
    
    private ZigbeeProtocolCodec codec = new ZigbeeProtocolCodec();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @Test
    public void testDecode_ValidData() throws ProtocolCodecException {
        // 构造有效的Zigbee协议数据
        String validDataStr = "123456789012345|25|22|75|2025-01-27 10:30:00";
        byte[] validData = validDataStr.getBytes();
        
        // 解码数据
        SensorData sensorData = codec.decode(validData);
        
        // 验证解码结果
        assertNotNull(sensorData);
        assertEquals("ZIGBEE", sensorData.getProtocolType());
        assertEquals("123456789012345", sensorData.getDeviceId());
        assertEquals(Integer.valueOf(25), sensorData.getSmokeConcentration());
        assertEquals(Integer.valueOf(22), sensorData.getTemperature());
        assertEquals(Integer.valueOf(75), sensorData.getBatteryLevel());
        assertNotNull(sensorData.getReportTime());
        assertEquals("2025-01-27 10:30:00", sensorData.getReportTime().format(formatter));
        assertEquals(validDataStr, sensorData.getRawData());
    }
    
    @Test
    public void testEncode_ValidData() throws ProtocolCodecException {
        // 创建有效的传感器数据
        SensorData sensorData = new SensorData();
        sensorData.setDeviceId("123456789012345");
        sensorData.setProtocolType("ZIGBEE");
        sensorData.setSmokeConcentration(30);
        sensorData.setTemperature(26);
        sensorData.setBatteryLevel(85);
        sensorData.setReportTime(LocalDateTime.parse("2025-01-27 14:45:30", formatter));
        
        // 编码数据
        byte[] encodedData = codec.encode(sensorData);
        
        // 验证编码结果
        assertNotNull(encodedData);
        assertTrue(encodedData.length > 0);
        
        // 解码编码后的数据，验证一致性
        SensorData decodedData = codec.decode(encodedData);
        assertEquals(sensorData.getDeviceId(), decodedData.getDeviceId());
        assertEquals(sensorData.getSmokeConcentration(), decodedData.getSmokeConcentration());
        assertEquals(sensorData.getTemperature(), decodedData.getTemperature());
        assertEquals(sensorData.getBatteryLevel(), decodedData.getBatteryLevel());
        assertEquals(sensorData.getReportTime().format(formatter), decodedData.getReportTime().format(formatter));
    }
    
    @Test(expected = ProtocolCodecException.class)
    public void testDecode_InvalidFieldsCount() throws ProtocolCodecException {
        // 构造字段数量错误的Zigbee协议数据
        String invalidDataStr = "123456789012345|25|22|75";
        byte[] invalidData = invalidDataStr.getBytes();
        codec.decode(invalidData);
    }
    
    @Test(expected = ProtocolCodecException.class)
    public void testDecode_InvalidNumberFormat() throws ProtocolCodecException {
        // 构造数字格式错误的Zigbee协议数据
        String invalidDataStr = "123456789012345|abc|22|75|2025-01-27 10:30:00";
        byte[] invalidData = invalidDataStr.getBytes();
        codec.decode(invalidData);
    }
    
    @Test(expected = ProtocolCodecException.class)
    public void testDecode_InvalidDateTimeFormat() throws ProtocolCodecException {
        // 构造日期时间格式错误的Zigbee协议数据
        String invalidDataStr = "123456789012345|25|22|75|2025-01-27";
        byte[] invalidData = invalidDataStr.getBytes();
        codec.decode(invalidData);
    }
    
    @Test(expected = ProtocolCodecException.class)
    public void testDecode_MissingDeviceId() throws ProtocolCodecException {
        // 构造缺少设备ID的Zigbee协议数据
        String invalidDataStr = "|25|22|75|2025-01-27 10:30:00";
        byte[] invalidData = invalidDataStr.getBytes();
        codec.decode(invalidData);
    }
    
    @Test(expected = ProtocolCodecException.class)
    public void testEncode_MissingRequiredFields() throws ProtocolCodecException {
        // 创建缺少必要字段的传感器数据
        SensorData sensorData = new SensorData();
        sensorData.setDeviceId("123456789012345");
        // 缺少烟雾浓度、温度、电池电量和上报时间
        
        codec.encode(sensorData);
    }
    
    @Test
    public void testGetProtocolType() {
        assertEquals("ZIGBEE", codec.getProtocolType());
    }
}
