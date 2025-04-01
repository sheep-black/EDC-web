<template>
  <div>
    <div class="page-container">
      <!-- 使用导航栏组件 -->
      <HeaderNav />
      <el-main class="SearchAOP-main">
        <p style="font-size: 40px;
                  margin-left: 6px;
                  font-weight: bold;
                  justify-content: center;
                  display: flex;
                  color: #1e1a1a;
                  text-shadow: 2px 2px 2px #ffcc66;"
                  >
          EDC-GECs  Search
        </p>
        <p style="font-size: 16px;
          margin-top: -10px;
          margin-left: 20%;
          width: 65%;
          color: #000000;
          text-align: center;">
          Search the prediction results from over about 230,000 globally existing chemicals (GECs) that were collected from three inventories, including the Inventory of Existing Chemical Substances of China (IECSC), the Toxic Substances Control Act (TSCA) chemical substance inventory of the US, and the Classification and Labelling (C&L) inventory of the EU.
        </p>
        <el-link :underline="false" type="primary"
                 @click="DownloadPredictResult('230000result.zip')"
                 style="justify-content: center;display: flex;margin-top: -10px; padding-bottom: 30px">
          <el-icon size="20"><Memo /></el-icon>
          <p style="font-size: 14px;
                        text-align: justify; /* 文本两端对齐 */
                        line-height: 1%; /* 设置行高 */
                        ">
            click here to download 23w substances predict result (zip)
          </p>
          <el-icon size="20"><Memo /></el-icon>
        </el-link>
        <el-radio-group fill="#ffcc66" v-model="selected" text-color="#1e1a1a"
                        style="font-weight: bold;justify-content: center;display: flex;">
          <el-radio-button label="qualitative_model" value="qualitative_model" size="large" />
          <el-radio-button label="quantitative_model" value="quantitative_model" size="large" />
        </el-radio-group>
        <el-divider />
        <component :is="selectedComponent" />
      </el-main>

    </div>
    <FooterNav />

  </div>

</template>

<script setup>

import {ref, reactive, watch, onMounted, computed} from "vue";
import axios from "axios";
import {InfoFilled, Memo, Search} from "@element-plus/icons-vue";
import router from "@/router/index.js";
import SearchDXresult from "@/components/SearchPage/SearchDXresult.vue";
import SearchDLresult from "@/components/SearchPage/SearchDLresult.vue";
import HeaderNav from "@/components/HeaderNav.vue";
import FooterNav from "@/components/FooterNav.vue";

const tableData = reactive([]);
const activeIndex = ref('1-3')
const selected = ref('qualitative_model')
const pageSize = ref(20);
const loading = ref(true);
const currentPageData = ref(tableData.slice(0, pageSize.value));

const selectedComponent = computed(() => {
  if (selected.value === 'qualitative_model') {
    return SearchDXresult;
  } else{
    return SearchDLresult;
  }
});
const DownloadPredictResult = (filename) => {
  console.info('文件名:', filename);

  // 使用axios发送请求，它会自动包含认证头
  axios({
    url: `/download?fileName=${filename}`,
    method: 'GET',
    responseType: 'blob', // 重要，指定响应类型为二进制数据
  }).then((response) => {
    // 创建一个blob链接并点击下载
    const url = window.URL.createObjectURL(new Blob([response.data]));
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', filename);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }).catch(error => {
    console.error("下载文件出错:", error);
  });
};


</script>

<style scoped>
/* 使用 ::v-deep 选择器穿透 Scoped CSS 限制 */
:deep(.el-radio__input.is-checked .el-radio__inner) {
  border-color: #ffcc66;
  background-color: #ffcc66;
}

:deep(.el-radio__input.is-checked + .el-radio__label) {
  color: #0c0c0c; /* 修改文字颜色 */
  font-weight: bold;
}

/* 修改 hover 和 focus 状态的颜色 */
:deep(.el-radio__input:hover .el-radio__inner),
:deep(.el-radio__input:focus .el-radio__inner) {
  border-color: #ffcc66;
}
/* 覆盖 el-pagination 的样式 */

:deep(.el-pagination .el-pager li:not(.is-active):hover) {
  color: #ffcc66; /* 悬停文本颜色 */
}
:deep(.el-pagination .el-pager li.is-active) {
  color: #ffcc66; /* 活动分页按钮文本颜色 */
}


.flex-grow {
  flex-grow: 1;
}
.SearchAOP-main{
  /* 设置图片作为背景 */
  background-image: url('../../assets/back.png');
  /* 背景设置为覆盖整个容器 */
  min-width: 1080px;
  min-height: 80vh;
  background-size: cover;
  background-position: center;
  height: 90vh;
/* //height: 500px; 根据需要设置高度 */
}

.footer {
  bottom: 0;
  min-width: 1080px;
  margin-left: -8px;
  margin-right: -8px;
  background-color: #3a3b3d;
  padding: 15px 0;
  text-align: center;
}

.footer-content {
  display: flex;
  justify-content: space-around;
  max-width: 1200px;
  margin: 0 auto;
}

.footer-section {
  flex: 1;
}

.footer-section h3 {
  font-size: 18px;
  margin-bottom: 10px;
}

.footer-section p {
  margin: 5px 0;
}

.footer-section ul {
  list-style-type: none;
  padding: 0;
}

.footer-section ul li {
  margin-bottom: 5px;
}

.footer-section ul li a {
  color: #ffffff;
  text-decoration: none;
}

.footer-section ul li a:hover {
  text-decoration: underline;
}


</style>
