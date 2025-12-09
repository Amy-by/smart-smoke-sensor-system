package com.iot.device.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 设备状态实体类单元测试
 * 
 * @author 开发人员
 * @date 2025-12-10
 */
class DeviceStatusTest {

    private DeviceStatus deviceStatus;

    @BeforeEach
    void setUp() {
        deviceStatus = new DeviceStatus();
    }

    /**
     * 测试DeviceStatus实体类的getter和setter方法
     */
    @Test
    void testDeviceStatusGetterSetter() {
        // 准备测试数据
        Date now = new Date();

        // 设置设备状态属性
        deviceStatus.setSensorId("123456789012345");
        deviceStatus.setOnlineStatus(1);
        deviceStatus.setBatteryLevel(80);
        deviceStatus.setSignalStrength(-70);
        deviceStatus.setSmokeConcentration(50);
        deviceStatus.setTemperature(25);
        deviceStatus.setHumidity(40);
        deviceStatus.setDeviceStatus(0);
        deviceStatus.setLastReportTime(now);
        deviceStatus.setLastOnlineTime(now);

        // 验证设备状态属性
        assertEquals("123456789012345", deviceStatus.getSensorId());
        assertEquals(1, deviceStatus.getOnlineStatus());
        assertEquals(80, deviceStatus.getBatteryLevel());
        assertEquals(-70, deviceStatus.getSignalStrength());
        assertEquals(50, deviceStatus.getSmokeConcentration());
        assertEquals(25, deviceStatus.getTemperature());
        assertEquals(40, deviceStatus.getHumidity());
        assertEquals(0, deviceStatus.getDeviceStatus());
        assertEquals(now, deviceStatus.getLastReportTime());
        assertEquals(now, deviceStatus.getLastOnlineTime());
    }

    /**
     * 测试DeviceStatus实体类的toString方法
     */
    @Test
    void testDeviceStatusToString() {
        // 设置设备状态属性
        deviceStatus.setSensorId("123456789012345");
        deviceStatus.setOnlineStatus(1);
        deviceStatus.setBatteryLevel(80);

        // 验证toString方法包含关键属性
        String toStringResult = deviceStatus.toString();
        assertTrue(toStringResult.contains("sensorId='123456789012345'"));
        assertTrue(toStringResult.contains("onlineStatus=1"));
        assertTrue(toStringResult.contains("batteryLevel=80"));
    }
}
