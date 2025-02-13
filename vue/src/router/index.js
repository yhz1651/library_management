import Vue from 'vue'
import VueRouter from 'vue-router'
import Layout from '../views/Layout.vue'
import Cookies from "js-cookie";

Vue.use(VueRouter)

const routes = [
    /*
    * 登录
    * */
    {
        path: '/login',
        name: 'Login',
        component: () => import('@/views/login/Login.vue')
    },


    {
        path: '/',
        name: 'Layout',
        component: Layout,
        redirect: '/home', // 重定向显示主页内容
        children: [ // 子路由
            /*
            * 主页
            * */
            {
                path: 'home',
                name: 'Home',
                component: () => import('@/views/home/HomeView.vue')
            },

            /*
            * 用户模块
            * */
            {
                path: 'userList', /* path会自动拼接，注意'/'不要重复 */
                name: 'List', /* 和vue文件里的name对应 */
                component: () => import('@/views/user/List.vue')
            },
            {
                path: 'addUser',
                name: 'Add',
                component: () => import('@/views/user/Add.vue')
            },
            {
                path: 'editUser',
                name: 'Edit',
                component: () => import('@/views/user/Edit.vue')
            },
            {
                path: 'checkUser',
                name: 'Check',
                component: () => import('@/views/user/Check.vue')
            },

            /*
            * 管理员模块
            * */
            {
                path: 'adminList',
                name: 'List',
                component: () => import('@/views/admin/List.vue')
            },
            {
                path: 'addAdmin',
                name: 'Add',
                component: () => import('@/views/admin/Add.vue')
            },
            {
                path: 'editAdmin',
                name: 'Edit',
                component: () => import('@/views/admin/Edit.vue')
            },

            /*
            * 图书分类模块
            * */
            {
                path: 'categoryList',
                name: 'CategoryList',
                component: () => import('@/views/category/List.vue')
            },
            {
                path: 'addCategory',
                name: 'AddCategory',
                component: () => import('@/views/category/Add.vue')
            },
            {
                path: 'editCategory',
                name: 'EditCategory',
                component: () => import('@/views/category/Edit.vue')
            },

            //  ====  图书模块  ====
            {path: 'bookList', name: 'BookList', component: () => import('@/views/book/List.vue')},
            {path: 'addBook', name: 'AddBook', component: () => import('@/views/book/Add.vue')},
            {path: 'editBook', name: 'EditBook', component: () => import('@/views/book/Edit.vue')},

            //  ====  借书模块  ====
            {path: 'borrowList', name: 'BorrowList', component: () => import('@/views/borrow/List.vue')},
            {path: 'addBorrow', name: 'AddBorrow', component: () => import('@/views/borrow/Add.vue')},
            {path: 'editBorrow', name: 'EditBorrow', component: () => import('@/views/borrow/Edit.vue')},

            //  ====  还书模块  ====
            {path: 'returList', name: 'returList', component: () => import('@/views/retur/List.vue')},
        ]
    },

    // 404路由
    {
        path: '*',
        component: () => import('@/views/404.vue'),
    },


]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

// 路由守卫（前端，后端使用jwt，双重保护数据安全）
// 判断缓存中是否有用户数据，如果没有用户数据，则跳转到登录界面
router.beforeEach((to, from, next) => {
    if (to.path === '/login') next() // 登录界面直接放行
    const admin = Cookies.get("admin") // 从cookies中取得admin信息
    if (!admin && to.path !== '/login') return next("/login") // 如果admin为空并且访问的不是登录界面，强制退回登录界面
    // 访问除登录界面以外的界面，如果admin存在，直接放行
    next()
})

export default router
