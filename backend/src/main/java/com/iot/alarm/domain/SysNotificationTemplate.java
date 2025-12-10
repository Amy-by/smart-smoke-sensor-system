package com.iot.alarm.domain;

import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 通知模板实体类
 * 对应数据库表：sys_notification_template
 * 
 * @author 开发人员
 * @date 2025-12-10
 */
public class SysNotificationTemplate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 关联产品ID（实现模板复用） */
    private Long productId;

    /** 短信签名（阿里云） */
    private String signName;

    /** 短信模板CODE（阿里云） */
    private String templateCode;

    /** 语音模板ID（腾讯云） */
    private String templateId;

    // getter和setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    @Override
    public String toString() {
        return "SysNotificationTemplate{" +
                "id=" + id +
                ", productId=" + productId +
                ", signName='" + signName + '\'' +
                ", templateCode='" + templateCode + '\'' +
                ", templateId='" + templateId + '\'' +
                '}';
    }
}
