<!-- src/views/Home.vue -->
<template>
  <div class="page-container">
    <!-- 使用导航栏组件 -->
    <HeaderNav />
    <el-main class="my_home-main">
      <el-row :gutter="20" style="width: 100%;display: flex;align-items: center; /* 垂直居中 */">
        <el-col :span="12">
          <p class="title-text">
            EDC Network  <br> & Database & Predictor
          </p>
          <div style="display: flex;
                    margin-top: -30px;
                    justify-content: flex-start;
                    padding-left: 15vh;
                    ">
            <p class="intro-text">
              EDC Network & Database & Predictor (EDCNPD.ai) is a free web server that provides information on endocrine disrupting chemicals (EDCs) related adverse outcome pathways (AOPs) and study data retrieval. It also provides a prediction service for screening potential EDCs. Through predictive modelling, statistical testing and a user-friendly web interface, EDCNDP.ai helps toxicologists identify endocrine disrupting patterns of EDCs and accelerate related research.
              <br>
              We are constantly adding new data and working to improve interfaces to EDCs information. Please check back often!
              <br>
            </p>
          </div>
          <el-row style="padding-left: 20vh;">
            <el-col :span="8">
              <el-link :underline="false" @click="router.push('/SearchAOP')">
                <transition name="fade">
                  <el-space v-if="isVisible" direction="vertical">
                    <el-icon :style="{ fontSize: '70px' }"><Document /></el-icon>
                    <p style="font-size: 1.2em;margin-top: -5%;font-weight: bold;">
                      AOP-Search
                    </p>
                  </el-space>
                </transition>
              </el-link>
            </el-col>
            <el-col :span="8">
              <el-link :underline="false" @click="router.push('/SearchData')">
                <transition name="fade">
                  <el-space v-if="isVisible" direction="vertical">
                    <el-icon :style="{ fontSize: '70px' }"><EditPen /></el-icon>
                    <p style="font-size: 1.2em;margin-top: -5%;font-weight: bold;">
                      Database-Search
                    </p>
                  </el-space>
                </transition>
              </el-link>
            </el-col>
            <el-col :span="8">
              <el-link :underline="false" @click="router.push('/Predict')">
                <transition name="fade">
                  <el-space v-if="isVisible" direction="vertical">
                    <el-icon :style="{ fontSize: '70px' }"><DataLine /></el-icon>
                    <p style="font-size: 1.2em;margin-top: -5%;font-weight: bold;">
                      EDC-Predict
                    </p>
                  </el-space>
                </transition>
              </el-link>
            </el-col>
          </el-row>
          <el-link style="margin-left: 15vh;margin-top: 2%" type="primary" @click="router.push('/About')" :underline="false">
            <transition name="fade">
              <p v-if="isVisible" style="font-size: 25px;margin-top: -5%;font-weight: bold;color: #ffffff;text-shadow: 5px 5px 5px #054079;">
                Learn More About EDCNDP.ai >
              </p>
            </transition>
          </el-link>
        </el-col>
      </el-row>
    </el-main>
  </div>

  <!-- 使用页脚组件 -->
  <FooterNav />
</template>

<script lang="ts" setup>
import { onMounted, ref } from 'vue'
import router from '../router'
import { Document, EditPen, DataLine } from '@element-plus/icons-vue'
import HeaderNav from './HeaderNav.vue'
import FooterNav from './FooterNav.vue'

const isVisible = ref(false)

onMounted(() => {
  isVisible.value = true
})
</script>

<style scoped>
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
:deep(.el-link){
  --el-link-text-color: #000000;
  --el-link-hover-text-color: #727272
}
.fade-enter-active, .fade-leave-active {
  transition: opacity 1s ease-out, transform 1s ease-out;
}
.fade-enter-from, .fade-leave-to {
  opacity: 0;
  transform: translateY(-20px);
}
.title-text {
  font-size: 45px;
  font-weight: bold;
  justify-content: flex-start;
  display: flex;
  text-shadow: 2px 2px 2px black;
  padding-top:3vh;
  padding-left: 15vh; /* 左边页边距 */
  color: #ffffff;
  animation: fadeIn 1s ease-out;
}
.intro-text {
  /* 中间介绍字体的样式*/
  font-size: 20px;
  max-width: 100%;
  text-align: justify; /* 文本两端对齐 */
  line-height: 150%; /* 设置行高 */
  font-family: 'Helvetica', sans-serif;
  animation: fadeIn 1s ease-out;
}
.my_home-main{
  /* 设置图片作为背景 */
  display: flex;
  background-image: url('@/assets/background4.png');
  /* 背景设置为覆盖整个容器 */
  min-width: 1080px;
  min-height: 900px;
  margin-top: -1%;
  margin-right: -5px;
  margin-left: -5px;
  background-size: cover;
  background-position: center;
  height: 100vh;
}
</style>
