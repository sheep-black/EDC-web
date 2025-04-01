<script setup>
import { ref } from 'vue';
import axios from "axios";
const SearchText = ref('');
const ifAD = ref('noAD');
import router from "@/router/index.js";
import {InfoFilled, Search, Warning} from "@element-plus/icons-vue";
const handleSearch = () => {
  const keyword = encodeURIComponent(SearchText.value);
  console.info('搜索关键词:', keyword,ifAD.value);
  router.push(`/PredictResult/${keyword}/${ifAD.value}`);
};

</script>

<template>
  <div style="justify-content: center;display: flex;">
    <el-popover
        placement="bottom-start"
        title="Tips"
        :width="300"
        offset="20"
        trigger="hover"
        content="This platform uses kernel functions to calculate the Application Domains (ADs) of compounds and therefore takes a long time to draw graphs. If you want to get the prediction results quickly and do not need the AD images, please select the 'no AD image' option."
    >
      <template #reference>
        <el-icon style="font-size: 20px;padding-right: 10px;padding-top: 8px">
          <QuestionFilled />
        </el-icon>
      </template>
    </el-popover>
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
