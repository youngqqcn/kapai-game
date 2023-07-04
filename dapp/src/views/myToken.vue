<template>
  <div class="my-token">
    <nut-pull-refresh v-model="refresh" @refresh="refreshFun">
      <div class="token-cell" v-for="(item,i) in list" :key="i">
        <div class="token-cell_top flex">
          <div class="cell-left">
            <!-- <img src="@/assets/imgs/p0.png" alt=""> -->
            <div :class="getPMclass(item)"></div>
          </div>
          <div class="cell-center flex-1">
            <div class="name">{{item.name}}</div>
            <div class="desc">
              <div name="算力值">{{$t("public.suanlizhi") }} {{item.myPower}}</div>
              <div name="产出上限">{{ $t("public.ccsx") }} ${{item.myOutput}}</div>
            </div>
          </div>
          <div class="cell-right">
            <!-- <div class="name">${{item.unitPrice}}</div> -->
            <div class="desc">
              <!-- {{['待铸造','生产中'][item.status] || '已过期'}} -->
              {{[$t("public.daizhuzao"),$t("public.shengchanzhong")][item.status] || $t("public.yiguoqi")}}
            </div>
          </div>
        </div>
        <div class="token-cell_bottom flex">
          <div class="left-but">${{item.unitPrice}}</div>
          <div class="right-but flex-1">
            <span class="text_5-0 default" style="margin-right:10px" @click="toPages('tokenDetails',item)" name="详情">
              {{$t('public.xiangqing')}}
            </span>
            <span class="text_5-0 primary" @click="onShowBottom(item)" name="铸造">{{$t('public.zhuzao')}}</span>
          </div>
        </div>
      </div>
    </nut-pull-refresh>
  </div>
  <!-- <van-popup v-model:show="showTop" position="top" :style="{ height: '30%' }" /> -->
  <van-popup :z-index="1" position="bottom" round closeable :style="{ height: '240px' }" v-model:show="showBottom" style="--nut-popup-close-icon-margin:10px">
    <div class="select-data_popup-content">
      <div class="popup-title" name="支付方式">
        {{$t('public.paymentMethod')}}
      </div>
      <div>
        <nut-radio-group class="m-radio-group" v-model="radioVal" text-position="left" style="display: block;">
            <nut-radio label="0" text-position="left">
              <!-- <span :class="{active:radioVal === 0}">{{getDb(1)}} AAA代币支付</span> -->
              <span :class="{active:radioVal === 0}">{{getDb(1)}}
                ART&nbsp;<span name="代币">&nbsp;{{$t('public.token')}}</span>
              <span name="支付">&nbsp;{{$t('public.payment')}}</span>
              </span>
            </nut-radio>
            <nut-radio label="1" text-position="left">
              <!-- <span :class="{active:radioVal === 1}">{{getDb(2)}} AAA代币 + {{nowItem.ep}} EP支付</span> -->
              <span :class="{active:radioVal === 1}">
                {{getDb(2)}} ART<span name="代币">&nbsp;{{$t('public.token')}}</span>
                  + {{nowItem.ep}} IOT<span name="支付">&nbsp;{{$t('public.payment')}}</span>
              </span>
            </nut-radio>
        </nut-radio-group>
<!--        <div class="popup-desc">-->
<!--          <div class="title" name="当前IOT余额">{{$t('public.dqEPye')}}</div>-->
<!--          <div class="desc-content">{{infoUser.value.ep}}</div>-->
<!--        </div>-->
      </div>
      <div class="popup-button">
        <nut-button block type="primary" @click="save()">{{$t('public.confirm')}}</nut-button>
      </div>
    </div>
  </van-popup>
</template>

<script>
import { showConfirmDialog,showLoadingToast } from 'vant';
import { showToast } from '@nutui/nutui';
import DAppError, {dapp} from '../dapp/dapp.js'
export default {
  inject:['infoUser','setPubNowItem'],
  data() {
    return {
      refresh: false,
      showBottom: false,
      radioVal: '0',
      nowItem:{
        unitPrice: 0,
        id: 0,
        ep: 0,
      },
      payPrice:{
        isInit: false,
        epRatio: 0,
        tokenA: 0,
        tokenB: 0,
      },
      list: [
        // {
        //   name: '令牌名称',
        //   desc: '算力值 10000',
        //   desc2: '产出上限 $29999',
        //   desc3: '生产中',
        //   desc4: '$289',
        // },
      ],
    }
  },
  created() {
    this.debounce = this.$basics.debounce();
    this.init();
  },
  methods: {
    refreshFun() {
      this.debounce(() => {
        this.getCards();
      }, 200);
    },
    init(){
      this.$ajax.getPrice().then((res) => {
          console.log('支付方式res: ', res);
          if(res.code == 200){
            const { epRatio, tokenA, tokenB } = res.data;
            this.payPrice = {
              isInit: true,
              epRatio: parseFloat(epRatio),
              tokenA: parseFloat(tokenA),
              tokenB: parseFloat(tokenB),
            }
          }
        });
      this.getCards();
    },
    getCards(){
      this.$ajax.getCards().then(res=>{
        if(res.code == 200){
          this.list = res.data;
        }
      }).finally(()=>{
        this.refresh = false;
      })
    },
    getDb(v){
      let a = this.nowItem.unitPrice * this.payPrice.tokenA;
      if(v == 1){
        return a.toFixed(4);
      }else if(v == 2){
        let b = a * this.payPrice.epRatio;
        return b.toFixed(4);
      }
    },
    toPages(path,item){
      this.setPubNowItem({...item});
      this.$router.push({ path,query:{id:item.id} })
    },
    onShowBottom(item){
      this.nowItem = item;
      this.showBottom = true;
      // this.$Dialog({
      //   title: '提示',
      //   message: '是否铸造？',
      // }).then(() => {
      //   // on confirm
      // }).catch(() => {
      //   // on cancel
      // });
    },
    save(){
      const toast = showLoadingToast({
        duration: 0,
        message: '',
        forbidClick: true,
      });
      const par = {
        cardId: this.nowItem.id,
        type: ~~this.radioVal,
      }
      this.$ajax.getCardPaySign(par).then((res) => {
        const data = res.data
        if(res.code == 200){
          console.log('api result: ', res);
          const transactions = [()=>dapp.switchEthereumChain(data.chainId)]
          if (par.type == 1) {
            transactions.push(()=>dapp.approve(data.contractEP, data.contractTokenA));
          }
          transactions.push(()=>dapp.sendTransaction(data.wallet, data.contractTokenA, data.data));
          dapp.calls(...transactions).then((data) => {
            console.log("sendTransaction: ", data)
            this.$ajax.getCardPaySend({"hash": data.pop(), "orderId": res.data.orderId}).then((data)=>{
              toast.close();
              console.log('api result: ', data);
              if(data.code === 200){
                showToast.success(this.$t('public.toast.jiaoyichenggong'));
                this.showBottom = false;
                // getChainListLog();
              } else {
                showToast.warn(store.public.$t('public.toast.jyfssb'));
              }
            }).catch((e)=>{
              toast.close();
              showToast.warn(store.public.$t('public.toast.jyfssb'));
            });
          }).catch((e)=>{
            toast.close();
            if (e instanceof DAppError) {
              switch (e.code) {
                case DAppError.CODE_GAS_PRICE:
                  showToast.warn(this.$t('public.toast.hqGASjg'));
                  break;
                case DAppError.CODE_ESTIMATE_GAS:
                  showToast.warn(this.$t('public.toast.GASfhdbslbz')+"!");
                  break;
                case DAppError.CODE_SEND_TRANSACTION:
                  showToast.warn(this.$t('public.toast.jyfssb'));
                  break;
                default:
                  showToast.warn(this.$t('public.toast.jiaoyishibai'));
                  break;
              }
            } else {
              showToast.warn(this.$t('public.toast.jiaoyishibai'));
            }
          });
        } else {
          toast.close();
          showToast.warn(this.$t('public.toast.jiaoyishibai'));
        }
      }).catch(() => {
        toast.close();
        showToast.warn(this.$t('public.toast.jiaoyishibai'));
      })
    },
    getPMclass: function (item) {
      const arrNm = {'青铜':'qt','白银':'by','黄金':'hj','钻石':'zs','荣耀':'ry'};
      const name = item.name || '';
      let className = '';
      for(let key in arrNm){
        if(name.indexOf(key) > -1){
          className = arrNm[key];
        }
      }
      return className;
    },
  }
}
</script>

<style lang="scss">
body{
  background: #F7F8FA;
}
.my-token{
  .text_5-0 {
    display: inline-block;
    border-radius: 8px;
    text-align: center;
    width: 56px;
    font-size: 12px;
    line-height: 25px;
    &.default{
      background-color: rgb(255, 255, 255);
      color: rgb(0, 0, 0);
      // 内阴影
      box-shadow: inset 0px 0px 0px 1px rgba(0, 0, 0, 0.1);
    }
    &.primary{
      background-color: rgba(0, 114, 255, 1);
      color: rgba(255, 255, 255, 1);
    }
  }
  .token-cell{
    position: relative;
    background: #fff;
    padding: 16px;
    // 分割线
    &::before{
      content: '';
      position: absolute;
      left: 0;
      right: 0;
      top: 0;
      height: 1px;
      background-color: #EBEDF0;
      transform: scaleY(0.5);
      transform-origin: 0 0;
    }
    .cell-left{
      width: 80px;
      height: 80px;
      overflow: hidden;
      border-radius: 8px;
      background: #F7F8FA;
      img{
        width: 100%;
        height: 100%;
      }
    }
    .cell-center{
      padding-left: 8px;
      position: relative;
      .name{
        font-size: 16px;
        color: #130F25;
      }
      .desc{
        position: absolute;
        font-size: 14px;
        color: #646566;
        line-height: 22px;
        bottom: 0;
        & div{
          white-space: nowrap;
        }
      }
    }
    .cell-right{
      position: relative;
      // width: 50px;
      text-align: right;
      .name{
        color: #130F25;
      }
      .desc{
        // position: absolute;
        right: 0;
        font-size: 14px;
        line-height: 22px;
        bottom: 24px;
        color: #FEB700;
      }
    }
    .token-cell_top{
      height: 80px;
    }
    .token-cell_bottom{
      text-align: right;
      .right-but{
        display: inline-block;
      }
      .left-but{
        line-height: 25px;
        font-size: 12px;
        text-align: center;
        width:80px
      }
    }
  }
}
.select-data_popup-content{
 .popup-title{
    text-align: center;
    font-size: 16px;
    font-family: PingFangSC-Medium, PingFang SC;
    color: #130F25;
    line-height: 48px;
 }
 .m-radio-group{
  padding: 0 16px;
  .nut-radio{
    line-height: 30px;
    --nut-radio-label-font-active-color: #0072FF;
    .active{
      color: #0072FF;
    }
  }
 }
 .popup-desc{
  padding: 0 16px;
  .title{
    font-size: 14px;
    font-weight: 400;
    color: #161B35;
    line-height: 20px;
  }
  .desc-content{
    font-size: 13px;
    font-weight: 400;
    color: #969799;
    line-height: 17px;
  }
 }
 .popup-button{
  padding: 0 16px;
  margin-top: 29px;
  --nut-button-primary-color: #fff;
  --nut-button-primary-background-color: #0072FF;
 }
}
</style>