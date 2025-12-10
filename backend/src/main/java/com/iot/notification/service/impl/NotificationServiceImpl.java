package com.iot.notification.service.impl;

import com.iot.notification.channel.NotificationChannel;
import com.iot.notification.channel.NotificationResult;
import com.iot.notification.channel.NotificationSendException;
import com.iot.notification.channel.SmsNotificationChannel;
import com.iot.notification.channel.VoiceNotificationChannel;
import com.iot.notification.entity.NotificationQuota;
import com.iot.notification.service.NotificationService;
import com.iot.notification.service.mapper.NotificationQuotaMapper;
import com.iot.notification.template.NotificationTemplate;
import com.iot.alarm.domain.AlarmLog;
import com.iot.device.service.DeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通知服务实现类
 * 
 * 说明：实现通知的管理和发送功能
 * 设计原则：依赖倒置原则 - 依赖于通知渠道接口而不是具体实现
 * 
 * @author IoT架构组
 * @date 2025-01-27
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Autowired
    private DeviceService deviceService;
    
    @Autowired
    private NotificationQuotaMapper notificationQuotaMapper;
    
    private NotificationChannel smsNotificationChannel;
    
    private NotificationChannel voiceNotificationChannel;
    
    /**
     * 构造方法，初始化模板缓存和通知渠道
     */
    public NotificationServiceImpl() {
        // 初始化通知渠道
        this.smsNotificationChannel = new SmsNotificationChannel();
        this.voiceNotificationChannel = new VoiceNotificationChannel();
        
        // 初始化默认模板
        initTemplates();
    }
    
    // 模拟通知模板缓存
    private Map<String, NotificationTemplate> templateCache = new HashMap<>();
    

    
    /**
     * 初始化通知模板
     */
    private void initTemplates() {
        // 短信模板
        NotificationTemplate smsFireTemplate = new NotificationTemplate();
        smsFireTemplate.setTemplateId("SMS_FIRE_ALARM");
        smsFireTemplate.setTemplateType("SMS");
        smsFireTemplate.setTemplateName("火灾报警短信模板");
        smsFireTemplate.setTemplateContent("【安全预警】设备{0}发生火灾报警，时间：{1}，请及时处理！");
        smsFireTemplate.setEnabled(true);
        
        NotificationTemplate smsTemperatureTemplate = new NotificationTemplate();
        smsTemperatureTemplate.setTemplateId("SMS_TEMPERATURE_ALARM");
        smsTemperatureTemplate.setTemplateType("SMS");
        smsTemperatureTemplate.setTemplateName("温度异常报警短信模板");
        smsTemperatureTemplate.setTemplateContent("【温度预警】设备{0}温度异常，当前值：{1}°C，时间：{2}，请及时处理！");
        smsTemperatureTemplate.setEnabled(true);
        
        NotificationTemplate smsLowBatteryTemplate = new NotificationTemplate();
        smsLowBatteryTemplate.setTemplateId("SMS_LOW_BATTERY_ALARM");
        smsLowBatteryTemplate.setTemplateType("SMS");
        smsLowBatteryTemplate.setTemplateName("低电量报警短信模板");
        smsLowBatteryTemplate.setTemplateContent("【电量预警】设备{0}电量过低({1}%)，请及时更换电池！");
        smsLowBatteryTemplate.setEnabled(true);
        
        // 语音模板
        NotificationTemplate voiceFireTemplate = new NotificationTemplate();
        voiceFireTemplate.setTemplateId("VOICE_FIRE_ALARM");
        voiceFireTemplate.setTemplateType("VOICE");
        voiceFireTemplate.setTemplateName("火灾报警语音模板");
        voiceFireTemplate.setTemplateContent("紧急通知，您的设备{0}发生火灾报警，请立即采取措施！");
        voiceFireTemplate.setEnabled(true);
        
        // 将模板添加到缓存
        templateCache.put(smsFireTemplate.getTemplateId(), smsFireTemplate);
        templateCache.put(smsTemperatureTemplate.getTemplateId(), smsTemperatureTemplate);
        templateCache.put(smsLowBatteryTemplate.getTemplateId(), smsLowBatteryTemplate);
        templateCache.put(voiceFireTemplate.getTemplateId(), voiceFireTemplate);
        
        log.info("初始化通知模板完成，共 {} 个模板", templateCache.size());
    }
    
    /**
     * 根据渠道类型获取对应的通知渠道
     * 
     * @param channelType 渠道类型
     * @return 通知渠道
     */
    private NotificationChannel getChannelByType(String channelType) {
        if ("SMS".equals(channelType)) {
            return smsNotificationChannel;
        } else if ("VOICE".equals(channelType)) {
            return voiceNotificationChannel;
        } else {
            throw new IllegalArgumentException("不支持的通知渠道类型：" + channelType);
        }
    }
    
    /**
     * 触发通知（根据报警信息）
     * 
     * @param sensorId 传感器ID
     * @param alarmType 报警类型
     * @return 发送结果列表
     */
    @Override
    public List<NotificationResult> triggerNotification(String sensorId, String alarmType) {
        List<NotificationResult> results = new ArrayList<>();
        
        try {
            log.info("触发通知：传感器ID={}，报警类型={}", sensorId, alarmType);
            
            // 获取设备信息（模拟）
            String deviceName = "设备" + sensorId.substring(sensorId.length() - 4);
            
            // 发送短信通知
            NotificationTemplate smsTemplate = getTemplateByType("SMS", alarmType);
            if (smsTemplate != null && smsTemplate.isEnabled()) {
                // 构造模板参数
                Object[] smsParams = {deviceName, "2025-01-27 10:30:00"};
                
                // 发送短信
                NotificationResult smsResult = sendNotification(sensorId, "SMS", "13800138000", smsTemplate.getTemplateId(), smsParams);
                results.add(smsResult);
            }
            
            // 发送语音通知
            NotificationTemplate voiceTemplate = getTemplateByType("VOICE", alarmType);
            if (voiceTemplate != null && voiceTemplate.isEnabled()) {
                // 构造模板参数
                Object[] voiceParams = {deviceName};
                
                // 发送语音
                NotificationResult voiceResult = sendNotification(sensorId, "VOICE", "13800138000", voiceTemplate.getTemplateId(), voiceParams);
                results.add(voiceResult);
            }
            
            log.info("通知触发完成，共发送 {} 条通知", results.size());
        } catch (Exception e) {
            log.error("触发通知失败：{}", e.getMessage(), e);
        }
        
        return results;
    }
    
    /**
     * 发送通知
     * 
     * @param deviceId 设备ID
     * @param channelType 通知渠道（SMS/VOICE）
     * @param recipient 接收人
     * @param templateId 模板ID
     * @param params 模板参数
     * @return 发送结果
     */
    @Override
    public NotificationResult sendNotification(String deviceId, String channelType, String recipient, String templateId, Object params) {
        NotificationResult result = new NotificationResult(false, "初始化失败");
        
        try {
            log.info("发送通知：设备ID={}，渠道={}，接收人={}，模板ID={}", deviceId, channelType, recipient, templateId);
            
            // 扣减通知额度
            if (!deductNotificationQuota(deviceId, channelType)) {
                result.setSuccess(false);
                result.setMessage("通知额度不足");
                result.setSendId("QUOTA_ERROR" + System.currentTimeMillis());
                return result;
            }
            
            // 获取模板
            NotificationTemplate template = getTemplate(templateId);
            if (template == null) {
                throw new IllegalArgumentException("模板不存在：" + templateId);
            }
            
            // 渲染模板
            String content = renderTemplate(template, params);
            
            // 获取通知渠道
            NotificationChannel channel = getChannelByType(channelType);
            
            // 发送通知
            result = channel.send(recipient, content);
            
            log.info("通知发送成功，追踪ID：{}", result.getSendId());
        } catch (NotificationSendException e) {
            log.error("通知发送失败：{}", e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            result.setSendId(e.getMessage().substring(0, 10)); // 模拟发送ID
        } catch (Exception e) {
            log.error("发送通知时发生异常：{}", e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("发送通知时发生异常：" + e.getMessage());
            result.setSendId("ERROR" + System.currentTimeMillis());
        }
        
        return result;
    }
    
    /**
     * 根据报警日志发送通知
     * 
     * @param alarmLog 报警日志
     * @return 发送结果列表
     */
    @Override
    public List<NotificationResult> sendNotificationByAlarm(AlarmLog alarmLog) {
        List<NotificationResult> results = new ArrayList<>();
        
        try {
            log.info("根据报警日志发送通知：报警ID={}", alarmLog.getId());
            
            // 根据报警类型转换为通知报警类型
            String alarmType = convertAlarmType(alarmLog.getAlarmType());
            
            // 调用触发通知方法
            results = triggerNotification(alarmLog.getSensorId(), alarmType);
        } catch (Exception e) {
            log.error("根据报警日志发送通知失败：{}", e.getMessage(), e);
        }
        
        return results;
    }
    
    /**
     * 将报警类型数值转换为字符串
     * 
     * @param alarmType 报警类型数值
     * @return 报警类型字符串
     */
    private String convertAlarmType(Integer alarmType) {
        if (alarmType == null) {
            return "UNKNOWN";
        }
        
        switch (alarmType) {
            case 1:
                return "FIRE";
            case 2:
                return "TEMPERATURE";
            case 3:
                return "LOW_BATTERY";
            case 4:
                return "TAMPER";
            default:
                return "UNKNOWN";
        }
    }
    
    /**
     * 获取通知模板
     * 
     * @param templateId 模板ID
     * @return 通知模板
     */
    @Override
    public NotificationTemplate getTemplate(String templateId) {
        NotificationTemplate template = templateCache.get(templateId);
        if (template == null) {
            log.warn("模板不存在：{}", templateId);
        }
        return template;
    }
    
    /**
     * 获取指定类型的模板
     * 
     * @param templateType 模板类型（SMS/VOICE）
     * @param alarmType 报警类型
     * @return 通知模板
     */
    @Override
    public NotificationTemplate getTemplateByType(String templateType, String alarmType) {
        // 构造模板ID
        String templateId = templateType + "_" + alarmType + "_ALARM";
        return getTemplate(templateId);
    }
    
    /**
     * 渲染模板内容
     * 
     * @param template 通知模板
     * @param params 模板参数
     * @return 渲染后的内容
     */
    @Override
    public String renderTemplate(NotificationTemplate template, Object params) {
        if (template == null) {
            throw new IllegalArgumentException("模板不能为空");
        }
        
        String content = template.getTemplateContent();
        if (params == null) {
            return content;
        }
        
        try {
            if (params instanceof Object[]) {
                // 数组参数
                content = MessageFormat.format(content, (Object[]) params);
            } else {
                // 单个参数
                content = MessageFormat.format(content, params);
            }
            
            log.debug("模板渲染完成：原始内容={}，参数={}，渲染后={}", template.getTemplateContent(), params, content);
        } catch (Exception e) {
            log.error("模板渲染失败：{}", e.getMessage(), e);
        }
        
        return content;
    }
    
    /**
     * 查询通知额度
     * 
     * @param sensorId 传感器ID
     * @return 通知额度信息
     */
    @Override
    public NotificationQuota queryNotificationQuota(String sensorId) {
        try {
            NotificationQuota quota = notificationQuotaMapper.selectNotificationQuotaBySensorId(sensorId);
            if (quota == null) {
                // 如果额度不存在，创建默认额度
                quota = new NotificationQuota();
                quota.setSensorId(sensorId);
                quota.setFreeVoiceCount(100); // 默认免费语音100条
                quota.setFreeSmsCount(100);   // 默认免费短信100条
                quota.setBalance(BigDecimal.valueOf(0.0));       // 默认余额0
                notificationQuotaMapper.insertNotificationQuota(quota);
            }
            return quota;
        } catch (Exception e) {
            log.error("查询通知额度失败：{}", e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * 充值余额
     * 
     * @param sensorId 传感器ID
     * @param amount 充值金额
     * @return 充值结果
     */
    @Override
    public boolean rechargeBalance(String sensorId, double amount) {
        try {
            log.info("充值余额：传感器ID={}，金额={}", sensorId, amount);
            
            // 检查传感器是否存在（这里可以调用设备服务验证）
            // 先查询现有额度
            NotificationQuota quota = queryNotificationQuota(sensorId);
            
            // 创建充值对象
            NotificationQuota rechargeQuota = new NotificationQuota();
            rechargeQuota.setSensorId(sensorId);
            rechargeQuota.setBalance(BigDecimal.valueOf(amount));
            
            // 执行充值
            int result = notificationQuotaMapper.rechargeBalance(rechargeQuota);
            return result > 0;
        } catch (Exception e) {
            log.error("充值余额失败：{}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 充值免费语音额度
     * 
     * @param sensorId 传感器ID
     * @param count 充值数量
     * @return 充值结果
     */
    @Override
    public boolean rechargeFreeVoiceQuota(String sensorId, int count) {
        try {
            log.info("充值免费语音额度：传感器ID={}，数量={}", sensorId, count);
            
            // 先查询现有额度
            NotificationQuota quota = queryNotificationQuota(sensorId);
            
            // 创建充值对象
            NotificationQuota rechargeQuota = new NotificationQuota();
            rechargeQuota.setSensorId(sensorId);
            rechargeQuota.setFreeVoiceCount(count);
            
            // 执行充值
            int result = notificationQuotaMapper.addFreeVoiceQuota(rechargeQuota);
            return result > 0;
        } catch (Exception e) {
            log.error("充值免费语音额度失败：{}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 充值免费短信额度
     * 
     * @param sensorId 传感器ID
     * @param count 充值数量
     * @return 充值结果
     */
    @Override
    public boolean rechargeFreeSmsQuota(String sensorId, int count) {
        try {
            log.info("充值免费短信额度：传感器ID={}，数量={}", sensorId, count);
            
            // 先查询现有额度
            NotificationQuota quota = queryNotificationQuota(sensorId);
            
            // 创建充值对象
            NotificationQuota rechargeQuota = new NotificationQuota();
            rechargeQuota.setSensorId(sensorId);
            rechargeQuota.setFreeSmsCount(count);
            
            // 执行充值
            int result = notificationQuotaMapper.addFreeSmsQuota(rechargeQuota);
            return result > 0;
        } catch (Exception e) {
            log.error("充值免费短信额度失败：{}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 扣减通知额度
     * 
     * @param sensorId 传感器ID
     * @param channelType 通知渠道（SMS/VOICE）
     * @return 是否成功扣减
     */
    @Override
    public boolean deductNotificationQuota(String sensorId, String channelType) {
        try {
            log.info("扣减通知额度：传感器ID={}，渠道={}", sensorId, channelType);
            
            // 先查询现有额度
            NotificationQuota quota = queryNotificationQuota(sensorId);
            if (quota == null) {
                return false;
            }
            
            int result = 0;
            if ("SMS".equals(channelType)) {
                // 优先使用免费短信额度
                if (quota.getFreeSmsCount() > 0) {
                    result = notificationQuotaMapper.deductFreeSmsQuota(sensorId);
                } else if (quota.getBalance().compareTo(BigDecimal.valueOf(0.1)) >= 0) { // 假设短信费用0.1元
                    NotificationQuota deductQuota = new NotificationQuota();
                    deductQuota.setSensorId(sensorId);
                    deductQuota.setBalance(BigDecimal.valueOf(0.1));
                    result = notificationQuotaMapper.deductBalance(deductQuota);
                }
            } else if ("VOICE".equals(channelType)) {
                // 优先使用免费语音额度
                if (quota.getFreeVoiceCount() > 0) {
                    result = notificationQuotaMapper.deductFreeVoiceQuota(sensorId);
                } else if (quota.getBalance().compareTo(BigDecimal.valueOf(0.3)) >= 0) { // 假设语音费用0.3元
                    NotificationQuota deductQuota = new NotificationQuota();
                    deductQuota.setSensorId(sensorId);
                    deductQuota.setBalance(BigDecimal.valueOf(0.3));
                    result = notificationQuotaMapper.deductBalance(deductQuota);
                }
            }
            
            return result > 0;
        } catch (Exception e) {
            log.error("扣减通知额度失败：{}", e.getMessage(), e);
            return false;
        }
    }
}
