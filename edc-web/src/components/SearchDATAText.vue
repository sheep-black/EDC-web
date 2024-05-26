<script setup>
import { Search } from '@element-plus/icons-vue'
import {reactive, ref, watch} from 'vue';
import axios from "axios";
import router from "@/router/index.js";

const selectedOptions = ref('Cas');
const SearchText = ref('');
const showTable = ref(false);
const pageSize = ref(20);
const currentPage = ref(1);
const loading = ref(true);
const total = ref(100); // 假设总数据量为 100
// 推荐搜索关键词列表
const tableData = reactive([]);
const currentPageData = ref(tableData.slice(0, 20));
const handleSearch = async () => {
  currentPage.value=1
  const keyword = encodeURIComponent(SearchText.value)
  const columnName = encodeURIComponent(selectedOptions.value)

  showTable.value = true;
  loading.value = true;
  // 处理搜索逻辑，这里只是简单地打印搜索关键词
  // console.info('搜索关键词:', SearchText.value);
  try {
    const response = await axios.get(`/searchDATA?keyword=${keyword}&columnName=${columnName}`);
    tableData.value = response.data;
    //拿到数据之后 需要初始化一系列参数
    currentPageData.value = tableData.value.slice(0, 20);
    total.value=response.data.length;

  } catch (error) {
    console.error('Error searching:', error);
  } finally {
    loading.value = false;
  }

};

const handleRowClick = (id) => {
  // console.info('点击的id：',id.id)
  router.push('/SearchDATAResult/'+ id.id);
};
const handleCurrentChange = (page) => {
  currentPage.value = page;
};
watch([tableData,currentPage, pageSize], () => {
  const startIndex = (currentPage.value - 1) * pageSize.value;
  const endIndex = startIndex + pageSize.value;
  currentPageData.value = tableData.value.slice(startIndex, endIndex);
  total.value = tableData.value.length;

},{ deep: true });

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
  <div style="justify-content: center;display: flex; margin-top: 0px;">
    <el-space wrap>
      <p style="font-size: 14px;font-weight: bold;justify-content: center;display: flex;margin-right: 20px" >Search By</p>
      <el-radio-group v-model="selectedOptions" class="ml-4">
        <el-radio label="Cas"></el-radio>
        <el-radio label="Smiles"></el-radio>
        <el-radio label="Name"></el-radio>
        <el-radio label="Assay"></el-radio>
      </el-radio-group>
    </el-space>
  </div>

  <p v-if="showTable" style="font-size: 20px;
                  margin-top: 30px;
                  margin-left: 10%;
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
              :data="currentPageData" border style="width: 90%;"
              :header-cell-style="{ background: '#dedede', color: '#000' }">
      <el-table-column prop="id" label="ID" width="80" align="center">
        <template #default="scope">
          <el-button type="text" @click="handleRowClick(scope.row)">{{ scope.row.id}}</el-button>
        </template>
      </el-table-column>
      <el-table-column prop="cas" label="Cas" width="120"></el-table-column>
      <el-table-column prop="activity" label="Activity" width="100"></el-table-column>
      <el-table-column prop="noael" label="NOAEL" width="100"></el-table-column>
      <el-table-column prop="unit" label="UNIT" width="150"></el-table-column>
      <el-table-column prop="assay" label="Assay" width="150"></el-table-column>
      <el-table-column prop="source" label="Source" width="120"></el-table-column>
      <el-table-column prop="smiles" label="Smiles"></el-table-column>
      <el-table-column prop="name" label="Name"></el-table-column>
      <el-table-column prop="endpoint" label="Endpoint"></el-table-column>
    </el-table>
  </div>
  <el-pagination
      style="justify-content: center;display: flex;margin-top: 10px"
      v-if="showTable"
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-size=pageSize
      layout="prev, pager, next, jumper"
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
