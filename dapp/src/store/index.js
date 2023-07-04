import { defineStore } from 'pinia'
import { getSystemConfig } from "@/api/index.js";
import router from '@/router/index.js';
// useStore 可以是 useUser、useCart 之类的任何东西
// 第一个参数是应用程序中 store 的唯一 id
export const useStore = defineStore('main', {
    state: () => {
        return {
            dev: import.meta.env.DEV,
            isAdmin: true,
            wallet: '',
            userInfo: null,
            systemConfig: null,
            public: {},
            logout:()=>{
              useStore().wallet = '';
              // 跳转登录
              router.push({path:'/login'});
            }
        };
    },
    getters: {},
    actions: {
      // 同步请求getSystemConfig
      async getSystemConfigAsync(){
        const res = await getSystemConfig();
        if(res.code === 0){
          this.systemConfig = res.data;
        }
      }
    },
})
