import request from '@/utils/request'

// 额度查询
export function getQuota(sensorId) {
  return request({
    url: '/api/notification/quota',
    method: 'get',
    params: { sensorId }
  })
}

// 额度调整
export function updateQuota(data) {
  return request({
    url: '/api/notification/updateQuota',
    method: 'post',
    data
  })
}

// 保存通知模板
export function saveNotifyTemplate(data) {
  return request({
    url: '/api/notification/template/save',
    method: 'post',
    data
  })
}

// 获取通知模板列表
export function getNotifyTemplateList() {
  return request({
    url: '/api/notification/template/list',
    method: 'get'
  })
}

// 保存通知配置
export function saveNotifyConfig(data) {
  return request({
    url: '/api/notification/config/save',
    method: 'post',
    data
  })
}

// 获取通知配置
export function getNotifyConfig(sensorId) {
  return request({
    url: '/api/notification/config/get',
    method: 'get',
    params: { sensorId }
  })
}

