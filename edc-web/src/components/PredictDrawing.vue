<script setup>
import { ref, onMounted } from 'vue';
import axios from "axios";

const loading = ref(true); // 定义一个响应式变量，用于控制加载状态
const ketcherSmiles = ref(''); // 定义一个响应式变量，用于存储 SMILES 字符串
const showTable = ref(false);
const updateSmiles = () => {
  const ketcherFrame = document.getElementById("katcher");
  const ketcher = ketcherFrame.contentWindow.ketcher;
  ketcher.getSmiles().then(
      (res) => {
        ketcherSmiles.value = res;
        console.log("ketcherSmiles", ketcherSmiles.value);
      }).catch((e) => {
    console.log(e);
  });
};
const Predict = async () => {
  const ketcherFrame = document.getElementById("katcher");
  const ketcher = ketcherFrame.contentWindow.ketcher;
  ketcher.getSmiles().then(
      async (res) => {
        ketcherSmiles.value = res;
        console.log("ketcherSmiles", ketcherSmiles.value);
        const keyword = encodeURIComponent(ketcherSmiles.value)
        try {
          const response = await axios.get(`/Predict?input=${keyword}`);
          console.info('response:', response.data.result);
        } catch (error) {
          console.error('Error searching:', error);
        } finally {
          showTable.value = true;
        }
      }).catch((e) => {
    console.log(e);
  });

};
onMounted(() => {
  const ketcherFrame = document.getElementById("katcher"); // 获取包含 Ketcher 的 iframe 元素
  ketcherFrame.onload = () => {
    const checkKetcherLoaded = setInterval(() => {
      const ketcher = ketcherFrame.contentWindow.ketcher; // 获取 Ketcher 实例
      if (ketcher && typeof ketcher.getSmiles === 'function') {
        clearInterval(checkKetcherLoaded); // 停止检查
        window.scrollTo(0, 0);
        loading.value = false;
      }
    }, 100); // 每100ms检查一次
  };
});
</script>

<template>
  <div v-loading="loading" style="justify-content: center;display: flex;">
    <iframe id="katcher" src="/ketcher/standalone/index.html" style="width: 70%; min-height: 500px; border: none;"></iframe>
  </div>
  <el-divider>
    <el-icon><star-filled /></el-icon>
    <el-icon><star-filled /></el-icon>
    <el-icon><star-filled /></el-icon>
  </el-divider>
  <div style=" display: flex;justify-content: center;">
    <el-popover
        placement="top"
        title="Tips:"
        :width="400"
        trigger="hover"
        content="Before making predictions, please ensure the structure is correct by using the 'Check Structure' button in the menu bar above."
    >
      <template #reference>
        <el-button type="primary" round size="large" @click="Predict">
          <p style="font-size: 16px">
            Click to predict
          </p>
        </el-button>
      </template>
    </el-popover>
  </div>
  <p v-if="showTable" style="font-size: 20px;
                  margin-top: 30px;
                  margin-left: 20%;
                  font-weight: bold;
                  display: flex;
                  color: #1B497BFF;">
    Predict Results
  </p>
  <div style="justify-content: center;display: flex; margin-top: 10px;">
    <el-table
        :data="tableData"
        height="300"
        style="width: 70%"
        :header-cell-style="{ background: '#dedede', color: '#000' }"
        v-if="showTable">
      <el-table-column prop="Assay ID" label="ID" align="center" >
        <template #default="scope">
          <el-button type="text" @click="handleRowClick(scope.row)">{{ scope.row.ID}}</el-button>
        </template>
      </el-table-column>
      <el-table-column prop="date" label="Date" width="180" align="center" />
      <el-table-column prop="name" label="Name" width="180" align="center" />
      <el-table-column prop="address" label="Address" align="center" />
    </el-table>
    <el-dialog
        v-model="showDialog"
        title="Tips"
        width="500"
        :before-close="handleClose"
    >
      <div style="justify-content: center;display: flex;">
        <el-image style="width: 600px;" :src=Result_image  />
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="showDialog = false">Cancel</el-button>
          <el-button type="primary" @click="showDialog = false">
            Confirm
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>

</template>

<style scoped>
html, body {
  height: 100%;
  overflow: hidden;
}
</style>
