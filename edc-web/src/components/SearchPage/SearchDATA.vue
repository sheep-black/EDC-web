<template>
  <div  class="common-layout">
    <div class="page-container">
      <!-- 使用导航栏组件 -->
      <HeaderNav />
      <el-main class="SearchDATA-main">
        <p style="font-size: 45px;
                  margin-left: 6px;
                  font-weight: bold;
                  justify-content: center;
                  display: flex;
                  color: #1e1a1a;
                  text-shadow: 2px 2px 2px #ffcc66;"
        >
          EDC-Database  Search
        </p><p style="font-size: 25px;
                  margin-top: -10px;
                  justify-content: center;
                  display: flex;
                  color: #000000;">
        Quickly find EDCs information from authoritative sources
      </p>
        <el-radio-group fill="#ffcc66" v-model="selected" text-color="#1e1a1a"
                        style="font-weight: bold;justify-content: center;display: flex;">
          <el-radio-button label="TextSearch" value="TextSearch" size="large" />
          <el-radio-button label="DataScreen" value="DataScreen" size="large" />
        </el-radio-group>
        <el-divider />
        <component :is="selectedComponent" />

      </el-main>

    </div>
    <FooterNav />

  </div>

</template>

<script lang="ts" setup>


import { ref ,computed} from 'vue'
import router from '../../router'
import SearchDATAText from "./SearchDATAText.vue";
import SearchDATAAssay from "./SearchDATAAssay.vue";
import HeaderNav from "../HeaderNav.vue";
import FooterNav from "../FooterNav.vue";
const activeIndex = ref('1-2')
const selected = ref('TextSearch')

const handleSelect = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}
const selectedComponent = computed(() => {
  if (selected.value === 'TextSearch') {
    return SearchDATAText;
  } else{
    return SearchDATAAssay;
  }
});
</script>

<style>
.flex-grow {
  flex-grow: 1;
}

.SearchDATA-main{
  /* 设置图片作为背景 */
  background-image: url('../../assets/back-none.png');
  /* 背景设置为覆盖整个容器 */
  min-height: 80vh;
  min-width: 1080px;
  background-size: cover;
  background-position: center;
  /* //height: 500px; 根据需要设置高度 */
}

.footer {
  min-width: 1080px;
  margin-left: -8px;
  margin-right: -8px;
  margin-bottom: -8px;
  background-color: #4d4d50;
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
