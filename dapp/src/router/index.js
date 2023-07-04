import { createRouter, createWebHistory } from 'vue-router'
import indexHome from '../views/index.vue'
import home from '../views/home.vue'
const tabsMateDef = {
	leftArrow: false, //是否展示返回
	headerShow: false, //顶部栏需要展示
};

const pageDef = {
	leftArrow: true, //是否展示返回
	headerShow: true, //顶部栏需要展示
};
const pageDef2 = {
	leftArrow: false, //是否展示返回
	headerShow: true, //顶部栏需要展示
};
let routes = [
  {
    path: '/',
    name: 'index',
    component: indexHome,
    meta: {
      ...tabsMateDef,
      title: '首页',
      i18n:'home'
    }
  },
  {
    path: '/home',
    name: 'home',
    component: home,
    meta: {
      ...pageDef2,
      title: '首页',
      i18n:'home'
    }
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('../views/login.vue'),
    meta: {
      ...tabsMateDef,
      title: '登陆',
      i18n:'login'
    }
  },
  {
    path: '/bindingInviter',
    name: 'bindingInviter',
    component: () => import('../views/bindingInviter.vue'),
    meta: {
      ...tabsMateDef,
      title: '绑定邀请人',
      i18n:'bindingInviter'
    }
  },
  {
    path: '/selectData',
    name: 'selectData',
    component: () => import('../views/selectData.vue'),
    meta: {
      ...tabsMateDef,
      title: '选择数据',
      i18n:'selectData'
    }
  },
  {
    path: '/castOnOoutwell',
    name: 'castOnOoutwell',
    component: () => import('../views/castOnOoutwell.vue'),
    meta: {
      ...pageDef,
      title: '铸造',
      i18n:'castOnOoutwell'
    }
  },
  {
    path: '/team',
    name: 'team',
    component: () => import('../views/team.vue'),
    meta: {
      ...pageDef,
      title: '我的团队',
      i18n:'team'
    }
  },
  {
    path: '/hashPowerParticulars',
    name: 'hashPowerParticulars',
    component: () => import('../views/hashPowerParticulars.vue'),
    meta: {
      ...pageDef,
      title: '明细', // 算力明细
      i18n:'hashPowerParticulars'
    }
  },
  {
    path: '/myToken',
    name: 'myToken',
    component: () => import('../views/myToken.vue'),
    meta: {
      ...pageDef,
      title: '我的令牌',
      i18n:'myToken'
    }
  },
  {
    path: '/dataQuery',
    name: 'dataQuery',
    component: () => import('../views/dataQuery.vue'),
    meta: {
      ...pageDef,
      title: '数据查询',
      i18n:'dataQuery'
    }
  },
  {
    path: '/tokenDetails',
    name: 'tokenDetails',
    component: () => import('../views/tokenDetails.vue'),
    meta: {
      ...pageDef,
      title: '详情', // 令牌详情
      i18n:'tokenDetails'
    }
  },
  {
    path: '/nodePage',
    name: 'nodePage',
    component: () => import('../views/nodePage.vue'),
    meta: {
      ...pageDef,
      title: '节点',
      i18n:'nodePage'
    }
  },
  {
    path: '/equityPage',
    name: 'equityPage',
    component: () => import('../views/nodePage/equityPage.vue'),
    meta: {
      ...pageDef,
      title: '权益说明', // 订单
      i18n:'equityPage'
    }
  },
  {
    path: '/nodeOrder',
    name: 'nodeOrder',
    component: () => import('../views/nodeOrder.vue'),
    meta: {
      ...pageDef,
      title: '订单', // 订单
      i18n:'nodeOrder'
    }
  },
  {
    path: '/levelDesc',
    name: 'levelDesc',
    component: () => import('../views/team/levelDesc.vue'),
    meta: {
      ...pageDef,
      title: '升级条件', // 订单
      i18n:'levelDesc'
    }
  },
]
const router = createRouter({
  history: createWebHistory(import.meta.env.VITE_APP_BASE_URL),
  // history: createWebHistory('/h5/'),
  routes: routes
})
router.beforeEach((to, from, next) => {
  if(to.name !== from.name){
    window.document.title = to.meta.title || ''
    next()
  }
})
export default router
