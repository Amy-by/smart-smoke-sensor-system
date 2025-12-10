<template>
  <div class="app-container">
    <el-card>
      <template #header>通知额度管理</template>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="140px" class="quota-form">
        <el-form-item label="传感器 IMEI" prop="sensorId">
          <el-input v-model="form.sensorId" placeholder="输入传感器 IMEI" />
        </el-form-item>
        <el-form-item label="免费语音条数" prop="freeVoiceCount">
          <el-input-number v-model="form.freeVoiceCount" :min="0" />
        </el-form-item>
        <el-form-item label="免费短信条数" prop="freeSmsCount">
          <el-input-number v-model="form.freeSmsCount" :min="0" />
        </el-form-item>
        <el-form-item label="余额（元）" prop="balance">
          <el-input-number v-model="form.balance" :min="0" :step="0.1" :precision="2" />
        </el-form-item>
        <el-form-item>
          <el-space>
            <el-button type="primary" :loading="loading" @click="loadQuota">查询</el-button>
            <el-button type="success" :loading="saving" @click="saveQuota">调整</el-button>
          </el-space>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { getQuota, updateQuota } from '@/api/notificationApi'
import { ElMessage } from 'element-plus'

const formRef = ref(null)
const loading = ref(false)
const saving = ref(false)

const form = ref({
  sensorId: '',
  freeVoiceCount: 0,
  freeSmsCount: 0,
  balance: 0
})

const rules = ref({
  sensorId: [
    { required: true, message: '请输入传感器 IMEI', trigger: 'blur' },
    { min: 15, max: 17, message: '传感器 IMEI 长度应为15-17位', trigger: 'blur' }
  ]
})

const loadQuota = async () => {
  // 验证表单
  if (!form.value.sensorId) {
    ElMessage.warning('请先输入传感器 IMEI')
    return
  }

  loading.value = true
  try {
    const res = await getQuota(form.value.sensorId)
    if (res && res.code === 200 && res.data) {
      form.value.freeVoiceCount = res.data.freeVoiceCount || 0
      form.value.freeSmsCount = res.data.freeSmsCount || 0
      form.value.balance = res.data.balance || 0
      ElMessage.success('查询成功')
    } else {
      ElMessage.error(res?.msg || '查询失败')
    }
  } catch (e) {
    ElMessage.error('查询失败：' + (e?.message || '网络错误'))
    console.error('查询额度失败：', e)
  } finally {
    loading.value = false
  }
}

const saveQuota = async () => {
  // 验证表单
  if (!form.value.sensorId) {
    ElMessage.warning('请先输入传感器 IMEI')
    return
  }

  saving.value = true
  try {
    const res = await updateQuota({ ...form.value })
    if (res && res.code === 200) {
      ElMessage.success('调整成功')
    } else {
      ElMessage.error(res?.msg || '调整失败')
    }
  } catch (e) {
    ElMessage.error('调整失败：' + (e?.message || '网络错误'))
    console.error('调整额度失败：', e)
  } finally {
    saving.value = false
  }
}
</script>

<style scoped>
.mb-16 {
  margin-bottom: 16px;
}
.quota-form {
  max-width: 420px;
}
</style>

