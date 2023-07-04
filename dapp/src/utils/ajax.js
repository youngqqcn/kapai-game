import axios from 'axios'
import {networkConfig} from '@/config/networkConfig'
import { useStore } from '@/store/index'
import { storeToRefs } from 'pinia';
// aes加密
// const aes = require('./aes')
import aes from './aes'
// 当前时间戳
 var timestamp = null;
  // 随机字符串
 var nonce = aes.createNonceStr();
 import { showToast, showSuccessToast, allowMultipleToast,showFailToast } from 'vant';
// 创建axios实例
export function requestService(configs = {baseURL:networkConfig.serverUrl}) {
    const service = axios.create({
        // axios中请求配置有baseURL选项，表示请求URL公共部分
        // baseURL: networkConfig.serverUrl,
        // 超时
        timeout: networkConfig.requestTimeout,
        ...configs
    })
    // request请求拦截器
    service.interceptors.request.use(config => {    
        // const store = useStore();
        // config.headers.token = store.wallet;
        // config.headers.Authorization = `Bearer 123456789`;
        const local = localStorage.getItem("locale")
        if (local) {
            if (local == 'zh') {
                config.headers['Accept-Language'] = 'zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5'
            } else {
                config.headers['Accept-Language'] = 'en-US,en;q=0.5'
            }
        }

        return config
    }, error => {
        console.log(error)
        Promise.reject(error)
    })
    // respone响应拦截器
    service.interceptors.response.use(
        response => {
            const res = response.data;
            if(res){
              if(res.code !== 200){
                if(res.message){
                  allowMultipleToast();
                  setTimeout(()=>{
                    showFailToast(res.message);
                  },600)
                }
              }
              if(res.code === 0) {
                let data = res.data;
                try {
                  data = JSON.parse(aes.decrypt(data));
                } catch (error) {
                  console.error('解析失败');
                }
                res.data = data;
              }
              if(res.code == 401){
                const store = useStore();
                store.logout();
              }
              return res
            }else{
              allowMultipleToast();
              showFailToast('接口请求异常');
              return {}
            }
        },
        error => {
            console.log(error)
            if((error.response && error.response.status == 401)){
              console.log('未登录:');
              const store = useStore();
              store.logout();
            }
            return Promise.reject(error)
        }
    )
    return service;
}

const ajax = requestService();
const ajaxDapp = requestService({
    baseURL: networkConfig.dappUrl
});
export {ajax,ajaxDapp}