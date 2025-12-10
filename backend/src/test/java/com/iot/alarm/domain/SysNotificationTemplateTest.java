package com.iot.alarm.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * SysNotificationTemplate 实体类单元测试
 * 
 * @author 开发人员
 * @date 2025-12-10
 */
public class SysNotificationTemplateTest {

    private SysNotificationTemplate notificationTemplate;

    @BeforeEach
    public void setUp() {
        notificationTemplate = new SysNotificationTemplate();
        notificationTemplate.setId(1L);
        notificationTemplate.setProductId(2001L);
        notificationTemplate.setSignName("智能烟感");
        notificationTemplate.setTemplateCode("SMS_123456789");
        notificationTemplate.setTemplateId("TTS_987654321");
    }

    @Test
    public void testGetterAndSetter() {
        assertNotNull(notificationTemplate.getId());
        assertEquals(2001L, notificationTemplate.getProductId());
        assertEquals("智能烟感", notificationTemplate.getSignName());
        assertEquals("SMS_123456789", notificationTemplate.getTemplateCode());
        assertEquals("TTS_987654321", notificationTemplate.getTemplateId());
    }

    @Test
    public void testToString() {
        String result = notificationTemplate.toString();
        assertNotNull(result);
        assertEquals("SysNotificationTemplate{id=1, productId=2001, signName='智能烟感', templateCode='SMS_123456789', templateId='TTS_987654321'}", result);
    }
}
