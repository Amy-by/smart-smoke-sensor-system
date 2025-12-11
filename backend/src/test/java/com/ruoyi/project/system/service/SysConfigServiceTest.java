package com.ruoyi.project.system.service;

import com.ruoyi.project.system.domain.SysConfig;
import com.ruoyi.project.system.service.impl.SysConfigServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * 配置服务测试类
 * 
 * @author ruoyi
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SysConfigServiceTest {

    @Autowired
    private SysConfigServiceImpl configService;

    @Test
    public void testSelectConfigById() {
        // 测试根据ID查询配置信息
        Long configId = 1L;
        SysConfig config = configService.selectConfigById(configId);
        assertNotNull(config);
        assertEquals(configId, config.getConfigId());
    }

    @Test
    public void testSelectConfigByKey() {
        // 测试根据键名查询配置信息
        String configKey = "sys.account.captchaEnabled";
        String configValue = configService.selectConfigByKey(configKey);
        assertNotNull(configValue);
        // 验证码开关应该是布尔值字符串
        assertTrue(configValue.equals("true") || configValue.equals("false"));
    }

    @Test
    public void testSelectCaptchaEnabled() {
        // 测试获取验证码开关
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        // 验证码开关应该是布尔值
        assertNotNull(captchaEnabled);
    }

    @Test
    public void testSelectConfigList() {
        // 测试查询配置列表
        SysConfig config = new SysConfig();
        config.setConfigKey("sys.account.captchaEnabled");
        config.setConfigName("验证码开关");
        int count = configService.selectConfigList(config).size();
        // 应该能查询到至少一条记录
        assertTrue(count > 0);
    }

    @Test
    public void testCheckConfigKeyUnique() {
        // 测试配置键名唯一性校验
        SysConfig config = new SysConfig();
        config.setConfigKey("sys.account.captchaEnabled");
        config.setConfigId(1L);
        boolean result = configService.checkConfigKeyUnique(config);
        // 系统默认配置键已存在，所以应该返回true（唯一）
        assertTrue(result);
    }
}