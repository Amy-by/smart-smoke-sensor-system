<template>
  <div class="app-container">
    <el-card>
      <template #header>传感器列表</template>
      <div class="mb-12">
        <el-space>
          <el-input v-model="query.sensorId" placeholder="IMEI" clearable />
          <el-button type="primary" :loading="loading" @click="loadData">刷新</el-button>
        </el-space>
      </div>
      <el-table :data="list" border size="small" style="width: 100%">
        <el-table-column prop="sensorId" label="IMEI" min-width="160" />
        <el-table-column prop="installationLocation" label="安装位置" min-width="120" />
        <el-table-column prop="status" label="状态" min-width="100">
          <template #default="{ row }">
            <el-tag :type="statusTag(row.status)">{{ row.status === '1' ? '已激活' : '未激活' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" min-width="160" />
        <el-table-column prop="updateTime" label="更新时间" min-width="160" />
        <el-table-column label="操作" min-width="120">
          <template #default="{ row }">
            <el-button type="primary" text size="small" @click="toDetail(row.sensorId)">详情</el-button>
          </template>
        </el-table-column>
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
import { useRouter } from 'vue-router'
import { listSensors } from '@/api/deviceApi'

const router = useRouter()
const loading = ref(false)
const list = ref([])
const query = ref({ sensorId: '' })
const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

const statusTag = (status) => {
  return status === '1' ? 'success' : 'info'
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await listSensors({
      ...query.value,
      pageNum: pagination.currentPage,
      pageSize: pagination.pageSize
    })
    if (res && res.rows) {
      list.value = res.rows
      pagination.total = res.total
    }
  } catch (e) {
    console.error('获取设备列表失败', e)
    list.value = []
  } finally {
    loading.value = false
  }
}

const toDetail = (sensorId) => {
  router.push({
    path: `/iot/device/detail/${sensorId}`
  })
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

