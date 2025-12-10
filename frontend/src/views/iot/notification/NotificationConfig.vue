<template>
  <div class="app-container">
    <el-card>
      <template #header>通知配置</template>
      
      <!-- 通知模板配置 -->
      <el-card class="mb-16">
        <template #header>通知模板配置</template>
        <el-form :model="templateForm" :rules="templateRules" ref="templateFormRef" label-width="120px">
          <el-form-item label="模板类型" prop="templateType">
            <el-select v-model="templateForm.templateType" placeholder="请选择模板类型">
              <el-option label="短信" value="sms" />
              <el-option label="语音" value="voice" />
            </el-select>
          </el-form-item>
          <el-form-item label="模板名称" prop="templateName">
            <el-input v-model="templateForm.templateName" placeholder="输入模板名称" />
          </el-form-item>
          <el-form-item label="签名名称" prop="signName">
            <el-input v-model="templateForm.signName" placeholder="输入短信签名名称（仅短信模板需要）" />
          </el-form-item>
          <el-form-item label="模板代码/ID" prop="templateCode">
            <el-input v-model="templateForm.templateCode" placeholder="输入模板代码（短信）或模板ID（语音）" />
          </el-form-item>
          <el-form-item label="模板内容">
            <el-input v-model="templateForm.content" type="textarea" :rows="4" placeholder="输入模板内容" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :loading="templateLoading" @click="saveTemplate">保存模板</el-button>
            <el-button @click="resetTemplateForm">重置</el-button>
          </el-form-item>
        </el-form>
      </el-card>

      <!-- 通知方式配置 -->
      <el-card>
        <template #header>通知方式配置</template>
        <el-form :model="notifyForm" :rules="notifyRules" ref="notifyFormRef" label-width="120px">
          <el-form-item label="传感器 IMEI" prop="sensorId">
            <el-input v-model="notifyForm.sensorId" placeholder="输入传感器 IMEI" />
          </el-form-item>
          <el-form-item label="通知方式">
            <el-checkbox-group v-model="notifyForm.notifyTypes">
              <el-checkbox label="sms">短信</el-checkbox>
              <el-checkbox label="voice">语音</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item label="通知电话" prop="phone">
            <el-input v-model="notifyForm.phone" placeholder="输入接收通知的手机号码" />
          </el-form-item>
          <el-form-item label="通知优先级" prop="priority">
            <el-select v-model="notifyForm.priority" placeholder="选择通知优先级">
              <el-option label="最高" value="1" />
              <el-option label="高" value="2" />
              <el-option label="中" value="3" />
              <el-option label="低" value="4" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :loading="notifyLoading" @click="saveNotificationConfig">保存配置</el-button>
            <el-button @click="resetNotifyForm">重置</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { saveNotifyTemplate, saveNotifyConfig } from '@/api/notificationApi'

// 模板配置表单
const templateFormRef = ref(null)
const templateLoading = ref(false)
const templateForm = ref({
  templateType: 'sms',
  templateName: '',
  signName: '',
  templateCode: '',
  content: ''
})

const templateRules = ref({
  templateType: [{ required: true, message: '请选择模板类型', trigger: 'change' }],
  templateName: [{ required: true, message: '请输入模板名称', trigger: 'blur' }],
  templateCode: [{ required: true, message: '请输入模板代码/ID', trigger: 'blur' }]
})

// 通知方式配置表单
const notifyFormRef = ref(null)
const notifyLoading = ref(false)
const notifyForm = ref({
  sensorId: '',
  notifyTypes: [],
  phone: '',
  priority: '1'
})

const notifyRules = ref({
  sensorId: [
    { required: true, message: '请输入传感器 IMEI', trigger: 'blur' },
    { min: 15, max: 17, message: '传感器 IMEI 长度应为15-17位', trigger: 'blur' }
  ],
  notifyTypes: [{ required: true, message: '请至少选择一种通知方式', trigger: 'change' }],
  phone: [
    { required: true, message: '请输入通知电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  priority: [{ required: true, message: '请选择通知优先级', trigger: 'change' }]
})

// 保存模板
const saveTemplate = async () => {
  // 验证表单
  await templateFormRef.value?.validate()
  
  templateLoading.value = true
  try {
    await saveNotifyTemplate(templateForm.value)
    ElMessage.success('模板保存成功')
  } catch (e) {
    ElMessage.error('模板保存失败：' + (e?.message || '网络错误'))
    console.error('保存模板失败：', e)
  } finally {
    templateLoading.value = false
  }
}

// 重置模板表单
const resetTemplateForm = () => {
  templateFormRef.value?.resetFields()
}

// 保存通知配置
const saveNotificationConfig = async () => {
  // 验证表单
  await notifyFormRef.value?.validate()
  
  notifyLoading.value = true
  try {
    await saveNotifyConfig(notifyForm.value)
    ElMessage.success('通知配置保存成功')
  } catch (e) {
    ElMessage.error('通知配置保存失败：' + (e?.message || '网络错误'))
    console.error('保存通知配置失败：', e)
  } finally {
    notifyLoading.value = false
  }
}

// 重置通知配置表单
const resetNotifyForm = () => {
  notifyFormRef.value?.resetFields()
}
</script>

<style scoped>
.mb-16 {
  margin-bottom: 16px;
}
</style>