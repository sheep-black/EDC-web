<template>
    <el-container >
      <el-header style="height: 6vh" >
<!--        头部菜单栏-->
        <el-menu
            :default-active="activeIndex"
            style="min-width: 1080px; margin-left: -25px;margin-right: -25px;margin-top: -5px;height: 100%"
            mode="horizontal"
            background-color="#3a3b3d"
            text-color="#fff"
            active-text-color="#ffcc66"
            :ellipsis="false"
        >
          <el-menu-item index="0" @click="router.push('/')">
            <!--      菜单左侧标志-->
            <img src="../assets/network.svg" style="height: 3vw; width: 3vw;" alt="Your Icon" />
            <p style="font-size: 2.5vh;margin-left: 6px;font-weight: bold;text-shadow: 0.2vw 0.2vw 0.2vw black;">
              EDC-Web
            </p>
          </el-menu-item>
          <div class="flex-grow" />
          <el-sub-menu index="1" style="width: 8vw">
            <template #title >
              <el-icon><Search /></el-icon>
              <p style="font-size: 2vh;margin-left: 0.5vh;">
                Search
              </p>
            </template>
            <el-menu-item index="1-1" style="justify-content: center;height: 5vh;min-width: 20vh"  @click="router.push('/SearchAOP')">
              <p style="justify-content: center; font-size: 1.5vh" >EDC-AOP</p>
            </el-menu-item>
            <el-menu-item index="1-2" style="justify-content: center;height: 5vh;min-width: 20vh" @click="router.push('/SearchDATA')">
              <p style="justify-content: center;font-size: 1.5vh">EDC-DATA</p>
            </el-menu-item>
          </el-sub-menu>
          <el-menu-item index="2" style="width: 8vw" @click="router.push('/Predict')">
            <template #title>
              <el-icon><Odometer /></el-icon>
              <p style="font-size: 2vh;text-shadow: 0px 0px 2px black;">
                Predict
              </p>
            </template>
          </el-menu-item>
          <el-menu-item index="3" style="width: 8vw" @click="router.push('/About')">
            <template #title>
              <el-icon><InfoFilled /></el-icon>
              <p style="font-size: 2vh;text-shadow: 0px 0px 2px black;">
                About
              </p>
            </template>
          </el-menu-item>
          <el-menu-item index="4" style="width: 8vw" @click="router.push('/Contact')">
            <template #title>
              <el-icon><UserFilled /></el-icon>
              <p style="font-size: 2vh;text-shadow: 0px 0px 2px black;">
                Contact
              </p>
            </template>
          </el-menu-item>
        </el-menu>
      </el-header>
      <el-main class="my_home-main">
        <el-row :gutter="20" style="width: 100%;">
          <el-col :span="12">
            <p style="font-size: 5vh;
                  font-weight: bold;
                  justify-content: flex-start;
                  display: flex;
                  text-shadow: 2px 2px 2px black;
                  padding-left: 15vh; /* 左边页边距 */
                  color: #ffffff;">
              EDC Network  <br> & Database & Predictor
            </p>
            <div style="display: flex;
                    margin-top: -30px;
                    justify-content: flex-start;
                    padding-left: 15vh;
                    ">
              <p class="intro-text">EDC Network & Database & Predictor is a free web server that provides information on endocrine disruptors (EDCs) adverse outcome pathways (AOPs) and retrieval of trial data. It also provides a prediction service for screening potential EDCs. Through predictive modelling, statistical testing and a user-friendly web interface, EDC Network & Database & Predictor helps biologists observe novel EDCs and accelerate related research.
                <br>
                We are constantly adding new data and working on improving interfaces to EDCs information. Please check back often!
                <br>

              </p>
            </div>
            <el-row style="padding-left: 20vh">
              <el-col :span="8">
                <el-space direction="vertical">
                  <el-icon :style="{ fontSize: '12vh' }"><Document /></el-icon>
                  <p style="font-size: 2.3vh;margin-top: -5%;font-weight: bold;">
                    AOP Search
                  </p>
                </el-space>
              </el-col>
              <el-col :span="8">
                <el-space direction="vertical">
                  <el-icon :style="{ fontSize: '12vh' }"><EditPen /></el-icon>
                  <p style="font-size: 2.3vh;margin-top: -5%;font-weight: bold;">
                    Free Drawing
                  </p>
                </el-space>

              </el-col>
              <el-col :span="8">
                <el-space direction="vertical">
                  <el-icon :style="{ fontSize: '12vh' }"><DataLine /></el-icon>
                  <p style="font-size: 2.3vh;margin-top: -5%;font-weight: bold;">
                    Predict Activity
                  </p>
                </el-space>
              </el-col>
            </el-row>

          </el-col>
          <el-col :span="10">
<!--            <div style="display: flex;-->
<!--                        /*justify-content: flex-start; !* 左对齐 *!*/-->
<!--                        align-items: center; /* 垂直居中 */-->
<!--                        height: 100%;-->
<!--                        padding-left: 10vh;-->
<!--                        ">-->
<!--              <img @load="ImageLoad" v-loading="ifImageload" style="width: 600px;" src="../assets/homeinfo.jpg">-->
<!--            </div>-->
          </el-col>
        </el-row>
        <el-link style="margin-left: 15vh;margin-top: 2%" type="primary" @click="router.push('/About')" :underline="false">
          <p style="font-size: 3vh;margin-top: -5%;font-weight: bold;color: #ffffff;text-shadow: 5px 5px 5px #054079;">
            Learn More About PubChem >
          </p>

        </el-link>
      </el-main>

    </el-container>
    <footer class="footer">
      <div class="footer-content">
        <div class="footer-section">
          <h3 style="color: #f8f8f8;letter-spacing: 1px; font-size: 2vh">Copyright</h3>
            <el-divider />
          <p style="color: #ffffff;font-size: 1.8vh" >All Rights © 2024</p>
          <p style="color: #ffffff;font-size: 1.8vh">Reserved</p>
        </div>
        <div class="footer-section">
          <h3 style="color: #f8f8f8;letter-spacing: 1px;font-size: 2vh">Contact</h3>
          <el-divider />
          <p style="color: #ffffff;font-size: 1.8vh">E-mail：njushiwei@nju.edu.cn</p>
          <p style="color: #ffffff;font-size: 1.8vh">Tel：123-456-7890</p>
        </div>
        <div class="footer-section">
          <h3 style="color: #f8f8f8;letter-spacing: 1px;font-size: 2vh">Registration</h3>
          <el-divider />
          <p style="color: #ffffff;font-size: 1.8vh" >2024--</p>
          <p style="color: #ffffff;font-size: 1.8vh">苏ICP备000000号</p>
        </div>
      </div>
    </footer>



</template>

<script lang="ts" setup>

import {nextTick, onMounted, ref} from 'vue'
import router from '../router'
import axios from "axios";

const activeIndex = ref('0')


</script>

<style scoped>
:deep(.el-sub-menu .el-sub-menu__icon-arrow) {
    font-size: 2vh;
    margin-top: -0.9vh;
    position: absolute;
    left: 12vh;
    top: 50%;
    transition: transform var(--el-transition-duration);
    width: inherit;
}

:deep(.el-sub-menu .el-icon) {
  width:2vh;
  font-size: 2vh;
}
:deep(.el-menu-item [class^=el-icon]) {
  font-size: 2vh;
  margin-right: 10px; /* 修改右边距为10px */
  text-align: center; /* 修改文本水平居中对齐 */
  vertical-align: middle; /* 修改垂直对齐方式为居中 */
  width:2vh; /* 保留自定义宽度 */
}

.flex-grow {
  flex-grow: 1;
}
.intro-text {
  /* 中间介绍字体的样式*/
  font-size: 2.5vh;
  max-width: 100%;
  text-align: justify; /* 文本两端对齐 */
  line-height: 150%; /* 设置行高 */
  font-family: 'Helvetica', sans-serif;
}
.my_home-main{
  /* 设置图片作为背景 */
  background-image: url('../assets/background4.png');
  /* 背景设置为覆盖整个容器 */
  min-width: 1080px;
  margin-top: -1%;
  margin-right: -5px;
  margin-left: -5px;
  background-size: cover;
  background-position: center;
  min-height: 90vh;
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
