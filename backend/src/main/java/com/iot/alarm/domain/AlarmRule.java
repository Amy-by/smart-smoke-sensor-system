package com.iot.alarm.domain;

import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 报警规则实体类
 * 
 * @author 开发人员
 * @date 2025-12-10
 */
public class AlarmRule extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 传感器IMEI号 */
    private String sensorId;

    /** 规则名称 */
    private String ruleName;

    /** 规则类型（1=烟雾报警，2=温度报警，3=低电报警，4=防拆报警） */
    private Integer ruleType;

    /** 报警阈值 */
    private Integer threshold;

    /** 报警优先级（1=最高，2=高，3=中，4=低） */
    private Integer priority;

    /** 规则状态（0=禁用，1=启用） */
    private Integer status;

    /** 描述 */
    private String description;

    // getter和setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public Integer getRuleType() {
        return ruleType;
    }

    public void setRuleType(Integer ruleType) {
        this.ruleType = ruleType;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "AlarmRule{" +
                "id=" + id +
                ", sensorId='" + sensorId + '\'' +
                ", ruleName='" + ruleName + '\'' +
                ", ruleType=" + ruleType +
                ", threshold=" + threshold +
                ", priority=" + priority +
                ", status=" + status +
                ", description='" + description + '\'' +
                '}';
    }
}
