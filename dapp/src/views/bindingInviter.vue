<template>
  <div class="m-login">
    <Navbar :title="$t('routes.'+$route.meta.i18n)" :isBack="false">
      <!-- <template #right>
        <div><img src="@/assets/imgs/8.png" alt=""></div>
      </template> -->
    </Navbar>
    <div class="m-login_body">
      <div class="box_3 flex-col">
        <span class="text_3" name="邀请人地址">
          {{ $t("public.inviterAddress") }}
        </span>
        <div class="input_1 flex-col">
          <nut-textarea v-model="value" />
        </div>
      </div>
      <div>
        <nut-button block type="primary" color="#0072FF" @click="handleClick(value)">
          {{ $t("public.binding") }}
        </nut-button>
        <nut-button v-if="dev" block plain color="#0072FF" @click="logout()" style="margin-top:10px;">退出登录-调试</nut-button>
        <nut-button v-if="dev" block plain color="#0072FF" @click="onHome()" style="margin-top:10px;">进入首页-调试</nut-button>
      </div>
    </div>
  </div>
</template>

<script>
import { showFailToast,showLoadingToast } from 'vant';
import {networkConfig} from '@/config/networkConfig'
export default {
  inject:['setInfoUser','setPubPayPrice','setInfoUserSuperior'],
  data() {
    return {
      value: '',
      value2: '',
      dev: networkConfig.dev
    }
  },
  created() {
  },
  methods: {
    handleClick(wallet) {
      const toast = showLoadingToast({
        duration: 0,
        message: '',
        forbidClick: true,
      });
      this.$ajax.getWalletBind({wallet}).then((res) => {
        if(res.code === 200){
          sessionStorage.setItem('isBindingInviter','1');
          this.setInfoUserSuperior(wallet);
          this.onHome();
        }else{
          // setTimeout(()=>{
          //   showFailToast(res.message || '绑定失败');
          // },600)
        }
      }).finally(() => {
        toast && toast.close();
      })
    },
    onHome(){
      sessionStorage.setItem('isBindingInviter','1');
      this.$router.replace('/home')
    },
    logout(){
      console.log('退出登录');
      this.$ajax.logout().then((res) => {
        console.log('退出登录: ', res);
      })
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