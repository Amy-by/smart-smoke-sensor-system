<template>
  <div class="app-container">
    <el-card>
      <template #header>数据查询</template>
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
          <el-button type="primary" :loading="loading" @click="loadData">查询</el-button>
        </el-space>
      </div>
      <el-table :data="list" border size="small" style="width: 100%">
        <el-table-column prop="deviceId" label="设备ID" min-width="160" />
        <el-table-column prop="sensorValue" label="传感器值" min-width="120" />
        <el-table-column prop="status" label="状态" min-width="100">
          <template #default="{ row }">
            <el-tag :type="statusTag(row.status)">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="collectTime" label="采集时间" min-width="160" />
        <el-table-column prop="createTime" label="记录时间" min-width="160" />
      </el-table>
      <div class="mt-12">
        <el-pagination
          v-model:current-page="pagination.currentPage"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { querySensorData } from '@/api/sensorDataApi'

const loading = ref(false)
const list = ref([])
const query = ref({ deviceId: '' })
const dateRange = ref([])
const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

const statusTag = (status) => {
  switch (status) {
    case '正常':
      return 'success'
    case '异常':
      return 'warning'
    case '报警':
      return 'danger'
    default:
      return 'info'
  }
}

const loadData = async () => {
  loading.value = true
  try {
    // 构建查询参数
    const params = {
      ...query.value,
      pageNum: pagination.currentPage,
      pageSize: pagination.pageSize
    }
    
    // 添加时间范围
    if (dateRange.value && dateRange.value.length === 2) {
      params.startTime = dateRange.value[0]
      params.endTime = dateRange.value[1]
    }
    
    const res = await querySensorData(params)
    if (res && res.rows) {
      list.value = res.rows
      pagination.total = res.total
    }
  } catch (e) {
    console.error('获取传感器数据失败', e)
    list.value = []
  } finally {
    loading.value = false
  }
}

const handleSizeChange = (size) => {
  pagination.pageSize = size
  loadData()
}

const handleCurrentChange = (current) => {
  pagination.currentPage = current
  loadData()
}

// 初始加载数据
loadData()
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
</style>
