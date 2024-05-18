<template>
  <div >
    <div style="justify-content: center;display: flex;" >
      <el-table :data="currentPageData" border style="width: 80%" height="500"
                :header-cell-style="{ background: '#dedede', color: '#000' }"
                @filter-change="handleFilterChange"
                @sort-change="handleSortChange">
        <el-table-column prop="id" label="ID" width="70" sortable='custom'></el-table-column>
        <el-table-column prop="cas" label="Cas" width="120"></el-table-column>
        <el-table-column prop="activity" label="Activity" width="100"
                         column-key="activity"
                         :filter-multiple=false
                         :filters="[{ text: 'True', value: 'yes' },{ text: 'False', value: 'no' },]">
        </el-table-column>
        <el-table-column prop="noael" label="NOAEL" width="100"
                         column-key="noael"
                         :filters="[{ text: '0-1000', value: '1' },
                                    { text: '1000-2000', value: '2' },
                                    { text: '2000-3000', value: '3' },
                                    { text: '>3000', value: '4' },]"></el-table-column>
        <el-table-column prop="unit" label="UNIT" width="150"></el-table-column>
        <el-table-column prop="assay" label="Assay" width="150"></el-table-column>
        <el-table-column prop="source" label="Source" width="120"></el-table-column>
        <el-table-column prop="smiles" label="Smiles"></el-table-column>

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
  </div>
</template>

<script setup>
import { ref, reactive, watch, onMounted } from "vue";
import axios from "axios";
import router from '../router'
// 定义响应式数据
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(100); // 假设总数据量为 100
const tableData = reactive([]); // 这里是你的数据，假设已经从后端获取
const filterData = ref([]);
const selectFilter=ref('');
const NoaelFilterResult=ref([]);
const ActivityFilterResult=ref([]);
const currentPageData = ref(tableData.slice(0, 10));

const handleSortChange = ({ prop,order })=>{
  if(prop==='id'){
    if (order === 'ascending') {
      filterData.value.sort((a, b) => a.id - b.id); // 升序排序
    } else if (order === 'descending'){
      filterData.value.sort((a, b) => b.id - a.id); // 降序排序
    }else{
      filterData.value.sort((a, b) => a.id - b.id); // 升序排序
    }
    console.info("order",order)
  }
}
const handleFilterChange=(filter)=>{
  console.log("filter",filter)
  const originData=reactive([...tableData.value]);
  // console.info("originData",originData);
  if(filter['noael']){
    selectFilter.value='noael';
    console.info("处理",selectFilter.value,"筛选")
    NoaelFilterResult.value=handleNoaelFilter(filter['noael'],originData)
  }
  if(filter['activity']){
    selectFilter.value='activity';
    console.info("处理",selectFilter.value,"筛选")
    ActivityFilterResult.value=handleActivityFilter(filter['activity'],originData)
  }
  filterData.value=NoaelFilterResult.value.filter(value => ActivityFilterResult.value.includes(value));
  console.info("filterData",filterData.value)
}
//专门处理activity筛选
const handleActivityFilter = (values,originData) => {
  if(values.length===0){
    console.info('1')
    return originData;
  }else{
    const temp = [];
    for (const range of values) {
      let filteredRange = [];
      console.info("range",range)
      if (range === 'yes') {
        filteredRange = tableData.value.filter(item => item.activity === 'yes');
      } else if (range === 'no') {
        filteredRange = tableData.value.filter(item => item.activity ==='no');
      }
      temp.push(...filteredRange);
    }
    return temp;
  }

};
//专门处理noael筛选
const handleNoaelFilter = (values,originData) => {
  if(values.length===0){
    return originData;
  }else{
    const temp = [];
    for (const range of values) {
      let filteredRange = [];
      if (range === '1') {
        filteredRange = tableData.value.filter(item => item.noael <= 1000);
      } else if (range === '2') {
        filteredRange = tableData.value.filter(item => item.noael > 1000 && item.noael <= 2000);
      } else if (range === '3') {
        filteredRange = tableData.value.filter(item => item.noael > 2000 && item.noael <= 3000);
      } else if (range === '4') {
        filteredRange = tableData.value.filter(item => item.noael > 3000);
      }
      temp.push(...filteredRange);
    }
    return temp;
    // console.log("filterData",filterData.value)
  }

};
watch([filterData,currentPage, pageSize], () => {
  const startIndex = (currentPage.value - 1) * pageSize.value;
  const endIndex = startIndex + pageSize.value;
  currentPageData.value = filterData.value.slice(startIndex, endIndex);
  total.value = filterData.value.length;
  console.info("数据变化")
},{ deep: true });

// 当前页码改变时的回调
const handleCurrentChange = (page) => {
  currentPage.value = page;
  console.info("currentPageData",currentPageData);
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
  //页面加载的初始化
  // 手动调用一次处理当前页码改变的方法，确保在页面刚加载时显示第一页的内容
  handleCurrentChange(currentPage.value);
  // 初始时先接收数据
  fetchData().then(data => {
    tableData.value = data; // 将获取到的数据赋值给 tabledata
    // console.info("tableData.value",tableData.value); // 打印获取到的数据
    //拿到数据之后 需要初始化一系列参数
    currentPageData.value = tableData.value.slice(0, 10);
    filterData.value = reactive([...tableData.value]);
    NoaelFilterResult.value=reactive([...tableData.value]);
    ActivityFilterResult.value=reactive([...tableData.value]);
    total.value=data.length;
  }).catch(error => {
    console.error('Error:', error); // 打印错误信息
  });

});


</script>

<style scoped>
/* 使用更具体的选择器覆盖默认样式 */
.my-el-select{
  width: 80px;
}
.my-el-select .el-input__suffix {
  display: block !important; /* 显示下拉箭头 */
}
.my-el-select .el-input__inner {
  display: none; /* 隐藏输入框 */
}
</style>
