import { createApp } from 'vue'
import App from './App.vue'
import './assets/main.css'
import axios from "axios";
import router from './router'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
axios.defaults.baseURL="http://localhost:8080"
const app = createApp(App)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
app.use(ElementPlus)
    .use(router)
    .mount('#app')

