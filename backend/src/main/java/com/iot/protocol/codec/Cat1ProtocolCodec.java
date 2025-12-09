package com.iot.protocol.codec;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import com.iot.protocol.model.SensorData;

/**
 * Cat.1协议编解码器
 * 
 * 说明：实现Cat.1协议的编码和解码功能
 * Cat.1协议帧结构：包头0x7E→协议版本→UTC时间（4字节）→帧号（2字节高位在前）→长度→指令→加密→消息体→CRC→包尾0x7E
 * 
 * @author IoT架构组
 * @date 2025-01-27
 */
public class Cat1ProtocolCodec implements ProtocolCodec {
    
    /** 协议类型 */
    private static final String PROTOCOL_TYPE = "CAT1";
    
    /** 帧头标识 */
    private static final byte FRAME_HEADER = (byte) 0x7E;
    
    /** 帧尾标识 */
    private static final byte FRAME_TAIL = (byte) 0x7E;
    
    /** 注册指令 */
    private static final byte CMD_REGISTER = 0x02;
    
    /** 状态/参数指令 */
    private static final byte CMD_STATUS_PARAM = 0x01;
    
    /** IMEI标签 */
    private static final byte TAG_IMEI = (byte) 0x65;
    
    /** 烟雾浓度标签 */
    private static final byte TAG_SMOKE_CONCENTRATION = (byte) 0x14;
    
    /** 温度标签 */
    private static final byte TAG_TEMPERATURE = (byte) 0x0B;
    
    /** 电池电量标签 */
    private static final byte TAG_BATTERY_LEVEL = (byte) 0x24;
    
    @Override
    public SensorData decode(byte[] rawData) throws ProtocolCodecException {
        // 1. 校验帧头和帧尾
        if (rawData == null || rawData.length < 16) {
            throw new ProtocolCodecException("Cat.1协议数据长度不足");
        }
        
        if (rawData[0] != FRAME_HEADER || rawData[rawData.length - 1] != FRAME_TAIL) {
            throw new ProtocolCodecException("Cat.1协议帧结构错误：缺少0x7E标识");
        }
        
        // 2. 解析帧结构
        int offset = 1;
        
        // 协议版本（1字节）
        byte version = rawData[offset++];
        
        // UTC时间（4字节）
        long utcTime = bytesToUInt32(rawData, offset);
        offset += 4;
        LocalDateTime reportTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(utcTime), ZoneId.systemDefault());
        
        // 帧号（2字节，高位在前）
        int frameNo = bytesToUInt16(rawData, offset);
        offset += 2;
        
        // 消息体长度（2字节）
        int bodyLength = bytesToUInt16(rawData, offset);
        offset += 2;
        
        // 指令（1字节）
        byte cmd = rawData[offset++];
        
        // 加密标识（1字节）
        byte encrypt = rawData[offset++];
        
        // 3. 解析消息体（TLV格式）
        Map<Byte, byte[]> tlvData = parseTlv(rawData, offset, bodyLength);
        
        // 4. 提取IMEI（必填字段）
        if (!tlvData.containsKey(TAG_IMEI)) {
            throw new ProtocolCodecException("Cat.1协议数据缺少IMEI字段");
        }
        String imei = parseBcdToString(tlvData.get(TAG_IMEI));
        
        // 5. 创建传感器数据对象
        SensorData sensorData = new SensorData();
        sensorData.setDeviceId(imei);
        sensorData.setProtocolType(PROTOCOL_TYPE);
        sensorData.setReportTime(reportTime);
        sensorData.setRawData(bytesToHexString(rawData));
        
        // 6. 根据指令解析其他字段
        if (cmd == CMD_STATUS_PARAM) {
            // 解析状态和参数
            if (tlvData.containsKey(TAG_SMOKE_CONCENTRATION)) {
                int smokeConcentration = bytesToUInt16(tlvData.get(TAG_SMOKE_CONCENTRATION));
                sensorData.setSmokeConcentration(smokeConcentration);
            }
            
            if (tlvData.containsKey(TAG_TEMPERATURE)) {
                int temperature = bytesToInt16(tlvData.get(TAG_TEMPERATURE));
                sensorData.setTemperature(temperature);
            }
            
            if (tlvData.containsKey(TAG_BATTERY_LEVEL)) {
                int batteryLevel = bytesToUInt8(tlvData.get(TAG_BATTERY_LEVEL)[0]);
                sensorData.setBatteryLevel(batteryLevel);
            }
        }
        
        return sensorData;
    }
    
    @Override
    public byte[] encode(SensorData sensorData) throws ProtocolCodecException {
        // 1. 参数验证
        if (sensorData == null || sensorData.getDeviceId() == null) {
            throw new ProtocolCodecException("传感器数据或设备ID不能为空");
        }
        
        // 2. 构建TLV消息体
        Map<Byte, byte[]> tlvData = new HashMap<>();
        
        // 添加IMEI
        tlvData.put(TAG_IMEI, stringToBcd(sensorData.getDeviceId()));
        
        // 添加状态数据（如果有）
        if (sensorData.getSmokeConcentration() != null) {
            tlvData.put(TAG_SMOKE_CONCENTRATION, uint16ToBytes(sensorData.getSmokeConcentration()));
        }
        
        if (sensorData.getTemperature() != null) {
            tlvData.put(TAG_TEMPERATURE, int16ToBytes(sensorData.getTemperature()));
        }
        
        if (sensorData.getBatteryLevel() != null) {
            tlvData.put(TAG_BATTERY_LEVEL, new byte[]{(byte) sensorData.getBatteryLevel().intValue()});
        }
        
        // 3. 计算消息体长度
        int bodyLength = 0;
        for (byte[] value : tlvData.values()) {
            bodyLength += 2 + value.length; // Tag(1字节) + Length(1字节) + Value
        }
        
        // 4. 构建完整帧
        int frameLength = 1 + 1 + 4 + 2 + 2 + 1 + 1 + bodyLength + 2 + 1; // 包头+版本+UTC+帧号+长度+指令+加密+消息体+CRC+包尾
        byte[] frame = new byte[frameLength];
        int offset = 0;
        
        // 包头
        frame[offset++] = FRAME_HEADER;
        
        // 协议版本
        frame[offset++] = 0x01; // 假设版本为1
        
        // UTC时间
        long utcTime = sensorData.getReportTime() != null 
                ? sensorData.getReportTime().atZone(ZoneId.systemDefault()).toEpochSecond()
                : System.currentTimeMillis() / 1000;
        System.arraycopy(uint32ToBytes(utcTime), 0, frame, offset, 4);
        offset += 4;
        
        // 帧号（简单实现，固定为1）
        System.arraycopy(uint16ToBytes(1), 0, frame, offset, 2);
        offset += 2;
        
        // 消息体长度
        System.arraycopy(uint16ToBytes(bodyLength), 0, frame, offset, 2);
        offset += 2;
        
        // 指令（固定为状态/参数指令）
        frame[offset++] = CMD_STATUS_PARAM;
        
        // 加密（固定为明文）
        frame[offset++] = 0x00;
        
        // 消息体
        for (Map.Entry<Byte, byte[]> entry : tlvData.entrySet()) {
            byte tag = entry.getKey();
            byte[] value = entry.getValue();
            
            frame[offset++] = tag;
            frame[offset++] = (byte) value.length;
            System.arraycopy(value, 0, frame, offset, value.length);
            offset += value.length;
        }
        
        // CRC（简单实现，固定为0x0000）
        frame[offset++] = 0x00;
        frame[offset++] = 0x00;
        
        // 包尾
        frame[offset] = FRAME_TAIL;
        
        return frame;
    }
    
    @Override
    public String getProtocolType() {
        return PROTOCOL_TYPE;
    }
    
    /**
     * 解析TLV格式的数据
     * 
     * @param data 原始数据
     * @param offset 偏移量
     * @param length 长度
     * @return TLV数据映射
     */
    private Map<Byte, byte[]> parseTlv(byte[] data, int offset, int length) {
        Map<Byte, byte[]> tlvMap = new HashMap<>();
        int endOffset = offset + length;
        
        while (offset < endOffset) {
            byte tag = data[offset++];
            byte valueLength = data[offset++];
            
            byte[] value = new byte[valueLength];
            System.arraycopy(data, offset, value, 0, valueLength);
            offset += valueLength;
            
            tlvMap.put(tag, value);
        }
        
        return tlvMap;
    }
    
    /**
     * BCD码转字符串
     * 
     * @param bcd BCD码
     * @return 字符串
     */
    private String parseBcdToString(byte[] bcd) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bcd) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
    
    /**
     * 字符串转BCD码
     * 
     * @param str 字符串
     * @return BCD码
     */
    private byte[] stringToBcd(String str) {
        int length = str.length();
        int bcdLength = length / 2;
        byte[] bcd = new byte[bcdLength];
        
        for (int i = 0; i < bcdLength; i++) {
            int high = Integer.parseInt(str.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(str.substring(i * 2 + 1, i * 2 + 2), 16);
            bcd[i] = (byte) ((high << 4) | low);
        }
        
        return bcd;
    }
    
    /**
     * 4字节转无符号32位整数
     * 
     * @param bytes 字节数组
     * @param offset 偏移量
     * @return 无符号32位整数
     */
    private long bytesToUInt32(byte[] bytes, int offset) {
        return ((bytes[offset] & 0xFF) << 24) | 
               ((bytes[offset + 1] & 0xFF) << 16) | 
               ((bytes[offset + 2] & 0xFF) << 8) | 
               (bytes[offset + 3] & 0xFF);
    }
    
    /**
     * 无符号32位整数转4字节
     * 
     * @param value 无符号32位整数
     * @return 字节数组
     */
    private byte[] uint32ToBytes(long value) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) ((value >> 24) & 0xFF);
        bytes[1] = (byte) ((value >> 16) & 0xFF);
        bytes[2] = (byte) ((value >> 8) & 0xFF);
        bytes[3] = (byte) (value & 0xFF);
        return bytes;
    }
    
    /**
     * 2字节转无符号16位整数
     * 
     * @param bytes 字节数组
     * @param offset 偏移量
     * @return 无符号16位整数
     */
    private int bytesToUInt16(byte[] bytes, int offset) {
        return ((bytes[offset] & 0xFF) << 8) | (bytes[offset + 1] & 0xFF);
    }
    
    /**
     * 2字节转无符号16位整数（默认偏移量0）
     * 
     * @param bytes 字节数组
     * @return 无符号16位整数
     */
    private int bytesToUInt16(byte[] bytes) {
        return bytesToUInt16(bytes, 0);
    }
    
    /**
     * 无符号16位整数转2字节
     * 
     * @param value 无符号16位整数
     * @return 字节数组
     */
    private byte[] uint16ToBytes(int value) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) ((value >> 8) & 0xFF);
        bytes[1] = (byte) (value & 0xFF);
        return bytes;
    }
    
    /**
     * 2字节转有符号16位整数
     * 
     * @param bytes 字节数组
     * @return 有符号16位整数
     */
    private int bytesToInt16(byte[] bytes) {
        return ((bytes[0] & 0xFF) << 8) | (bytes[1] & 0xFF);
    }
    
    /**
     * 有符号16位整数转2字节
     * 
     * @param value 有符号16位整数
     * @return 字节数组
     */
    private byte[] int16ToBytes(int value) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) ((value >> 8) & 0xFF);
        bytes[1] = (byte) (value & 0xFF);
        return bytes;
    }
    
    /**
     * 字节转无符号8位整数
     * 
     * @param b 字节
     * @return 无符号8位整数
     */
    private int bytesToUInt8(byte b) {
        return b & 0xFF;
    }
    
    /**
     * 字节数组转十六进制字符串
     * 
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
