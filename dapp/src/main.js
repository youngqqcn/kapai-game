import { createApp } from 'vue'
import { createPinia } from 'pinia'
import {networkConfig} from '@/config/networkConfig'
import { useStore } from '@/store/index'

import { createI18n } from 'vue-i18n'


import components from './components'
import App from './App.vue'
import router from './router'
import api from './api'
import utils from './utils'
import './assets/main.scss'
import basics from '@/utils/basics'
import NutUI from "@nutui/nutui";
import { Locale } from '@nutui/nutui';
import enUS from '@nutui/nutui/dist/packages/locale/lang/en-US';
import zhCn from '@nutui/nutui/dist/packages/locale/lang/zh-CN';

import "@nutui/nutui/dist/style.css";
import vant from "vant";
import { showToast } from 'vant';
import 'vant/es/toast/style';
// import { Dialog } from "vant";
// Dialog
import { showDialog } from 'vant';
import 'vant/es/dialog/style';
const pinia = createPinia()
const app = createApp(App)

// i18n
import enLocale from './i18n/en/index';
import zhLocale from './i18n/zh/index';
const messages = {
  zh: zhLocale,
  cn: zhLocale,
  en: enLocale,
  us: enLocale,
  
}

const i18n = createI18n({
  globalInjection: true, //全局生效$t
  locale: localStorage.getItem('locale') || 'zh',
  messages,
  legacy: false,
})

app.use(i18n);
// END i18n
// console.log(app.config.globalProperties.$t('pleaseSelectNation'))
app.use(basics);
app.use(vant);
app.config.globalProperties.$NetworkConfig = networkConfig;
app.use(NutUI);
// app.use(NutUI);
app.use(api);
app.use(components);
app.use(pinia);
app.use(utils);
app.use(createPinia());
app.use(router);

// Locale.use('en', localStorage.getItem('locale') || 'zh');
Locale.use('zh', zhCn);
Locale.use('en', enUS);
if(localStorage.getItem('locale') == 'zh'){
  Locale.use('zh', zhCn);
}else{
  Locale.use('en', enUS);
}

app.config.globalProperties.$Dialog = showDialog;
app.config.globalProperties.$Toast = showToast;
app.config.globalProperties.$useStore = useStore();
if(networkConfig.dev){
  const useStore = app.config.globalProperties.$useStore;
  useStore.public.equityDesc = localStorage.equityDesc || '';
  useStore.public.levelDesc = localStorage.levelDesc || '';
}
app.config.globalProperties.$useStore.public.$t = app.config.globalProperties.$t;
app.mount('#app');