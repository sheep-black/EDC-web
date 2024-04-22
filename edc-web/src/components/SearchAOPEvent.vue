<template>
  <div >
    <div style="justify-content: center;display: flex;" >
      <el-table :data="currentPageData" border style="width: 80%" height="500"
                :header-cell-style="{ background: '#dedede', color: '#000' }">
        <!-- 这里是表格的列 -->
<!--        <el-table-column prop="Title" label="Title" >-->
<!--          <template #default="scope">-->
<!--            <el-button type="text" @click="handleRowClick(scope.row)">{{ scope.row.Title}}</el-button>-->
<!--          </template>-->
<!--        </el-table-column>-->
        <el-table-column prop="id" label="ID" width="60"></el-table-column>
        <el-table-column prop="cas" label="Cas" width="120"></el-table-column>
        <el-table-column prop="activity" label="Activity" width="80"></el-table-column>
        <el-table-column prop="noael" label="NOAEL" width="80"></el-table-column>
        <el-table-column prop="unit" label="UNIT" width="150"></el-table-column>
        <el-table-column prop="assay" label="Assay" width="150"></el-table-column>
        <el-table-column prop="source" label="Source" width="120"></el-table-column>
        <el-table-column prop="smiles" label="Smiles" :filters="ageFilters" :filter-method="handleAgeFilter"></el-table-column>

      </el-table>
    </div>
    <el-pagination
        style="justify-content: center;display: flex;"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-size="10"
        layout="prev, pager, next, jumper"
        :total="total">
    </el-pagination>
    <!-- 详情表格 -->
    <el-divider v-if="showDetailTable">
      <el-icon><star-filled /></el-icon>
    </el-divider>
    <p v-if="showDetailTable" style="font-size: 20px;
                  margin-top: 30px;
                  margin-left: 20%;
                  font-weight: bold;
                  display: flex;
                  color: #1B497BFF;">
      Search Results
    </p>
    <div style="justify-content: center;display: flex;">
      <el-table v-if="showDetailTable" :data="detailTableData" border style="width: 60%" height="300"
                :header-cell-style="{ background: '#dedede', color: '#000' }">
        <el-table-column prop="detailName" label="详情名称"></el-table-column>
        <el-table-column prop="detailValue" label="详情数值"></el-table-column>
        <!-- 其他列 -->
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, watch, onMounted } from "vue";
import axios from "axios";

// 定义响应式数据
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(100); // 假设总数据量为 100
const tableData = reactive([]); // 这里是你的数据，假设已经从后端获取
// 详情表格数据
const detailTableData = ref([]);
// 是否显示详情表格
const showDetailTable = ref(false);

// 年龄筛选项
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
  currentPageData.value = tableData.value.slice(startIndex, endIndex);
});


// 当前页码改变时的回调
const handleCurrentChange = (page) => {
  currentPage.value = page;
  // console.info("currentPage",currentPage.value)
};
const handleRowClick = (row) => {
  // 点击第一列按钮时触发，获取对应的详情数据，并显示详情表格
  detailTableData.value = row.detail;
  showDetailTable.value = true;
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


const fetchData = async () => {
  try {
    const response = await axios.get('/getData');
    // console.info("response",response.data);
    return response.data;
  } catch (error) {
    console.error('Error fetching data:', error);
    return [];
  }
};

onMounted(() => {
  // 手动调用一次处理当前页码改变的方法，确保在页面刚加载时显示第一页的内容
  handleCurrentChange(currentPage.value);
  // 初始时先接收数据
  fetchData().then(data => {
    tableData.value = data; // 将获取到的数据赋值给 tabledata
    // console.info("tableData.value",tableData.value); // 打印获取到的数据
    currentPageData.value = tableData.value.slice(0, 10);
    total.value=data.length;
    console.info("size",total.value);
  }).catch(error => {
    console.error('Error:', error); // 打印错误信息
  });

});


</script>
