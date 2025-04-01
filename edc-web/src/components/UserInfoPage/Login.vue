<template>
  <el-container style="height: 100vh; display: flex; justify-content: center; align-items: center;" class="my_login">
    <el-card style="width: 400px;">
      <template #header>
        <div style="display: flex; justify-content: center;">
          <span>
            <strong>EDC-Web Login</strong>
          </span>
        </div>
      </template>
      <el-form :model="form" ref="formRef" label-width="80px">
        <el-form-item label="Username" prop="username" :rules="rules.username">
          <el-input v-model="form.username" />
        </el-form-item>

        <el-form-item label="Password" prop="password" :rules="rules.password">
          <el-input type="password" v-model="form.password" />
        </el-form-item>
      </el-form>

      <el-link type="primary" @click="goToRegister" :underline="false" style="display: flex; justify-content: left;">
        <p style="font-size: 12px;margin-top: -5%;margin-bottom: -5%;">
            Don't have an account? Register here
        </p>
      </el-link>
      <template #footer>
        <el-button type="primary" plain @click="submitForm" style="width: 100%;">Login</el-button>

      </template>
    </el-card>
  </el-container>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';
import { ElMessage } from "element-plus";
import router from "@/router/index.js"; // 导入路由

const form = ref({
  username: '',
  password: ''
});

const rules = {
  username: [{ required: true, message: 'Please enter your username', trigger: 'blur' }],
  password: [{ required: true, message: 'Please enter your password', trigger: 'blur' }]
};

const formRef = ref(null);

const submitForm = async () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      // 创建用户数据对象
      const userData = {
        userName: form.value.username,
        password: form.value.password
      };
      try {
        // 发送 POST 请求到后端进行登录
        console.info(userData);
        const response = await axios.post('/login', userData, {
          headers: {
            'Content-Type': 'application/json'
          }
        });
        console.info(response.data); // 打印响应
        ElMessage.success('Login Success！'); // 显示成功消息
        router.push('/'); // 登录成功后跳转到主页
      } catch (error) {
        console.error('登录失败:', error);
        // 检查 error 对象是否包含 response
        if (error.response && error.response.data && error.response.data.message) {
          ElMessage.error(error.response.data.message); // 显示后端返回的错误信息
        } else {
          ElMessage.error('Login Error！'); // 默认错误信息
        }
      }
    } else {
      console.log('error submit!!');
      return false;
    }
  });
};

// 跳转到注册页面
const goToRegister = () => {
  router.push('/register'); // 确保路由配置中有注册页面的路径
};
</script>

<style scoped>
.el-card {
  padding: 20px;
}

.my_login {
  display: flex;
  background-image: url('../../assets/back.png');
  min-width: 1080px;
  margin-top: -1%;
  margin-right: -5px;
  margin-left: -5px;
  background-size: cover;
  background-position: center;
}
</style>
