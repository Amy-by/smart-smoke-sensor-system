package com.iot.device.service;

import com.iot.device.domain.Device;
import com.iot.device.domain.DeviceStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 设备管理Service接口
 * 
 * @author ruoyi
 * @date 2025-12-09
 */
public interface DeviceService
{
    /**
     * 查询设备列表
     * 
     * @param device 设备信息
     * @return 设备集合
     */
    public List<Device> selectDeviceList(Device device);

    /**
     * 通过传感器ID查询设备
     * 
     * @param sensorId 传感器ID
     * @return 设备信息
     */
    public Device selectDeviceBySensorId(String sensorId);

    /**
     * 新增设备
     * 
     * @param device 设备信息
     * @return 结果
     */
    public int insertDevice(Device device);

    /**
     * 修改设备
     * 
     * @param device 设备信息
     * @return 结果
     */
    public int updateDevice(Device device);

    /**
     * 批量删除设备
     * 
     * @param sensorIds 需要删除的设备传感器ID
     * @return 结果
     */
    public int deleteDeviceBySensorIds(String[] sensorIds);

    /**
     * 删除设备信息
     * 
     * @param sensorId 设备传感器ID
     * @return 结果
     */
    public int deleteDeviceBySensorId(String sensorId);

    /**
     * 设备注册
     * 
     * @param device 设备信息
     * @return 结果
     */
    public int registerDevice(Device device);

    /**
     * 设备绑定
     * 
     * @param sensorId 传感器ID
     * @param userId 用户ID
     * @return 结果
     */
    public int bindDevice(String sensorId, Long userId);

    /**
     * 设备解绑
     * 
     * @param sensorId 传感器ID
     * @return 结果
     */
    public int unbindDevice(String sensorId);

    /**
     * 更新设备状态
     * 
     * @param deviceStatus 设备状态信息
     * @return 结果
     */
    public int updateDeviceStatus(DeviceStatus deviceStatus);

    /**
     * 查询设备状态
     * 
     * @param sensorId 传感器ID
     * @return 设备状态信息
     */
    public DeviceStatus selectDeviceStatusBySensorId(String sensorId);

    /**
     * 查询在线设备数量
     * 
     * @return 在线设备数量
     */
    public int getOnlineDeviceCount();

    /**
     * 批量导入设备
     *
     * @param file Excel文件
     * @return 导入成功的设备数量
     * @throws Exception 导入异常
     */
    public int batchImportDevices(MultipartFile file) throws Exception;

    /**
     * 查询设备总数
     * 
     * @return 设备总数
     */
    public int getTotalDeviceCount();
}