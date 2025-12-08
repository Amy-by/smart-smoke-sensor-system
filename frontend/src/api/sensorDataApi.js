import request from '@/utils/request'

// 查询传感器数据
export function querySensorData(query) {
  return request({
    url: '/api/sensor/data/query',
    method: 'get',
    params: query
  })
}

// 获取数据统计
export function getStatistics(query) {
  return request({
    url: '/api/sensor/data/statistics',
    method: 'get',
    params: query
  })
}

