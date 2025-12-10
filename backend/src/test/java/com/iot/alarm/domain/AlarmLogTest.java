package com.iot.alarm.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * AlarmLog 实体类单元测试
 * 
 * @author 开发人员
 * @date 2025-12-10
 */
public class AlarmLogTest {

    private AlarmLog alarmLog;

    @BeforeEach
    public void setUp() {
        alarmLog = new AlarmLog();
        alarmLog.setId(1L);
        alarmLog.setSensorId("123456789012345");
        alarmLog.setAlarmType(1);
        alarmLog.setTriggerTag(1);
        alarmLog.setTriggerValue("烟雾浓度:120%");
        alarmLog.setAlarmTime(new Date());
        alarmLog.setHandleStatus(0);
        alarmLog.setHandleTime(new Date());
        alarmLog.setHandleUser("admin");
    }

    @Test
    public void testGetterAndSetter() {
        assertNotNull(alarmLog.getId());
        assertEquals("123456789012345", alarmLog.getSensorId());
        assertEquals(1, alarmLog.getAlarmType());
        assertEquals(1, alarmLog.getTriggerTag());
        assertEquals("烟雾浓度:120%", alarmLog.getTriggerValue());
        assertNotNull(alarmLog.getAlarmTime());
        assertEquals(0, alarmLog.getHandleStatus());
        assertNotNull(alarmLog.getHandleTime());
        assertEquals("admin", alarmLog.getHandleUser());
    }

    @Test
    public void testToString() {
        String result = alarmLog.toString();
        assertNotNull(result);
        assertEquals("AlarmLog{id=1, sensorId='123456789012345', alarmType=1, triggerTag=1, triggerValue='烟雾浓度:120%', alarmTime=" + alarmLog.getAlarmTime() + ", handleStatus=0, handleTime=" + alarmLog.getHandleTime() + ", handleUser='admin'}", result);
    }
}
