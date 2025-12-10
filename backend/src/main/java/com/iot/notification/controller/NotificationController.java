package com.iot.notification.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.iot.notification.channel.NotificationResult;
import com.iot.notification.entity.NotificationQuota;
import com.iot.notification.service.NotificationService;
import com.iot.notification.template.NotificationTemplate;

/**
 * 通知管理控制器
 * 
 * 说明：提供通知的触发、发送和模板管理接口
 * 设计原则：RESTful接口设计 - 使用HTTP方法表示操作类型
 * 
 * @author IoT架构组
 * @date 2025-01-27
 */
@RestController
@RequestMapping("/iot/notification")
public class NotificationController extends BaseController
{
    @Autowired
    private NotificationService notificationService;

    /**
     * 触发通知
     * 
     * @param sensorId 传感器ID
     * @param alarmType 报警类型
     * @return 发送结果列表
     */
    @Log(title = "通知管理", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('project:notification:trigger')")
    @PostMapping("/trigger")
    public AjaxResult triggerNotification(@RequestParam String sensorId, @RequestParam String alarmType)
    {
        List<NotificationResult> results = notificationService.triggerNotification(sensorId, alarmType);
        return success(results);
    }

    /**
     * 发送通知
     * 
     * @param request 请求参数
     * @return 发送结果
     */
    @Log(title = "通知管理", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('project:notification:send')")
    @PostMapping("/send")
    public AjaxResult sendNotification(@RequestBody NotificationRequest request)
    {
        NotificationResult result = notificationService.sendNotification(
                request.getDeviceId(),
                request.getChannelType(),
                request.getRecipient(),
                request.getTemplateId(),
                request.getParams()
        );
        return success(result);
    }

    /**
     * 根据报警ID发送通知
     * 
     * @param alarmId 报警ID
     * @return 发送结果列表
     */
    @Log(title = "通知管理", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('project:notification:alarm')")
    @PostMapping("/alarm/{alarmId}")
    public AjaxResult sendNotificationByAlarm(@PathVariable Long alarmId)
    {
        // 这里需要先获取报警日志，暂时模拟
        return error("此接口需要与报警服务集成，暂未实现");
    }

    /**
     * 获取通知模板
     * 
     * @param templateId 模板ID
     * @return 模板信息
     */
    @PreAuthorize("@ss.hasPermi('project:notification:template:query')")
    @GetMapping("/template/{templateId}")
    public AjaxResult getTemplate(@PathVariable String templateId)
    {
        NotificationTemplate template = notificationService.getTemplate(templateId);
        return success(template);
    }

    /**
     * 获取指定类型的模板
     * 
     * @param templateType 模板类型
     * @param alarmType 报警类型
     * @return 模板信息
     */
    @PreAuthorize("@ss.hasPermi('project:notification:template:query')")
    @GetMapping("/template")
    public AjaxResult getTemplateByType(@RequestParam String templateType, @RequestParam String alarmType)
    {
        NotificationTemplate template = notificationService.getTemplateByType(templateType, alarmType);
        return success(template);
    }
    
    /**
     * 查询通知额度
     * 
     * @param sensorId 传感器ID
     * @return 额度信息
     */
    @PreAuthorize("@ss.hasPermi('project:notification:quota:query')")
    @GetMapping("/quota/{sensorId}")
    public AjaxResult queryNotificationQuota(@PathVariable String sensorId)
    {
        NotificationQuota quota = notificationService.queryNotificationQuota(sensorId);
        return success(quota);
    }
    
    /**
     * 充值余额
     * 
     * @param request 充值请求参数
     * @return 充值结果
     */
    @Log(title = "通知额度管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('project:notification:quota:recharge')")
    @PostMapping("/quota/recharge/balance")
    public AjaxResult rechargeBalance(@RequestBody RechargeRequest request)
    {
        boolean result = notificationService.rechargeBalance(request.getSensorId(), request.getAmount());
        return result ? success() : error("充值失败");
    }
    
    /**
     * 充值免费语音额度
     * 
     * @param request 充值请求参数
     * @return 充值结果
     */
    @Log(title = "通知额度管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('project:notification:quota:recharge')")
    @PostMapping("/quota/recharge/voice")
    public AjaxResult rechargeFreeVoiceQuota(@RequestBody RechargeCountRequest request)
    {
        boolean result = notificationService.rechargeFreeVoiceQuota(request.getSensorId(), request.getCount());
        return result ? success() : error("充值失败");
    }
    
    /**
     * 充值免费短信额度
     * 
     * @param request 充值请求参数
     * @return 充值结果
     */
    @Log(title = "通知额度管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('project:notification:quota:recharge')")
    @PostMapping("/quota/recharge/sms")
    public AjaxResult rechargeFreeSmsQuota(@RequestBody RechargeCountRequest request)
    {
        boolean result = notificationService.rechargeFreeSmsQuota(request.getSensorId(), request.getCount());
        return result ? success() : error("充值失败");
    }

    /**
     * 通知请求参数类
     */
    public static class NotificationRequest {
        private String deviceId;
        private String channelType;
        private String recipient;
        private String templateId;
        private Object params;

        // getter和setter方法
        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getChannelType() {
            return channelType;
        }

        public void setChannelType(String channelType) {
            this.channelType = channelType;
        }

        public String getRecipient() {
            return recipient;
        }

        public void setRecipient(String recipient) {
            this.recipient = recipient;
        }

        public String getTemplateId() {
            return templateId;
        }

        public void setTemplateId(String templateId) {
            this.templateId = templateId;
        }

        public Object getParams() {
            return params;
        }

        public void setParams(Object params) {
            this.params = params;
        }
    }
    
    /**
     * 充值请求参数类
     */
    public static class RechargeRequest {
        private String sensorId;
        private double amount;
        
        // getter和setter方法
        public String getSensorId() {
            return sensorId;
        }
        
        public void setSensorId(String sensorId) {
            this.sensorId = sensorId;
        }
        
        public double getAmount() {
            return amount;
        }
        
        public void setAmount(double amount) {
            this.amount = amount;
        }
    }
    
    /**
     * 数量充值请求参数类
     */
    public static class RechargeCountRequest {
        private String sensorId;
        private int count;
        
        // getter和setter方法
        public String getSensorId() {
            return sensorId;
        }
        
        public void setSensorId(String sensorId) {
            this.sensorId = sensorId;
        }
        
        public int getCount() {
            return count;
        }
        
        public void setCount(int count) {
            this.count = count;
        }
    }
}
