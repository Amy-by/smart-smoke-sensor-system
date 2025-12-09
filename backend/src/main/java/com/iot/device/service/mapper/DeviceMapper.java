package com.iot.device.service.mapper;

import com.iot.device.domain.Device;
import com.iot.device.domain.DeviceStatus;

import java.util.List;

/**
 * 设备管理Mapper接口
 * 
 * @author ruoyi
 * @date 2025-12-09
 */
public interface DeviceMapper
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
     * 删除设备
     * 
     * @param sensorId 设备传感器ID
     * @return 结果
     */
    public int deleteDeviceBySensorId(String sensorId);

    /**
     * 批量删除设备
     * 
     * @param sensorIds 需要删除的设备传感器ID
     * @return 结果
     */
    public int deleteDeviceBySensorIds(String[] sensorIds);

    /**
     * 设备绑定
     * 
     * @param device 设备信息
     * @return 结果
     */
    public int bindDevice(Device device);

    /**
     * 设备解绑
     * 
     * @param sensorId 传感器ID
     * @return 结果
     */
    public int unbindDevice(String sensorId);

    /**
     * 查询设备状态
     * 
     * @param sensorId 传感器ID
     * @return 设备状态信息
     */
    public DeviceStatus selectDeviceStatusBySensorId(String sensorId);

    /**
     * 新增设备状态
     * 
     * @param deviceStatus 设备状态信息
     * @return 结果
     */
    public int insertDeviceStatus(DeviceStatus deviceStatus);

    /**
     * 更新设备状态
     * 
     * @param deviceStatus 设备状态信息
     * @return 结果
     */
    public int updateDeviceStatus(DeviceStatus deviceStatus);

    /**
     * 查询在线设备数量
     * 
     * @return 在线设备数量
     */
    public int getOnlineDeviceCount();

    /**
     * 查询设备总数
     * 
     * @return 设备总数
     */
    public int getTotalDeviceCount();

    /**
     * 批量插入设备
     * 
     * @param deviceList 设备列表
     * @return 插入成功的数量
     */
    public int batchInsertDevices(List<Device> deviceList);
}