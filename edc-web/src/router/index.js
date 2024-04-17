// import Vue from 'vue'
import { createRouter,createWebHashHistory } from "vue-router";

import Home from '../components/Home.vue'
import Contact from '../components/Contact.vue'
import About from '../components/About.vue'
import SearchAOP from '../components/SearchAOP.vue'
import SearchDATA from '../components/SearchDATA.vue'
import SearchDATAResult from "@/components/SearchDATAResult.vue"
import Predict from '../components/Predict.vue'

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
    {
        path:'/About',
        name:'About',
        component:About,
        meta: {
            title: '关于'
        }
    },
    {
        path:'/SearchAOP',
        name:'SearchAOP',
        component:SearchAOP,
        meta: {
            title: 'AOP搜索'
        }
    },
    {
        path:'/SearchDATA',
        name:'SearchDATA',
        component:SearchDATA,
        meta: {
            title: 'DATA搜索'
        }
    },
    {
        path:'/SearchDATAResult',
        name:'SearchDATAResult',
        component:SearchDATAResult,
        meta: {
            title: 'DATA搜索结果'
        }
    },
    {
        path:'/Predict',
        name:'Predict',
        component:Predict,
        meta: {
            title: '预测'
        }
    },

  ]
const router = createRouter({
    history:createWebHashHistory(),
    routes
})
  export default router;
