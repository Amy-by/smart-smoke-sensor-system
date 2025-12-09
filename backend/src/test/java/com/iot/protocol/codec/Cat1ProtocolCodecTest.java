package com.iot.protocol.codec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Test;

import com.iot.protocol.model.SensorData;

/**
 * Cat.1协议编解码器测试类
 * 
 * @author IoT架构组
 * @date 2025-01-27
 */
public class Cat1ProtocolCodecTest {
    
    private Cat1ProtocolCodec codec = new Cat1ProtocolCodec();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @Test
    public void testDecode_ValidData() throws ProtocolCodecException {
        // 构造有效的Cat.1协议数据
        // 帧结构：0x7E（头） + 版本(1) + UTC时间(1609459200) + 帧号(1) + 长度(10) + 指令(1) + 加密(0) + TLV数据 + CRC(0000) + 0x7E（尾）
        // TLV数据：Tag0x65(IMEI) + Length(8) + Value(1234567890123456) + Tag0x14(烟雾) + Length(2) + Value(50) + Tag0x0B(温度) + Length(2) + Value(25) + Tag0x24(电量) + Length(1) + Value(80)
        byte[] validData = hexStringToBytes("7E015E1E9C8000010018010065081234567890123456140200320B02001924015000007E");
        
        // 解码数据
        SensorData sensorData = codec.decode(validData);
        
        // 验证解码结果
        assertNotNull(sensorData);
        assertEquals("CAT1", sensorData.getProtocolType());
        assertEquals("1234567890123456", sensorData.getDeviceId());
        assertEquals(Integer.valueOf(50), sensorData.getSmokeConcentration());
        assertEquals(Integer.valueOf(25), sensorData.getTemperature());
        assertEquals(Integer.valueOf(80), sensorData.getBatteryLevel());
        assertNotNull(sensorData.getReportTime());
        assertEquals("2021-01-01 00:00:00", sensorData.getReportTime().format(formatter));
        assertNotNull(sensorData.getRawData());
    }
    
    @Test
    public void testEncode_ValidData() throws ProtocolCodecException {
        // 创建有效的传感器数据
        SensorData sensorData = new SensorData();
        sensorData.setDeviceId("1234567890123456");
        sensorData.setProtocolType("CAT1");
        sensorData.setSmokeConcentration(50);
        sensorData.setTemperature(25);
        sensorData.setBatteryLevel(80);
        sensorData.setReportTime(LocalDateTime.parse("2021-01-01 00:00:00", formatter));
        
        // 编码数据
        byte[] encodedData = codec.encode(sensorData);
        
        // 验证编码结果
        assertNotNull(encodedData);
        assertTrue(encodedData.length > 0);
        assertEquals((byte) 0x7E, encodedData[0]); // 验证帧头
        assertEquals((byte) 0x7E, encodedData[encodedData.length - 1]); // 验证帧尾
        
        // 解码编码后的数据，验证一致性
        SensorData decodedData = codec.decode(encodedData);
        assertEquals(sensorData.getDeviceId(), decodedData.getDeviceId());
        assertEquals(sensorData.getSmokeConcentration(), decodedData.getSmokeConcentration());
        assertEquals(sensorData.getTemperature(), decodedData.getTemperature());
        assertEquals(sensorData.getBatteryLevel(), decodedData.getBatteryLevel());
    }
    
    @Test(expected = ProtocolCodecException.class)
    public void testDecode_InvalidFrameHeader() throws ProtocolCodecException {
        // 构造帧头错误的Cat.1协议数据
        byte[] invalidData = hexStringToBytes("7F015E1E9C800001001001006508123456789012345600007E");
        codec.decode(invalidData);
    }
    
    @Test(expected = ProtocolCodecException.class)
    public void testDecode_InvalidFrameTail() throws ProtocolCodecException {
        // 构造帧尾错误的Cat.1协议数据
        byte[] invalidData = hexStringToBytes("7E015E1E9C800001001001006508123456789012345600007F");
        codec.decode(invalidData);
    }
    
    @Test(expected = ProtocolCodecException.class)
    public void testDecode_MissingImei() throws ProtocolCodecException {
        // 构造缺少IMEI的Cat.1协议数据
        byte[] invalidData = hexStringToBytes("7E015E1E9C800001000201001402003200007E");
        codec.decode(invalidData);
    }
    
    @Test
    public void testGetProtocolType() {
        assertEquals("CAT1", codec.getProtocolType());
    }
    
    /**
     * 十六进制字符串转字节数组
     * 
     * @param hexString 十六进制字符串
     * @return 字节数组
     */
    private byte[] hexStringToBytes(String hexString) {
        int length = hexString.length();
        byte[] bytes = new byte[length / 2];
        
        for (int i = 0; i < length; i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + 
                                  Character.digit(hexString.charAt(i + 1), 16));
        }
        
        return bytes;
    }
}
