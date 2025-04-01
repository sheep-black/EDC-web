// import Vue from 'vue'
import { createRouter,createWebHashHistory } from "vue-router";

import Home from '../components/Home.vue'
import Login from '../components/UserInfoPage/Login.vue'
import Register from '../components/UserInfoPage/Register.vue'
import Contact from '../components/Contact.vue'
import About from '../components/About.vue'
import SearchAOP from '../components/SearchAOP.vue'
import SearchDATA from '../components/SearchDATA.vue'
import SearchDATAResult from "../components/SearchDATAResult.vue"
import SearchAOPResult from "../components/SearchAOPResult.vue"
import PredictResult from "../components/PredictResult.vue"
import Predict from '../components/Predict.vue'
import SearchPredict from "../components/SearchPredict.vue";


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
        path:'/Register',
        name:'Register',
        component:Register,
        meta: {
            title: '注册'
        }
    },
    {
        path:'/Login',
        name:'Login',
        component:Login,
        meta: {
            title: '登录'
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
        path:'/SearchPredict',
        name:'SearchPredict',
        component:SearchPredict,
        meta: {
            title: 'Predict搜索'
        }
    },
    {
        path:'/SearchDATAResult/:dataId',
        name:'SearchDATAResult',
        component:SearchDATAResult,
        meta: {
            title: 'DATA搜索结果'
        }
    },
    {
        path:'/SearchAOPResult/:dataId',
        name:'SearchAOPResult',
        component:SearchAOPResult,
        meta: {
            title: 'AOP搜索结果'
        }
    },
    {
        path: '/PredictResult/:smiles/:ifAD',
        name: 'PredictResult',
        component: PredictResult,
        meta: {
            title: 'Predict结果'
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
