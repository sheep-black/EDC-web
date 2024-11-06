<template>
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
              EDCNDP.ai
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
            <el-menu-item index="1-1" style="justify-content: center;"  @click="router.push('/SearchAOP')">
              <p style="justify-content: center;">EDC-AOP</p>
            </el-menu-item>
            <el-menu-item index="1-2" style="justify-content: center;" @click="router.push('/SearchDATA')">
              <p style="justify-content: center;">EDC-DATA</p>
            </el-menu-item>
            <el-menu-item index="1-3" style="justify-content: center;" @click="router.push('/SearchPredict')">
              <p style="justify-content: center;">EDC-GECs</p>
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

    </el-container>
  <footer class="footer">
    <div class="footer-content">
      <div class="footer-section">
        <h3 style="color: #f8f8f8;letter-spacing: 1px;">Copyright</h3>
        <el-divider />
        <p style="color: #ffffff;">All Rights © 2024</p>
        <p style="color: #ffffff;">Nanjing University Reserved</p>
      </div>
      <div class="footer-section">
        <h3 style="color: #f8f8f8;letter-spacing: 1px;">Contact</h3>
        <el-divider />
        <p style="color: #ffffff;">E-mail：njutanhaoyue@nju.edu.cn</p>
        <p style="color: #ffffff;">Postcode：210023</p>
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



</template>

<script lang="ts" setup>

import {nextTick, onMounted, ref} from 'vue'
import router from '../router'
import axios from "axios";
const isVisible = ref(false);
const activeIndex = ref('0')
onMounted(() => {
  isVisible.value = true;

});

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
.flex-grow {
  flex-grow: 1;
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
  background-image: url('../assets/background4.png');
  /* 背景设置为覆盖整个容器 */
  min-width: 1080px;
  margin-top: -1%;
  margin-right: -5px;
  margin-left: -5px;
  background-size: cover;
  background-position: center;
  height: 100vh;
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
