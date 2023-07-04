<template>
  <!-- <Navbar title="首页" :isBack="false">
    <template #right>
      <div><img src="@/assets/imgs/9.png" alt=""></div>
    </template>
  </Navbar> -->
  <div class="index-home">
    <!-- "wallet": "string - 钱包地址",
      "superior": "string - 推荐人钱包",
      "ztPower": "long - 直推算力",
      "ep": "string - EP数量",
      "tokenA": "string - tokenA数量",
      "tokenB": "string - tokenB数量",
      "checkTokenA": "string - tokenB签到数量",
      "checkTokenB": "string - tokenB签到数量",
      "level": "int - 团队等级",
      "power": "int - 算力",
      "smallPower": "int - 小区算力",
      "bigPower": "int - 大区算力",
      "dynamicPower": "string - 动态算力" -->
    <div class="index-home_body">
            <nut-pull-refresh v-model="refresh" @refresh="refreshFun">

      <div class="i18n-select" :class="{zh:locale=='zh'}">
        <span class="en" @click="setLocale('en')">English</span>
        <span class="zh" @click="setLocale('zh')">简体中文</span>
      </div>
      <view class="nut-textarea">
        <div class="nut-textarea__textarea" style="word-wrap: break-word;">
          {{userInfo.wallet}}
        </div>
      </view>
      <div style="display:inline-block;width:100%">
        <div class="m-card" :style="{backgroundImage:`url(${cardImg})`}">
          <div class="flex flex-center" style="padding: 0 24px;">
            <div class="flex flex-col c-itme" style="width: 50%;">
               <div class="flex-1" style="position: relative;">
                <div class="text_1" name="ART静态算力值">{{$t('public.jtslz_a')}}</div>
                <div class="text_2"></div>
                <div class="text_3">
                  <span>{{ num4(userInfo.power) }}</span>
                </div>
              </div>
              <div class="flex-1" style="position: relative;">
                <div class="text_1" name="ART动态算力值">{{$t('public.dtslz_a')}}</div>
                <div class="text_2"></div>
                <div class="text_3">
                  <span>{{ num4(userInfo.dynamicPower) }}</span>
                </div>
              </div>
              <div class="flex-1" style="position: relative;">
                <div class="text_1" name="AAAA币">{{$t('home.aaaaB')}}</div>
                <div class="text_2"></div>
                <div class="text_3 underline">
                  <span>{{ num4(userInfo.tokenA) }}</span>
                </div>
              </div>
              <div class="flex-1 flex-center" style="position: relative;">
                <nut-button name="签到领取" class="but h-item_but" type="primary" size="small" color="rgb(31 34 41 / 45%)" :loading="isLoading" :disabled="isLoading" @click="payWallet(1)">
                {{$t('home.qdlq')}}
                </nut-button>
              </div>
            </div>
            <div class="flex flex-col c-itme" style="width: 50%;">
              <div class="flex-1" style="position: relative;">
                <div class="text_1" name="SOUL的静态算力">{{$t('public.jtslz_b')}}</div>
                <div class="text_2"></div>
                <div class="text_3">
                  <span>{{ num4(userInfo.bPower) }}</span>
                </div>
              </div>
              <div class="flex-1" style="position: relative;">
                <div class="text_1" name="SOUL的动态算力">{{$t('public.dtslz_b')}}</div>
                <div class="text_2"></div>
                <div class="text_3">
                  <span>{{ num4(userInfo.dynamicPower) }}</span>
                </div>
              </div>
              <div class="flex-1" style="position: relative;">
                <div class="text_1" name="BBBB余额">{{$t('home.bbbbyE')}}</div>
                <div class="text_2"></div>
                <div class="text_3 underline">
                  <span>{{ num4(userInfo.tokenB) }}</span>
                </div>
              </div>
              <div class="flex-1 flex-center" style="position: relative;">
                <nut-button name="签到领取" class="but h-item_but" type="primary" size="small" color="rgb(31 34 41 / 45%)" :loading="isLoading2" :disabled="isLoading2" @click="payWallet(2)">
                  {{$t('home.qdlq')}}
                </nut-button>
              </div>
            </div>
          </div>
        </div>
        <div class="node-r" @click="toPages('nodePage')">
          <img src="@/assets/imgs/p2.png" alt="">
        </div>
        <div style="text-align:left">
          <div class="card-item">
            <div class="item" @click="toPages('castOnOoutwell')">
              <img src="@/assets/imgs/icon1.png" alt="">
              <div class="title-name" name="铸造">
                {{$t('public.zhuzao')}}
              </div>
            </div>
            <div class="item" @click="toPages('dataQuery')">
              <img src="@/assets/imgs/icon2.png" alt="">
              <div class="title-name" name="数据">
                {{$t('home.body.dataQuery')}}
              </div>
            </div>
            <div class="item" @click="toPages('team')">
              <img src="@/assets/imgs/icon3.png" alt="">
              <div class="title-name" name="我的团队">
                {{$t('home.body.team')}}
              </div>
            </div>
            <div class="item" @click="toPages('myToken')">
              <img src="@/assets/imgs/icon4.png" alt="">
              <div class="title-name" name="我的令牌">
                {{$t('home.body.myToken')}}
              </div>
            </div>
          </div>
          <div style="margin-top:14px;">
            <nut-infinite-loading
                    v-model="infinityValue"
                    :has-more="hasMore"
                    :load-more-txt="$t('public.finished')"
                    @load-more="loadMore"
                >
                <nut-cell-group>
                  <template v-for="(item, index) in chainListLog" :key="index">
                    <div class="m-cell">
                      <div class="m-cell__title">
                        <div class="m-cell__title__text">{{item.memo}}</div>
                        <div class="m-cell__title__right">{{getDesc(item)}}</div>
                      </div>
                      <div class="m-cell__content">
                        <div class="m-cell__content__text">{{userInfo.wallet}}</div>
                      </div>
                      <div class="m-cell__content">
                        <div class="m-cell__content__time">{{item.createTime}}</div>
                      </div>
                    </div>
                  </template>
                </nut-cell-group>
              </nut-infinite-loading>
              <div v-show="chainListLog.length == 0">
                <div class="no-data">
                  <img class="no-data-icon" src="@/assets/imgs/no-data.png" alt="">
                  <div class="no-data-text" name="暂无数据">
                    {{$t('public.zwsj')}}
                  </div>
                </div>
              </div>
          </div>
        </div>
      </div>
            </nut-pull-refresh>
    </div>
    <div class="is-w" v-if="!wallet" @click="connect()"></div>
  </div>
</template>

<script lang="ts">
import { Locale } from '@nutui/nutui';
import enUS from '@nutui/nutui/dist/packages/locale/lang/en-US';
import zhCn from '@nutui/nutui/dist/packages/locale/lang/zh-CN';
import { useStore } from '@/store/index'
import cardImg from '@/assets/imgs/card4.png'
// import { showToast } from '@nutui/nutui';
import { showToast, showSuccessToast, showFailToast } from 'vant';
let ethereum: any = null;
import { showConfirmDialog,showLoadingToast } from 'vant';
export default {
  inject:['infoUser','num4','getTypeSourceName','updateUserInfo','vRefresh'],
  data() {
    return {
      refresh: false,
      isloading: false,
      locale: 'zh',
      infinityValue: false,
      hasMore: true,
      isLoading:false,
      isLoading2:false,
      cardImg,
      wallet:'',
      userInfo:  {
        userStatus: -1, // 标记 -1 未获取 0 未绑定 1 已绑定
        "user_id": '', // "用户ID",
        "wallet": '', // "钱包地址",
        "integral": '0', // "积分数量",
        "chain_number": '0', // "PSG数量"
      },
      // 分页
      pages: {
        page: 0,
        total: 0
      },
      chainListLog: [
        // {
        //   "id": 1, // "ID",
        //   "user_id": 1, // "用户ID",
        //   "before_number": 0, // "变动前数量",
        //   "after_number": 0, // "变动后数量",
        //   "number": 0, // "变动数量",
        //   "memo": "转入", // "备注",
        //   "create_time": "2021-07-01 00:00:00" // "创建时间"
        // }
      ],
      powerInfo:{
        aTodayDestroy: 0, // ART今日销毁
        bTodayDestroy: 0, // SOUL今日销毁
        aCirculating: 0, //  ART流通
        bCirculating: 0, // SOUL流通
        aStaticPower: 0, // ART静态
        bStaticPower: 0, // SOUL静态
        aDynamicPower: 0, // ART动态
        bDynamicPower: 0, // SOUL动态 
        allPower_a: 0, // 全网ART算力值
        allPower_b: 0, // 全网SOUL算力值
      }
    }
  },
  created() {
    window._dd = this;
    this.locale = this.$i18n.locale;
    const store = useStore();
    this.userInfo = store.userInfo;
      if(store.wallet){
        this.wallet = store.wallet;
      }
    this.init();
  },
  computed: {
    // 是否有钱包
    alletInput() {
      let wallet = this.wallet.replace(/(\w{10})(\w+)(\w{10})/g, '$1********$3');
      return wallet;
    }
  },
  methods: {
    refreshFun(){
      this.hasMore = true;
      this.pages.page = 0;
      this.chainListLog = [];
      this.getChainListLog();
    },
    setLocale(locale: string) {
      this.locale = locale;
      this.$i18n.locale = locale;
      localStorage.setItem('locale', locale);
      if(locale == 'zh'){
        Locale.use('zh');
        // Locale.use('zh', zhCn);
      }else{
        Locale.use('en');
        // Locale.use('en', enUS);
      }
    },
    loadMore(){
      if(!this.hasMore || this.isloading){
        return;
      }
      this.getChainListLog();
    },
    connect(){
      // const store = useStore();
      showToast(this.$t('public.toast.qzappzdk'));
    },
    payWallet(type) {
      const toast = showLoadingToast({
        duration: 0,
        message: this.$t('public.toast.submitting'),
        forbidClick: true,
      });
      this.$ajax.getWalletCheckIn('type='+type).then((res: any) => {
        // console.log('返回数据: ', res);
        toast.close();
        if(res.code === 200) {
          showToast({
            message: this.$t('public.toast.success'),
            duration: 2000,
            position: 'bottom',
          });
          this.updateUserInfo().then(()=>{
            setTimeout(()=>{
              this.vRefresh();
            })
          })
        }else{
          // setTimeout(()=>{
          //   showToast({
          //     message: res.message || this.$t('public.toast.fail'),
          //     duration: 2000,
          //     position: 'bottom',
          //   });
          // },300)
        }
      });
    },
    init(wallet){
      // this.wallet = wallet;
      // // this.getUserInfo();
      this.getChainListLog();
      this.getPowerInfo();
    },
    getPowerInfo(){
      this.$ajax.getPowerInfo().then(res=>{
        if(res.code == 200){
          // 全网ART算力值 = aStaticPower + aDynamicPower
          // 所有算力值的相加 = bStaticPower + bDynamicPower
          let { aTodayDestroy, bTodayDestroy, aCirculating, bCirculating, aStaticPower, bStaticPower, aDynamicPower, bDynamicPower } = res.data;
          this.powerInfo.aTodayDestroy = aTodayDestroy;
          this.powerInfo.bTodayDestroy = bTodayDestroy;
          this.powerInfo.aCirculating = aCirculating;
          this.powerInfo.bCirculating = bCirculating;

          this.powerInfo.aStaticPower = Number(aStaticPower || 0);
          this.powerInfo.aDynamicPower = Number(aDynamicPower || 0);
          this.powerInfo.allPower_a = this.powerInfo.aStaticPower + this.powerInfo.aDynamicPower;

          this.powerInfo.bStaticPower = Number(bStaticPower || 0);
          this.powerInfo.bDynamicPower = Number(bDynamicPower || 0);
          this.powerInfo.allPower_b = this.powerInfo.bStaticPower + this.powerInfo.bDynamicPower;
        }
      })
    },
    getUserInfo(isReFresh = false) {
      const store = useStore();
      if(store.userInfo && !isReFresh){
        this.userInfo = store.userInfo;
        return;
      }
      this.$ajax.getUserInfo().then((res: any) => {
        // console.log('返回数据: ', res);
        if(res.code === 0) {
          this.userInfo = res.data;
          this.userInfo.userStatus = 1;
          store.userInfo = res.data;
        }else{
          this.userInfo.userStatus = 0;
        }
      });
    },
    getChainListLog() {
      if(this.isloading){
        return;
      }
      this.isloading = true;
      const par = 'type=1&type=2&type=3&type=5&source=1&source=2&source=3&source=9&page='+this.pages.page;
      this.$ajax.getLogs(par).then((res: any) => {
 /*        
        // 调试 测试数据 60条
        this.pages.page++;
        for(let i = 0; i < 20; i++) {
          this.chainListLog.push({
            "id": 1, // "ID",
            "user_id": 1, // "用户ID",
            "before_number": 0, // "变动前数量",
            "after_number": 0, // "变动后数量",
            "number": 0, // "变动数量",
            "memo": "转入", // "备注",
            "create_time": "2021-07-01 00:00:00" // "创建时间"
          })
        }
        this.pages.total = 3;
        if(this.pages.page >= this.pages.total){
          this.hasMore = false;
        }
        // END 调试
 */

        if(res.code === 200) {
          this.pages.page++;
          this.chainListLog = this.chainListLog.concat(res.data.content);
          // 计算总页数
          this.pages.total = Math.ceil(res.data.total / res.data.pageable.size);
          if(this.pages.page >= this.pages.total){
            this.hasMore = false;
          }
        }
        
        /* if(res.code === 200) {
          this.pages.page++;
          this.chainListLog = res.data.content;
          // 计算总页数
          this.pages.total = Math.ceil(res.data.total / res.data.pageable.size);
        } */
      }).finally(() => {
        this.isloading = false;
        this.infinityValue = false;
        this.refresh = false;
      });
    },
    toPages(path: string) {
      this.$router.push({ path })
    },
    getSign() {
      const store = useStore();
      return new Promise((resolve, reject) => {
        this.$ajax.getSign({wallet:store.wallet}).then((res: any) => {
          console.log('res: ', res);
          if(res.code === 200) {
            resolve(res.data);
          }else{
            reject(res.msg);
          }
        }).catch((err:any)=>{
          reject(err);
        });
      })
    },
    getDesc(item){
      // 前缀变量
      let before = '';
      if(item.amount > 0){
        before = '+';
      }
      item.memo = this.getTypeSourceName(item.type,item.source);
      // item.after_number保留两位小数
      let name = '';
      if(item.type === 2){
        name = ` ART ${this.$t('public.token')}`
      }else if(item.type === 3){
        name = ` SOUL ${this.$t('public.token')}`
      }else if(item.type === 6){
        if([8].includes(item.source)){
          name = ` USDT ${this.$t('public.token')}`
        }
      }
      return `${before}${Number(Number(item.amount).toFixed(4))} ${name}`;
    },
  }
}
</script>
<style lang="scss" scoped>
.index-home {
  .i18n-select{
    text-align: right;
    span{
      font-size: 14px;
      font-weight: 400;
      color: #130F25;
      line-height: 20px;
      display: inline-block;
      padding: 10px 10px;
    }
    .en{
      color: #0072FF;
    }
    &.zh{
      .zh{
        color: #0072FF;
      }
      .en{
        color: #130F25;
      }
    }
  }
  .m-cell{
    padding: 12px 16px;
    .m-cell__title{
      line-height: 26px;
      display: flex;
      .m-cell__title__text{
        font-size: 12px;
        line-height: 25px;
        display: inline-block;
        // width: 40px;
        padding: 0 8px;
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
    // height: 36px;
    background: #FFFFFF;
    border-radius: 8px;
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
    min-height: 48.3vw;
    padding: 16px 0;
    max-width: 700px;
    max-height: 365px;
    width: 100%;
    background-size: 100% 100%;
    text-align: left;
    color: #FFFFFF;
      // 垂直居中
      display: flex;
      flex-direction: column;
      justify-content: center;
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
      // margin: 24px 0;
      // margin: 8vw 0;
      // margin-bottom: 24px;
      &:last-child{
        margin-bottom: 0;
      }
      .text_1,.text_2,.text_3{
        text-align: center;
        width: 100%;
      }
      .text_1{
        font-size: 12px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #DCDEE0;
        line-height: 17px;
        display: inline-block;
        margin-bottom: 10px;
        margin-top: 10px;
      }
      .text_2,.text_3{
        font-size: 16px;
        position: relative;
        height: 18px;
        bottom: 0;
      }
      .text_3{
        position: absolute;
        bottom: 0;
      }
      .h-item_but{
        margin-top: 10px;
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
  .node-r{
    padding-top: 16px;
    img{
      width: 100%;
    }
  }
}
</style>
<style>

</style>