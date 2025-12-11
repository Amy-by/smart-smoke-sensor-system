<template>
  <div class="app-container">
    <el-card>
      <template #header>数据可视化</template>
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
          <el-select v-model="chartType" placeholder="图表类型">
            <el-option label="折线图" value="line" />
            <el-option label="柱状图" value="bar" />
          </el-select>
          <el-button type="primary" :loading="loading" @click="loadChartData">生成图表</el-button>
        </el-space>
      </div>
      
      <div v-if="chartData.length > 0" class="chart-container">
        <div ref="chartRef" class="chart" style="width: 100%; height: 400px;"></div>
      </div>
      
      <div v-else-if="!loading" class="no-data">
        <el-empty description="暂无数据可展示" />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { querySensorData } from '@/api/sensorDataApi'
import * as echarts from 'echarts'

const loading = ref(false)
const query = ref({ deviceId: '' })
const dateRange = ref([])
const chartType = ref('line')
const chartData = ref([])
const chartRef = ref(null)
let chartInstance = null

const initChart = () => {
  if (chartRef.value) {
    chartInstance = echarts.init(chartRef.value)
  }
}

const updateChart = () => {
  if (!chartInstance || chartData.value.length === 0) {
    return
  }
  
  // 提取时间和数值
  const times = chartData.value.map(item => item.collectTime)
  const values = chartData.value.map(item => item.sensorValue)
  
  // 配置图表选项
  const option = {
    title: {
      text: '传感器数据趋势',
      left: 'center'
    },
    tooltip: {
      trigger: 'axis',
      formatter: '{b}<br/>传感器值: {c}'
    },
    xAxis: {
      type: 'category',
      data: times,
      axisLabel: {
        rotate: 45,
        fontSize: 12
      }
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        data: values,
        type: chartType.value,
        smooth: true,
        itemStyle: {
          color: '#409EFF'
        },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(64, 158, 255, 0.5)' },
            { offset: 1, color: 'rgba(64, 158, 255, 0.1)' }
          ])
        }
      }
    ]
  }
  
  chartInstance.setOption(option)
}

const loadChartData = async () => {
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
      endTime: dateRange.value[1],
      pageNum: 1,
      pageSize: 1000 // 获取足够的数据用于图表展示
    }
    
    const res = await querySensorData(params)
    if (res && res.rows) {
      chartData.value = res.rows
      // 按时间排序
      chartData.value.sort((a, b) => new Date(a.collectTime) - new Date(b.collectTime))
      updateChart()
    }
  } catch (e) {
    console.error('获取图表数据失败', e)
    ElMessage.error('获取图表数据失败')
    chartData.value = []
  } finally {
    loading.value = false
  }
}

// 监听图表类型变化
watch(chartType, () => {
  updateChart()
})

// 监听窗口大小变化，自适应图表
window.addEventListener('resize', () => {
  if (chartInstance) {
    chartInstance.resize()
  }
})

onMounted(() => {
  initChart()
})
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

.chart-container {
  margin-top: 20px;
}

.chart {
  margin: 0 auto;
}

.no-data {
  margin-top: 40px;
}
</style>
