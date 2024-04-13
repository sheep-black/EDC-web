<script setup>
import { Search } from '@element-plus/icons-vue'
import { ref } from 'vue';
const SearchText = ref('');
// 推荐搜索关键词列表
const tableData = [{},
  // {
  //   date: '2016-05-03',
  //   name: 'Tom',
  //   address: 'No. 189, Grove St, Los Angeles',
  // },
]
const recommendations = ref(['Vue.js', 'Element UI', '搜索框', '推荐']);
const handleSearch = () => {
  // 处理搜索逻辑，这里只是简单地打印搜索关键词
  console.info('搜索关键词:', SearchText);
  tableData.value = [
    {
      date: '2016-05-03',
      name: 'Tom',
      address: 'No. 189, Grove St, Los Angeles',
    },
    // 其他搜索结果
  ];
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
    推荐搜索：
    <span v-for="recommendation in recommendations" :key="recommendation" class="recommendation">
        {{ recommendation }}
      </span>
  </div>
  <div style="justify-content: center;display: flex; margin-top: 10px;">
    <el-table :data="tableData" height="250" style="width: 60%" stripe="true">
      <el-table-column prop="date" label="Date" width="180" align="center" />
      <el-table-column prop="name" label="Name" width="180" align="center" />
      <el-table-column prop="address" label="Address" align="center" />
    </el-table>
  </div>

</template>

<style scoped>
.recommendations {
  margin-top: 10px;
}

.recommendation {
  margin-right: 10px;
  color: #1890ff;
  cursor: pointer;
}

.recommendation:hover {
  text-decoration: underline;
}
</style>
