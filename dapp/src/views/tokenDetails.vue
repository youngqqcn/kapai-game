<template>
  <div class="token-details">
    <!-- 令牌详情 -->
    <div class="token-details-header">
      <div class="token-details-header-img">
        <!-- <img src="@/assets/imgs/p0.png" alt=""> -->
        <div :class="getPMclass(item)"></div>
      </div>
      <div class="token-details-header-name">{{item.name}}</div>
      <div class="token-details-header-name_val">${{item.unitPrice}}</div>
      <div class="header-desc">
        <div class="name">
          <span class="flex-1" name="算力值">{{$t("public.suanlizhi") }}</span>
          <span class="flex-1" name="产出上限">{{ $t("public.ccsx") }}</span>
          <span class="flex-1" name="状态"> {{$t("public.state") }} </span>
        </div>
        <div class="value">
          <span class="flex-1">{{item.myPower}}</span>
          <span class="flex-1">${{item.myOutput}}</span>
          <span class="flex-1" style="font-size:14px">
            <!-- {{['待铸造','生产中'][item.status] || '已过期'}} -->
            {{[$t("public.daizhuzao"),$t("public.shengchanzhong")][item.status] || $t("public.yiguoqi")}}
          </span>
        </div>
      </div>
      <!-- 底部 -->
      <div class="token-details-bottom">
        <nut-button type="primary" size="small" @click="showBottom=true" name="开始铸造">
          {{$t("public.kaishizhuzao")}}
        </nut-button>
      </div>
    </div>
    <!-- 令牌详情 -->
    <div>
      <div class="m-card">
        <div class="my-cell flex">
          <div class="left-text" name="已产出时间">
            {{$t("tokenDetails.elapsedTime")}}
          </div>
          <div class="right-text flex-1">{{item.days}} {{$t("public.days") }}</div>
        </div>
        <div class="my-cell flex">
          <div class="left-text" name="当前B代币算力">
            {{$t("tokenDetails.bToken")}}
          </div>
          <div class="right-text flex-1">{{item.myPowerB}}</div>
        </div>
      </div>
      <div class="m-card">
        <van-list
          v-model:loading="loading"
          :finished="finished"
          :finished-text="$t('public.finished')"
          @load="onLoad"
        >
        <nut-cell-group>
          <template v-for="(item, index) in chainListLog" :key="index">
            <nut-cell center :title="item.memo" :sub-title="item.createTime">
               <template v-slot:link>
                <div style="text-align: right;">
                  <div style="font-size: 18px;color: #0072FF;">
                    <span>{{getDesc(item)}}</span>
                    <span style="font-size: 14px;padding-left:6px;text-align: right;"></span>
                  </div>
                  <div v-if="item.amountName">${{item.amountName}}</div>
                </div>
              </template>
            </nut-cell>
          </template>
        </nut-cell-group>
        </van-list>
      </div>
    </div>
    <van-popup :z-index="1" position="bottom" round closeable :style="{ height: '240px' }" v-model:show="showBottom" style="--nut-popup-close-icon-margin:10px">
    <div class="select-data_popup-content" v-if="showBottom">
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
              <!-- <span :class="{active:radioVal === 1}">{{getDb(2)}} AAA代币 + {{item.ep}} EP支付</span> -->
              <span :class="{active:radioVal === 1}">
                {{getDb(2)}} ART<span name="代币">&nbsp;{{$t('public.token')}}</span>
                  + {{item.ep}} IOT<span name="支付">&nbsp;{{$t('public.payment')}}</span>
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
  </div>
</template>

<script>
import { showConfirmDialog,showLoadingToast } from 'vant';
import { showToast } from '@nutui/nutui';
import DAppError, {dapp} from "../dapp/dapp";
export default {
  inject:['infoUser','pubNowItem','getTypeSourceName','pubPayPrice'],
  data() {
    return {
      showBottom: false,
      radioVal: '0',
      loading: false,
      finished: false,
      // 分页
      pages: {
        page: 0,
        total: 0
      },
      item:{
        id: -1,
        power: 0,
        output: 0,
        myOutput: 0,
        unitPrice: 0,
      },
      chainListLog: [
        // {
        //   memo: '铸造',
        //   createTime: '2020-10-10 10:10:10',
        //   type: 2,
        //   amount: 100,
        //   source: 3,
        // },
      ],
    }
  },
  created() {
    this.item = this.pubNowItem.value;
    this.getList();
  },
  methods: {
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
    getDb(v){
      let a = this.item.unitPrice * this.pubPayPrice.value.tokenA;
      if(v == 1){
        return a.toFixed(4);
      }else if(v == 2){
        let b = a * this.pubPayPrice.value.epRatio;
        return b.toFixed(4);
      }
    },
    getDesc(item) {
      item.amountName = '';
      let val = '';
      let amount = Number(item.amount || 0).toFixed(4);
      if (amount > 0) {
        val = `+${amount}`
      } else if (amount === 0){
        val = `-${amount}`
      }else{
        val = `${amount}`
      }
      item.memo = this.getTypeSourceName(item.type, item.source);
      if(item.type == 2 || item.type == 3){
        const {tokenA,tokenB} = this.pubPayPrice.value;
        const numItem = {2:{num:tokenA,val:'ART'},3:{num:tokenB,val:'SOUL'}}[item.type];
        val+=` ${numItem.val}`
        if(item.source == 3 || item.source == 4){
          item.amountName = (item.amount / numItem.num).toFixed(4)
        }
      }
      return val;
    },
    onLoad() {
      // 异步更新数据
      setTimeout(() => {
        // 加载状态结束
        this.loading = false;
        // 数据全部加载完成
        if (this.chainListLog.length >= this.pages.total) {
          this.finished = true;
        } else {
          this.getList();
        }
      }, 500);
    },
    getList(){
      const par = `type=1&type=2&type=3&type=5&source=3&source=4&linkId=${this.item.id}&page=`+this.pages.page;
      this.$ajax.getLogs(par).then((res) => {
        if(!this.chainListLog || this.pages.page === 0){
          // this.chainListLog = [];
        }
        if(res.code === 200) {
          this.pages.page++;
          this.chainListLog.push(...res.data.content);
          // 计算总页数
          this.pages.total = Math.ceil(res.data.total / res.data.pageable.size);
          if (this.chainListLog.length >= this.pages.total) {
            this.finished = true;
          }
        }
      });
    },
    save(){
      const that = this;
      const toast = showLoadingToast({
        duration: 0,
        message: '',
        forbidClick: true,
      });
      const par = {
        cardId: this.item.id,
        type: ~~this.radioVal,
        quantity: 1,
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
                state.showBottom = false;
                getChainListLog();
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
    }
  },
}
</script>

<style lang="scss">
body{
  background: #F5F5F5;
}
.token-details{
  .token-details-header-img{
    div{
      width: 100px;
      height: 100px;
      background-size: 100% 100%;
      display: inline-block;
    }
  }
  .m-card{
  margin-bottom: 10px;
    &:first-child{
      margin-top: 10px;
      &::before{
        display: none;
      }
    }
  }
  .token-details-header{
    background: #fff;
    text-align: center;
    padding-top: 20px;
    .token-details-header-name{
      font-size: 18px;
      color: #130F25;
      margin-top: 16px;
    }
    .token-details-header-name_val{
      margin-top: 6px;
    }
    .header-desc{
      margin-top: 16px;
      .name{
        display: flex;
        font-size: 14px;
        color: #646566;
        justify-content: space-between;
        margin-bottom: 10px;
      }
      .value{
        display: flex;
        justify-content: space-between;
        font-size: 16px;
        color: #130F25;
      }
    }
    .token-details-bottom{
      padding: 20px 0;
      // 字间距
      letter-spacing: 1px;
      --nut-button-small-padding: 0 26px;
    }
  }
}
</style>