<template>
  <div>
    <div class="page-container">
      <!-- 使用导航栏组件 -->
      <HeaderNav />
      <el-main class="SearchAOP-main">
        <p style="font-size: 45px;
                  margin-left: 6px;
                  font-weight: bold;
                  justify-content: center;
                  display: flex;
                  color: #1e1a1a;
                  text-shadow: 2px 2px 2px #ffcc66;"
                  >
          EDC-AOP  Search
        </p><p style="font-size: 25px;
                  margin-top: -10px;
                  justify-content: center;
                  display: flex;
                  color: #000000;">
        Begin your AOP data search from over about 130,000 records
      </p>

        <div style="justify-content: center;display: flex;">
          <el-input
              v-model="SearchText"
              style="max-width: 600px"
              size="large"
              placeholder="Please enter Event Title or ID"
              @keyup.enter="handleSearch"
          >
            <template #append>
              <el-button :icon="Search" @click="handleSearch"/>
            </template>
          </el-input>

        </div>
        <div style="justify-content: center;display: flex; margin-top: 0px;">
          <el-space wrap>
            <p style="font-size: 14px;font-weight: bold;justify-content: center;display: flex;margin-right: 20px" >Search By</p>
            <el-radio-group  v-model="selectedOptions">
              <el-radio label="EventTitle"></el-radio>
              <el-radio label="EventID"></el-radio>
            </el-radio-group>
          </el-space>
        </div>
        <div style="justify-content: center;display: flex; margin-top: 10px;">
        <el-table
                  v-loading="loading"
                  stripe
                  max-height="400"
                  :data="currentPageData" border style="width: 60%;"
                  :header-cell-style="{ background: '#dedede', color: '#000' }">
          <el-table-column prop="eventId" label="ID" width="150" align="center"></el-table-column>
          <el-table-column prop="eventTitle" label="Title" width="200" align="center"></el-table-column>
          <el-table-column prop="aops" label="AOPs" align="center">
            <template #default="scope">
              <div>
                <el-button
                    v-for="number in scope.row.aops.split(',')"
                    :key="number"
                    type="text"
                    @click="handleRowClick(number)"
                >AOP_{{ number }}</el-button>
              </div>
            </template>
          </el-table-column>
        </el-table>
        </div>
        <el-pagination
            style="justify-content: center;display: flex;margin-top: 10px"
            @current-change="handleCurrentChange"
            :current-page="currentPage"
            :page-size=pageSize
            layout="prev, pager, next, jumper"
            :total="total">
        </el-pagination>
      </el-main>

    </div>
    <FooterNav />

  </div>

</template>

<script setup>

import {ref, reactive, watch, onMounted, computed} from "vue";
import axios from "axios";
import {Search} from "@element-plus/icons-vue";
import router from "@/router/index.js";
import {useStore} from "vuex";
import {ElMessageBox} from "element-plus";
import HeaderNav from "@/components/HeaderNav.vue";
import FooterNav from "@/components/FooterNav.vue";


const tableData = reactive([]);
const activeIndex = ref('1-1')
const selected = ref('TextSearch')
const pageSize = ref(20);
const SearchText = ref('');
const loading = ref(true);
const currentPageData = ref(tableData.slice(0, pageSize.value));
const selectedOptions = ref('EventTitle');
const currentPage = ref(1);
const total = ref(100); // 假设总数据量为 100




const store = useStore()
const isVisible = ref(false)

// 从Vuex获取认证状态
const isAuthenticated = computed(() => store.getters.isAuthenticated)
// 从Vuex获取用户名
const userName = computed(() => store.getters.currentUser?.userName || 'User')


// 处理登出操作
const handleLogout = () => {
  ElMessageBox.confirm(
      'Are you sure you want to logout?',
      'Logout',
      {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning',
      }
  ).then(() => {
    store.dispatch('logout')
    router.push('/')
  }).catch(() => {
    // 用户取消登出操作
  })
}
const handleCurrentChange = (page) => {
  currentPage.value = page;
};
const handleRowClick = (id) => {

  router.push('/SearchAOPResult/'+ id);
};
const handleSearch = async () => {
  currentPage.value=1
  const keyword = encodeURIComponent(SearchText.value)
  const columnName = encodeURIComponent(selectedOptions.value)
  console.info("keyword",keyword)
  console.info("columnName",columnName)
  loading.value = true;
  // 处理搜索逻辑，这里只是简单地打印搜索关键词
  // console.info('搜索关键词:', SearchText.value);
  try {
    const response = await axios.get(`/searchEvent?keyword=${keyword}&columnName=${columnName}`);
    tableData.value = response.data;
    //拿到数据之后 需要初始化一系列参数
    currentPageData.value = tableData.value.slice(0, pageSize.value);
    total.value=response.data.length;
    // console.info('currentPageData:', currentPageData.value);
    // console.info('total:', total.value);
  } catch (error) {
    console.error('Error searching:', error);
  } finally {
    loading.value = false;
  }


};
const fetchData = async () => {
  try {
    const response = await axios.get('/getEventData');
    // console.info("response",response.data);
    return response.data;
  } catch (error) {
    console.error('Error fetching data:', error);
    return [];
  }
};

watch([tableData,currentPage, pageSize], () => {
  const startIndex = (currentPage.value - 1) * pageSize.value;
  const endIndex = startIndex + pageSize.value;
  currentPageData.value = tableData.value.slice(startIndex, endIndex);
  total.value = tableData.value.length;
  // console.info("数据变化")
},{ deep: true });

onMounted( () => {
  //页面加载的初始化
  // 手动调用一次处理当前页码改变的方法，确保在页面刚加载时显示第一页的内容
  isVisible.value = true
  handleCurrentChange(currentPage.value);
  // 初始时先接收数据
  fetchData().then(data => {
    tableData.value = data; // 将获取到的数据赋值给 tabledata
    //拿到数据之后 需要初始化一系列参数
    currentPageData.value = tableData.value.slice(0, pageSize.value);
    total.value=data.length;
    loading.value=false
    // console.info("currentPageData.value",currentPageData.value); // 打印获取到的数据
  }).catch(error => {
    console.error('Error:', error); // 打印错误信息
  });
})


</script>

<style scoped>
/* 使用 ::v-deep 选择器穿透 Scoped CSS 限制 */
:deep(.el-radio__input.is-checked .el-radio__inner) {
  border-color: #ffcc66;
  background-color: #ffcc66;
}

:deep(.el-radio__input.is-checked + .el-radio__label) {
  color: #0c0c0c; /* 修改文字颜色 */
  font-weight: bold;
}

/* 修改 hover 和 focus 状态的颜色 */
:deep(.el-radio__input:hover .el-radio__inner),
:deep(.el-radio__input:focus .el-radio__inner) {
  border-color: #ffcc66;
}
/* 覆盖 el-pagination 的样式 */

:deep(.el-pagination .el-pager li:not(.is-active):hover) {
  color: #ffcc66; /* 悬停文本颜色 */
}
:deep(.el-pagination .el-pager li.is-active) {
  color: #ffcc66; /* 活动分页按钮文本颜色 */
}


.flex-grow {
  flex-grow: 1;
}
.SearchAOP-main{
  /* 设置图片作为背景 */
  background-image: url('../../assets/back.png');
  /* 背景设置为覆盖整个容器 */
  min-width: 1080px;
  min-height: 80vh;
  background-size: cover;
  background-position: center;
  height: 90vh;
/* //height: 500px; 根据需要设置高度 */
}

.footer {
  bottom: 0;
  min-width: 1080px;
  margin-left: -8px;
  margin-right: -8px;
  background-color: #3a3b3d;
  padding: 15px 0;
  text-align: center;
}

.footer-content {
  display: flex;
  justify-content: space-around;
  max-width: 1200px;
  margin: 0 auto;
}

.footer-section {
  flex: 1;
}

.footer-section h3 {
  font-size: 18px;
  margin-bottom: 10px;
}

.footer-section p {
  margin: 5px 0;
}

.footer-section ul {
  list-style-type: none;
  padding: 0;
}

.footer-section ul li {
  margin-bottom: 5px;
}

.footer-section ul li a {
  color: #ffffff;
  text-decoration: none;
}

.footer-section ul li a:hover {
  text-decoration: underline;
}


</style>
