<script setup>
import { Search } from '@element-plus/icons-vue'
import {nextTick, onMounted, reactive, ref, watch} from 'vue';
import axios from "axios";
import router from "@/router/index.js";

const SearchText = ref('');
const showTable = ref(false);
const pageSize = ref(20);
const currentPage = ref(1);
const loading = ref(true);
const total = ref(100); // 假设总数据量为 100
// 推荐搜索关键词列表
const tableData = reactive([]);
const handleSearch = async () => {
  currentPage.value=1
  const keyword = encodeURIComponent(SearchText.value)
  showTable.value = true;
  loading.value = true;
  try {
    const response = await axios.get(`/searchSMILESDL?smiles=${keyword}&pagenum=${currentPage.value-1}&pagesize=${pageSize.value}`);
    tableData.value = response.data.data;
    total.value=response.data.total;
    console.info("response.total",response)
  } catch (error) {
    console.error('Error searching:', error);
  } finally {
    loading.value = false;
  }
};

const handleCurrentChange = async (page) => {
  const keyword = encodeURIComponent(SearchText.value)
  showTable.value = true;
  loading.value = true;
  currentPage.value = page;
  try {
    const response = await axios.get(`/searchSMILESDL?smiles=${keyword}&pagenum=${currentPage.value - 1}&pagesize=${pageSize.value}`);
    tableData.value = response.data.data;
    total.value = response.data.total;
    console.info("response.total", response)
  } catch (error) {
    console.error('Error searching:', error);
  } finally {
    loading.value = false;
  }
};

onMounted(async () => {
  const keyword = ""
  showTable.value = true;
  loading.value = true;
  currentPage.value = 1;
  try {
    const response = await axios.get(`/searchSMILESDL?smiles=${keyword}&pagenum=${currentPage.value - 1}&pagesize=${pageSize.value}`);
    tableData.value = response.data.data;
    total.value = response.data.total;
    console.info("response.total", response)
  } catch (error) {
    console.error('Error searching:', error);
  } finally {
    loading.value = false;
  }
});
</script>

<template>
  <div style="justify-content: center;display: flex;">
    <el-input
        v-model="SearchText"
        style="max-width: 600px"
        size="large"
        placeholder="Please input keyword"
        @keyup.enter="handleSearch"
    >
      <template #append>
        <el-button :icon="Search" @click="handleSearch" />
      </template>
    </el-input>

  </div>

  <p v-if="showTable" style="font-size: 20px;
                  margin-top: 30px;
                  margin-left: 20%;
                  font-weight: bold;
                  display: flex;
                  color: #1B497BFF;">
    Search Results
  </p>
  <div style="justify-content: center;display: flex; margin-top: 10px;">
    <el-table v-if="showTable"
              v-loading="loading"
              stripe
              max-height="600"
              :data="tableData.value" border style="width: 1050px;"
              :header-cell-style="{ background: '#dedede', color: '#000' }">
      <el-table-column prop="id" label="ID" width="100"></el-table-column>
      <el-table-column prop="smiles" label="SMILES" width="500"></el-table-column>
      <el-table-column prop="sAOP" label="Sensitive AOP" width="150"></el-table-column>
      <el-table-column prop="sAO" label="Sensitive AO" width="150"></el-table-column>
      <el-table-column prop="sNOAEL" label="Sensitive NOAEL" width="150"></el-table-column>
    </el-table>
  </div>
  <el-pagination
      style="justify-content: center;display: flex;margin-top: 10px"
      v-if="showTable"
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-size=pageSize
      layout="prev, pager, next,jumper"
      :total="total">
  </el-pagination>

</template>

<style scoped>
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
</style>
