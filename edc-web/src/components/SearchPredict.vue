<template>
  <div>
    <el-container style="margin-top: -2px;">
      <el-header>
        <!--        头部菜单栏-->
        <el-menu
            :default-active="activeIndex"
            style="min-width: 1080px; margin-left: -25px;margin-right: -25px;margin-top: -5px"
            mode="horizontal"
            background-color="#3a3b3d"
            text-color="#fff"
            active-text-color="#ffcc66"
            :ellipsis="false"
        >
          <el-menu-item index="0" @click="router.push('/')">
            <!--      菜单左侧标志-->
            <img src="../assets/network.svg" style="height: 45px; width: 45px;" alt="Your Icon" />
            <p style="font-size: 18px;margin-left: 6px;font-weight: bold;text-shadow: 1px 1px 2px black;">
              EDC-Web
            </p>
          </el-menu-item>
          <div class="flex-grow" />
          <el-sub-menu index="1">
            <template #title >
              <el-icon :size="20"><Search /></el-icon>
              <p style="font-size: 16px;text-shadow: 0px 0px 2px black;">
                Search
              </p>
            </template>
            <el-menu-item index="1-1" style="justify-content: center;" @click="router.push('/SearchAOP')">
              <p style="justify-content: center;">EDC-AOP</p>
            </el-menu-item>
            <el-menu-item index="1-2" style="justify-content: center;" @click="router.push('/SearchDATA')">
              <p style="justify-content: center;">EDC-DATA</p>
            </el-menu-item>
            <el-menu-item index="1-3" style="justify-content: center;" @click="router.push('/SearchPredict')">
              <p style="justify-content: center;">Predict-Result</p>
            </el-menu-item>
          </el-sub-menu>
          <el-menu-item index="2" @click="router.push('/Predict')">
            <template #title>
              <el-icon :size="20"><Odometer /></el-icon>
              <p style="font-size: 16px;text-shadow: 0px 0px 2px black;">
                Predict
              </p>
            </template>
          </el-menu-item>
          <el-menu-item index="3" @click="router.push('/About')">
            <template #title>
              <el-icon :size="20"><InfoFilled /></el-icon>
              <p style="font-size: 16px;text-shadow: 0px 0px 2px black;">
                About
              </p>
            </template>
          </el-menu-item>
          <el-menu-item index="4" @click="router.push('/Contact')">
            <template #title>
              <el-icon :size="20"><UserFilled /></el-icon>
              <p style="font-size: 16px;text-shadow: 0px 0px 2px black;">
                Contact
              </p>
            </template>
          </el-menu-item>
        </el-menu>
      </el-header>
      <el-main class="SearchAOP-main">
        <p style="font-size: 45px;
                  margin-left: 6px;
                  font-weight: bold;
                  justify-content: center;
                  display: flex;
                  color: #1e1a1a;
                  text-shadow: 2px 2px 2px #ffcc66;"
                  >
          Predict-Result  Search
        </p><p style="font-size: 25px;
                  margin-top: -10px;
                  justify-content: center;
                  display: flex;
                  color: #000000;">
        Search from over about 230,000 records
      </p>

        <el-radio-group fill="#ffcc66" v-model="selected" text-color="#1e1a1a"
                        style="font-weight: bold;justify-content: center;display: flex;">
          <el-radio-button label="qualitative_model" value="qualitative_model" size="large" />
          <el-radio-button label="quantitative_model" value="quantitative_model" size="large" />
        </el-radio-group>
        <el-divider />
        <component :is="selectedComponent" />
      </el-main>

    </el-container>
    <footer class="footer">
      <div class="footer-content">
        <div class="footer-section">
          <h3 style="color: #f8f8f8;letter-spacing: 1px;">Copyright</h3>
          <el-divider />
          <p style="color: #ffffff;">All Rights © 2024</p>
          <p style="color: #ffffff;">Reserved</p>
        </div>
        <div class="footer-section">
          <h3 style="color: #f8f8f8;letter-spacing: 1px;">Contact</h3>
          <el-divider />
          <p style="color: #ffffff;">E-mail：example@example.com</p>
          <p style="color: #ffffff;">Tel：123-456-7890</p>
        </div>
        <div class="footer-section">
          <h3 style="color: #f8f8f8;letter-spacing: 1px;">Registration</h3>
          <el-divider />
          <ul>
            <li><a href="#">2024--</a></li>
            <li><a href="#">苏ICP备000000号</a></li>
          </ul>
        </div>
      </div>
    </footer>

  </div>

</template>

<script setup>

import {ref, reactive, watch, onMounted, computed} from "vue";
import axios from "axios";
import {Search} from "@element-plus/icons-vue";
import router from "@/router/index.js";
import SearchDXresult from "@/components/SearchDXresult.vue";
import SearchDLresult from "@/components/SearchDLresult.vue";

const tableData = reactive([]);
const activeIndex = ref('1-3')
const selected = ref('qualitative_model')
const pageSize = ref(20);
const loading = ref(true);
const currentPageData = ref(tableData.slice(0, pageSize.value));

const selectedComponent = computed(() => {
  if (selected.value === 'qualitative_model') {
    return SearchDXresult;
  } else{
    return SearchDLresult;
  }
});


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
  background-image: url('../assets/back.png');
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
