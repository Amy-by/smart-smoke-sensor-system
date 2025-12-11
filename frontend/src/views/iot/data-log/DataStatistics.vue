<template>
  <div class="app-container">
    <el-card>
      <template #header>数据统计</template>
      <div class="mb-12">
        <el-space>
          <el-input v-model="query.deviceId" placeholder="设备ID" clearable />
          <el-date-picker
            v-model="dateRange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
          <el-select v-model="query.statisticsType" placeholder="统计类型" clearable>
            <el-option label="平均值" value="AVG" />
            <el-option label="最大值" value="MAX" />
            <el-option label="最小值" value="MIN" />
          </el-select>
          <el-button type="primary" :loading="loading" @click="loadStatistics">统计</el-button>
        </el-space>
      </div>
      
      <div v-if="statisticsData" class="statistics-result">
        <el-card shadow="hover" class="mb-12">
          <template #header>统计结果</template>
          <div class="result-grid">
            <div class="result-item">
              <span class="result-label">设备ID</span>
              <span class="result-value">{{ statisticsData.deviceId }}</span>
            </div>
            <div class="result-item">
              <span class="result-label">统计类型</span>
              <span class="result-value">{{ getStatisticsTypeName(statisticsData.statisticsType) }}</span>
            </div>
            <div class="result-item">
              <span class="result-label">统计值</span>
              <span class="result-value">{{ statisticsData.value }}</span>
            </div>
            <div class="result-item">
              <span class="result-label">数据条数</span>
              <span class="result-value">{{ statisticsData.count }}</span>
            </div>
            <div class="result-item">
              <span class="result-label">开始时间</span>
              <span class="result-value">{{ statisticsData.startTime }}</span>
            </div>
            <div class="result-item">
              <span class="result-label">结束时间</span>
              <span class="result-value">{{ statisticsData.endTime }}</span>
            </div>
          </div>
        </el-card>
      </div>
      
      <div v-else-if="!loading" class="no-data">
        <el-empty description="暂无统计数据" />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { getStatistics } from '@/api/sensorDataApi'

const loading = ref(false)
const query = ref({ 
  deviceId: '', 
  statisticsType: 'AVG' 
})
const dateRange = ref([])
const statisticsData = ref(null)

const getStatisticsTypeName = (type) => {
  switch (type) {
    case 'AVG':
      return '平均值'
    case 'MAX':
      return '最大值'
    case 'MIN':
      return '最小值'
    default:
      return type
  }
}

const loadStatistics = async () => {
  if (!query.value.deviceId) {
    ElMessage.warning('请输入设备ID')
    return
  }
  
  if (!dateRange.value || dateRange.value.length !== 2) {
    ElMessage.warning('请选择时间范围')
    return
  }
  
  loading.value = true
  try {
    // 构建查询参数
    const params = {
      ...query.value,
      startTime: dateRange.value[0],
      endTime: dateRange.value[1]
    }
    
    const res = await getStatistics(params)
    if (res) {
      statisticsData.value = res
    }
  } catch (e) {
    console.error('获取统计数据失败', e)
    ElMessage.error('获取统计数据失败')
    statisticsData.value = null
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.mb-16 {
  margin-bottom: 16px;
}
.mb-12 {
  margin-bottom: 12px;
}
.mt-12 {
  margin-top: 12px;
}

.statistics-result {
  margin-top: 20px;
}

.result-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
}

.result-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 10px;
}

.result-label {
  font-size: 14px;
  color: #606266;
  margin-bottom: 5px;
}

.result-value {
  font-size: 24px;
  font-weight: bold;
  color: #409EFF;
}

.no-data {
  margin-top: 40px;
}
</style>
