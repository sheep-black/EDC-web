<template>
  <div  class="common-layout">
    <div class="page-container">
      <!-- 使用导航栏组件 -->
      <HeaderNav />
      <el-main class="Predict-main">
        <p style="font-size: 45px;
                  margin-left: 6px;
                  font-weight: bold;
                  justify-content: center;
                  display: flex;
                  color: #1e1a1a;
                  text-shadow: 2px 2px 2px #ffcc66;">
          EDC Predictor
        </p><p style="font-size: 18px;
                  margin-top: -10px;
                  justify-content: center;
                  display: flex;
                  color: #000000;">
        Quickly predict and prioritise EDCs using qualitative & quantitative prediction models
      </p>
        <el-radio-group fill="#ffcc66" v-model="selected" text-color="#1e1a1a"
                        style="font-weight: bold;justify-content: center;display: flex;margin-top:30px">
          <el-radio-button label="Inputting a SMILES" value="Smiles" size="large" />
          <el-radio-button label="Drawing a molecule in the JSME" value="Drawing" size="large" />
        </el-radio-group>
        <el-divider style="width: 50%;margin: 20px auto;"></el-divider>
        <component :is="selectedComponent" />

      </el-main>

    </div>
    <FooterNav />

  </div>

</template>

<script lang="ts" setup>


import { ref ,computed} from 'vue'
import router from '../../router'
import PredictSmiles from "../PredictPage/PredictSmiles.vue";
import PredictDrawing from "../PredictPage/PredictDrawing.vue";
import HeaderNav from "../HeaderNav.vue";
import FooterNav from "../FooterNav.vue";
const activeIndex = ref('2')
const selected = ref('Smiles')
const handleSelect = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}
const selectedComponent = computed(() => {
  if (selected.value === 'Smiles') {
    return PredictSmiles;
  } else if(selected.value === 'Drawing'){
    return PredictDrawing;
  }
});
</script>

<style>
.flex-grow {
  flex-grow: 1;
}

.Predict-main{
  /* 设置图片作为背景 */
  background-image: url('../../assets/back.png');
  /* 背景设置为覆盖整个容器 */
  min-width: 1080px;
  min-height: 80vh;
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
  min-width:60%;
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
