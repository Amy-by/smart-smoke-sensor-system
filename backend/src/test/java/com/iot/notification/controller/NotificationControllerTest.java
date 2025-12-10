package com.iot.notification.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot.notification.channel.NotificationResult;
import com.iot.notification.entity.NotificationQuota;
import com.iot.notification.service.NotificationService;
import com.iot.notification.template.NotificationTemplate;
import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
public class NotificationControllerTest {
    private MockMvc mockMvc;
    
    @Mock
    private NotificationService notificationService;
    
    @InjectMocks
    private NotificationController notificationController;
    
    private ObjectMapper objectMapper;
    
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(notificationController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testTriggerNotification() throws Exception {
        // 准备测试数据
        String sensorId = "123456";
        String alarmType = "fire";
        List<NotificationResult> results = new ArrayList<>();
        NotificationResult result = new NotificationResult(true, "Notification sent successfully");
        results.add(result);
        
        // 模拟service层方法
        when(notificationService.triggerNotification(sensorId, alarmType)).thenReturn(results);
        
        // 执行测试
        mockMvc.perform(post("/iot/notification/trigger")
            .param("sensorId", sensorId)
            .param("alarmType", alarmType))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value(200));
        
        // 验证
        verify(notificationService, times(1)).triggerNotification(sensorId, alarmType);
    }

    @Test
    void testSendNotification() throws Exception {
        // 准备测试数据
        NotificationController.NotificationRequest request = new NotificationController.NotificationRequest();
        request.setDeviceId("1234567890");
        request.setChannelType("sms");
        request.setRecipient("13800138000");
        request.setTemplateId("template123");
        Map<String, Object> params = new HashMap<>();
        params.put("deviceName", "测试设备");
        request.setParams(params);
        
        NotificationResult result = new NotificationResult(true, "Notification sent successfully");
        
        // 模拟service层方法
        when(notificationService.sendNotification(
                eq(request.getDeviceId()),
                eq(request.getChannelType()),
                eq(request.getRecipient()),
                eq(request.getTemplateId()),
                eq(request.getParams())))
            .thenReturn(result);
        
        // 执行测试
        mockMvc.perform(post("/iot/notification/send")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value(200));
        
        // 验证
        verify(notificationService, times(1)).sendNotification(
                eq(request.getDeviceId()),
                eq(request.getChannelType()),
                eq(request.getRecipient()),
                eq(request.getTemplateId()),
                eq(request.getParams()));
    }

    @Test
    void testSendNotificationByAlarm() throws Exception {
        // 准备测试数据
        Long alarmId = 12345L;
        
        // 执行测试
        mockMvc.perform(post("/iot/notification/alarm/{alarmId}", alarmId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value(500))
            .andExpect(jsonPath("$.msg").value("此接口需要与报警服务集成，暂未实现"));
    }

    @Test
    void testGetTemplate() throws Exception {
        // 准备测试数据
        String templateId = "template123";
        NotificationTemplate template = new NotificationTemplate();
        template.setTemplateId(templateId);
        template.setTemplateType("sms");
        
        // 模拟service层方法
        when(notificationService.getTemplate(templateId)).thenReturn(template);
        
        // 执行测试
        mockMvc.perform(get("/iot/notification/template/{templateId}", templateId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.data.templateId").value(templateId))
            .andExpect(jsonPath("$.data.templateType").value("sms"));
        
        // 验证
        verify(notificationService, times(1)).getTemplate(templateId);
    }

    @Test
    void testGetTemplateByType() throws Exception {
        // 准备测试数据
        String templateType = "voice";
        String alarmType = "gas";
        NotificationTemplate template = new NotificationTemplate();
        template.setTemplateType(templateType);
        
        // 模拟service层方法
        when(notificationService.getTemplateByType(templateType, alarmType)).thenReturn(template);
        
        // 执行测试
        mockMvc.perform(get("/iot/notification/template")
            .param("templateType", templateType)
            .param("alarmType", alarmType))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.data.templateType").value(templateType));
        
        // 验证
        verify(notificationService, times(1)).getTemplateByType(templateType, alarmType);
    }

    @Test
    void testQueryNotificationQuota() throws Exception {
        // 准备测试数据
        String sensorId = "123456";
        NotificationQuota quota = new NotificationQuota();
        quota.setSensorId(sensorId);
        quota.setBalance(new BigDecimal("100.0"));
        quota.setFreeSmsCount(50);
        quota.setFreeVoiceCount(20);
        
        // 模拟service层方法
        when(notificationService.queryNotificationQuota(sensorId)).thenReturn(quota);
        
        // 执行测试
        mockMvc.perform(get("/iot/notification/quota/{sensorId}", sensorId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.data.sensorId").value(sensorId))
            .andExpect(jsonPath("$.data.balance").value(100.0))
            .andExpect(jsonPath("$.data.freeSmsCount").value(50))
            .andExpect(jsonPath("$.data.freeVoiceCount").value(20));
        
        // 验证
        verify(notificationService, times(1)).queryNotificationQuota(sensorId);
    }

    @Test
    void testRechargeBalance() throws Exception {
        // 准备测试数据
        NotificationController.RechargeRequest request = new NotificationController.RechargeRequest();
        request.setSensorId("123456");
        request.setAmount(50.0);
        
        // 模拟service层方法
        when(notificationService.rechargeBalance(request.getSensorId(), request.getAmount())).thenReturn(true);
        
        // 执行测试
        mockMvc.perform(post("/iot/notification/quota/recharge/balance")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value(200));
        
        // 验证
        verify(notificationService, times(1)).rechargeBalance(request.getSensorId(), request.getAmount());
    }

    @Test
    void testRechargeFreeVoiceQuota() throws Exception {
        // 准备测试数据
        NotificationController.RechargeCountRequest request = new NotificationController.RechargeCountRequest();
        request.setSensorId("123456");
        request.setCount(30);
        
        // 模拟service层方法
        when(notificationService.rechargeFreeVoiceQuota(request.getSensorId(), request.getCount())).thenReturn(true);
        
        // 执行测试
        mockMvc.perform(post("/iot/notification/quota/recharge/voice")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value(200));
        
        // 验证
        verify(notificationService, times(1)).rechargeFreeVoiceQuota(request.getSensorId(), request.getCount());
    }

    @Test
    void testRechargeFreeSmsQuota() throws Exception {
        // 准备测试数据
        NotificationController.RechargeCountRequest request = new NotificationController.RechargeCountRequest();
        request.setSensorId("123456");
        request.setCount(100);
        
        // 模拟service层方法
        when(notificationService.rechargeFreeSmsQuota(request.getSensorId(), request.getCount())).thenReturn(true);
        
        // 执行测试
        mockMvc.perform(post("/iot/notification/quota/recharge/sms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value(200));
        
        // 验证
        verify(notificationService, times(1)).rechargeFreeSmsQuota(request.getSensorId(), request.getCount());
    }
}
