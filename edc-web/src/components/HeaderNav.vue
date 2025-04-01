<!-- src/components/HeaderNav.vue -->
<template>
  <el-header>
    <!--        头部菜单栏-->
    <el-menu
        :default-active="activeIndex"
        style="min-width: 1080px; margin-left: -25px;margin-right: -25px;margin-top: -8px"
        mode="horizontal"
        background-color="#3a3b3d"
        text-color="#fff"
        active-text-color="#ffcc66"
        :ellipsis="false"
    >
      <el-menu-item index="0" @click="router.push('/')">
        <!--      菜单左侧标志-->
        <img src="@/assets/network.svg" style="height: 45px; width: 45px;" alt="Your Icon" />
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
          <el-icon :size="20"><Message /></el-icon>
          <p style="font-size: 16px;text-shadow: 0px 0px 2px black;">
            Contact
          </p>
        </template>
      </el-menu-item>

      <!-- 条件渲染：未登录显示Login，已登录显示用户信息 -->
      <template v-if="!isAuthenticated">
        <el-menu-item index="5" @click="router.push('/Login')">
          <template #title>
            <el-icon :size="20"><UserFilled /></el-icon>
            <p style="font-size: 16px;text-shadow: 0px 0px 2px black;">
              Login
            </p>
          </template>
        </el-menu-item>
      </template>
      <template v-else>
        <el-sub-menu index="5">
          <template #title>
            <el-icon :size="20"><UserFilled /></el-icon>
            <p style="font-size: 16px;text-shadow: 0px 0px 2px black;">
              {{ userName }}
            </p>
          </template>
          <el-menu-item index="5-1" style="justify-content: center;" @click="handleLogout">
            <p style="justify-content: center;"><el-icon><SwitchButton /></el-icon>Login out</p>
          </el-menu-item>
          <el-menu-item index="5-2" style="justify-content: center;" @click="router.push('/UserCount')">
            <p style="justify-content: center;"><el-icon><SwitchButton /></el-icon>My Count</p>
          </el-menu-item>
        </el-sub-menu>
      </template>
    </el-menu>
  </el-header>
</template>

<script lang="ts" setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { Search, Odometer, InfoFilled, UserFilled, SwitchButton, Message } from '@element-plus/icons-vue'
import { useStore } from 'vuex'
import { ElMessageBox } from 'element-plus'

const router = useRouter()
const route = useRoute()
const store = useStore()

// 根据当前路由设置激活菜单项
const activeIndex = computed(() => {
  const path = route.path
  if (path === '/') return '0'
  if (path.includes('/Search')) return '1'
  if (path === '/Predict') return '2'
  if (path === '/About') return '3'
  if (path === '/Contact') return '4'
  if (path === '/Login' || path === '/UserCount') return '5'
  return '0'
})

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
</script>

<style scoped>
.flex-grow {
  flex-grow: 1;
}
</style>
