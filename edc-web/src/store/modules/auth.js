// src/store/modules/auth.js
import axios from 'axios';

const state = {
    token: localStorage.getItem('token') || '',
    user: JSON.parse(localStorage.getItem('user') || '{}'),
    status: ''
};

const getters = {
    isAuthenticated: state => !!state.token,
    authStatus: state => state.status,
    currentUser: state => state.user
};

const actions = {
    // 登录动作
    async login({ commit }, userData) {
        commit('auth_request');
        try {
            const response = await axios.post('/login', userData, {
                headers: { 'Content-Type': 'application/json' }
            });

            const token = response.data.token;
            const user = response.data.user || { userName: userData.userName };

            // 将令牌存储在本地存储中
            localStorage.setItem('token', token);
            localStorage.setItem('user', JSON.stringify(user));

            // 设置axios的Authorization头
            axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;

            commit('auth_success', { token, user });
            return response;
        } catch (error) {
            commit('auth_error');
            localStorage.removeItem('token');
            localStorage.removeItem('user');
            throw error;
        }
    },

    // 注册动作
    async register({ commit }, userData) {
        commit('auth_request');
        try {
            const response = await axios.post('/register', userData);
            const token = response.data.token;
            const user = response.data.user || { userName: userData.userName };

            localStorage.setItem('token', token);
            localStorage.setItem('user', JSON.stringify(user));

            axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;

            commit('auth_success', { token, user });
            return response;
        } catch (error) {
            commit('auth_error');
            localStorage.removeItem('token');
            localStorage.removeItem('user');
            throw error;
        }
    },

    // 登出动作
    logout({ commit }) {
        return new Promise((resolve) => {
            commit('logout');
            localStorage.removeItem('token');
            localStorage.removeItem('user');
            delete axios.defaults.headers.common['Authorization'];
            resolve();
        });
    },

    // 从本地存储中恢复认证状态
    autoLogin({ commit }) {
        const token = localStorage.getItem('token');
        if (token) {
            const user = JSON.parse(localStorage.getItem('user') || '{}');
            commit('auth_success', { token, user });
            axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        }
    }
};

const mutations = {
    auth_request(state) {
        state.status = 'loading';
    },
    auth_success(state, { token, user }) {
        state.status = 'success';
        state.token = token;
        state.user = user;
    },
    auth_error(state) {
        state.status = 'error';
        state.token = '';
        state.user = {};
    },
    logout(state) {
        state.status = '';
        state.token = '';
        state.user = {};
    }
};

export default {
    state,
    getters,
    actions,
    mutations
};

