<template>
  <div  class="common-layout">
    <div class="page-container">
      <!-- 使用导航栏组件 -->
      <HeaderNav />
      <el-main class="PredictResult-main">
        <p style="font-size: 35px;
                  margin-left: 6px;
                  font-weight: bold;
                  justify-content: center;
                  display: flex;
                  color: #1e1a1a;
                  margin-top: -5px;
                  text-shadow: 2px 2px 2px #ffcc66;"
        >
          EDC-Predict  Result

        </p>
        <el-radio-group fill="#ffcc66" v-model="selected" text-color="#1e1a1a"
                        style="font-weight: bold;justify-content: center;display: flex;">
          <el-radio-button label="Qualitative-Analysis" value="Qualitative-Analysis" size="default" />
          <el-radio-button label="Quantitative-Analysis" value="Quantitative-Analysis" size="default" />
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
import PredictQualitative from "./PredictQualitative.vue";
import PredictQuantitative from "./PredictQuantitative.vue";
import HeaderNav from "../HeaderNav.vue";
import FooterNav from "../FooterNav.vue";
const activeIndex = ref('2')
const selected = ref('Qualitative-Analysis')

const selectedComponent = computed(() => {
  if (selected.value === 'Qualitative-Analysis') {
    return PredictQualitative;
  } else{
    return PredictQuantitative;
  }
});
</script>

<style>
.flex-grow {
  flex-grow: 1;
}

.PredictResult-main{
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
  background-color: #2b5e8d;
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
