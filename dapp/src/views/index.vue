<template>
  <Navbar title="首页">
    <template #right>
      <div><img src="@/assets/imgs/9.png" alt=""></div>
    </template>
  </Navbar>
  <!-- 该页面为过度页面，不用国际化 -->
  <div class="index-home-i">
    <div class="index-home_body">
      <div style="display:inline-block;width:100%">
        <div class="m-card" :style="{backgroundImage:`url(${cardImg})`}">
          <div style="padding: 0 24px;">
            <div class="c-itme flex">
              <div class="flex-1">
                <span class="text_1">静态算力值</span>
                <span class="text_2">100000</span>
              </div>
              <div class="flex-1">
                <span class="text_1">静态算力值</span>
                <span class="text_2 underline">100000</span>
              </div>
              <div class="flex-1 flex-center" style="">
                <nut-button class="but" type="primary" size="small" color="rgb(31 34 41 / 45%)" :loading="isLoading" :disabled="isLoading">转入钱包</nut-button>
              </div>
            </div>
            <div class="c-itme flex">
              <div class="flex-1">
                <span class="text_1">动态算力值</span>
                <span class="text_2">100000</span>
              </div>
              <div class="flex-1">
                <span class="text_1">SOUL余额</span>
                <span class="text_2 underline">100000</span>
              </div>
              <div class="flex-1 flex-center" style="">
                <nut-button class="but" type="primary" size="small" color="rgb(31 34 41 / 45%)" :loading="isLoading" :disabled="isLoading">转入钱包</nut-button>
              </div>
            </div>
          </div>
        </div>
        <div style="text-align:left">
          <div class="card-item">
            <div class="item" @click="toPages('castOnOoutwell')">
              <img src="@/assets/imgs/icon1.png" alt="">
              <div class="title-name">
                铸造
              </div>
            </div>
            <div class="item" @click="toPages('selectData')">
              <img src="@/assets/imgs/icon2.png" alt="">
              <div class="title-name">
                数据
              </div>
            </div>
            <div class="item" @click="toPages('team')">
              <img src="@/assets/imgs/icon3.png" alt="">
              <div class="title-name">
                我的团队
              </div>
            </div>
            <div class="item" @click="toPages('myToken')">
              <img src="@/assets/imgs/icon4.png" alt="">
              <div class="title-name">
                我的令牌
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="is-w" v-if="!wallet" @click="connect()"></div>
  </div>
</template>

<script lang="ts">
import aes from '@/utils/aes'
import { useStore } from '@/store/index'
import cardImg from '@/assets/imgs/card4.png'
import { showToast } from '@nutui/nutui';
import { showConfirmDialog,showLoadingToast } from 'vant';
let ethereum: any = null;
export default {
  data() {
    return {
      isLoading:false,
      wallet:'',
      cardImg
    }
  },
  created() {
    this.init();
  },
  computed: {},
  methods: {
    init(){
      const store = useStore();
      // const toast = showLoadingToast({
      //   duration: 0,
      //   message: '',
      //   forbidClick: true,
      // });
      // 判断登录
      this.$ajax.getUserInfo().then((res: any) => {
        // console.log('用户信息: ', res);
        if(res.code === 200) {
          store.userInfo = res.data;
          if(!res.data.superior){
            sessionStorage.setItem('isBindingInviter','-1');
            this.$router.replace('/bindingInviter');
          }else{
           sessionStorage.setItem('isBindingInviter','');
          }
        }else{
          this.$router.replace({ path:'login' })
        }
      }).catch((err: any) => {
        this.$router.replace({ path:'login' })
      }).finally(() => {
        // toast.clear();
      })
      // if(!sessionStorage.getItem('wallet_login')){
      //   this.$router.replace({ path:'login' })
      // }
    },
    connect(){
      this.$utils.getWallet().then((res) => {
        this.wallet = res;
      }).finally(() => {
      })
    }
  }
}
</script>
<style lang="scss">
.index-home-i {
  .m-cell{
    padding: 12px 16px;
    .m-cell__title{
      line-height: 26px;
      display: flex;
      .m-cell__title__text{
        font-size: 12px;
        line-height: 25px;
        display: inline-block;
        background: #FEB700;
        border-radius: 5px;
        text-align: center;
        color: #FFFFFF;
      }
      .m-cell__title__right{
        display: inline-block;
        text-align: right;
        flex: 1;
        font-size: 16px;
        font-family: PingFangSC-Semibold, PingFang SC;
        font-weight: 600;
        color: #130F25;
        line-height: 22px;
      }
    }
    .m-cell__content{
      font-size: 12px;
      line-height: 17px;
      margin-top: 6px;
      .m-cell__content__time{
        color: #999999;
      }
      .m-cell__content__text{
        // 自动 换行
        word-break: break-all;
      }
    }
  }
  .flex-center{
    display: flex;
    align-items: center;
    justify-content: center;
  }
  .nut-cell__value{
    font-size: 14px;
    color: #52C41A;
  }
  .is-w{
    position: fixed;
    top:0;
    left:0;
    height: 100%;
    width: 100%;
    background: #363646;
    z-index: 999;
    opacity: 0;
  }
  background: #F9FAFB;
  padding:15px 20px;
  height:calc(100vh - 31px);
  .virtual-input{
    width: 100%;
    height: 36px;
    background: #FFFFFF;
    border-radius: 32px;
    padding: 0 11px;
    color: #363646;
    box-sizing: border-box;
    overflow: hidden;
    .nut-input{
      padding: 10px 0px;
    }
    span{
      font-size: 14px;
      // 强制不换行
      white-space: nowrap;
    }
  }
  .index-home_body{
    text-align: center;
  }
  .m-card{
    display: inline-block;
    margin-top: 24px;
    // min-height: 176px;48.3vw
    height: 48.3vw;
    // width: 100vw;
    max-width: 700px;
    max-height: 365px;
    width: 100%;
    background-size: 100% 100%;
    text-align: left;
    color: #FFFFFF;
    .m-card_title{
      margin-top: 24px;
      font-size: 14px;
      font-weight: 400;
      color: #FFFFFF;
      line-height: 14px;
    }
    .m-card_body{
      margin-top: 24px;
      height: 60px;
    }
    .num{
      font-size: 24px;
      font-weight: 600;
    }
    .desc{
      font-size: 14px;
      .desc-sub{
        margin-top: 8px;
        font-size: 14px;
        line-height: 14px;
      }
    }
    .c-itme{
      .text_1{
        font-size: 12px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #DCDEE0;
        line-height: 17px;
        display: inline-block;
        margin-bottom: 10px;
      }
      .text_2{
        font-size: 16px;
      }
      .underline{
        text-decoration: underline;
        // 下划线间距
        text-decoration-skip-ink: none;
      }
    }
  }
  // 底部样式
  .m-card_footer{
    width: 100%;
    // margin-top: 34px;
  }
  .but{
    border: 1px solid #1b68e7;
    margin-left:16px;
    --nut-button-border-radius:4px
  }
  .card-item{
    display: grid;
    grid-template-columns: 1fr 1fr 1fr 1fr;
    .item{
      display: inline-block;
      text-align: center;
      padding-top: 20px;
      // margin-right: 4px;
      img{
        width: 28px;
        height: 28px;
      }
      .title-name{
        margin-top: -2px;
        font-size: 14px;
      }
    }
  }
  .f-center{
    // 垂直居中
    display: flex;
    align-items: center;
    // justify-content: center;
  }
  .list{
    background: #fff;
    border:1px solid #eee;
    border-radius: 8px 8px 0px 0px;
    .nut-list-item{
      height: 60px;
      padding: 14px 14px 14px 14px;
      border-bottom: 1px solid #eee;
      &:last-child{
        border-bottom: none;
      }
    }
    .desc{
      // margin-top:8px;
      .desc-name{
        font-size: 14px;
        color: #363646;
      }
      .desc-sub{
        margin-top:4px;
        font-size: 12px;
        color: rgba(54, 54, 68, 0.5);
      }
    }
  }
  .growth-value{
    font-size: 14px;
    color: #52C41A;
    text-align: right;
  }
  
}
</style>
<style>

</style>