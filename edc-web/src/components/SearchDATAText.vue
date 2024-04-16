<script setup>
import { Search } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { ref } from 'vue';
const SearchText = ref('');
const showTable = ref(false);
// 选中的搜索选项
const selectedOptions = ref([]);
const router = useRouter();

const tableData = ref([]);

const handleRowClick = (row) => {
  console.info('点击的行数：',row)
  router.push('/SearchDATAResult')
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
  };
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
  <div style="justify-content: center;display: flex; margin-top: 0px;">
    <el-space wrap>
      <p style="font-size: 14px;font-weight: bold;justify-content: center;display: flex;margin-right: 20px" >Search By</p>
      <el-checkbox-group v-model="selectedOptions">
        <el-checkbox label="Smiles"></el-checkbox>
        <el-checkbox label="Cas"></el-checkbox>
        <el-checkbox label="Name"></el-checkbox>
        <el-checkbox label="ID"></el-checkbox>
        <!-- 其他复选框 -->
      </el-checkbox-group>
    </el-space>

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
