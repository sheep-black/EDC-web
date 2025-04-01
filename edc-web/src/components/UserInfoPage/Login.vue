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
          <el-input type="password" v-model="form.password" show-password @keyup.enter="submitForm" />
        </el-form-item>
      </el-form>

      <el-link type="primary" @click="goToRegister" :underline="false" style="display: flex; justify-content: left;">
        <p style="font-size: 12px;margin-top: -5%;margin-bottom: -5%;">
          Don't have an account? Register here
        </p>
      </el-link>
      <template #footer>
        <el-button type="primary" plain @click="submitForm" style="width: 100%;" :loading="loading">Login</el-button>
      </template>
    </el-card>
  </el-container>
</template>

<script setup>
import { ref } from 'vue';
import { useStore } from 'vuex';
import { ElMessage } from "element-plus";
import { useRouter, useRoute } from 'vue-router';

const store = useStore();
const router = useRouter();
const route = useRoute();

const form = ref({
  username: '',
  password: ''
});

const loading = ref(false);

const rules = {
  username: [{ required: true, message: 'Please enter your username', trigger: 'blur' }],
  password: [{ required: true, message: 'Please enter your password', trigger: 'blur' }]
};

const formRef = ref(null);

const submitForm = async () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true;

      // 创建用户数据对象
      const userData = {
        userName: form.value.username,
        password: form.value.password
      };

      try {
        // 使用Vuex action进行登录
        await store.dispatch('login', userData);

        ElMessage.success('Login Success!');

        // 获取重定向路径（如果存在）
        const redirectPath = route.query.redirect || '/';
        // 添加短暂延迟，确保状态已更新
        setTimeout(() => {
          router.push(redirectPath);
        }, 100);
      } catch (error) {
        console.error('Login failed:', error);

        // 检查 error 对象是否包含 response
        if (error.response && error.response.data && error.response.data.message) {
          ElMessage.error(error.response.data.message); // 显示后端返回的错误信息
        } else {
          ElMessage.error(error.message || 'Login Error!'); // 显示错误信息
        }
      } finally {
        loading.value = false;
      }
    } else {
      console.log('error submit!!');
      return false;
    }
  });
};

// 跳转到注册页面
const goToRegister = () => {
  router.push('/register');
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

