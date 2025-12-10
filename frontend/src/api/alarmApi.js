import request from '@/utils/request'

// 报警列表
export function listAlarms(query) {
  return request({
    url: '/api/alarm/list',
    method: 'get',
    params: query
  })
}

// 分页查询报警列表
export function getAlarmList(query) {
  return request({
    url: '/api/alarm/list',
    method: 'get',
    params: query
  })
}

// 获取报警详情
export function getAlarmInfo(alarmId) {
  return request({
    url: `/api/alarm/info/${alarmId}`,
    method: 'get'
  })
}

// 处理报警
export function handleAlarm(alarmId, handleRemark) {
  return request({
    url: `/api/alarm/handle`,
    method: 'post',
    data: {
      alarmId,
      handleRemark
    }
  })
}

// 报警确认/消音
export function ackAlarm(data) {
  return request({
    url: '/api/alarm/ack',
    method: 'post',
    data
  })
}

