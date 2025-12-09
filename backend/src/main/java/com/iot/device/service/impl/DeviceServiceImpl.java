package com.iot.device.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ruoyi.common.utils.StringUtils;
import com.iot.device.domain.Device;
import com.iot.device.domain.DeviceStatus;
import com.iot.device.service.mapper.DeviceMapper;
import com.iot.device.service.DeviceService;
import java.io.IOException;
import java.io.InputStream;

/**
 * 设备管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-09
 */
@Service
public class DeviceServiceImpl implements DeviceService
{
    private static final Logger log = LoggerFactory.getLogger(DeviceServiceImpl.class);

    @Autowired
    private DeviceMapper deviceMapper;

    /**
     * 查询设备列表
     * 
     * @param device 设备信息
     * @return 设备集合
     */
    @Override
    public List<Device> selectDeviceList(Device device)
    {
        return deviceMapper.selectDeviceList(device);
    }

    /**
     * 通过传感器ID查询设备
     * 
     * @param sensorId 传感器ID
     * @return 设备信息
     */
    @Override
    public Device selectDeviceBySensorId(String sensorId)
    {
        return deviceMapper.selectDeviceBySensorId(sensorId);
    }

    /**
     * 新增设备
     * 
     * @param device 设备信息
     * @return 结果
     */
    @Override
    public int insertDevice(Device device)
    {
        return deviceMapper.insertDevice(device);
    }

    /**
     * 修改设备
     * 
     * @param device 设备信息
     * @return 结果
     */
    @Override
    public int updateDevice(Device device)
    {
        return deviceMapper.updateDevice(device);
    }

    /**
     * 删除设备信息
     * 
     * @param sensorId 设备传感器ID
     * @return 结果
     */
    @Override
    public int deleteDeviceBySensorId(String sensorId)
    {
        return deviceMapper.deleteDeviceBySensorId(sensorId);
    }

    /**
     * 批量删除设备
     * 
     * @param sensorIds 需要删除的设备传感器ID
     * @return 结果
     */
    @Override
    public int deleteDeviceBySensorIds(String[] sensorIds)
    {
        return deviceMapper.deleteDeviceBySensorIds(sensorIds);
    }

    /**
     * 设备注册
     * 
     * @param device 设备信息
     * @return 结果
     */
    @Override
    @Transactional
    public int registerDevice(Device device)
    {
        // 检查设备是否已存在
        Device existingDevice = deviceMapper.selectDeviceBySensorId(device.getSensorId());
        if (existingDevice != null)
        {
            log.info("设备已存在: {}", device.getSensorId());
            return 0;
        }
        
        // 设置设备默认信息
        device.setStatus("0"); // 默认为未激活状态
        device.setCreateTime(new Date());
        device.setUpdateTime(new Date());
        
        // 新增设备
        int result = deviceMapper.insertDevice(device);
        
        // 新增设备状态
        if (result > 0)
        {
            DeviceStatus deviceStatus = new DeviceStatus();
            deviceStatus.setSensorId(device.getSensorId());
            deviceStatus.setOnlineStatus(0); // 初始为离线状态
            deviceStatus.setBatteryLevel(100); // 初始电池电量为100%
            deviceStatus.setSignalStrength(0); // 初始信号强度为0
            deviceStatus.setCreateTime(new Date());
            deviceStatus.setUpdateTime(new Date());
            
            deviceMapper.insertDeviceStatus(deviceStatus);
        }
        
        return result;
    }

    /**
     * 设备绑定
     * 
     * @param sensorId 传感器ID
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public int bindDevice(String sensorId, Long userId)
    {
        Device device = new Device();
        device.setSensorId(sensorId);
        device.setUserId(userId);
        device.setStatus("1"); // 绑定后设备状态变为已激活
        device.setUpdateTime(new Date());
        
        return deviceMapper.bindDevice(device);
    }

    /**
     * 设备解绑
     * 
     * @param sensorId 传感器ID
     * @return 结果
     */
    @Override
    public int unbindDevice(String sensorId)
    {
        return deviceMapper.unbindDevice(sensorId);
    }

    /**
     * 更新设备状态
     * 
     * @param deviceStatus 设备状态信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateDeviceStatus(DeviceStatus deviceStatus)
    {
        // 检查设备状态是否已存在
        DeviceStatus existingStatus = deviceMapper.selectDeviceStatusBySensorId(deviceStatus.getSensorId());
        
        // 更新设备状态
        deviceStatus.setUpdateTime(new Date());
        
        if (existingStatus != null)
        {
            // 如果状态已存在，则更新
            return deviceMapper.updateDeviceStatus(deviceStatus);
        }
        else
        {
            // 如果状态不存在，则新增
            deviceStatus.setCreateTime(new Date());
            return deviceMapper.insertDeviceStatus(deviceStatus);
        }
    }

    /**
     * 查询设备状态
     * 
     * @param sensorId 传感器ID
     * @return 设备状态信息
     */
    @Override
    public DeviceStatus selectDeviceStatusBySensorId(String sensorId)
    {
        return deviceMapper.selectDeviceStatusBySensorId(sensorId);
    }

    /**
     * 查询在线设备数量
     * 
     * @return 在线设备数量
     */
    @Override
    public int getOnlineDeviceCount()
    {
        // TODO: 实现在线设备数量统计
        return 0;
    }

    /**
     * 查询设备总数
     * 
     * @return 设备总数
     */
    @Override
    public int getTotalDeviceCount()
    {
        return deviceMapper.getTotalDeviceCount();
    }

    @Override
    @Transactional
    public int batchImportDevices(MultipartFile file) throws IOException {
        // 检查文件是否为空
        if (file.isEmpty()) {
            throw new IOException("上传的文件不能为空");
        }

        // 读取文件内容
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            // 获取第一个工作表
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                throw new IOException("Excel文件中没有工作表");
            }

            // 解析工作表内容
            List<Device> deviceList = new ArrayList<>();
            Date now = new Date();

            // 从第二行开始读取（跳过表头）
            for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    continue;
                }

                // 创建设备对象
                Device device = new Device();

                // 设置传感器ID
                Cell sensorIdCell = row.getCell(0);
                if (sensorIdCell != null) {
                    device.setSensorId(getCellStringValue(sensorIdCell));
                }

                // 设置物料编码
                Cell materialCodeCell = row.getCell(1);
                if (materialCodeCell != null) {
                    device.setMaterialCode(getCellStringValue(materialCodeCell));
                }

                // 设置安装位置
                Cell installationLocationCell = row.getCell(2);
                if (installationLocationCell != null) {
                    device.setInstallationLocation(getCellStringValue(installationLocationCell));
                }

                // 设置其他默认值
                device.setCreateTime(now);
                device.setUpdateTime(now);

                // 添加到设备列表
                deviceList.add(device);
            }

            // 如果没有设备数据，直接返回
            if (deviceList.isEmpty()) {
                return 0;
            }

            // 批量插入设备数据
            return deviceMapper.batchInsertDevices(deviceList);
        }
    }

    /**
     * 获取单元格的字符串值
     */
    private String getCellStringValue(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return null;
        }
    }
}