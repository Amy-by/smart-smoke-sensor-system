package com.iot.protocol.adapter;

import com.iot.protocol.model.SensorData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Cat.1协议适配器
 * 
 * 说明：实现Cat.1协议的解析，将Cat.1设备上报的JSON格式数据转换为标准化的SensorData
 * 
 * @author IoT架构组
 * @date 2025-01-27
 */
public class Cat1ProtocolAdapter implements ProtocolAdapter {
    
    private static final String PROTOCOL_TYPE = "CAT1";
    private static final Gson gson = new Gson();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @Override
    public SensorData adapt(String rawData) throws DataParseException {
        if (rawData == null || rawData.isEmpty()) {
            throw new DataParseException("原始数据不能为空");
        }
        
        try {
            // 解析JSON格式的原始数据
            JsonObject json = gson.fromJson(rawData, JsonObject.class);
            
            // 验证必要字段
            if (!json.has("deviceId") || !json.has("data") || !json.has("reportTime")) {
                throw new DataParseException("原始数据缺少必要字段");
            }
            
            JsonObject dataJson = json.getAsJsonObject("data");
            
            // 创建并填充SensorData对象
            SensorData sensorData = new SensorData();
            sensorData.setDeviceId(json.get("deviceId").getAsString());
            sensorData.setProtocolType(PROTOCOL_TYPE);
            sensorData.setSmokeConcentration(dataJson.has("smokeConcentration") ? dataJson.get("smokeConcentration").getAsInt() : null);
            sensorData.setTemperature(dataJson.has("temperature") ? dataJson.get("temperature").getAsInt() : null);
            sensorData.setBatteryLevel(dataJson.has("batteryLevel") ? dataJson.get("batteryLevel").getAsInt() : null);
            sensorData.setReportTime(LocalDateTime.parse(json.get("reportTime").getAsString(), formatter));
            sensorData.setRawData(rawData);
            
            return sensorData;
        } catch (Exception e) {
            throw new DataParseException("Cat.1协议数据解析失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public String getProtocolType() {
        return PROTOCOL_TYPE;
    }
}