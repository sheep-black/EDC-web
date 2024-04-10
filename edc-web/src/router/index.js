// import Vue from 'vue'
import { createRouter,createWebHashHistory } from "vue-router";

import Home from '../components/Home.vue'
import Contact from '../components/Contact.vue'
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
    {
        path:'/Contact',
        name:'Contact',
        component:Contact,
        meta: {
            title: '联系'
        }
    },
  ]
const router = createRouter({
    history:createWebHashHistory(),
    routes
})
  export default router;
