package com.iot.device.controller;

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
import org.springframework.web.multipart.MultipartFile;

import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.iot.device.domain.Device;
import com.iot.device.domain.DeviceStatus;
import com.iot.device.service.DeviceService;

/**
 * 设备管理控制器
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/iot/device")
public class DeviceController extends BaseController
{
    @Autowired
    private DeviceService deviceService;

    /**
     * 获取设备列表
     */
    @PreAuthorize("@ss.hasPermi('project:device:list')")
    @GetMapping("/list")
    public TableDataInfo list(Device device)
    {
        startPage();
        List<Device> list = deviceService.selectDeviceList(device);
        return getDataTable(list);
    }

    /**
     * 根据传感器ID获取设备详细信息
     */
    @PreAuthorize("@ss.hasPermi('project:device:query')")
    @GetMapping("/{sensorId}")
    public AjaxResult getInfo(@PathVariable String sensorId)
    {
        return success(deviceService.selectDeviceBySensorId(sensorId));
    }

    /**
     * 设备注册
     */
    @Log(title = "设备管理", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('project:device:register')")
    @PostMapping("/register")
    public AjaxResult registerDevice(@RequestBody Device device)
    {
        if (deviceService.registerDevice(device) > 0)
        {
            return success("设备注册成功");
        }
        return error("设备已存在");
    }

    /**
     * 新增设备
     */
    @Log(title = "设备管理", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('project:device:add')")
    @PostMapping
    public AjaxResult add(@RequestBody Device device)
    {
        return toAjax(deviceService.insertDevice(device));
    }

    /**
     * 修改设备
     */
    @Log(title = "设备管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('project:device:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody Device device)
    {
        return toAjax(deviceService.updateDevice(device));
    }

    /**
     * 删除设备
     */
    @Log(title = "设备管理", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('project:device:remove')")
    @DeleteMapping("/{sensorIds}")
    public AjaxResult remove(@PathVariable String[] sensorIds)
    {
        return toAjax(deviceService.deleteDeviceBySensorIds(sensorIds));
    }

    /**
     * 设备绑定
     */
    @Log(title = "设备管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('project:device:bind')")
    @PostMapping("/bind")
    public AjaxResult bindDevice(String sensorId, Long userId)
    {
        return toAjax(deviceService.bindDevice(sensorId, userId));
    }

    /**
     * 设备解绑
     */
    @Log(title = "设备管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('project:device:unbind')")
    @PostMapping("/unbind")
    public AjaxResult unbindDevice(String sensorId)
    {
        return toAjax(deviceService.unbindDevice(sensorId));
    }

    /**
     * 更新设备状态
     */
    @Log(title = "设备管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('project:device:updateStatus')")
    @PutMapping("/status")
    public AjaxResult updateDeviceStatus(@RequestBody DeviceStatus deviceStatus)
    {
        return toAjax(deviceService.updateDeviceStatus(deviceStatus));
    }

    /**
     * 获取设备状态
     */
    @PreAuthorize("@ss.hasPermi('project:device:queryStatus')")
    @GetMapping("/status/{sensorId}")
    public AjaxResult getDeviceStatus(@PathVariable String sensorId)
    {
        return success(deviceService.selectDeviceStatusBySensorId(sensorId));
    }

    /**
     * 获取设备总数
     */
    @PreAuthorize("@ss.hasPermi('project:device:statistics')")
    @GetMapping("/statistics/total")
    public AjaxResult getTotalDeviceCount()
    {
        return success(deviceService.getTotalDeviceCount());
    }

    /**
     * 获取在线设备数量
     */
    @PreAuthorize("@ss.hasPermi('project:device:statistics')")
    @GetMapping("/statistics/online")
    public AjaxResult getOnlineDeviceCount()
    {
        return success(deviceService.getOnlineDeviceCount());
    }

    /**
     * 批量导入设备（Excel）
     */
    @Log(title = "设备管理", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('project:device:import')")
    @PostMapping("/batchImport")
    public AjaxResult batchImport(@RequestParam("file") MultipartFile file)
    {
        if (file.isEmpty())
        {
            return error("文件不能为空");
        }
        try
        {
            int result = deviceService.batchImportDevices(file);
            return success("导入成功，共导入" + result + "条记录");
        }
        catch (Exception e)
        {
            return error("导入失败：" + e.getMessage());
        }
    }
}