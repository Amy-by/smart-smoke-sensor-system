package com.iot.alarm.rule;

import com.iot.alarm.domain.AlarmRule;
import com.iot.alarm.processor.Alarm;
import com.iot.alarm.rule.impl.AlarmRuleEngineImpl;
import com.iot.protocol.model.SensorData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 报警规则引擎单元测试
 * 
 * @author 开发人员
 * @date 2025-12-10
 */
public class AlarmRuleEngineTest {
    
    private AlarmRuleEngine alarmRuleEngine;
    
    @BeforeEach
    public void setUp() {
        alarmRuleEngine = new AlarmRuleEngineImpl();
    }
    
    @Test
    public void testParseSmokeRule() throws RuleParseException {
        // 创建烟雾报警规则
        AlarmRule smokeRule = new AlarmRule();
        smokeRule.setId(1L);
        smokeRule.setSensorId("1234567890");
        smokeRule.setRuleName("烟雾报警规则");
        smokeRule.setRuleType(1);
        smokeRule.setThreshold(50);
        smokeRule.setPriority(1);
        smokeRule.setStatus(1);
        
        // 解析规则
        Rule rule = alarmRuleEngine.parseRule(smokeRule);
        assertNotNull(rule);
        assertEquals(Integer.valueOf(1), rule.getRuleType());
    }
    
    @Test
    public void testParseTemperatureRule() throws RuleParseException {
        // 创建温度报警规则
        AlarmRule tempRule = new AlarmRule();
        tempRule.setId(2L);
        tempRule.setSensorId("1234567890");
        tempRule.setRuleName("温度报警规则");
        tempRule.setRuleType(2);
        tempRule.setThreshold(60);
        tempRule.setPriority(2);
        tempRule.setStatus(1);
        
        // 解析规则
        Rule rule = alarmRuleEngine.parseRule(tempRule);
        assertNotNull(rule);
        assertEquals(Integer.valueOf(2), rule.getRuleType());
    }
    
    @Test
    public void testParseLowBatteryRule() throws RuleParseException {
        // 创建低电报警规则
        AlarmRule batteryRule = new AlarmRule();
        batteryRule.setId(3L);
        batteryRule.setSensorId("1234567890");
        batteryRule.setRuleName("低电报警规则");
        batteryRule.setRuleType(3);
        batteryRule.setThreshold(20);
        batteryRule.setPriority(3);
        batteryRule.setStatus(1);
        
        // 解析规则
        Rule rule = alarmRuleEngine.parseRule(batteryRule);
        assertNotNull(rule);
        assertEquals(Integer.valueOf(3), rule.getRuleType());
    }
    
    @Test
    public void testParseTamperRule() throws RuleParseException {
        // 创建防拆报警规则
        AlarmRule tamperRule = new AlarmRule();
        tamperRule.setId(4L);
        tamperRule.setSensorId("1234567890");
        tamperRule.setRuleName("防拆报警规则");
        tamperRule.setRuleType(4);
        tamperRule.setPriority(1);
        tamperRule.setStatus(1);
        
        // 解析规则
        Rule rule = alarmRuleEngine.parseRule(tamperRule);
        assertNotNull(rule);
        assertEquals(Integer.valueOf(4), rule.getRuleType());
    }
    
    @Test
    public void testParseInvalidRuleType() throws RuleParseException {
        // 创建无效类型的规则
        AlarmRule invalidRule = new AlarmRule();
        invalidRule.setId(5L);
        invalidRule.setSensorId("1234567890");
        invalidRule.setRuleName("无效规则");
        invalidRule.setRuleType(99); // 无效的规则类型
        invalidRule.setThreshold(50);
        invalidRule.setPriority(1);
        invalidRule.setStatus(1);
        
        // 解析规则，预期抛出异常
        assertThrows(RuleParseException.class, () -> {
            alarmRuleEngine.parseRule(invalidRule);
        });
    }
    
    @Test
    public void testExecuteSmokeAlarmRule() {
        // 创建烟雾报警规则
        AlarmRule smokeRule = new AlarmRule();
        smokeRule.setId(1L);
        smokeRule.setSensorId("1234567890");
        smokeRule.setRuleName("烟雾报警规则");
        smokeRule.setRuleType(1);
        smokeRule.setThreshold(50);
        smokeRule.setPriority(1);
        smokeRule.setStatus(1);
        
        // 创建传感器数据（烟雾浓度超过阈值）
        SensorData sensorData = new SensorData();
        sensorData.setDeviceId("1234567890");
        sensorData.setSmokeConcentration(80);
        sensorData.setTemperature(25);
        sensorData.setBatteryLevel(80);
        sensorData.setReportTime(LocalDateTime.now());
        
        List<AlarmRule> rules = new ArrayList<>();
        rules.add(smokeRule);
        
        // 执行规则
        List<Alarm> alarms = alarmRuleEngine.executeRules(sensorData, rules);
        assertNotNull(alarms);
        assertEquals(1, alarms.size());
        assertEquals("火灾", alarms.get(0).getAlarmType());
    }
    
    @Test
    public void testExecuteTemperatureAlarmRule() {
        // 创建温度报警规则
        AlarmRule tempRule = new AlarmRule();
        tempRule.setId(2L);
        tempRule.setSensorId("1234567890");
        tempRule.setRuleName("温度报警规则");
        tempRule.setRuleType(2);
        tempRule.setThreshold(60);
        tempRule.setPriority(2);
        tempRule.setStatus(1);
        
        // 创建传感器数据（温度超过阈值）
        SensorData sensorData = new SensorData();
        sensorData.setDeviceId("1234567890");
        sensorData.setSmokeConcentration(20);
        sensorData.setTemperature(70);
        sensorData.setBatteryLevel(80);
        sensorData.setReportTime(LocalDateTime.now());
        
        List<AlarmRule> rules = new ArrayList<>();
        rules.add(tempRule);
        
        // 执行规则
        List<Alarm> alarms = alarmRuleEngine.executeRules(sensorData, rules);
        assertNotNull(alarms);
        assertEquals(1, alarms.size());
        assertEquals("温度", alarms.get(0).getAlarmType());
    }
    
    @Test
    public void testExecuteLowBatteryAlarmRule() {
        // 创建低电报警规则
        AlarmRule batteryRule = new AlarmRule();
        batteryRule.setId(3L);
        batteryRule.setSensorId("1234567890");
        batteryRule.setRuleName("低电报警规则");
        batteryRule.setRuleType(3);
        batteryRule.setThreshold(20);
        batteryRule.setPriority(3);
        batteryRule.setStatus(1);
        
        // 创建传感器数据（电池电量低于阈值）
        SensorData sensorData = new SensorData();
        sensorData.setDeviceId("1234567890");
        sensorData.setSmokeConcentration(20);
        sensorData.setTemperature(25);
        sensorData.setBatteryLevel(10);
        sensorData.setReportTime(LocalDateTime.now());
        
        List<AlarmRule> rules = new ArrayList<>();
        rules.add(batteryRule);
        
        // 执行规则
        List<Alarm> alarms = alarmRuleEngine.executeRules(sensorData, rules);
        assertNotNull(alarms);
        assertEquals(1, alarms.size());
        assertEquals("低电", alarms.get(0).getAlarmType());
    }
    
    @Test
    public void testExecuteMultipleRules() {
        // 创建多个报警规则
        AlarmRule smokeRule = new AlarmRule();
        smokeRule.setId(1L);
        smokeRule.setSensorId("1234567890");
        smokeRule.setRuleName("烟雾报警规则");
        smokeRule.setRuleType(1);
        smokeRule.setThreshold(50);
        smokeRule.setPriority(1);
        smokeRule.setStatus(1);
        
        AlarmRule tempRule = new AlarmRule();
        tempRule.setId(2L);
        tempRule.setSensorId("1234567890");
        tempRule.setRuleName("温度报警规则");
        tempRule.setRuleType(2);
        tempRule.setThreshold(60);
        tempRule.setPriority(2);
        tempRule.setStatus(1);
        
        // 创建传感器数据（烟雾和温度都超过阈值）
        SensorData sensorData = new SensorData();
        sensorData.setDeviceId("1234567890");
        sensorData.setSmokeConcentration(80);
        sensorData.setTemperature(70);
        sensorData.setBatteryLevel(80);
        sensorData.setReportTime(LocalDateTime.now());
        
        List<AlarmRule> rules = new ArrayList<>();
        rules.add(smokeRule);
        rules.add(tempRule);
        
        // 执行规则
        List<Alarm> alarms = alarmRuleEngine.executeRules(sensorData, rules);
        assertNotNull(alarms);
        assertEquals(2, alarms.size());
        
        // 验证两个报警都被触发
        boolean smokeAlarmFound = false;
        boolean tempAlarmFound = false;
        for (Alarm alarm : alarms) {
            if ("火灾".equals(alarm.getAlarmType())) {
                smokeAlarmFound = true;
            } else if ("温度".equals(alarm.getAlarmType())) {
                tempAlarmFound = true;
            }
        }
        assertTrue(smokeAlarmFound);
        assertTrue(tempAlarmFound);
    }
    
    @Test
    public void testExecuteDisabledRule() {
        // 创建禁用的烟雾报警规则
        AlarmRule smokeRule = new AlarmRule();
        smokeRule.setId(1L);
        smokeRule.setSensorId("1234567890");
        smokeRule.setRuleName("烟雾报警规则");
        smokeRule.setRuleType(1);
        smokeRule.setThreshold(50);
        smokeRule.setPriority(1);
        smokeRule.setStatus(0); // 禁用状态
        
        // 创建传感器数据（烟雾浓度超过阈值）
        SensorData sensorData = new SensorData();
        sensorData.setDeviceId("1234567890");
        sensorData.setSmokeConcentration(80);
        sensorData.setTemperature(25);
        sensorData.setBatteryLevel(80);
        sensorData.setReportTime(LocalDateTime.now());
        
        List<AlarmRule> rules = new ArrayList<>();
        rules.add(smokeRule);
        
        // 执行规则，禁用的规则不应触发报警
        List<Alarm> alarms = alarmRuleEngine.executeRules(sensorData, rules);
        assertNotNull(alarms);
        assertEquals(0, alarms.size());
    }
    
    @Test
    public void testExecuteRulesBySensorId() {
        // 创建传感器数据
        SensorData sensorData = new SensorData();
        sensorData.setDeviceId("1234567890");
        sensorData.setSmokeConcentration(80);
        sensorData.setTemperature(25);
        sensorData.setBatteryLevel(80);
        sensorData.setReportTime(LocalDateTime.now());
        
        // 执行规则（由于没有Mapper/Service，预期返回空列表）
        List<Alarm> alarms = alarmRuleEngine.executeRulesBySensorId(sensorData);
        assertNotNull(alarms);
        assertTrue(alarms.isEmpty());
    }
}
