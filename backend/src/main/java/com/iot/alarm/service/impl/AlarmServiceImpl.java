package com.iot.alarm.service.impl;

import com.iot.alarm.domain.AlarmLog;
import com.iot.alarm.domain.AlarmQueryDTO;
import com.iot.alarm.domain.AlarmVO;
import com.iot.alarm.processor.Alarm;
import com.iot.alarm.processor.AlarmProcessor;
import com.iot.alarm.processor.AlarmProcessException;
import com.iot.alarm.service.AlarmService;
import com.iot.alarm.service.mapper.AlarmMapper;
import com.iot.device.domain.Device;
import com.iot.device.service.DeviceService;
import com.iot.protocol.model.SensorData;
import com.ruoyi.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 报警管理Service业务层实现
 * 
 * @author IoT开发人员
 * @date 2025-12-11
 */
@Service
public class AlarmServiceImpl implements AlarmService {

    private static final Logger log = LoggerFactory.getLogger(AlarmServiceImpl.class);

    @Autowired
    private AlarmMapper alarmMapper;

    @Autowired
    private DeviceService deviceService;
    
    @Autowired
    private AlarmProcessor alarmProcessor;

    /**
     * 处理传感器报警数据
     *
     * @param sensorData 传感器数据
     * @return 处理结果（报警日志ID列表）
     */
    @Override
    @Transactional
    public List<Long> processAlarm(SensorData sensorData) {
        List<Long> alarmIds = new ArrayList<>();
        
        try {
            // 调用报警处理器处理传感器数据
            List<Alarm> alarms = alarmProcessor.process(sensorData);
            
            if (alarms != null && !alarms.isEmpty()) {
                log.info("处理传感器数据：{}，检测到报警：{}", sensorData.getDeviceId(), alarms.size());
                
                // 保存每个报警记录
                for (Alarm alarm : alarms) {
                    AlarmLog alarmLog = new AlarmLog();
                    alarmLog.setSensorId(sensorData.getDeviceId());
                    
                    // 将String类型的alarmType转换为Integer类型
                    Integer alarmTypeValue = convertAlarmType(alarm.getAlarmType());
                    alarmLog.setAlarmType(alarmTypeValue);
                    
                    // 设置触发值
                    alarmLog.setTriggerValue(alarm.getDescription());
                    alarmLog.setAlarmTime(new Date());
                    alarmLog.setHandleStatus(0); // 默认未处理
                    
                    // 保存报警记录
                    Long alarmId = saveAlarm(alarmLog);
                    if (alarmId != null) {
                        alarmIds.add(alarmId);
                    }
                }
            }
        } catch (AlarmProcessException e) {
            log.error("处理传感器报警数据失败：{}", e.getMessage(), e);
        }
        
        return alarmIds;
    }
    
    /**
     * 将报警类型字符串转换为对应的Integer值
     * 
     * @param alarmType 报警类型字符串
     * @return 对应的Integer值
     */
    private Integer convertAlarmType(String alarmType) {
        if ("火灾".equals(alarmType)) {
            return 1;
        } else if ("温度".equals(alarmType)) {
            return 2;
        } else if ("低电".equals(alarmType)) {
            return 3;
        } else if ("防拆".equals(alarmType)) {
            return 4;
        } else {
            return 0; // 默认值
        }
    }

    /**
     * 保存报警记录
     *
     * @param alarmLog 报警日志
     * @return 报警日志ID
     */
    @Override
    @Transactional
    public Long saveAlarm(AlarmLog alarmLog) {
        // 设置默认值
        if (alarmLog.getAlarmTime() == null) {
            alarmLog.setAlarmTime(new Date());
        }
        if (alarmLog.getHandleStatus() == null) {
            alarmLog.setHandleStatus(0); // 默认未处理
        }

        int result = alarmMapper.insertAlarm(alarmLog);
        if (result > 0) {
            log.info("保存报警记录成功，报警ID：{}", alarmLog.getId());
            return alarmLog.getId();
        } else {
            log.error("保存报警记录失败");
            return null;
        }
    }

    /**
     * 分页查询报警列表
     *
     * @param queryDTO 查询条件
     * @return 报警列表
     */
    @Override
    public List<AlarmVO> getAlarmList(AlarmQueryDTO queryDTO) {
        List<AlarmVO> alarmList = alarmMapper.selectAlarmList(queryDTO);
        
        // 补充设备信息
        for (AlarmVO alarmVO : alarmList) {
            if (StringUtils.isNotEmpty(alarmVO.getSensorId())) {
                Device device = deviceService.selectDeviceBySensorId(alarmVO.getSensorId());
                if (device != null) {
                    // 由于Device类没有sensorName字段，使用sensorId作为传感器名称
                    alarmVO.setSensorName(alarmVO.getSensorId());
                    alarmVO.setInstallationLocation(device.getInstallationLocation());
                }
            }
        }
        
        return alarmList;
    }

    /**
     * 根据ID查询报警详情
     *
     * @param alarmId 报警ID
     * @return 报警详情
     */
    @Override
    public AlarmVO getAlarmById(Long alarmId) {
        AlarmVO alarmVO = alarmMapper.selectAlarmById(alarmId);
        
        // 补充设备信息
        if (alarmVO != null && StringUtils.isNotEmpty(alarmVO.getSensorId())) {
            Device device = deviceService.selectDeviceBySensorId(alarmVO.getSensorId());
            if (device != null) {
                // 由于Device类没有sensorName字段，使用sensorId作为传感器名称
                alarmVO.setSensorName(alarmVO.getSensorId());
                alarmVO.setInstallationLocation(device.getInstallationLocation());
            }
        }
        
        return alarmVO;
    }

    /**
     * 处理报警（如消音）
     *
     * @param alarmId 报警ID
     * @param handleUser 处理人
     * @return 处理结果
     */
    @Override
    @Transactional
    public Boolean handleAlarm(Long alarmId, String handleUser) {
        // 先查询报警记录是否存在
        AlarmLog alarmLog = new AlarmLog();
        alarmLog.setId(alarmId);
        alarmLog.setHandleStatus(1); // 已处理
        alarmLog.setHandleTime(new Date());
        alarmLog.setHandleUser(handleUser);

        int result = alarmMapper.updateAlarm(alarmLog);
        if (result > 0) {
            log.info("处理报警成功，报警ID：{}，处理人：{}", alarmId, handleUser);
            return true;
        } else {
            log.error("处理报警失败，报警ID：{}", alarmId);
            return false;
        }
    }

    /**
     * 根据传感器ID查询报警记录
     *
     * @param sensorId 传感器ID
     * @param limit 查询数量
     * @return 报警记录列表
     */
    @Override
    public List<AlarmLog> getAlarmsBySensorId(String sensorId, Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10; // 默认查询10条
        }
        return alarmMapper.selectLatestAlarmsBySensorId(sensorId, limit);
    }
}
