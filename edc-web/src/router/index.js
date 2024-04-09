// import Vue from 'vue'
import { createRouter,createWebHashHistory } from "vue-router";

import Home from '../components/Home.vue'

// Vue.use(VueRouter)
const routes = [
    {
        path:'/',
        name:'Home',
        component:Home,
        meta: {
            title: '主页'
        }
    },
  ]
const router = createRouter({
    history:createWebHashHistory(),
    routes
})
  export default router;
