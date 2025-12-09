import request from '@/utils/request'

// 设备列表
export function listSensors(query) {
  return request({
    url: '/iot/device/list',
    method: 'get',
    params: query
  })
}

// 设备详情查询
export function getSensorParams(sensorId) {
  return request({
    url: `/iot/device/${sensorId}`,
    method: 'get'
  })
}

// 设备状态查询
export function getDeviceStatus(sensorId) {
  return request({
    url: `/iot/device/status/${sensorId}`,
    method: 'get'
  })
}

// 批量导入传感器（Excel）
export function importSensors(formData) {
  return request({
    url: '/api/iot/device/batchImport',
    method: 'post',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: formData
  })
}

