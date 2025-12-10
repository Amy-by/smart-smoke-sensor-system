package com.iot.alarm.service.mapper;

import com.iot.alarm.domain.AlarmLog;
import com.iot.alarm.domain.AlarmQueryDTO;
import com.iot.alarm.domain.AlarmVO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 报警管理Mapper接口
 * 
 * @author IoT开发人员
 * @date 2025-12-11
 */
@Mapper
public interface AlarmMapper
{
    /**
     * 查询报警日志列表
     * 
     * @param queryDTO 查询条件
     * @return 报警日志集合
     */
    List<AlarmVO> selectAlarmList(AlarmQueryDTO queryDTO);

    /**
     * 通过ID查询报警日志
     * 
     * @param id 报警日志ID
     * @return 报警日志
     */
    AlarmVO selectAlarmById(Long id);

    /**
     * 新增报警日志
     * 
     * @param alarmLog 报警日志信息
     * @return 结果
     */
    int insertAlarm(AlarmLog alarmLog);

    /**
     * 修改报警日志
     * 
     * @param alarmLog 报警日志信息
     * @return 结果
     */
    int updateAlarm(AlarmLog alarmLog);

    /**
     * 删除报警日志
     * 
     * @param id 报警日志ID
     * @return 结果
     */
    int deleteAlarmById(Long id);

    /**
     * 批量删除报警日志
     * 
     * @param ids 需要删除的报警日志ID
     * @return 结果
     */
    int deleteAlarmByIds(Long[] ids);
    
    /**
     * 根据传感器ID查询报警记录
     * 
     * @param sensorId 传感器ID
     * @return 报警记录列表
     */
    List<AlarmLog> selectAlarmsBySensorId(String sensorId);
    
    /**
     * 根据传感器ID查询最近的报警记录
     * 
     * @param sensorId 传感器ID
     * @param limit 查询数量
     * @return 报警记录列表
     */
    List<AlarmLog> selectLatestAlarmsBySensorId(String sensorId, Integer limit);
}
