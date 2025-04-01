<template>
  <div class="common-layout">
    <div class="page-container">
      <!-- 使用导航栏组件 -->
      <HeaderNav />
      <el-main class="UserProfile-main">
        <p style="font-size: 45px;
                  margin-left: 6px;
                  font-weight: bold;
                  justify-content: center;
                  display: flex;
                  color: #1e1a1a;
                  text-shadow: 2px 2px 2px #ffcc66;"
        >
          User Profile
        </p>

        <!-- 简化的用户信息卡片 -->
        <div class="profile-section">
          <div v-if="loading" class="loading-container">
            <el-skeleton :rows="4" animated />
          </div>
          <div v-else-if="error" class="error-container">
            <el-alert
                title="Failed to load user information"
                type="error"
                description="Please try refreshing the page or contact support if the problem persists."
                show-icon
                :closable="false"
            />
          </div>
          <div v-else class="profile-card">
            <el-row>
              <el-col :span="6">
                <div class="user-avatar">
                  <el-avatar :size="100" :src="userInfo.avatar">
                    <el-icon><UserFilled /></el-icon>
                  </el-avatar>
                </div>
              </el-col>
              <el-col :span="18">
                <el-descriptions title="User Information" :column="1" border>
                  <el-descriptions-item label="Username">{{ userInfo.username }}</el-descriptions-item>
                  <el-descriptions-item label="Email">{{ userInfo.email }}</el-descriptions-item>
                  <el-descriptions-item label="Region">{{ userInfo.region }}</el-descriptions-item>
                  <el-descriptions-item label="Remaining Predictions">
                    <el-tag type="success" size="large">{{ userInfo.remainingPredictions }}</el-tag>
                  </el-descriptions-item>
                </el-descriptions>
              </el-col>
            </el-row>
          </div>
        </div>
      </el-main>
    </div>
    <FooterNav />
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue'
import router from '../../router'
import { Search, Odometer, InfoFilled, UserFilled } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'
import HeaderNav from "@/components/HeaderNav.vue";
import FooterNav from "@/components/FooterNav.vue";

const activeIndex = ref('4')
const loading = ref(true)
const error = ref(false)

// 简化的用户信息数据
const userInfo = reactive({
  username: '',
  email: '',
  region: '',
  remainingPredictions: 0,
  avatar: '',
})

// 处理菜单选择
const handleSelect = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}

// 获取用户信息
const fetchUserInfo = async () => {
  loading.value = true
  error.value = false

  try {
    // 修改为您后端实际的API端点
    const response = await axios.get('/getUserInfoByToken', {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`
      }
    })

    // 更新前端数据结构以匹配后端响应格式
    const data = response.data
    if (data.success) {
      const user = data.user  // 后端将用户信息封装在user属性中

      // 匹配后端返回的属性名
      userInfo.username = user.userName  // 注意属性名可能不同
      userInfo.email = user.email
      userInfo.region = user.region
      userInfo.remainingPredictions = user.predicttimes  // 属性名不同

      // 如果后端返回了头像URL
      if (user.avatar) {
        userInfo.avatar = user.avatar
      }

      loading.value = false
    } else {
      // 处理后端返回的错误信息
      throw new Error(data.message || 'Failed to get user information')
    }
  } catch (err) {
    console.error('Failed to fetch user info:', err)
    error.value = true
    loading.value = false
    ElMessage.error(err.message || 'Failed to load user information')
  }
}

// 组件挂载时获取用户信息
onMounted(() => {
  fetchUserInfo()
})

</script>

<style>
.flex-grow {
  flex-grow: 1;
}

.UserProfile-main {
  /* 设置图片作为背景 */
  background-image: url('../../assets/back-none.png');
  /* 背景设置为覆盖整个容器 */
  min-height: 80vh;
  min-width: 1080px;
  background-size: cover;
  background-position: center;
}

.profile-section {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.profile-card {
  background-color: rgba(255, 255, 255, 0.9);
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  padding: 30px;
  width: 80%;
  max-width: 800px;
}

.loading-container,
.error-container {
  background-color: rgba(255, 255, 255, 0.9);
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  padding: 30px;
  width: 80%;
  max-width: 800px;
}

.user-avatar {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
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
