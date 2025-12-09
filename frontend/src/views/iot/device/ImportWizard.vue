<template>
  <div class="app-container">
    <el-card>
      <template #header>批量导入传感器</template>
      <el-alert
        type="info"
        :closable="false"
        show-icon
        description="上传 Excel 后将批量导入传感器数据到系统中。"
        class="mb-16"
      />
      <el-upload
        drag
        :auto-upload="false"
        :on-change="onFileChange"
        :limit="1"
        accept=".xls,.xlsx"
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">拖拽或点击上传 Excel</div>
      </el-upload>
      <div class="mt-16">
        <el-button type="primary" :loading="uploading" :disabled="!file" @click="submit">提交导入</el-button>
        <span v-if="file" class="ml-8">已选择：{{ file.name }}</span>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { importSensors } from '@/api/deviceApi'
import { ElMessage } from 'element-plus'

const file = ref(null)
const uploading = ref(false)

const onFileChange = (uploadFile) => {
  file.value = uploadFile.raw
}

const submit = async () => {
  if (!file.value) {
    ElMessage.warning('请选择要上传的Excel文件')
    return
  }
  uploading.value = true
  try {
    const formData = new FormData()
    formData.append('file', file.value)
    const response = await importSensors(formData)
    if (response.code === 200) {
      ElMessage.success(`成功导入 ${response.data} 个设备`)
      // 重置表单
      file.value = null
    } else {
      ElMessage.error(response.msg || '导入失败')
    }
  } catch (e) {
    console.error('导入失败:', e)
    ElMessage.error('导入失败，请检查文件格式和内容是否正确')
  } finally {
    uploading.value = false
  }
}
</script>

<style scoped>
.mb-16 {
  margin-bottom: 16px;
}
.mt-16 {
  margin-top: 16px;
}
.ml-8 {
  margin-left: 8px;
}
</style>

