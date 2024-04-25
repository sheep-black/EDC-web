<script setup>
import { Search } from '@element-plus/icons-vue'
import { ref } from 'vue';
import axios from "axios";
const SearchText = ref('');
const showTable = ref(false);
// 推荐搜索关键词列表
const tableData = ref([]);
const recommendations = ref(['123', '11', '3213', '321','steroid','Cancer','Reproductive','Metabolic']);
const handleSearch = async () => {
  // 处理搜索逻辑，这里只是简单地打印搜索关键词
  // console.info('搜索关键词:', SearchText.value);
  try {
    const response = await axios.get(`/search?keyword=${SearchText.value}`);
    tableData.value = response.data;
    // console.info('tableData:', tableData.value);
  } catch (error) {
    console.error('Error searching:', error);
  }
  showTable.value = true;
};

const fillKeyword = (recommendation) => {
  SearchText.value = recommendation;
  handleSearch();
};

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
  <div style="justify-content: center;display: flex; margin-top: 10px;">
    <span v-for="recommendation in recommendations" :key="recommendation" class="recommendation" @click="fillKeyword(recommendation)">
        {{ recommendation }}
    </span>
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
              stripe
              max-height="600"
              :data="tableData" border style="width: 80%"
              :header-cell-style="{ background: '#dedede', color: '#000' }">
      <el-table-column prop="id" label="ID" width="70" sortable></el-table-column>
      <el-table-column prop="cas" label="Cas" width="120"></el-table-column>
      <el-table-column prop="activity" label="Activity" width="100"></el-table-column>
      <el-table-column prop="noael" label="NOAEL" width="100"></el-table-column>
      <el-table-column prop="unit" label="UNIT" width="150"></el-table-column>
      <el-table-column prop="assay" label="Assay" width="150"></el-table-column>
      <el-table-column prop="source" label="Source" width="120"></el-table-column>
      <el-table-column prop="smiles" label="Smiles"></el-table-column>

    </el-table>
  </div>

</template>

<style scoped>
.recommendations {
  margin-top: 10px;
}

.recommendation {
  margin-top: -2px;
  margin-right: 10px;
  font-size: 14px;
  color: #1890ff;
  cursor: pointer;
}

.recommendation:hover {
  text-decoration: underline;
}
</style>
