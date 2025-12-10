package com.iot.notification.template;

/**
 * 通知模板模型
 * 
 * 说明：封装通知模板的信息，用于渲染不同类型的通知内容
 * 设计原则：封装性原则 - 将模板相关信息封装在一起
 * 
 * @author IoT架构组
 * @date 2025-01-27
 */
public class NotificationTemplate {
    
    /** 模板ID */
    private String templateId;
    
    /** 模板类型（SMS/VOICE） */
    private String templateType;
    
    /** 模板名称 */
    private String templateName;
    
    /** 模板内容 */
    private String templateContent;
    
    /** 是否启用 */
    private boolean enabled;
    
    /** 创建时间 */
    private String createTime;
    
    /** 更新时间 */
    private String updateTime;
    
    // 构造方法
    public NotificationTemplate() {
    }
    
    public NotificationTemplate(String templateId, String templateType, String templateContent) {
        this.templateId = templateId;
        this.templateType = templateType;
        this.templateContent = templateContent;
        this.enabled = true;
    }
    
    // Getters and Setters
    public String getTemplateId() {
        return templateId;
    }
    
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
    
    public String getTemplateType() {
        return templateType;
    }
    
    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }
    
    public String getTemplateName() {
        return templateName;
    }
    
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
    
    public String getTemplateContent() {
        return templateContent;
    }
    
    public void setTemplateContent(String templateContent) {
        this.templateContent = templateContent;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public String getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    
    public String getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    
    @Override
    public String toString() {
        return "NotificationTemplate{" +
                "templateId='" + templateId + '\'' +
                ", templateType='" + templateType + '\'' +
                ", templateName='" + templateName + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}
