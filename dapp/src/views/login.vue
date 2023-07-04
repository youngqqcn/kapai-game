<template>
  <div class="m-login">
    <Navbar :title="$t('routes.'+$route.meta.i18n)">
      <!-- <template #right>
        <div><img src="@/assets/imgs/8.png" alt=""></div>
      </template> -->
    </Navbar>
    <div class="m-login_body">
      <div class="box_3 flex-col">
        <span class="text_3" name="当前地址">
          {{ $t("public.currentAddress") }}
        </span>
        <div class="input_1 flex-col">
          <nut-textarea v-model="value" />
        </div>
      </div>
      <div>
        <nut-button block type="primary" color="#0072FF" @click="handleClick">{{ $t("loginAuthorized") }}</nut-button>
      </div>
    </div>
  </div>
</template>

<script>
import { useStore } from '@/store/index'
import { showConfirmDialog,showLoadingToast } from 'vant';
import {networkConfig} from '@/config/networkConfig'
import { showToast, showSuccessToast, showFailToast } from 'vant';
export default {
  inject:['setInfoUser','setPubPayPrice'],
  data() {
    return {
      value: '',
      value2: ''
    }
  },
  created() {
    this.init();
  },
  methods: {
    init(){
      const store = useStore();
      if(store.wallet){
        this.value = store.wallet;
      }else{
        const toast = showLoadingToast({
          duration: 0,
          message: '',
          forbidClick: true,
        });
        this.$utils.getWallet().then((res) => {
          this.value = res;
          toast.close();
        }).finally(() => {
          toast.close();
        })
      }
    },
    getSign() {
      const store = useStore();
      return new Promise((resolve, reject) => {
        this.$ajax.getSign({wallet:store.wallet}).then((res) => {
          if(res.code === 200) {
            resolve(res.data);
          }else{
            reject(res.msg);
          }
        }).catch((err)=>{
          reject(err);
        });
      })
    },
    handleClick() {
      const share = this.$route.query.share || '';
      const store = useStore();
      if(store.wallet){
        const toast = showLoadingToast({
          duration: 0,
          message: '',
          forbidClick: true,
        });
        this.getSign().then((personal_sign) => {
          this.$utils.getSign(personal_sign, store.wallet).then((sign)=>{
            let par = {
              "wallet": this.value,
              "message": personal_sign,//"string - 原始签名内容",
              "sign": sign,//"string - 已签名的信息"
              //  share: share // 绑定邀请人地址（从url上取）
            }
            if(share){
              par.share = share;
            }
            this.login(par,toast);
          }).catch((err) => {
            showToast('签名失败');
            console.error(err);
            toast.close();
          })
          // this.$router.push('/home')
        }).catch((err) => {
          console.log('获取签名失败');
          console.error(err);
          toast.close();
        })
      }
    },
    login(par,toast){
      this.$ajax.onSign(par).then((res) => {
        // console.log('登录: ', res);
        if(res.code === 200){
          let { equityDesc, levelDesc } = res.data;
          equityDesc = this.wrap(equityDesc);
          levelDesc = this.wrap(levelDesc);
          localStorage.setItem('equityDesc', equityDesc);
          localStorage.setItem('levelDesc', levelDesc);
          this.$useStore.public.equityDesc = equityDesc;
          this.$useStore.public.levelDesc = levelDesc;
          this.$ajax.getUserInfo().then((res) => {
            // console.log('用户信息: ', res);
            if(res && res.code === 200) {
              this.setInfoUser(res.data);
              this.setPubPayPrice();
              sessionStorage.setItem('isLogin', 'true');
              // this.$router.replace('/home');
              if(res.data.superior){
                sessionStorage.setItem('isLogin', 'true');
                sessionStorage.setItem('isBindingInviter','');
                this.$router.replace('/home');
              }else{
                sessionStorage.setItem('isBindingInviter','-1');
                this.$router.replace('/bindingInviter'); // 绑定邀请人
              }
            }
          })
        }else{
          // showToast(res.message || '登录失败');
        }
      }).catch((res)=>{
        // setTimeout(() => {
        //   if(res.response && res.response.data){
        //     showToast(res.response.data.message || '登录失败');
        //   }else{
        //     showToast(res.message || '登录失败');
        //   }
        // }, 600);
      }).finally(() => {
        toast && toast.close();
      })
    },
    wrap(doc){
      return doc.replace(/[\r\n]/g,"<br/>");
    }
  }
}
</script>

<style lang="scss">
.m-login{
  .m-navbar .nut-navbar__left {
    color: #000;
  }
  .nut-navbar__title .title{
    color: #000;
    font-size: 16px;
    font-weight: 600;
  }
  .m-login_body{
    padding: 16px;
    margin-top: 48px;
    --nut-title-color: #323233;
    .nut-textarea{
      background: transparent;
      font-weight: 600;
      padding: 14px 14px;
    }
    .box_3{
      margin-bottom: 32px;
    }
  }
  .text_3 {
    height: 20px;
    overflow-wrap: break-word;
    color: rgba(100, 101, 102, 1);
    font-size: 14px;
    text-align: left;
    white-space: nowrap;
    line-height: 20px;
    margin: 0 0 0 15px;
  }
  .input_1 {
    background-color: rgba(231, 241, 252, 1);
    border-radius: 10px;
    height: 72px;
    margin: 8px 0 0 0;
  }

  .text_4 {
    height: 40px;
    overflow-wrap: break-word;
    color: rgba(50, 50, 51, 1);
    font-size: 14px;
    text-transform: uppercase;
    font-weight: 600;
    text-align: left;
    line-height: 20px;
    background-color: transparent;
  }
}
</style>