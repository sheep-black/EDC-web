<template>
  <div >
    <div style="justify-content: center;display: flex; margin-top: 0px;">
      <el-space direction="vertical">
        <el-row :gutter="100">
          <el-col :span="6" style="width: 90px;">
            <p class="font">Activity:</p>
          </el-col>
          <el-col :span="18">
            <el-select
                v-model="ActivityScreen"
                placeholder="Select"
                style="width: 300px;"
            >
              <el-option
                  v-for="item in ActivityOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
              />
            </el-select>
          </el-col>
        </el-row>
        <el-row :gutter="100">
          <el-col :span="6" style="width: 90px">
            <p class="font">Assay:</p>
          </el-col>
          <el-col :span="18">
            <el-select
                v-model="AssayScreen"
                multiple
                collapse-tags
                placeholder="Select"
                style="width: 300px;"
            >
              <el-option
                  v-if="selectAllOptionVisible1"
                  :key="'selectAll'"
                  :label="'All'"
                  :value="selectAllValue1"
                  @click="handleSelectAll1"
              />
              <el-option
                  v-for="item in AssayOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
              />
            </el-select>
          </el-col>
        </el-row>
        <el-row :gutter="100">
          <el-col :span="6" style="width: 90px">
            <p class="font">Endpoint:</p>
          </el-col>
          <el-col :span="18">
            <el-select
                v-model="EndpointScreen"
                multiple
                collapse-tags
                placeholder="Select"
                style="width: 300px;"
            >
              <el-option
                  v-if="selectAllOptionVisible2"
                  :key="'selectAll'"
                  :label="'All'"
                  :value="selectAllValue2"
                  @click="handleSelectAll2"
              />
              <el-option
                  v-for="item in EndpointOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
              />
            </el-select>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-button type="primary" style="width: 100px" @click="handleScreen">Confirm</el-button>
          </el-col>
          <el-col :span="12">
            <el-button type="primary" plain style="width: 100px" @click="cancelScreen">Reset</el-button>
          </el-col>
        </el-row>
      </el-space>

    </div>

    <p v-if="showTable" style="font-size: 20px;
                  margin-top: 30px;
                  margin-left: 10%;
                  font-weight: bold;
                  display: flex;
                  color: #1B497BFF;">
      Search Results
    </p>
    <div style="justify-content: center;display: flex; margin-top: 10px;">
      <el-table v-if="showTable"
                v-loading="loading"
                stripe
                max-height="600"
                :data="currentPageData" border style="width: 90%;"
                :header-cell-style="{ background: '#dedede', color: '#000' }">
        <el-table-column prop="id" label="ID" width="80" align="center">
          <template #default="scope">
            <el-button type="text" @click="handleRowClick(scope.row)">{{ scope.row.id}}</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="cas" label="Cas" width="120"></el-table-column>
        <el-table-column prop="activity" label="Activity" width="100"></el-table-column>
        <el-table-column prop="noael" label="NOAEL" width="100"></el-table-column>
        <el-table-column prop="unit" label="UNIT" width="150"></el-table-column>
        <el-table-column prop="assay" label="Assay" width="150"></el-table-column>
        <el-table-column prop="endpoint" label="Endpoint" width="120"></el-table-column>
        <el-table-column prop="smiles" label="Smiles"></el-table-column>
        <el-table-column prop="name" label="Name"></el-table-column>
        <el-table-column prop="endpoint" label="Endpoint"></el-table-column>
      </el-table>
    </div>
    <el-pagination
        v-if="showTable"
        style="justify-content: center;display: flex;margin-top: 10px"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-size=pageSize
        layout="prev, pager, next, jumper"
        :total="total">
    </el-pagination>
  </div>
</template>

<script setup>
import { ref, reactive, watch, onMounted } from "vue";
import axios from "axios";
import router from "@/router/index.js";

// 定义响应式数据
const currentPage = ref(1);
const pageSize = ref(20);
const showTable = ref(false);
const total = ref(100); // 假设总数据量为 100
const tableData = reactive([]); // 这里是你的数据，假设已经从后端获取
const EndpointScreen = ref([]);
const AssayScreen = ref([]);
const ActivityScreen = ref([]);
const ActivityOptions = ref([])
const AssayOptions = ref([])
const EndpointOptions = ref([])
// 定义全选按钮的可见性、值和点击事件处理函数
const selectAllOptionVisible1 = ref(true);
const selectAllValue1 = 'selectAll';
let selectAllClicked1 = false; // 记录全选按钮点击状态，默认为 false
const selectAllOptionVisible2 = ref(true);
const selectAllValue2 = 'selectAll';
let selectAllClicked2 = false; // 记录全选按钮点击状态，默认为 false
const currentPageData = ref(tableData.slice(0, 20));
const loading = ref(true);
const handleSelectAll1 = () => {
  if (selectAllClicked1) {
    // 如果是第二次点击全选按钮，则清空已选项
    AssayScreen.value = [];
    selectAllClicked1 = false;
  } else {
    // 如果是第一次点击全选按钮，则选中所有选项
    AssayScreen.value = AssayOptions.value.map(item => item.value);
    selectAllClicked1 = true;
  }
};
const handleSelectAll2 = () => {
  if (selectAllClicked2) {
    // 如果是第二次点击全选按钮，则清空已选项
    EndpointScreen.value = [];
    selectAllClicked2 = false;
  } else {
    // 如果是第一次点击全选按钮，则选中所有选项
    EndpointScreen.value = EndpointOptions.value.map(item => item.value);
    selectAllClicked2 = true;
  }
};
const handleRowClick = (id) => {
  // console.info('点击的id：',id.id)
  router.push('/SearchDATAResult/'+ id.id);
};
const handleScreen = async () =>{

  showTable.value = true;
  loading.value = true;

  const requestData = {
    endpoint: EndpointScreen.value,
    assay: AssayScreen.value,
    activity: ActivityScreen.value
  };
// 发送 POST 请求给后端
    axios.post('/dataScreen', requestData)
        .then(response => {
          // 请求成功处理逻辑
          // console.log('后端返回的数据：', response.data);
          tableData.value =  response.data; // 将获取到的数据赋值给 tabledata
          //拿到数据之后 需要初始化一系列参数
          currentPageData.value = tableData.value.slice(0, pageSize.value);
          loading.value = false;
          total.value= tableData.value.length;
        })
        .catch(error => {
          // 请求失败处理逻辑
          console.error('请求出错：', error);
        });

}
const  cancelScreen =() =>{
  showTable.value=false;
  EndpointScreen.value = [];
  AssayScreen.value = [];
  ActivityScreen.value ='';

}

watch([currentPage, pageSize], () => {
  const startIndex = (currentPage.value - 1) * pageSize.value;
  const endIndex = startIndex + pageSize.value;
  currentPageData.value = tableData.value.slice(startIndex, endIndex);
  total.value = tableData.value.length;

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

  axios.get('/findDistinct', {
    params: {
      fieldName: 'Activity' // 这里将 fieldName 设置为 Activity，如果需要动态传入参数，可以修改成对应的变量名
    }
  }).then(response => {
    // 将 activityObject 转换为数组
    const activityArray = Object.values(response.data);
// 遍历 activityArray，并将每个值作为 value 和 label 添加到 ActivityOptions 中
    activityArray.forEach(item => {
      ActivityOptions.value.push({ value: item, label: item });
    });
    // console.log('ActivityOptions data:', ActivityOptions.value);
  }).catch(error => {
    console.error('Error fetching data:', error);});

  axios.get('/findDistinct', {
    params: {
      fieldName: 'assay'
    }
  }).then(response => {
    const assayArray = Object.values(response.data);
    assayArray.forEach(item => {
      if (item!== null) {//判断是否空白
        AssayOptions.value.push({ value: item, label: item });
      }
    });
    // console.log('AssayOptions data:', AssayOptions.value);
  }).catch(error => {
    console.error('Error fetching data:', error);});

  axios.get('/findDistinct', {
    params: {
      fieldName: 'endpoint'
    }
  }).then(response => {
    const endpointArray = Object.values(response.data);
    endpointArray.forEach(item => {
      if (item!== null) {//判断是否空白
        EndpointOptions.value.push({value: item, label: item});
      }
    });
    // console.log('EndpointScreen data:', EndpointOptions.value);
  }).catch(error => {
    console.error('Error fetching data:', error);});

  fetchData().then(data => {
    tableData.value = data; // 将获取到的数据赋值给 tabledata
    //拿到数据之后 需要初始化一系列参数
    currentPageData.value = tableData.value.slice(0, 20);
    loading.value = false;
    total.value=data.length;
  }).catch(error => {
    console.error('Error:', error); // 打印错误信息
  });

});


</script>

<style scoped>

.font{
  display: flex;
  justify-content: left; /* 水平居中 */
  margin-top: 0;
  font-size: 20px; /* 修改字体大小 */
  font-weight: bold; /* 修改字体粗细 */
  //font-family: '宋体', 'SimSun', 'STSong',serif; /* 修改字体为宋体 */
  color: #151583; /* 修改字体颜色 */
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
}
</style>
