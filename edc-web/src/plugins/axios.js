// src/plugins/axios.js
import axios from 'axios';
import store from '@/store';
import router from '@/router';

// 请求拦截器

/**
在每个请求发送前，检查 Vuex store 中是否存在认证 token
如果 token 存在，自动将其添加到请求头的 Authorization 字段中，使用 "Bearer" 方案
这样用户登录后，所有的 API 请求都会自动携带 JWT token，不需要在每个请求中手动添加
**/
axios.interceptors.request.use(
    config => {
        const token = store.state.auth.token;
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

// 响应拦截器

/**
 监听所有响应错误
 特别检查 401（未授权）错误，这通常表示 token 已过期或无效
 使用 _retry 标记确保错误处理逻辑只执行一次，避免循环
 当遇到 401 错误时：
 调用 store 中的 logout 方法清除用户会话信息
 将用户重定向到登录页面
 将当前路径作为查询参数传递，以便用户登录后可以返回原页面
 **/
axios.interceptors.response.use(
    response => {
        return response;
    },
    error => {
        const originalRequest = error.config;

        // 如果响应状态码是401（未授权）且尚未尝试过重新请求
        if (error.response && error.response.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true;

            // 登出用户
            store.dispatch('logout').then(() => {
                // 重定向到登录页面
                router.push({
                    name: 'Login',
                    query: { redirect: router.currentRoute.value.fullPath }
                });
            });
        }

        return Promise.reject(error);
    }
);

export default axios;

