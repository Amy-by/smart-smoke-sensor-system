<template>
  <div class="app-container">
    <el-card>
      <template #header>传感器详情</template>
      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="6" animated />
      </div>
      <div v-else>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="IMEI">{{ deviceInfo.sensorId }}</el-descriptions-item>
          <el-descriptions-item label="安装位置">{{ deviceInfo.installationLocation }}</el-descriptions-item>
          <el-descriptions-item label="设备状态">
            <el-tag :type="deviceInfo.status === '1' ? 'success' : 'info'">
              {{ deviceInfo.status === '1' ? '已激活' : '未激活' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="在线状态">
            <el-tag :type="deviceStatus.onlineStatus === 1 ? 'success' : 'danger'">
              {{ deviceStatus.onlineStatus === 1 ? '在线' : '离线' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ deviceInfo.createTime }}</el-descriptions-item>
          <el-descriptions-item label="更新时间">{{ deviceInfo.updateTime }}</el-descriptions-item>
        </el-descriptions>
        <el-divider />
        <h4>实时状态数据</h4>
        <el-space class="mb-12">
          <span>烟雾浓度：{{ deviceStatus.smokeConcentration }}%</span>
          <span>温度：{{ deviceStatus.temperature }}℃</span>
          <span>湿度：{{ deviceStatus.humidity }}%</span>
          <span>电量：{{ deviceStatus.batteryLevel }}%</span>
          <span>信号强度：{{ deviceStatus.signalStrength }}dBm</span>
        </el-space>
        <el-divider />
        <h4>设备状态</h4>
        <el-tag :type="deviceStatus.deviceStatus === 0 ? 'success' : deviceStatus.deviceStatus === 1 ? 'danger' : 'warning'">
          {{ deviceStatus.deviceStatus === 0 ? '正常' : deviceStatus.deviceStatus === 1 ? '报警' : '故障' }}
        </el-tag>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { getSensorParams, getDeviceStatus } from '@/api/deviceApi'

const route = useRoute()
const loading = ref(false)
const deviceInfo = ref({
  sensorId: '',
  installationLocation: '',
  status: '',
  createTime: '',
  updateTime: ''
})
const deviceStatus = ref({
  smokeConcentration: 0,
  temperature: 0,
  humidity: 0,
  batteryLevel: 0,
  signalStrength: 0,
  onlineStatus: 0,
  deviceStatus: 0
})

const loadDeviceInfo = async () => {
  const sensorId = route.params.sensorId
  if (!sensorId) return
  
  loading.value = true
  try {
    // 获取设备基本信息
    const infoRes = await getSensorParams(sensorId)
    if (infoRes && infoRes.data) {
      deviceInfo.value = infoRes.data
    }
    
    // 获取设备状态信息
    const statusRes = await getDeviceStatus(sensorId)
    if (statusRes && statusRes.data) {
      deviceStatus.value = statusRes.data
    }
  } catch (e) {
    console.error('获取设备详情失败', e)
  } finally {
    loading.value = false
  }
}

// 监听路由参数变化，重新加载数据
watch(() => route.params.sensorId, () => {
  loadDeviceInfo()
})

// 初始加载数据
onMounted(() => {
  loadDeviceInfo()
})
</script>

<style scoped>
.mb-16 {
  margin-bottom: 16px;
}
.mb-12 {
  margin-bottom: 12px;
}
.loading-container {
  padding: 16px 0;
}
</style>

