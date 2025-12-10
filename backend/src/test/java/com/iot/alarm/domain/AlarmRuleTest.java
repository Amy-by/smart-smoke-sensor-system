package com.iot.alarm.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * AlarmRule 实体类单元测试
 * 
 * @author 开发人员
 * @date 2025-12-10
 */
public class AlarmRuleTest {

    private AlarmRule alarmRule;

    @BeforeEach
    public void setUp() {
        alarmRule = new AlarmRule();
        alarmRule.setId(1L);
        alarmRule.setSensorId("123456789012345");
        alarmRule.setRuleName("烟雾报警规则");
        alarmRule.setRuleType(1);
        alarmRule.setThreshold(80);
        alarmRule.setPriority(1);
        alarmRule.setStatus(1);
        alarmRule.setDescription("烟雾浓度超过80%时触发报警");
    }

    @Test
    public void testGetterAndSetter() {
        assertNotNull(alarmRule.getId());
        assertEquals("123456789012345", alarmRule.getSensorId());
        assertEquals("烟雾报警规则", alarmRule.getRuleName());
        assertEquals(1, alarmRule.getRuleType());
        assertEquals(80, alarmRule.getThreshold());
        assertEquals(1, alarmRule.getPriority());
        assertEquals(1, alarmRule.getStatus());
        assertEquals("烟雾浓度超过80%时触发报警", alarmRule.getDescription());
    }

    @Test
    public void testToString() {
        String result = alarmRule.toString();
        assertNotNull(result);
        assertEquals("AlarmRule{id=1, sensorId='123456789012345', ruleName='烟雾报警规则', ruleType=1, threshold=80, priority=1, status=1, description='烟雾浓度超过80%时触发报警'}", result);
    }
}
