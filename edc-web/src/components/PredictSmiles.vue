<script setup>
import { Search } from '@element-plus/icons-vue'
import { ref } from 'vue';
const SearchText = ref('');
const handleSearch = () => {
  // 处理搜索逻辑，这里只是简单地打印搜索关键词
  console.info('搜索关键词:', SearchText);
};

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


</template>

<style scoped>

</style>
