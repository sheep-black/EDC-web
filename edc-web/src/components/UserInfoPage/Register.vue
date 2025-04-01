<template>
  <el-container style="height: 100vh; display: flex; justify-content: center; align-items: center;" class="my_login">
    <el-card style="width: 400px;">
      <template #header>
        <div style="display: flex; justify-content: center;">
          <span>
            <strong>EDC-Web Register</strong>
          </span>
        </div>
      </template>
      <el-form :model="form" ref="formRef" label-width="80px">
        <el-form-item label="UserName" prop="username" :rules="rules.username">
          <el-input v-model="form.username" />
        </el-form-item>

        <el-form-item label="Email" prop="email" :rules="rules.email">
          <el-input v-model="form.email" />
        </el-form-item>

        <el-form-item label="Password" prop="password" :rules="rules.password">
          <el-input type="password" v-model="form.password" />
        </el-form-item>

        <el-form-item label="Confirm" prop="confirmPassword" :rules="rules.confirmPassword">
          <el-input type="password" v-model="form.confirmPassword" />
        </el-form-item>

      </el-form>
      <template #footer>
        <el-button type="primary" plain @click="submitForm" style="width: 100%;">Register</el-button>
      </template>
    </el-card>
  </el-container>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';
import {ElMessage} from "element-plus";
import router from "@/router/index.js"; // 导入 Axios

const form = ref({
  username: '',
  email: '',
  password: '',
  confirmPassword: ''
});

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== form.value.password) {
    callback(new Error('The two passwords do not match!'));
  } else {
    callback();
  }
};

const rules = {
  username: [{ required: true, message: 'Please enter your username', trigger: 'blur' }],
  email: [{ required: true, message: 'Please enter your email', trigger: 'blur' }],
  password: [{ required: true, message: 'Please enter your password', trigger: 'blur' }],
  confirmPassword: [
    { required: true, message: 'Please confirm your password', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
};

const formRef = ref(null);

const submitForm = async () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      // 创建用户数据对象
      const userData = {
        userName: form.value.username,
        email: form.value.email,
        password: form.value.password
      };
      try {
        // 发送 POST 请求到后端
        console.info(userData);
        const response = await axios.post('/register', userData, {
          headers: {
            'Content-Type': 'application/json'
          }
        });
        console.info(response.data); // 打印响应
        ElMessage.success('Register Success！'); // 使用 success 而不是 error
        router.push('/');
      } catch (error) {
        console.error('注册失败:', error);
        // 检查 error 对象是否包含 response
        if (error.response && error.response.data && error.response.data.message) {
          ElMessage.error(error.response.data.message); // 显示后端返回的错误信息
        } else {
          ElMessage.error('Register Error！'); // 默认错误信息
        }
      }
    } else {
      console.log('error submit!!');
      return false;
    }
  });
};
</script>

<style scoped>
.el-card {
  padding: 20px;
}
.my_login{
  /* 设置图片作为背景 */
  display: flex;
  background-image: url('../../assets/back.png');
  /* 背景设置为覆盖整个容器 */
  min-width: 1080px;
  margin-top: -1%;
  margin-right: -5px;
  margin-left: -5px;
  background-size: cover;
  background-position: center;
}
</style>
