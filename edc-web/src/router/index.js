// import Vue from 'vue'
import { createRouter, createWebHashHistory } from "vue-router";

import Home from '../components/Home.vue';
import Login from '../components/UserInfoPage/Login.vue';
import Register from '../components/UserInfoPage/Register.vue';
import Contact from '../components/Contact.vue';
import About from '../components/About.vue';
import SearchAOP from '../components/SearchPage/SearchAOP.vue';
import SearchDATA from '../components/SearchPage/SearchDATA.vue';
import SearchDATAResult from "../components/SearchPage/SearchDATAResult.vue";
import SearchAOPResult from "../components/SearchPage/SearchAOPResult.vue";
import PredictResult from "../components/PredictPage/PredictResult.vue";
import Predict from '../components/AboutPage/Predict.vue';
import SearchPredict from "../components/SearchPage/SearchPredict.vue";
import UserCount from "@/components/UserInfoPage/UserCount.vue";
import store from "@/store/index.js";

const routes = [
    {
        path: '/',
        name: 'Home',
        component: Home,
        meta: {
            title: 'Home'
        }
    },
    {
        path: '/Register',
        name: 'Register',
        component: Register,
        meta: {
            title: 'Register'
        }
    },
    {
        path: '/Login',
        name: 'Login',
        component: Login,
        meta: {
            title: 'Login'
        }
    },
    {
        path: '/UserCount',
        name: 'UserCount',
        component: UserCount,
        meta: {
            title: 'UserCount',
            requiresAuth: true // 需要认证
        }
    },
    {
        path: '/Contact',
        name: 'Contact',
        component: Contact,
        meta: {
            title: 'Contact'
        }
    },
    {
        path: '/About',
        name: 'About',
        component: About,
        meta: {
            title: 'About'
        }
    },
    {
        path: '/SearchAOP',
        name: 'SearchAOP',
        component: SearchAOP,
        meta: {
            title: 'SearchAOP',
            requiresAuth: true // 需要认证
        }
    },
    {
        path: '/SearchDATA',
        name: 'SearchDATA',
        component: SearchDATA,
        meta: {
            title: 'SearchDATA',
            requiresAuth: true // 需要认证
        }
    },
    {
        path: '/SearchPredict',
        name: 'SearchPredict',
        component: SearchPredict,
        meta: {
            title: 'SearchPredict',
            requiresAuth: true // 需要认证
        }
    },
    {
        path: '/SearchDATAResult/:dataId',
        name: 'SearchDATAResult',
        component: SearchDATAResult,
        meta: {
            title: 'SearchDATAResult',
            requiresAuth: true // 需要认证
        }
    },
    {
        path: '/SearchAOPResult/:dataId',
        name: 'SearchAOPResult',
        component: SearchAOPResult,
        meta: {
            title: 'SearchAOPResult',
            requiresAuth: true // 需要认证
        }
    },
    {
        path: '/PredictResult/:smiles/:ifAD',
        name: 'PredictResult',
        component: PredictResult,
        meta: {
            title: 'PredictResult',
            requiresAuth: true // 需要认证
        }
    },
    {
        path: '/Predict',
        name: 'Predict',
        component: Predict,
        meta: {
            title: 'Predict',
            requiresAuth: true // 需要认证
        }
    },
];

const router = createRouter({
    history: createWebHashHistory(),
    routes
});

// 全局路由守卫
router.beforeEach((to, from, next) => {
    // 更新页面标题
    document.title = to.meta.title || '默认标题';

    const isAuthenticated = store.getters.isAuthenticated; // 从 Vuex Store 获取认证状态

    if (to.meta.requiresAuth && !isAuthenticated) {
        // 保存用户想要访问的路径，登录后可以重定向回来
        next({
            name: 'Login',
            query: { redirect: to.fullPath }
        });
    } else if (isAuthenticated && (to.name === 'Login' || to.name === 'Register')) {
        // 如果已经登录，访问登录或注册页面时重定向到首页
        next({ name: 'Home' });
    } else {
        next(); // 继续导航
    }
});

export default router;
