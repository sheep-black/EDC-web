<script setup>
import { Search } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { ref } from 'vue';

const SearchText = ref('');
const showTable = ref(false);
const recommendations = ref(['123', '11', '3213', '321','steroid','Cancer','Reproductive','Metabolic']);

// 选中的搜索选项
const selectedOptions = ref([]);
const router = useRouter();

const tableData = ref([]);

const handleRowClick = (row) => {
  console.info('点击的行数：',row)
  router.push('/SearchDATAResult')
};
const fillKeyword = (recommendation) => {
  SearchText.value = recommendation;
  handleSearch();
};
const handleSearch = () => {
  tableData.value=[];
  // 处理搜索逻辑，这里只是简单地打印搜索关键词
  console.info('搜索关键词:', SearchText);
  console.info('selectedOptions:', selectedOptions.value);
  if (selectedOptions.value.includes('Name')) {
    // 模拟按名称搜索逻辑
    tableData.value.push({
      ID:'11111',
      date: '2016-05-03',
      name: 'Tom',
      address: 'No. 189, Grove St, Los Angeles',
    },);
  }
  if (selectedOptions.value.includes('ID')) {
    // 模拟按日期搜索逻辑
    tableData.value.push({
      ID:'11111',
      date: '111111111111111111',
      name: '2',
      address: 'N1111geles',
    },);
  }
  if(selectedOptions.value.length===0){
    tableData.value.push({
      ID:'11111',
      date: '空空空空空空空空空空',
      name: '2',
      address: 'N1111geles',
    },);
  }
  showTable.value = true;
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
                  margin-left: 20%;
                  font-weight: bold;
                  display: flex;
                  color: #1B497BFF;">
    Search Results
  </p>
  <div style="justify-content: center;display: flex; margin-top: 10px;">
    <el-table
        :data="tableData"
        height="300"
        style="width: 60%"
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
  </div>

</template>

<style scoped>

</style>
