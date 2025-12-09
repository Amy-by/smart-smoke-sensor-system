package com.iot.protocol.codec;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.iot.protocol.model.SensorData;

/**
 * Zigbee协议编解码器
 * 
 * 说明：实现Zigbee协议的编码和解码功能
 * Zigbee协议格式：设备ID|烟雾浓度|温度|电池电量|上报时间
 * 
 * @author IoT架构组
 * @date 2025-01-27
 */
public class ZigbeeProtocolCodec implements ProtocolCodec {
    
    /** 协议类型 */
    private static final String PROTOCOL_TYPE = "ZIGBEE";
    
    /** 分隔符 */
    private static final String DELIMITER = "|";
    
    /** 日期时间格式 */
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /** 期望的字段数量 */
    private static final int EXPECTED_FIELDS_COUNT = 5;
    
    @Override
    public SensorData decode(byte[] rawData) throws ProtocolCodecException {
        // 1. 将字节数组转换为字符串
        String rawStr = new String(rawData);
        
        // 2. 分割数据字段
        String[] fields = rawStr.split(DELIMITER);
        if (fields.length != EXPECTED_FIELDS_COUNT) {
            throw new ProtocolCodecException("Zigbee协议数据字段数量错误，期望" + EXPECTED_FIELDS_COUNT + "个字段，实际" + fields.length + "个字段");
        }
        
        // 3. 解析字段
        String deviceId = fields[0].trim();
        if (deviceId.isEmpty()) {
            throw new ProtocolCodecException("Zigbee协议数据缺少设备ID");
        }
        
        // 烟雾浓度
        Integer smokeConcentration = null;
        try {
            smokeConcentration = Integer.parseInt(fields[1].trim());
        } catch (NumberFormatException e) {
            throw new ProtocolCodecException("Zigbee协议数据烟雾浓度字段解析失败: " + e.getMessage());
        }
        
        // 温度
        Integer temperature = null;
        try {
            temperature = Integer.parseInt(fields[2].trim());
        } catch (NumberFormatException e) {
            throw new ProtocolCodecException("Zigbee协议数据温度字段解析失败: " + e.getMessage());
        }
        
        // 电池电量
        Integer batteryLevel = null;
        try {
            batteryLevel = Integer.parseInt(fields[3].trim());
        } catch (NumberFormatException e) {
            throw new ProtocolCodecException("Zigbee协议数据电池电量字段解析失败: " + e.getMessage());
        }
        
        // 上报时间
        LocalDateTime reportTime = null;
        try {
            reportTime = LocalDateTime.parse(fields[4].trim(), DATE_TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new ProtocolCodecException("Zigbee协议数据上报时间字段解析失败: " + e.getMessage());
        }
        
        // 4. 创建传感器数据对象
        SensorData sensorData = new SensorData();
        sensorData.setDeviceId(deviceId);
        sensorData.setProtocolType(PROTOCOL_TYPE);
        sensorData.setSmokeConcentration(smokeConcentration);
        sensorData.setTemperature(temperature);
        sensorData.setBatteryLevel(batteryLevel);
        sensorData.setReportTime(reportTime);
        sensorData.setRawData(rawStr);
        
        return sensorData;
    }
    
    @Override
    public byte[] encode(SensorData sensorData) throws ProtocolCodecException {
        // 1. 参数验证
        if (sensorData == null || sensorData.getDeviceId() == null) {
            throw new ProtocolCodecException("Zigbee协议编码失败：传感器数据或设备ID不能为空");
        }
        
        if (sensorData.getSmokeConcentration() == null || sensorData.getTemperature() == null 
                || sensorData.getBatteryLevel() == null || sensorData.getReportTime() == null) {
            throw new ProtocolCodecException("Zigbee协议编码失败：缺少必要的传感器数据字段");
        }
        
        // 2. 构建Zigbee协议字符串
        StringBuilder sb = new StringBuilder();
        sb.append(sensorData.getDeviceId())
          .append(DELIMITER)
          .append(sensorData.getSmokeConcentration())
          .append(DELIMITER)
          .append(sensorData.getTemperature())
          .append(DELIMITER)
          .append(sensorData.getBatteryLevel())
          .append(DELIMITER)
          .append(sensorData.getReportTime().format(DATE_TIME_FORMATTER));
        
        // 3. 转换为字节数组
        return sb.toString().getBytes();
    }
    
    @Override
    public String getProtocolType() {
        return PROTOCOL_TYPE;
    }
}
