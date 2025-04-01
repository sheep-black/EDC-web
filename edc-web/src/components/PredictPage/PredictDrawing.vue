<script setup>
import { ref, onMounted } from 'vue';
import axios from "axios";
import router from '../../router/index.js'
const loading = ref(true); // 定义一个响应式变量，用于控制加载状态
const ketcherSmiles = ref(''); // 定义一个响应式变量，用于存储 SMILES 字符串
const ifAD=ref('noAD');
const Predict = async () => {
  const ketcherFrame = document.getElementById("katcher");
  const ketcher = ketcherFrame.contentWindow.ketcher;
  ketcher.getSmiles().then(
      async (res) => {
        ketcherSmiles.value = res;
        console.log("ketcherSmiles", ketcherSmiles.value);
        const keyword = encodeURIComponent(ketcherSmiles.value)
        router.push(`/PredictResult/${keyword}/${ifAD.value}`);
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
    <iframe id="katcher" src="/ketcher/standalone/index.html" style="width: 60%; min-height: 500px; border: none;"></iframe>
  </div>
  <div style=" display: flex;justify-content: center;margin-top: 5px;">
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
  <div style="justify-content: center;display: flex; margin-top: 5px;">
    <el-radio-group  v-model="ifAD">
      <el-radio label="AD">With AD image</el-radio>
      <el-radio label="noAD">No AD image</el-radio>
    </el-radio-group>
  </div>

</template>

<style scoped>
html, body {
  height: 100%;
  overflow: hidden;
}
</style>
