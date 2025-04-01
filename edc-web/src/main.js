import { createApp } from 'vue';
import App from './App.vue';
import './assets/main.css';
import axios from "axios";
import router from './router';
import * as ElementPlusIconsVue from '@element-plus/icons-vue';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import store from './store'; // 导入 Vuex Store
import './plugins/axios'; // 导入我们配置的axios拦截器

// 设置axios基础URL
// axios.defaults.baseURL="http://49.234.4.144:7777"
// axios.defaults.baseURL="https://edc.sheepblack.cn:7777"//部署了ssl证书的情况
axios.defaults.baseURL="http://localhost:8888"

const app = createApp(App);

// 注册Element Plus图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component);
}

// 尝试自动登录（从localStorage恢复认证状态）
store.dispatch('autoLogin');


// 确保Vuex先于路由初始化
app.use(store)
    .use(ElementPlus)
    .use(router)
    .mount('#app');
