# FE-0001-设备API路径不匹配

## 1. 错误描述

### 错误标题
设备管理前端API调用与后端接口路径不匹配

### 错误现象
前端调用设备列表、设备详情等接口时返回404错误，无法获取设备数据

### 发生环境
- 操作系统版本：Windows 11
- 浏览器类型和版本：Chrome 120.0.6099.109
- 系统版本号：v1.0.0

### 复现步骤
1. 打开设备管理页面
2. 系统尝试加载设备列表数据
3. 浏览器控制台显示404错误
4. 设备列表显示为空

### 错误截图
无

## 2. 错误分析

### 错误定位
- 文件路径：/var/ctt_wkplace/iot/frontend/src/api/deviceApi.js
- 函数/方法名：listSensors、getSensorParams
- 行号：10-25

### 错误日志
```
GET http://localhost:8080/api/device/sensor/list 404 (Not Found)
GET http://localhost:8080/api/device/sensor/params 404 (Not Found)
```

### 可能原因
- 前端API调用路径与后端接口定义路径不一致
- 后端接口路径发生变更但前端未同步更新

### 影响范围
- 影响的功能模块：设备管理模块
- 影响的用户群体：所有使用设备管理功能的用户
- 影响的业务流程：设备列表查询、设备详情查看、设备参数配置

## 3. 解决方案

### 解决思路
- 更新前端API调用路径，使其与后端接口定义路径保持一致
- 修改设备管理相关组件的API调用方式

### 具体步骤
1. 查看后端DeviceController接口定义
2. 调整前端deviceApi.js中的接口路径
3. 更新SensorList.vue和SensorDetail.vue中的API调用

### 代码修改
```javascript
// 错误代码
const listSensors = () => request({
  url: '/api/device/sensor/list',
  method: 'get'
})

const getSensorParams = (sensorId) => request({
  url: `/api/device/sensor/params?sensorId=${sensorId}`,
  method: 'get'
})

// 修复后的代码
const listSensors = (query) => request({
  url: '/project/device/list',
  method: 'get',
  params: query
})

const getSensorParams = (sensorId) => request({
  url: `/project/device/${sensorId}`,
  method: 'get'
})
```

### 相关依赖
无

## 4. 验证结果

### 验证方法
- 手动测试设备管理页面功能
- 检查浏览器控制台是否有错误
- 验证设备数据是否正确展示

### 验证环境
- 操作系统版本：Windows 11
- 浏览器类型和版本：Chrome 120.0.6099.109
- 系统版本号：v1.0.0

### 验证结果
- 错误不再复现
- 设备列表能够正常加载
- 设备详情页面能够正确显示设备信息
- 其他相关功能正常

### 测试截图
无

## 5. 相关信息

- **记录人**：开发人员
- **记录时间**：2025-12-09 10:30:00
- **解决人**：开发人员
- **解决时间**：2025-12-09 11:00:00
- **严重程度**：中等
- **关联任务**：设备管理域开发
- **关联文档**：接口设计文档
- **备注**：无