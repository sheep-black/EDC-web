<template>
  <div style="display: flex;justify-content: center;">
    <el-upload
        drag
        action="https://run.mocky.io/v3/9d059bf9-4660-45f2-925d-ce80ad6c4d15"
        multiple
    >
      <el-icon class="el-icon--upload" style="width: 600px"><upload-filled /></el-icon>
      <div class="el-upload__text">
        Drop file here or <em>click to upload</em>
      </div>
    </el-upload>
  </div>
  <div style="display: flex;justify-content: center;">
    <el-table :data="tableData" style="width:50%">
      <el-table-column prop="submitTime" label="提交时间" />
      <el-table-column prop="fileName" label="文件名" />
      <el-table-column prop="predictionStatus" label="预测状态">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.predictionStatus)">
            {{ row.predictionStatus }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="下载">
        <template #default="{ row }">
          <el-button
              type="primary"
              :disabled="row.predictionStatus !== '预测完成'"
              @click="downloadFile(row.fileName)"
          >
            下载
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { UploadFilled } from '@element-plus/icons-vue'

const tableData = ref([
  { submitTime: '2024-08-01 10:00', fileName: 'file1.txt', predictionStatus: '预测中' },
  { submitTime: '2024-08-02 11:00', fileName: 'file2.txt', predictionStatus: '预测失败' },
  { submitTime: '2024-08-03 12:00', fileName: 'file3.txt', predictionStatus: '预测完成' },
]);

const getStatusType = (status) => {
  switch (status) {
    case '预测中':
      return 'info'; // 蓝色
    case '预测失败':
      return 'danger'; // 红色
    case '预测完成':
      return 'success'; // 绿色
    default:
      return '';
  }
};

const downloadFile = (fileName) => {
  // 处理文件下载的逻辑
  console.log(`Downloading ${fileName}`);
  // 这里可以添加实际的下载逻辑
};
</script>

<style scoped>
.el-table {
  margin-top: 20px;
}
</style>
