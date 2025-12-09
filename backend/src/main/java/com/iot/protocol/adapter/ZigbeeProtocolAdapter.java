package com.iot.protocol.adapter;

import com.iot.protocol.model.SensorData;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Zigbee协议适配器
 * 
 * 说明：实现Zigbee协议的解析，将Zigbee设备上报的分隔符格式数据转换为标准化的SensorData
 * 
 * @author IoT架构组
 * @date 2025-01-27
 */
public class ZigbeeProtocolAdapter implements ProtocolAdapter {
    
    private static final String PROTOCOL_TYPE = "ZIGBEE";
    private static final String DELIMITER = "\\|";
    private static final int EXPECTED_FIELDS = 5;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @Override
    public SensorData adapt(String rawData) throws DataParseException {
        if (rawData == null || rawData.isEmpty()) {
            throw new DataParseException("原始数据不能为空");
        }
        
        try {
            // 解析分隔符格式的原始数据
            String[] fields = rawData.split(DELIMITER);
            
            // 验证字段数量
            if (fields.length != EXPECTED_FIELDS) {
                throw new DataParseException("Zigbee协议数据解析失败: 原始数据字段数量错误，期望" + EXPECTED_FIELDS + "个字段，实际" + fields.length + "个字段");
            }
            
            // 创建并填充SensorData对象
            SensorData sensorData = new SensorData();
            sensorData.setDeviceId(fields[0].trim());
            sensorData.setProtocolType(PROTOCOL_TYPE);
            sensorData.setSmokeConcentration(parseIntField(fields[1], "烟雾浓度"));
            sensorData.setTemperature(parseIntField(fields[2], "温度"));
            sensorData.setBatteryLevel(parseIntField(fields[3], "电池电量"));
            sensorData.setReportTime(LocalDateTime.parse(fields[4].trim(), formatter));
            sensorData.setRawData(rawData);
            
            return sensorData;
        } catch (DataParseException e) {
            throw e;
        } catch (Exception e) {
            throw new DataParseException("Zigbee协议数据解析失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 解析整数字段
     * 
     * @param fieldValue 字段值
     * @param fieldName 字段名称
     * @return 整数类型的字段值
     * @throws DataParseException 数据解析异常
     */
    private Integer parseIntField(String fieldValue, String fieldName) throws DataParseException {
        if (fieldValue == null || fieldValue.trim().isEmpty()) {
            return null;
        }
        
        try {
            return Integer.parseInt(fieldValue.trim());
        } catch (NumberFormatException e) {
            throw new DataParseException("Zigbee协议数据解析失败: " + fieldName + "字段解析失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public String getProtocolType() {
        return PROTOCOL_TYPE;
    }
}