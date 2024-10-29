<script setup>
import { ref } from 'vue';
import axios from "axios";
const SearchText = ref('');
const ifAD = ref('noAD');
import router from "@/router/index.js";
import {Search} from "@element-plus/icons-vue";
const handleSearch = () => {
  const keyword = encodeURIComponent(SearchText.value);
  console.info('搜索关键词:', keyword,ifAD.value);
  router.push(`/PredictResult/${keyword}/${ifAD.value}`);
};

</script>

<template>
  <div style="justify-content: center;display: flex;">
    <el-input
        v-model="SearchText"
        style="max-width: 600px"
        placeholder="Please input smiles"
        size="large"
        @keyup.enter="handleSearch"
    >
      <template #prepend>
        <el-select v-model="ifAD" placeholder="Options" style="width: 170px" size="large">
          <el-option label="with AD image" value="AD" />
          <el-option label="no AD image" value="noAD" />
        </el-select>
      </template>
      <template #append>
        <el-button :icon="Search" @click="handleSearch" />
      </template>
    </el-input>
  </div>

</template>

<style scoped>

</style>
