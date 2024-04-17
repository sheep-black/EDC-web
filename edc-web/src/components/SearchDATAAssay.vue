<template>
  <div >
    <div style="justify-content: center;display: flex;" >
      <el-table :data="currentPageData" border style="width: 60%" height="350" >
        <!-- 这里是表格的列 -->
        <el-table-column prop="Assay ID" label="ID" >
          <template #default="scope">
            <el-button type="text" @click="handleRowClick(scope.row)">{{ scope.row.ID}}</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="Name" label="Name"></el-table-column>
        <el-table-column prop="Level" label="Level" :filters="ageFilters" :filter-method="handleAgeFilter"></el-table-column>
        <el-table-column prop="E" label="E" ></el-table-column>

      </el-table>
    </div>
    <el-pagination
        style="justify-content: center;display: flex; margin-top: 2vh;"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-size="10"
        layout="prev, pager, next, jumper"
        :total="total">
    </el-pagination>

  </div>
</template>

<script setup>
import { ref, reactive, watch, onMounted } from "vue";

// 定义响应式数据
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(100); // 假设总数据量为 100
const tableData = reactive([]); // 这里是你的数据，假设已经从后端获取

const ageFilters = [
  { text: '大于20', value: 'gt20' },
  { text: '小于等于20', value: 'lte20' },
  // 其他筛选项
];

// 根据当前页和每页显示的数量截取数据
const currentPageData = ref(tableData.slice(0, 10));
watch([currentPage, pageSize], () => {
  const startIndex = (currentPage.value - 1) * pageSize.value;
  const endIndex = startIndex + pageSize.value;
  currentPageData.value = tableData.slice(startIndex, endIndex);
});


// 当前页码改变时的回调
const handleCurrentChange = (page) => {
  currentPage.value = page;
  // console.info("currentPage",currentPage.value)
};
const handleRowClick = (row) => {
  console.info('点击的行数：',row)

};
// 年龄筛选方法
const handleAgeFilter = (value, row) => {
  const age = row.age;
  if (value === 'gt20') {
    return age > 20;
  } else if (value === 'lte20') {
    return age <= 20;
  }
};

// 模拟从后端获取数据
const fetchData = () => {
  // 模拟异步请求
  setTimeout(() => {
    tableData.splice(0, tableData.length, ...generateData(total.value));
  }, 500);
};

onMounted(() => {
  fetchData();
  console.info("tableData",tableData)
  // 手动调用一次处理当前页码改变的方法，确保在页面刚加载时显示第一页的内容
  handleCurrentChange(currentPage.value);
  // 初始时先接收数据
  tableData.splice(0, tableData.length, ...generateData(total.value));
  currentPageData.value = tableData.slice(0, 10);
  // console.info("tableData",tableData)
  // console.info("currentPageData",currentPageData)
});

// 模拟生成数据
const generateData = (count) => {
  const data = [];
  for (let i = 0; i < count; i++) {
    data.push({
      ID: (i + 1),
      Name: (i + 1),
      Level: (i + 1),
      E: Math.floor(Math.random() * 50) + 18,
      // 其他字段
    });
  }
  return data;
};
</script>
