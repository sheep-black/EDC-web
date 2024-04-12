<template>
  <div class="common-layout" >
    <el-container style="height: 85vh;">
      <el-header>
        <!--头部菜单栏-->
        <el-menu
            :default-active="activeIndex"
            style="min-width: 1080px;margin-left: -25px;margin-right: -25px;margin-top: -5px"
            mode="horizontal"
            background-color="#1A6B9AFF"
            text-color="#fff"
            active-text-color="#ffcc66"
            :ellipsis="false"
        >
          <el-menu-item index="0" @click="this.$router.push('/')">
            <!--      菜单左侧标志-->
            <el-icon
                color="#409efc"
                :size="30">
              <SwitchFilled />
            </el-icon>
            <p style="font-size: 18px; font-style: italic; margin-left: 6px;font-weight: bold;text-shadow: 1px 1px 2px black;">
              EDC  Database & Network & Predictor
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
            <el-menu-item index="1-1" style="justify-content: center;">
              <p style="justify-content: center;">EDC-AOP</p>
            </el-menu-item>
            <el-menu-item index="1-2" style="justify-content: center;">
              <p style="justify-content: center;">EDC-DATA</p>
            </el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="2">
            <template #title>
              <el-icon :size="20"><Odometer /></el-icon>
              <p style="font-size: 16px;text-shadow: 0px 0px 2px black;">
                Predict
              </p>
            </template>
            <el-menu-item index="2-1" style="justify-content: center;">
              <p style="justify-content: center;">Qualitative predictor</p>
            </el-menu-item>
            <el-menu-item index="2-2" style="justify-content: center;">
              <p style="justify-content: center;">Quantitative predictor</p>
            </el-menu-item>
          </el-sub-menu>
          <el-menu-item index="3" @click="this.$router.push('/About')">
            <template #title>
              <el-icon :size="20"><InfoFilled /></el-icon>
              <p style="font-size: 16px;text-shadow: 0px 0px 2px black;">
                About
              </p>
            </template>
          </el-menu-item>
          <el-menu-item index="4" @click="this.$router.push('/Contact')">
            <template #title>
              <el-icon :size="20"><UserFilled /></el-icon>
              <p style="font-size: 16px;text-shadow: 0px 0px 2px black;">
                Contact
              </p>
            </template>
          </el-menu-item>
        </el-menu>
      </el-header>
      <el-container style="background-size: cover;
                           background-position: center;
                           height: 105%;"
                           >
        <el-aside width="220px">
          <el-menu
              default-active="EDC AOP Network"
              class="el-menu-vertical-demo"
              @select="SelectAbout"
              style="height: 100%;"
              active-text-color="#1A6B9AFF"
          >
            <el-menu-item index="EDC AOP Network" @click="showContent('Network')">
              <template #title>
                <el-icon><share /></el-icon>
                <p style="font-size: 16px;text-shadow: 0px 0px 1px black;">
                  EDC AOP Network
                </p>
              </template>
            </el-menu-item>
            <el-menu-item index="EDC Database" @click="showContent('Database')">
              <template #title>
                <el-icon><coin /></el-icon>
                <p style="font-size: 16px;text-shadow: 0px 0px 1px black;">
                  EDC Database
                </p>
              </template>
            </el-menu-item>
            <el-menu-item index="EDC Predictor" @click="showContent('Predict')">
              <template #title>
                <el-icon><DataLine /></el-icon>
                <p style="font-size: 16px;text-shadow: 0px 0px 1px black;">
                  EDC Predictor
                </p>
              </template>
            </el-menu-item>
          </el-menu>
        </el-aside>
        <el-main style="height: 100%;min-width: 900px">
          <component :is="selectedContentComponent" />
        </el-main>
      </el-container>

    </el-container>

  </div>

</template>

<script lang="ts" setup>

import { ref, Ref, onMounted } from 'vue';
import Content1 from './Network.vue';
import Content2 from './Database.vue';
import Content3 from './Predict.vue';
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
  }
}
// 钩子函数 默认加载第一个界面
onMounted(() => {
  showContent('Network');
});
</script>

<style>
.flex-grow {
  flex-grow: 1;
}



</style>
