<script setup>
import { ref } from 'vue';
import axios from "axios";
const SearchText = ref('');
const showTable = ref(false);
const tableData = ref([]);
const Result_image ="/src/assets/PredictResult.png"
const showDialog = ref(false);
const handleSearch = async () => {
  const keyword = encodeURIComponent(SearchText.value)
  console.info('搜索关键词:', keyword);
  try {
    const response = await axios.get(`/Predict?input=${keyword}`);
    console.info('response:', response.data.result);
  } catch (error) {
    console.error('Error searching:', error);
  } finally {
    showTable.value = true;
  }
};

const handleRowClick=()=>{
  showDialog.value=true;

}
</script>

<template>
  <div style="justify-content: center;display: flex;">
    <el-input
        v-model="SearchText"
        style="max-width: 600px;height: 50px;margin-top: 10px;"
        size="large"
        placeholder="Please input smiles"
        @keyup.enter="handleSearch"
    >
      <template #append>
        <el-button @click="handleSearch">
        Input
        </el-button>
      </template>
    </el-input>

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

</style>
