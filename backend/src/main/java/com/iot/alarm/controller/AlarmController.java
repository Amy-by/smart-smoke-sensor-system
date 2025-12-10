package com.iot.alarm.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.iot.alarm.domain.AlarmQueryDTO;
import com.iot.alarm.domain.AlarmVO;
import com.iot.alarm.service.AlarmService;

/**
 * 报警管理控制器
 * 
 * @author IoT开发人员
 * @date 2025-12-11
 */
@RestController
@RequestMapping("/iot/alarm")
public class AlarmController extends BaseController
{
    @Autowired
    private AlarmService alarmService;

    /**
     * 获取报警列表
     */
    @PreAuthorize("@ss.hasPermi('project:alarm:list')")
    @GetMapping("/list")
    public TableDataInfo list(AlarmQueryDTO queryDTO)
    {
        startPage();
        List<AlarmVO> list = alarmService.getAlarmList(queryDTO);
        return getDataTable(list);
    }

    /**
     * 根据ID获取报警详细信息
     */
    @PreAuthorize("@ss.hasPermi('project:alarm:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(alarmService.getAlarmById(id));
    }

    /**
     * 处理报警
     */
    @Log(title = "报警管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('project:alarm:handle')")
    @PutMapping("/handle/{id}")
    public AjaxResult handleAlarm(@PathVariable Long id, @RequestParam String handleUser)
    {
        Boolean result = alarmService.handleAlarm(id, handleUser);
        if (result) {
            return success("报警处理成功");
        } else {
            return error("报警处理失败");
        }
    }

    /**
     * 根据传感器ID查询报警记录
     */
    @PreAuthorize("@ss.hasPermi('project:alarm:query')")
    @GetMapping("/sensor/{sensorId}")
    public AjaxResult getAlarmsBySensorId(@PathVariable String sensorId, @RequestParam(required = false, defaultValue = "10") Integer limit)
    {
        return success(alarmService.getAlarmsBySensorId(sensorId, limit));
    }
}
