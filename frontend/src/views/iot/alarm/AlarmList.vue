<template>
  <div class="app-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>报警记录管理</span>
        </div>
      </template>
      
      <!-- 查询表单 -->
      <el-form :model="queryParams" ref="queryFormRef" :inline="true" size="small" class="query-form">
        <el-form-item label="传感器ID">
          <el-input v-model="queryParams.sensorId" placeholder="请输入传感器ID" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item label="报警类型">
          <el-select v-model="queryParams.alarmType" placeholder="请选择报警类型" clearable style="width: 150px">
            <el-option label="火灾" value="火灾" />
            <el-option label="烟雾" value="烟雾" />
            <el-option label="温度" value="温度" />
          </el-select>
        </el-form-item>
        <el-form-item label="处理状态">
          <el-select v-model="queryParams.handleStatus" placeholder="请选择处理状态" clearable style="width: 120px">
            <el-option label="未处理" value="0" />
            <el-option label="已处理" value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 300px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleQuery">查询</el-button>
          <el-button icon="RefreshRight" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
      
      <!-- 表格 -->
      <el-table v-loading="loading" :data="alarmList" border size="small" style="width: 100%">
        <el-table-column type="index" label="序号" width="50" />
        <el-table-column prop="sensorId" label="传感器ID" min-width="160" />
        <el-table-column prop="alarmType" label="报警类型" min-width="100">
          <template #default="{ row }">
            <el-tag :type="row.alarmType === '火灾' ? 'danger' : 'warning'">
              {{ row.alarmType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="alarmLevel" label="报警等级" min-width="100">
          <template #default="{ row }">
            <el-tag :type="getAlarmLevelType(row.alarmLevel)">
              {{ getAlarmLevelName(row.alarmLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="alarmValue" label="触发值" min-width="100" />
        <el-table-column prop="alarmTime" label="报警时间" min-width="160" />
        <el-table-column prop="handleStatus" label="处理状态" min-width="100">
          <template #default="{ row }">
            <el-tag :type="row.handleStatus === 0 ? 'warning' : 'success'">
              {{ row.handleStatus === 0 ? '未处理' : '已处理' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="handler" label="处理人" min-width="100" />
        <el-table-column prop="handleTime" label="处理时间" min-width="160" />
        <el-table-column prop="handleRemark" label="处理备注" min-width="150" />
        <el-table-column label="操作" min-width="150" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="primary" text @click="showHandleDialog(row)" :disabled="row.handleStatus === 1">
              处理
            </el-button>
            <el-button size="small" type="info" text @click="getAlarmDetail(row)" :disabled="false">
              详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.currentPage"
          v-model:page-size="pagination.pageSize"
          :total="pagination.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
      
      <!-- 处理报警对话框 -->
      <el-dialog v-model="dialogVisible" title="处理报警" width="500px" destroy-on-close>
        <el-form ref="alarmFormRef" :model="alarmForm" label-width="100px">
          <el-form-item label="传感器ID">
            <el-input v-model="alarmForm.sensorId" disabled />
          </el-form-item>
          <el-form-item label="报警类型">
            <el-input v-model="alarmForm.alarmType" disabled />
          </el-form-item>
          <el-form-item label="报警时间">
            <el-input v-model="alarmForm.alarmTime" disabled />
          </el-form-item>
          <el-form-item label="触发值">
            <el-input v-model="alarmForm.alarmValue" disabled />
          </el-form-item>
          <el-form-item label="处理备注" prop="handleRemark">
            <el-input
              v-model="alarmForm.handleRemark"
              type="textarea"
              :rows="3"
              placeholder="请输入处理备注"
            />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveHandleAlarm">保存</el-button>
        </template>
      </el-dialog>
      
      <!-- 报警详情对话框 -->
      <el-dialog v-model="detailVisible" title="报警详情" width="600px" destroy-on-close>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="传感器ID">{{ detailForm.sensorId }}</el-descriptions-item>
          <el-descriptions-item label="报警类型">{{ detailForm.alarmType }}</el-descriptions-item>
          <el-descriptions-item label="报警等级">{{ getAlarmLevelName(detailForm.alarmLevel) }}</el-descriptions-item>
          <el-descriptions-item label="触发值">{{ detailForm.alarmValue }}</el-descriptions-item>
          <el-descriptions-item label="报警时间">{{ detailForm.alarmTime }}</el-descriptions-item>
          <el-descriptions-item label="处理状态">{{ detailForm.handleStatus === 0 ? '未处理' : '已处理' }}</el-descriptions-item>
          <el-descriptions-item label="处理人">{{ detailForm.handler }}</el-descriptions-item>
          <el-descriptions-item label="处理时间">{{ detailForm.handleTime }}</el-descriptions-item>
          <el-descriptions-item label="处理备注" :span="2">{{ detailForm.handleRemark }}</el-descriptions-item>
          <el-descriptions-item label="描述" :span="2">{{ detailForm.description }}</el-descriptions-item>
        </el-descriptions>
        <template #footer>
          <el-button @click="detailVisible = false">关闭</el-button>
        </template>
      </el-dialog>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { getAlarmList, handleAlarm, getAlarmInfo } from '@/api/alarmApi'

// 表格数据
const loading = ref(false)
const alarmList = ref([])

// 查询参数
const queryParams = reactive({
  sensorId: '',
  alarmType: '',
  handleStatus: '',
  startTime: '',
  endTime: ''
})

// 日期范围
const dateRange = ref([])

// 监听日期范围变化
watch(dateRange, (newVal) => {
  if (newVal && newVal.length === 2) {
    queryParams.startTime = newVal[0]
    queryParams.endTime = newVal[1]
  } else {
    queryParams.startTime = ''
    queryParams.endTime = ''
  }
})

// 分页信息
const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

// 处理对话框
const dialogVisible = ref(false)
const alarmFormRef = ref(null)
const alarmForm = reactive({
  alarmId: null,
  sensorId: '',
  alarmType: '',
  alarmTime: '',
  alarmValue: '',
  handleRemark: ''
})

// 详情对话框
const detailVisible = ref(false)
const detailForm = reactive({
  alarmId: null,
  sensorId: '',
  alarmType: '',
  alarmLevel: '',
  alarmValue: '',
  alarmTime: '',
  handleStatus: 0,
  handler: '',
  handleTime: '',
  handleRemark: '',
  description: ''
})

// 初始化
onMounted(() => {
  getList()
})

// 获取报警列表
async function getList() {
  loading.value = true
  try {
    const params = {
      ...queryParams,
      pageNum: pagination.currentPage,
      pageSize: pagination.pageSize
    }
    const { data } = await getAlarmList(params)
    alarmList.value = data.rows
    pagination.total = data.total
  } catch (error) {
    ElMessage.error('获取报警列表失败')
    console.error('获取报警列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 查询
function handleQuery() {
  pagination.currentPage = 1
  getList()
}

// 重置
function resetQuery() {
  Object.assign(queryParams, {
    sensorId: '',
    alarmType: '',
    handleStatus: '',
    startTime: '',
    endTime: ''
  })
  dateRange.value = []
  pagination.currentPage = 1
  getList()
}

// 分页大小变化
function handleSizeChange(val) {
  pagination.pageSize = val
  pagination.currentPage = 1
  getList()
}

// 当前页变化
function handleCurrentChange(val) {
  pagination.currentPage = val
  getList()
}

// 显示处理报警对话框
function showHandleDialog(row) {
  Object.assign(alarmForm, {
    alarmId: row.alarmId,
    sensorId: row.sensorId,
    alarmType: row.alarmType,
    alarmTime: row.alarmTime,
    alarmValue: row.alarmValue,
    handleRemark: ''
  })
  dialogVisible.value = true
}

// 保存处理结果
async function saveHandleAlarm() {
  try {
    await handleAlarm(alarmForm.alarmId, alarmForm.handleRemark)
    ElMessage.success('处理成功')
    dialogVisible.value = false
    getList()
  } catch (error) {
    ElMessage.error('处理失败')
    console.error('处理报警失败:', error)
  }
}

// 获取报警详情
async function getAlarmDetail(row) {
  try {
    const { data } = await getAlarmInfo(row.alarmId)
    Object.assign(detailForm, data)
    detailVisible.value = true
  } catch (error) {
    ElMessage.error('获取报警详情失败')
    console.error('获取报警详情失败:', error)
  }
}

// 获取报警等级类型
function getAlarmLevelType(level) {
  switch (level) {
    case 1:
      return 'danger'
    case 2:
      return 'warning'
    case 3:
      return 'info'
    default:
      return 'default'
  }
}

// 获取报警等级名称
function getAlarmLevelName(level) {
  switch (level) {
    case 1:
      return '一级报警'
    case 2:
      return '二级报警'
    case 3:
      return '三级报警'
    default:
      return '未知等级'
  }
}
</script>

<style scoped>
.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.query-form {
  margin-bottom: 16px;
}
</style>