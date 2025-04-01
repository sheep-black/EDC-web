<template>
  <div class="common-layout" >
    <div class="page-container">
      <!-- 使用导航栏组件 -->
      <HeaderNav />
      <el-container class="about-main">
        <el-aside>
          <el-menu
              default-active="Basic Information"
              class="el-menu-vertical-demo"
              @select="SelectAbout"
              style="height: 100%;width: 100%"
              active-text-color="#1A6B9AFF"
          >
            <el-menu-item index="Basic Information" @click="showContent('BasicInfo')" style="display: flex;">
              <template #title>
                <el-icon><info-filled /></el-icon>
                <p style="font-size: 18px;font-weight: bold;">
                  Basic Information
                </p>
              </template>
            </el-menu-item>
            <el-menu-item index="EDC AOP Network" @click="showContent('Network')" style="display: flex;">
              <template #title>
                <el-icon><share /></el-icon>
                <p style="font-size: 18px;font-weight: bold;">
                  EDC AOP Network
                </p>
              </template>
            </el-menu-item>
            <el-menu-item index="EDC Database" @click="showContent('Database')" style="display: flex;">
              <template #title>
                <el-icon><coin /></el-icon>
                <p style="font-size: 18px;font-weight: bold;">
                  EDC Database
                </p>
              </template>
            </el-menu-item>
            <el-menu-item index="EDC Predictor" @click="showContent('Predict')" style="display: flex;">
              <template #title>
                <el-icon><DataLine /></el-icon>
                <p style="font-size: 18px;font-weight: bold;">
                  EDC Predictor
                </p>
              </template>
            </el-menu-item>
          </el-menu>
        </el-aside>
        <el-main style="height: 100%;justify-content: center;">
          <component :is="selectedContentComponent"/>
        </el-main>
      </el-container>
    </div>
    <FooterNav />
  </div>

</template>

<script lang="ts" setup>
import router from '../router'
import { ref, Ref, onMounted } from 'vue';
import Content1 from './AboutPage/Network.vue';
import Content2 from './AboutPage/Database.vue';
import Content3 from './AboutPage/AboutPredict.vue';
import Content4 from './AboutPage/BasicInfo.vue';
import {InfoFilled} from "@element-plus/icons-vue";
import HeaderNav from "@/components/HeaderNav.vue";
import FooterNav from "@/components/FooterNav.vue";
const activeIndex = ref('3')
const selectedContent = ref('');
const selectedContentComponent = ref('');

const SelectAbout = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}
const showContent = (key :string) =>{
  selectedContent.value = key;
  if (key === 'Network') {
    selectedContentComponent.value = <string>Content1;
  } else if (key === 'Database') {
    selectedContentComponent.value = <string>Content2;
  } else if (key === 'Predict') {
    selectedContentComponent.value = <string>Content3;
  } else if(key==='BasicInfo'){
    selectedContentComponent.value = <string>Content4;
  }
}
// 钩子函数 默认加载第一个界面
onMounted(() => {
  showContent('BasicInfo');
});
</script>

<style>
.flex-grow {
  flex-grow: 1;
}
.about-main{
  flex: auto;
  justify-content: center;
  background-size: cover;
  background-position: center;
  height: 80vh;
}


</style>
