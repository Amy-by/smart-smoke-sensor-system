package com.iot.device.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.iot.device.domain.Device;
import com.iot.device.domain.DeviceStatus;
import com.iot.device.service.DeviceService;

@ExtendWith(MockitoExtension.class)
public class DeviceControllerTest
{
    private MockMvc mockMvc;
    
    @Mock
    private DeviceService deviceService;
    
    @InjectMocks
    private DeviceController deviceController;
    
    private ObjectMapper objectMapper;
    
    @BeforeEach
    void setUp()
    {
        mockMvc = MockMvcBuilders.standaloneSetup(deviceController).build();
        objectMapper = new ObjectMapper();
    }
    
    @Test
    void testListDevices() throws Exception
    {
        // 准备测试数据
        Device device = new Device();
        List<Device> deviceList = new ArrayList<>();
        deviceList.add(device);
        
        // 模拟service层方法
        when(deviceService.selectDeviceList(any(Device.class))).thenReturn(deviceList);
        
        // 执行测试
        mockMvc.perform(get("/iot/device/list"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value(200));
        
        // 验证
        verify(deviceService, times(1)).selectDeviceList(any(Device.class));
    }
    
    @Test
    void testGetDeviceInfo() throws Exception
    {
        // 准备测试数据
        String sensorId = "123456";
        Device device = new Device();
        device.setSensorId(sensorId);
        
        // 模拟service层方法
        when(deviceService.selectDeviceBySensorId(sensorId)).thenReturn(device);
        
        // 执行测试
        mockMvc.perform(get("/iot/device/{sensorId}", sensorId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.data.sensorId").value(sensorId));
        
        // 验证
        verify(deviceService, times(1)).selectDeviceBySensorId(sensorId);
    }
    
    @Test
    void testRegisterDevice_Success() throws Exception
    {
        // 准备测试数据
        Device device = new Device();
        device.setSensorId("123456");
        
        // 模拟service层方法
        when(deviceService.registerDevice(any(Device.class))).thenReturn(1);
        
        // 执行测试
        mockMvc.perform(post("/iot/device/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(device)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.msg").value("设备注册成功"));
        
        // 验证
        verify(deviceService, times(1)).registerDevice(any(Device.class));
    }
    
    @Test
    void testRegisterDevice_Exist() throws Exception
    {
        // 准备测试数据
        Device device = new Device();
        device.setSensorId("123456");
        
        // 模拟service层方法
        when(deviceService.registerDevice(any(Device.class))).thenReturn(0);
        
        // 执行测试
        mockMvc.perform(post("/iot/device/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(device)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value(500))
            .andExpect(jsonPath("$.msg").value("设备已存在"));
        
        // 验证
        verify(deviceService, times(1)).registerDevice(any(Device.class));
    }
    
    @Test
    void testAddDevice() throws Exception
    {
        // 准备测试数据
        Device device = new Device();
        
        // 模拟service层方法
        when(deviceService.insertDevice(any(Device.class))).thenReturn(1);
        
        // 执行测试
        mockMvc.perform(post("/iot/device")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(device)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value(200));
        
        // 验证
        verify(deviceService, times(1)).insertDevice(any(Device.class));
    }
    
    @Test
    void testEditDevice() throws Exception
    {
        // 准备测试数据
        Device device = new Device();
        
        // 模拟service层方法
        when(deviceService.updateDevice(any(Device.class))).thenReturn(1);
        
        // 执行测试
        mockMvc.perform(put("/iot/device")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(device)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value(200));
        
        // 验证
        verify(deviceService, times(1)).updateDevice(any(Device.class));
    }
    
    @Test
    void testRemoveDevice() throws Exception
    {
        // 准备测试数据
        String[] sensorIds = {"123456", "789012"};
        
        // 模拟service层方法
        when(deviceService.deleteDeviceBySensorIds(sensorIds)).thenReturn(2);
        
        // 执行测试
        mockMvc.perform(delete("/iot/device/{sensorIds}", "123456,789012"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value(200));
        
        // 验证
        verify(deviceService, times(1)).deleteDeviceBySensorIds(sensorIds);
    }
    
    @Test
    void testBindDevice() throws Exception
    {
        // 准备测试数据
        String sensorId = "123456";
        Long userId = 1L;
        
        // 模拟service层方法
        when(deviceService.bindDevice(sensorId, userId)).thenReturn(1);
        
        // 执行测试
        mockMvc.perform(post("/iot/device/bind")
            .param("sensorId", sensorId)
            .param("userId", userId.toString()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value(200));
        
        // 验证
        verify(deviceService, times(1)).bindDevice(sensorId, userId);
    }
    
    @Test
    void testUnbindDevice() throws Exception
    {
        // 准备测试数据
        String sensorId = "123456";
        
        // 模拟service层方法
        when(deviceService.unbindDevice(sensorId)).thenReturn(1);
        
        // 执行测试
        mockMvc.perform(post("/iot/device/unbind")
            .param("sensorId", sensorId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value(200));
        
        // 验证
        verify(deviceService, times(1)).unbindDevice(sensorId);
    }
    
    @Test
    void testUpdateDeviceStatus() throws Exception
    {
        // 准备测试数据
        DeviceStatus deviceStatus = new DeviceStatus();
        deviceStatus.setSensorId("123456");
        
        // 模拟service层方法
        when(deviceService.updateDeviceStatus(any(DeviceStatus.class))).thenReturn(1);
        
        // 执行测试
        mockMvc.perform(put("/iot/device/status")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(deviceStatus)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value(200));
        
        // 验证
        verify(deviceService, times(1)).updateDeviceStatus(any(DeviceStatus.class));
    }
    
    @Test
    void testGetDeviceStatus() throws Exception
    {
        // 准备测试数据
        String sensorId = "123456";
        DeviceStatus deviceStatus = new DeviceStatus();
        deviceStatus.setSensorId(sensorId);
        
        // 模拟service层方法
        when(deviceService.selectDeviceStatusBySensorId(sensorId)).thenReturn(deviceStatus);
        
        // 执行测试
        mockMvc.perform(get("/iot/device/status/{sensorId}", sensorId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value(200));
        
        // 验证
        verify(deviceService, times(1)).selectDeviceStatusBySensorId(sensorId);
    }
    
    @Test
    void testGetTotalDeviceCount() throws Exception
    {
        // 模拟service层方法
        when(deviceService.getTotalDeviceCount()).thenReturn(100);
        
        // 执行测试
        mockMvc.perform(get("/iot/device/statistics/total"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.data").value(100));
        
        // 验证
        verify(deviceService, times(1)).getTotalDeviceCount();
    }
    
    @Test
    void testGetOnlineDeviceCount() throws Exception
    {
        // 模拟service层方法
        when(deviceService.getOnlineDeviceCount()).thenReturn(80);
        
        // 执行测试
        mockMvc.perform(get("/iot/device/statistics/online"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.data").value(80));
        
        // 验证
        verify(deviceService, times(1)).getOnlineDeviceCount();
    }
}