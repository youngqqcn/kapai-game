<!--<script setup>
import { RouterLink, RouterView } from 'vue-router'
import HelloWorld from './components/HelloWorld.vue'
</script> -->

<template>
  <Navbar v-if="$route.meta && $route.meta.headerShow" :title="$t('routes.'+$route.meta.i18n)" :isBack="$route.meta && $route.meta.leftArrow">
    <template #right>
      <div class="share">
        <div @click="share()">
          <img src="@/assets/imgs/icon5.png" alt="">
        </div>
      </div>
    </template>
  </Navbar>
  <!-- <header>
    <img alt="Vue logo" class="logo" src="@/assets/logo.svg" width="125" height="125" />

    <div class="wrapper">
      <HelloWorld msg="You did it!" />

      <nav>
        <RouterLink to="/">Home</RouterLink>
        <RouterLink to="/about">About</RouterLink>
      </nav>
    </div>
  </header> -->
  <RouterView v-if="!show"/>
  <van-overlay :show="show" style="--van-overlay-background:rgba(0, 0, 0, 0)">
    <div class="overlay-index" @click.stop>
      <van-loading />
    </div>
  </van-overlay>
</template>
<script>
import { useStore } from '@/store/index'
import { provide,readonly,ref,reactive, toRefs,onMounted, getCurrentInstance, watch, nextTick } from "vue"
import { useRouter, useRoute } from "vue-router";
import { showToast, showSuccessToast, showFailToast } from 'vant';
export default {
  setup(){
    let userInfo = ref({});
    let pubNowItem = ref({});
    let payPrice = ref({
      tokenA: 0,
      tokenB: 0,
      epRatio: 0,
    });
    const router = useRouter();
    const route = useRoute();
    const store = useStore();
    const { appContext } = getCurrentInstance();
    // const ajax = appContext.config.globalProperties.$ajax;
    const { $NetworkConfig: NetworkConfig, $ajax: ajax, $t } = appContext.config.globalProperties;
     // 响应式数据
    const state = reactive({
      show: true,
    })
    // 监听路由变化
    watch(() =>router.currentRoute.value.path,(newValue,oldValue)=> {
      if(newValue === '/home'){
        const isBindingInviter = sessionStorage.getItem('isBindingInviter');
        if(isBindingInviter === '-1'){
          router.replace('/bindingInviter'); // 解决在【绑定邀请人】用户强制返回首页的问题
        }
      }
    },{ immediate: true })
    const methods = {
      share(){
        // 复制链接
        const url = window.location.origin+'/?share='+store.wallet;
        const input = document.createElement('input');
        input.setAttribute('style', 'position: fixed;bottom: 1000px;left:-1000px;');
        input.setAttribute('readonly', 'readonly');
        input.setAttribute('value', url);
        document.body.appendChild(input);
        input.select();
        input.setSelectionRange(0, 9999);
        // copy链接到 剪切板
        if (document.execCommand('copy')) {
          document.body.removeChild(input);
          showToast({
            // message: '分享链接已复制到剪切板',
            message: this.$t('public.toast.sharesuccess'),
            duration: 2000,
          });
        }
      },
      updateUserInfo(){
        return new Promise((resolve,reject) => {
          ajax.getUserInfo().then((res) => {
            if(res.code === 200) {
              store.userInfo = res.data;
              store.wallet = res.data.wallet;
              userInfo.value = res.data;
            }
            resolve(res);
          }).finally(() => {
            state.show = false;
          })
        })
      },
      init(){
        const query = window.location.search.urlQuery().query;
        console.log('初始化....');
          console.log('NetworkConfig.dev: ', NetworkConfig.dev);
        if(!NetworkConfig.dev){
          const wallet_login = sessionStorage.getItem('wallet_login');
          if(wallet_login == 'true'){
            ajax.logout().then((res) => {
              console.log('退出登录: ', res);
            })
          }
          router.push({ path:'login',query:query });
          setTimeout(() => {
            state.show = false;
          });
        }else{
          ajax.getUserInfo().then((res) => {
            // console.log('用户信息: ', res);
            if(res.code === 200) {
              store.userInfo = res.data;
              store.wallet = res.data.wallet;
              userInfo.value = res.data;
              methods.getp();
              if(res.data.superior){
                sessionStorage.setItem('isBindingInviter','');
                router.replace({ path:'home' })
              }else{
                sessionStorage.setItem('isBindingInviter','-1');
                router.replace('/bindingInviter');
              }
            }else{
              router.push({ path:'login' })
            }
          }).finally(() => {
            setTimeout(() => {
              state.show = false;
            });
          })
        }
      },
      getp(){
        ajax.getPrice().then((res) => {
          console.log('支付方式res: ', res);
          if(res.code == 200){
            const { epRatio, tokenA, tokenB } = res.data;
            console.log('支付方式: ', epRatio, tokenA, tokenB);
            payPrice.value.epRatio = parseFloat(epRatio);
            payPrice.value.tokenA = parseFloat(tokenA);
            payPrice.value.tokenB = parseFloat(tokenB);
          }
        })
      }
    }
    methods.init();
    provide('infoUser',userInfo);
    // 虚拟刷新
    provide('vRefresh',()=>{
      state.show = true;
      nextTick(() => {
        state.show = false;
      });
    });
    provide('updateUserInfo',methods.updateUserInfo);
    
    provide('num4',(num)=>{
      // 保留4位小数
      return Number(Number(num).toFixed(4));
    });
    provide('pubPayPrice',payPrice); // 支付方式
    provide('setPubPayPrice',()=>{
      methods.getp();
    });
    provide('pubNowItem',pubNowItem); // 临时缓存item
    provide('setPubNowItem',(data)=>{
      pubNowItem.value = data;
    });
    provide('setInfoUser',(data)=>{
      store.userInfo = data;
      store.wallet = data.wallet;
      userInfo.value = data;
    });
    provide('setInfoUserSuperior',(data)=>{
      store.userInfo.superior = data;
      userInfo.value.superior = data;
    });
    provide('getTypeSourceName',(type, source)=> {
      // console.log('type, source: ', type, source);
      // type=2 source=5 购买节点增加冻结数量
      // type=2 source=6 节点直推释放
      // type=2 source=7 节点动态释放
      // type=2 source=9 节点分红
            var name = "",key = '';
            switch (type) {
                case 1:
                    switch (source) {
                        case 1:  name = "铸造";key='public.zhuzao';  break;
                        case 2:  name = "直推";key='public.zhitui';  break;
                    }
                    break
                case 3:
                    switch (source) {
                        case 1:  name = "提现";key='public.tixian';  break;
                        case 2:  name = "签到";key='public.qiandao';  break;
                        case 3:  name = "静态释放";key='public.jingtaishifang';  break;
                        case 4:  name = "动态释放";key='public.dongtaishifang';  break;
                        case 9:  name = "SOUL节点分红";key='public.SOUL_jdfh';  break;
                    }
                    break
                case 2:
                  switch (source) {
                        case 1:  name = "提现";key='public.tixian';  break;
                        case 2:  name = "签到";key='public.qiandao';  break;
                        case 3:  name = "静态释放";key='public.jingtaishifang';  break;
                        case 4:  name = "动态释放";key='public.dongtaishifang';  break;
                        case 5:  name = "购买节点增加冻结数量";key='public.gmjdzjdjsl';  break;
                        case 6:  name = "直推释放";key='public.zhituisf';  break;
                        case 7:  name = "动态释放";key='public.dongtaishifang';  break;
                        case 9:  name = "ART节点分红";key='public.ART_jdfh';  break;
                    }
                    break
                case 5:
                    switch (source) {
                        case 1:  name = "兑换";key='public.duihuan';  break;
                        case 2:  name = "铸造";key='public.zhuzao';  break;
                        case 3:  name = "铸造失败返还";key='public.shibaifanhuan';  break;
                    }
                    break;
                  case 6:
                  switch (source) {
                      case 6:  name = "直推收益";key='public.zhituisy';  break;
                      case 8:  name = "动态收益";key='public.dtsy';  break;
                      // case 8:  name = "团队收益";key='public.teamBenefits';  break;
                  }
                  break;
                default: break;
            }
            if(key){
              return $t(key)
            }
            return type+'_'+source;
        });
    return {
      ...toRefs(state),
      ...methods,
      userInfo
    }
  },
  // provide(){
  //   return{
  //     // infoUser: userInfo,
  //     infoWallet: 123
  //   }
  // },
  // name: 'App',
  // data(){
  //   return{
  //     wallet: ''
  //   }
  // },
  // components: {},
  // mixins:[myMixin],
  // created(){
  //   console.log('---infoWallet: ', this.infoWallet);
  //   window._t = this;
  //   const store = useStore();
  //   this.$ajax.getUserInfo().then((res) => {
  //     // console.log('用户信息: ', res);
  //     if(res.code === 200) {
  //       store.userInfo = res.data;
  //       store.wallet = res.data.wallet;
  //       this.wallet = res.data.wallet;
  //       for(let key in res.data){
  //         userInfo[key] = res.data[key];
  //       }
  //       provide('infoUser',userInfo)
  //       this.$router.replace({ path:'home' })
  //     }else{
  //       this.$router.push({ path:'login' })
  //     }
  //   }).finally(() => {
  //     this.show = false;
  //   })
  // },
  // methods:{
  // }
}
</script>
<style lang="scss">
@import "./style/globalStyle.css";
.f-center{
    // 垂直居中
    display: flex;
    align-items: center;
    // justify-content: center;
  }
.share{
  position: relative;
  width: 0px;
  & div{
    position: absolute;
    transform: translate(-50%,-50%);
    padding: 11px;
    height: 44px;
    width: 54px;
  }
  & img{
    // position: absolute;
    // transform: translate(-50%,-50%);
    height: 22px;
    width: 22px;
  }
}
.van-list__finished-text{
  color: #999;
  text-align: center;
}
.overlay-index{
  position: absolute;
  width: 120px;
  height: 120px;
  padding-top: 46px;
  left: 50%;
  transform: translateX(-60px);
  text-align: center;
  margin-top: 50%;
  border-radius: 6px;
  // background: rgba(51, 51, 51, 0.5);
  // color: #fff;
}
input:focus{
  outline: 0;
  border: 1px solid royalblue ;
}
body{
  --nut-button-primary-border-color: #0072FF;
  --nut-button-primary-background-color: #0072FF;
}
.my-cell{
  position: relative;
  padding: 0 16px;
  line-height: 44px;
  background: #fff;
  // 分割线
  &::before{
    content: '';
    position: absolute;
    left: 16px;
    right: 0;
    top: 0;
    height: 1px;
    background-color: #EBEDF0;
    transform: scaleY(0.5);
    transform-origin: 0 0;
  }
  .right-text{
    text-align: right;
  }
  &:first-child{
    &::before{
      display: none;
    }
  }
}
header {
  line-height: 1.5;
  max-height: 100vh;
}
.no-data{
  text-align: center;
  margin-top: 20px;
  color: rgba(121, 93, 245, 0.5);
  .no-data-icon{
    height: 160px;
  }
}
.logo {
  display: block;
  margin: 0 auto 2rem;
}

nav {
  width: 100%;
  font-size: 12px;
  text-align: center;
  margin-top: 2rem;
}

nav a.router-link-exact-active {
  color: var(--color-text);
}

nav a.router-link-exact-active:hover {
  background-color: transparent;
}

nav a {
  display: inline-block;
  padding: 0 1rem;
  border-left: 1px solid var(--color-border);
}

nav a:first-of-type {
  border: 0;
}

@media (min-width: 1024px) {
  header {
    display: flex;
    place-items: center;
    padding-right: calc(var(--section-gap) / 2);
  }

  .logo {
    margin: 0 2rem 0 0;
  }

  header .wrapper {
    display: flex;
    place-items: flex-start;
    flex-wrap: wrap;
  }

  nav {
    text-align: left;
    margin-left: -1rem;
    font-size: 1rem;

    padding: 1rem 0;
    margin-top: 1rem;
  }
}
.gradient_radius {
  border: solid 1px transparent;
  border-radius: 20px;
  background-image: linear-gradient(#fff, #fff),
    linear-gradient(135deg, rgba(183, 40, 255, 1), rgba(40, 112, 255, 1));
  background-origin: border-box;
  background-clip: content-box, border-box;
  position: relative;
  display: inline-block;
  width: auto;
  .but_in{
    position: relative;
    display: inline-block;
    padding: 0px 12px;
    color: #7A5DF5;
    font-size: 12px;
    margin-bottom: 5px;
  }
}
body{
  .nut-calendar .nut-calendar__header .nut-calendar__weekdays .nut-calendar__weekday:first-of-type, .nut-calendar .nut-calendar__header .nut-calendar__weekdays .nut-calendar__weekday:last-of-type,
    .nut-calendar .nut-calendar__content .nut-calendar__panel .nut-calendar__days .nut-calendar__days-item .nut-calendar__day:nth-child(7n+0), .nut-calendar .nut-calendar__content .nut-calendar__panel .nut-calendar__days .nut-calendar__days-item .nut-calendar__day:nth-child(7n+1)
    {
      color: #333;
    }
    .nut-calendar .nut-calendar__footer .nut-calendar__confirm{
      // background: linear-gradient(131deg, #FC5D9F 0%, #5863FC 100%);
      background: #6872fb;
      width: 90%;
    }
    .nut-calendar .nut-calendar__content .nut-calendar__panel .nut-calendar__days .nut-calendar__day--active{
      background: transparent;
      .nut-calendar__day-value{
        padding: 0;
        width: 37px;
        height: 37px;
        line-height: 37px;
        border-radius: 50%;
        display: inline-block;
        // background: linear-gradient(131deg, #FC5D9F 0%, #5863FC 100%);
        background: #6872fb;
      }
    }
}
.qt{
  background: url('~/imgs/qt.png');
}
.by{
  background: url('~/imgs/by.png');
}
.hj{
  background: url('~/imgs/hj.png');
}
.zs{
  background: url('~/imgs/zs.png');
}
.ry{
  background: url('~/imgs/ry.png');
}
.qt,.by,.hj,.zs,.ry{
  width: 80px;
  height: 80px;
}
</style>
