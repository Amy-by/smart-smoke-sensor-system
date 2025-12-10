package com.iot.alarm.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * SensorContact 实体类单元测试
 * 
 * @author 开发人员
 * @date 2025-12-10
 */
public class SensorContactTest {

    private SensorContact sensorContact;

    @BeforeEach
    public void setUp() {
        sensorContact = new SensorContact();
        sensorContact.setId(1L);
        sensorContact.setSensorId("123456789012345");
        sensorContact.setUserId(1001L);
        sensorContact.setPhone("13800138000");
        sensorContact.setNotifyType(1);
        sensorContact.setPriority(1);
    }

    @Test
    public void testGetterAndSetter() {
        assertNotNull(sensorContact.getId());
        assertEquals("123456789012345", sensorContact.getSensorId());
        assertEquals(1001L, sensorContact.getUserId());
        assertEquals("13800138000", sensorContact.getPhone());
        assertEquals(1, sensorContact.getNotifyType());
        assertEquals(1, sensorContact.getPriority());
    }

    @Test
    public void testToString() {
        String result = sensorContact.toString();
        assertNotNull(result);
        assertEquals("SensorContact{id=1, sensorId='123456789012345', userId=1001, phone='13800138000', notifyType=1, priority=1}", result);
    }
}
