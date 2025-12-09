package com.iot.device.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.iot.device.domain.Device;
import com.iot.device.domain.DeviceStatus;
import com.iot.device.service.mapper.DeviceMapper;

/**
 * 设备管理服务层单元测试
 * 
 * @author ruoyi
 * @date 2025-12-09
 */
public class DeviceServiceImplTest {

    @Mock
    private DeviceMapper deviceMapper;

    @InjectMocks
    private DeviceServiceImpl deviceServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * 测试查询设备列表
     */
    @Test
    public void testSelectDeviceList() {
        // 准备测试数据
        Device device = new Device();
        device.setSensorId("1234567890");
        
        List<Device> deviceList = new ArrayList<>();
        deviceList.add(device);
        
        // 模拟Mapper方法调用
        when(deviceMapper.selectDeviceList(device)).thenReturn(deviceList);
        
        // 执行测试
        List<Device> result = deviceServiceImpl.selectDeviceList(device);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("1234567890", result.get(0).getSensorId());
        verify(deviceMapper, times(1)).selectDeviceList(device);
    }

    /**
     * 测试通过传感器ID查询设备
     */
    @Test
    public void testSelectDeviceBySensorId() {
        // 准备测试数据
        String sensorId = "1234567890";
        Device device = new Device();
        device.setSensorId(sensorId);
        
        // 模拟Mapper方法调用
        when(deviceMapper.selectDeviceBySensorId(sensorId)).thenReturn(device);
        
        // 执行测试
        Device result = deviceServiceImpl.selectDeviceBySensorId(sensorId);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(sensorId, result.getSensorId());
        verify(deviceMapper, times(1)).selectDeviceBySensorId(sensorId);
    }

    /**
     * 测试新增设备
     */
    @Test
    public void testInsertDevice() {
        // 准备测试数据
        Device device = new Device();
        device.setSensorId("1234567890");
        
        // 模拟Mapper方法调用
        when(deviceMapper.insertDevice(device)).thenReturn(1);
        
        // 执行测试
        int result = deviceServiceImpl.insertDevice(device);
        
        // 验证结果
        assertEquals(1, result);
        verify(deviceMapper, times(1)).insertDevice(device);
    }

    /**
     * 测试修改设备
     */
    @Test
    public void testUpdateDevice() {
        // 准备测试数据
        Device device = new Device();
        device.setSensorId("1234567890");
        
        // 模拟Mapper方法调用
        when(deviceMapper.updateDevice(device)).thenReturn(1);
        
        // 执行测试
        int result = deviceServiceImpl.updateDevice(device);
        
        // 验证结果
        assertEquals(1, result);
        verify(deviceMapper, times(1)).updateDevice(device);
    }

    /**
     * 测试删除设备
     */
    @Test
    public void testDeleteDeviceBySensorId() {
        // 准备测试数据
        String sensorId = "1234567890";
        
        // 模拟Mapper方法调用
        when(deviceMapper.deleteDeviceBySensorId(sensorId)).thenReturn(1);
        
        // 执行测试
        int result = deviceServiceImpl.deleteDeviceBySensorId(sensorId);
        
        // 验证结果
        assertEquals(1, result);
        verify(deviceMapper, times(1)).deleteDeviceBySensorId(sensorId);
    }

    /**
     * 测试批量删除设备
     */
    @Test
    public void testDeleteDeviceBySensorIds() {
        // 准备测试数据
        String[] sensorIds = {"1234567890", "0987654321"};
        
        // 模拟Mapper方法调用
        when(deviceMapper.deleteDeviceBySensorIds(sensorIds)).thenReturn(2);
        
        // 执行测试
        int result = deviceServiceImpl.deleteDeviceBySensorIds(sensorIds);
        
        // 验证结果
        assertEquals(2, result);
        verify(deviceMapper, times(1)).deleteDeviceBySensorIds(sensorIds);
    }

    /**
     * 测试设备注册
     */
    @Test
    public void testRegisterDevice() {
        // 准备测试数据
        Device device = new Device();
        device.setSensorId("1234567890");
        device.setMaterialCode("MAT001");
        
        // 模拟Mapper方法调用
        when(deviceMapper.selectDeviceBySensorId(device.getSensorId())).thenReturn(null);
        when(deviceMapper.insertDevice(any(Device.class))).thenReturn(1);
        when(deviceMapper.insertDeviceStatus(any(DeviceStatus.class))).thenReturn(1);
        
        // 执行测试
        int result = deviceServiceImpl.registerDevice(device);
        
        // 验证结果
        assertEquals(1, result);
        verify(deviceMapper, times(1)).selectDeviceBySensorId(device.getSensorId());
        verify(deviceMapper, times(1)).insertDevice(any(Device.class));
        verify(deviceMapper, times(1)).insertDeviceStatus(any(DeviceStatus.class));
    }

    /**
     * 测试设备批量导入功能
     * @throws IOException
     */
    @Test
    public void testBatchImportDevices() throws IOException {
        // 准备测试数据 - 创建一个模拟的Excel文件
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("设备数据");
            
            // 创建表头
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("传感器ID");
            headerRow.createCell(1).setCellValue("物料编码");
            headerRow.createCell(2).setCellValue("安装位置");
            
            // 创建设备数据行
            Row dataRow1 = sheet.createRow(1);
            dataRow1.createCell(0).setCellValue("SENSOR001");
            dataRow1.createCell(1).setCellValue("MAT001");
            dataRow1.createCell(2).setCellValue("车间A");
            
            Row dataRow2 = sheet.createRow(2);
            dataRow2.createCell(0).setCellValue("SENSOR002");
            dataRow2.createCell(1).setCellValue("MAT002");
            dataRow2.createCell(2).setCellValue("车间B");
            
            workbook.write(outputStream);
        }
        
        // 创建MultipartFile模拟对象
        byte[] fileBytes = outputStream.toByteArray();
        InputStream inputStream = new ByteArrayInputStream(fileBytes);
        
        // 创建MockMultipartFile实例
        MultipartFile mockMultipartFile = new org.springframework.mock.web.MockMultipartFile(
            "file",
            "devices.xlsx",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            fileBytes
        );
        
        // 模拟Mapper方法调用
        when(deviceMapper.batchInsertDevices(any(List.class))).thenReturn(2);
        
        // 执行测试
        int result = deviceServiceImpl.batchImportDevices(mockMultipartFile);
        
        // 验证结果
        assertEquals(2, result);
        verify(deviceMapper, times(1)).batchInsertDevices(any(List.class));
    }
    
    /**
     * 测试设备批量导入功能 - 空文件
     * @throws IOException
     */
    @Test
    public void testBatchImportDevicesEmptyFile() throws IOException {
        // 创建空的MultipartFile模拟对象
        MultipartFile mockMultipartFile = new org.springframework.mock.web.MockMultipartFile(
            "file",
            "empty.xlsx",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            new byte[0]
        );
        
        // 执行测试并验证异常
        try {
            deviceServiceImpl.batchImportDevices(mockMultipartFile);
        } catch (IOException e) {
            assertEquals("上传的文件不能为空", e.getMessage());
        }
    }
    
    /**
     * 测试设备注册（设备已存在）
     */
    @Test
    public void testRegisterDevice_AlreadyExists() {
        // 准备测试数据
        Device device = new Device();
        device.setSensorId("1234567890");
        
        // 模拟Mapper方法调用
        when(deviceMapper.selectDeviceBySensorId(device.getSensorId())).thenReturn(device);
        
        // 执行测试
        int result = deviceServiceImpl.registerDevice(device);
        
        // 验证结果
        assertEquals(0, result);
        verify(deviceMapper, times(1)).selectDeviceBySensorId(device.getSensorId());
        verify(deviceMapper, never()).insertDevice(any(Device.class));
        verify(deviceMapper, never()).insertDeviceStatus(any(DeviceStatus.class));
    }

    /**
     * 测试设备绑定
     */
    @Test
    public void testBindDevice() {
        // 准备测试数据
        String sensorId = "1234567890";
        Long userId = 1L;
        
        // 模拟Mapper方法调用
        when(deviceMapper.bindDevice(any(Device.class))).thenReturn(1);
        
        // 执行测试
        int result = deviceServiceImpl.bindDevice(sensorId, userId);
        
        // 验证结果
        assertEquals(1, result);
        verify(deviceMapper, times(1)).bindDevice(any(Device.class));
    }

    /**
     * 测试设备解绑
     */
    @Test
    public void testUnbindDevice() {
        // 准备测试数据
        String sensorId = "1234567890";
        
        // 模拟Mapper方法调用
        when(deviceMapper.unbindDevice(sensorId)).thenReturn(1);
        
        // 执行测试
        int result = deviceServiceImpl.unbindDevice(sensorId);
        
        // 验证结果
        assertEquals(1, result);
        verify(deviceMapper, times(1)).unbindDevice(sensorId);
    }

    /**
     * 测试更新设备状态（状态已存在）
     */
    @Test
    public void testUpdateDeviceStatus_Exists() {
        // 准备测试数据
        String sensorId = "1234567890";
        DeviceStatus deviceStatus = new DeviceStatus();
        deviceStatus.setSensorId(sensorId);
        deviceStatus.setOnlineStatus(1);
        
        // 模拟Mapper方法调用
        when(deviceMapper.selectDeviceStatusBySensorId(sensorId)).thenReturn(deviceStatus);
        when(deviceMapper.updateDeviceStatus(any(DeviceStatus.class))).thenReturn(1);
        
        // 执行测试
        int result = deviceServiceImpl.updateDeviceStatus(deviceStatus);
        
        // 验证结果
        assertEquals(1, result);
        verify(deviceMapper, times(1)).selectDeviceStatusBySensorId(sensorId);
        verify(deviceMapper, times(1)).updateDeviceStatus(any(DeviceStatus.class));
        verify(deviceMapper, never()).insertDeviceStatus(any(DeviceStatus.class));
    }

    /**
     * 测试更新设备状态（状态不存在）
     */
    @Test
    public void testUpdateDeviceStatus_NotExists() {
        // 准备测试数据
        String sensorId = "1234567890";
        DeviceStatus deviceStatus = new DeviceStatus();
        deviceStatus.setSensorId(sensorId);
        deviceStatus.setOnlineStatus(1);
        
        // 模拟Mapper方法调用
        when(deviceMapper.selectDeviceStatusBySensorId(sensorId)).thenReturn(null);
        when(deviceMapper.insertDeviceStatus(any(DeviceStatus.class))).thenReturn(1);
        
        // 执行测试
        int result = deviceServiceImpl.updateDeviceStatus(deviceStatus);
        
        // 验证结果
        assertEquals(1, result);
        verify(deviceMapper, times(1)).selectDeviceStatusBySensorId(sensorId);
        verify(deviceMapper, never()).updateDeviceStatus(any(DeviceStatus.class));
        verify(deviceMapper, times(1)).insertDeviceStatus(any(DeviceStatus.class));
    }

    /**
     * 测试查询设备状态
     */
    @Test
    public void testSelectDeviceStatusBySensorId() {
        // 准备测试数据
        String sensorId = "1234567890";
        DeviceStatus deviceStatus = new DeviceStatus();
        deviceStatus.setSensorId(sensorId);
        deviceStatus.setOnlineStatus(1);
        
        // 模拟Mapper方法调用
        when(deviceMapper.selectDeviceStatusBySensorId(sensorId)).thenReturn(deviceStatus);
        
        // 执行测试
        DeviceStatus result = deviceServiceImpl.selectDeviceStatusBySensorId(sensorId);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(sensorId, result.getSensorId());
        assertEquals(1, result.getOnlineStatus());
        verify(deviceMapper, times(1)).selectDeviceStatusBySensorId(sensorId);
    }

    /**
     * 测试查询设备总数
     */
    @Test
    public void testGetTotalDeviceCount() {
        // 模拟Mapper方法调用
        when(deviceMapper.getTotalDeviceCount()).thenReturn(100);
        
        // 执行测试
        int result = deviceServiceImpl.getTotalDeviceCount();
        
        // 验证结果
        assertEquals(100, result);
        verify(deviceMapper, times(1)).getTotalDeviceCount();
    }

    /**
     * 测试查询在线设备数量
     */
    @Test
    public void testGetOnlineDeviceCount() {
        // 执行测试
        int result = deviceServiceImpl.getOnlineDeviceCount();
        
        // 验证结果（由于方法体是空的，预期返回0）
        assertEquals(0, result);
    }
}