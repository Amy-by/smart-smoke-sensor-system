package com.iot.alarm.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * NotificationQuota 实体类单元测试
 * 
 * @author 开发人员
 * @date 2025-12-10
 */
public class NotificationQuotaTest {

    private NotificationQuota notificationQuota;

    @BeforeEach
    public void setUp() {
        notificationQuota = new NotificationQuota();
        notificationQuota.setId(1L);
        notificationQuota.setSensorId("123456789012345");
        notificationQuota.setFreeVoiceCount(100);
        notificationQuota.setFreeSmsCount(50);
        notificationQuota.setBalance(new BigDecimal(100.50));
    }

    @Test
    public void testGetterAndSetter() {
        assertNotNull(notificationQuota.getId());
        assertEquals("123456789012345", notificationQuota.getSensorId());
        assertEquals(100, notificationQuota.getFreeVoiceCount());
        assertEquals(50, notificationQuota.getFreeSmsCount());
        assertEquals(new BigDecimal(100.50), notificationQuota.getBalance());
    }

    @Test
    public void testToString() {
        String result = notificationQuota.toString();
        assertNotNull(result);
        assertEquals("NotificationQuota{id=1, sensorId='123456789012345', freeVoiceCount=100, freeSmsCount=50, balance=100.5}", result);
    }
}
