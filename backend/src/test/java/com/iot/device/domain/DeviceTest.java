package com.iot.device.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 设备实体类单元测试
 * 
 * @author 开发人员
 * @date 2025-12-10
 */
class DeviceTest {

    private Device device;

    @BeforeEach
    void setUp() {
        device = new Device();
    }

    /**
     * 测试Device实体类的getter和setter方法
     */
    @Test
    void testDeviceGetterSetter() {
        // 设置设备属性
        device.setSensorId("123456789012345");
        device.setMaterialCode("JTY-GD-TCN2010");
        device.setInstallationLocation("测试位置");
        device.setAreaId(1L);
        device.setAreaName("测试区域");
        device.setIccid("89860112345678901234");
        device.setModuleVersion("V1.0");
        device.setModuleVendor("03");
        device.setTerminalType(152);
        device.setNormalWorkInterval(60);
        device.setEmergencyAlarmInterval(10);
        device.setSmokeThreshold(100);
        device.setTempUpperLimit(50);
        device.setTempLowerLimit(-10);
        device.setBatteryLife("3年");
        device.setProtectionLevel("IP30");

        // 验证设备属性
        assertEquals("123456789012345", device.getSensorId());
        assertEquals("JTY-GD-TCN2010", device.getMaterialCode());
        assertEquals("测试位置", device.getInstallationLocation());
        assertEquals(1L, device.getAreaId());
        assertEquals("测试区域", device.getAreaName());
        assertEquals("89860112345678901234", device.getIccid());
        assertEquals("V1.0", device.getModuleVersion());
        assertEquals("03", device.getModuleVendor());
        assertEquals(152, device.getTerminalType());
        assertEquals(60, device.getNormalWorkInterval());
        assertEquals(10, device.getEmergencyAlarmInterval());
        assertEquals(100, device.getSmokeThreshold());
        assertEquals(50, device.getTempUpperLimit());
        assertEquals(-10, device.getTempLowerLimit());
        assertEquals("3年", device.getBatteryLife());
        assertEquals("IP30", device.getProtectionLevel());
    }

    /**
     * 测试Device实体类的toString方法
     */
    @Test
    void testDeviceToString() {
        // 设置设备属性
        device.setSensorId("123456789012345");
        device.setMaterialCode("JTY-GD-TCN2010");
        device.setInstallationLocation("测试位置");

        // 验证toString方法包含关键属性
        String toStringResult = device.toString();
        assertTrue(toStringResult.contains("sensorId='123456789012345'"));
        assertTrue(toStringResult.contains("materialCode='JTY-GD-TCN2010'"));
        assertTrue(toStringResult.contains("installationLocation='测试位置'"));
    }
}
