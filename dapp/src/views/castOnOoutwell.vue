<template>
  <div>
    <div class="select-data">
      <!-- <Navbar title="铸造">
        <template #right>
          <div><img src="@/assets/imgs/9.png" alt=""></div>
        </template>
      </Navbar> -->
      <div class="flex-1">
        <template v-if="chainListLog">
          <template v-if="chainListLog.length > 0">
            <nut-pull-refresh v-model="refresh" @refresh="refreshFun">
              <nut-infinite-loading
                  v-model="infinityValue"
                  :has-more="hasMore"
                  :load-more-txt="$t('public.finished')"
                  @load-more="loadMore"
              >
              <nut-cell-group>
                <template v-for="(item, index) in chainListLog" :key="index">
                  <!-- <nut-cell center :title="item.memo" :sub-title="item.create_time" :desc="getDesc(item)"></nut-cell> -->
                  <div class="list-items_1-0 flex-row">
                    <div class="group_2-0 flex-col" >
                      <div :class="getPMclass(item)"></div>
                    </div>
                    <div class="text-group_3-0 flex-col">
                      <span class="text_1-0">{{item.name}}</span>
                      <span class="text_2-0" name="算力值">
                        {{ $t("public.suanlizhi") }}&nbsp;{{item.power}}
                        </span>
                      <span class="text_3-0" name="产出上限">
                        {{ $t("public.ccsx") }}&nbsp;${{item.output}}
                      </span>
                    </div>
                    <div class="block_1-0 flex-1 justify-between">
                      <span class="text_4-0">${{item.unitPrice}}</span>
                      <div class="button_1-0">
                        <span class="text_5-0" @click="onShowBottom(item)" name="铸造">
                          {{$t('public.zhuzao')}}
                        </span>
                      </div>
                    </div>
                  </div>
                </template>
              </nut-cell-group>
            </nut-infinite-loading>
            </nut-pull-refresh>
          </template>
          <div v-else class="list-desc">
            <div class="no-data">
            <img class="no-data-icon" src="@/assets/imgs/no-data.png" alt="">
            <div class="no-data-text">{{$t('public.zwsj')}}</div>
          </div>
          </div>
        </template>
      </div>
    </div>
    <nut-calendar
      v-model:visible="isVisible"
      :default-value="date"
      @close="closeSwitch('isVisible')"
      @choose="setChooseValue"
      :start-date="startDate"
      :end-date="endDate"
    >
    </nut-calendar>
    <nut-popup :z-index="1" position="bottom" round closeable :style="{ height: '240px' }" v-model:visible="showBottom" style="--nut-popup-close-icon-margin:10px">
      <div class="select-data_popup-content">
        <div class="popup-title" name="支付方式">
          {{$t('public.paymentMethod')}}
        </div>
        <div>
          <nut-radio-group class="m-radio-group" v-model="radioVal" text-position="left" style="display: block;">
              <nut-radio label="0" text-position="left">
                <span :class="{active:radioVal === 0}">{{getDb(1)}}
                   ART&nbsp;<span name="代币">&nbsp;{{$t('public.token')}}</span>
                  <span name="支付">&nbsp;{{$t('public.payment')}}</span>
                </span>
              </nut-radio>
              <nut-radio label="1" text-position="left">
                <span :class="{active:radioVal === 1}">
                  {{getDb(2)}} ART<span name="代币">&nbsp;{{$t('public.token')}}</span>
                   + {{nowItem.ep}} IOT<span name="支付">&nbsp;{{$t('public.payment')}}</span>
                </span>
              </nut-radio>
          </nut-radio-group>
<!--          <div class="popup-desc">-->
<!--            <div class="title" name="当前IOT余额">{{$t('public.dqEPye')}}</div>-->
<!--            <div class="desc-content">{{userInfo.ep}}</div>-->
<!--          </div>-->
        </div>
        <div class="popup-button">
          <nut-button block type="primary" @click="save()">
            {{$t('public.confirm')}}
          </nut-button>
        </div>
      </div>
    </nut-popup>
  </div>
</template>
<script lang="ts">
// 获取本月天数
const getMonthDays = (year: number, month: number) => {
  let date = new Date();
  date.setMonth(date.getMonth() + 1); // 先设置为下个月
  date.setDate(0); // 再置0，变成当前月最后一天
  return date.getDate();
}
// 当月最后一天
const getLastDay = () => {
  const data = new Date();
  let new_year = data.getFullYear(); //取当前的年份
  let month = data.getMonth() + 1;
  let new_month = month++; //取下一个月的第一天，方便计算（最后一天不固定）
  if (month > 12) {
    //如果当前大于12月，则年份转到下一年
    new_month -= 12; //月份减
    new_year++; //年份增
  }
  return `${new_year}-${new_month}-${getMonthDays(new_year, new_month)}`;
};

// 本月第一天
const getFirstDay = () => {
  const data = new Date();
  let new_year = data.getFullYear(); //取当前的年份
  let month = data.getMonth() + 1;
  let new_month = month++; //取下一个月的第一天，方便计算（最后一天不固定）
  if (month > 12) {
    //如果当前大于12月，则年份转到下一年
    new_month -= 12; //月份减
    new_year++; //年份增
  }
  return `${new_year}-${new_month}-01`;
};
import { ref,reactive, toRefs,onMounted, getCurrentInstance,watch } from 'vue';
import { useStore } from '@/store/index'
import { showToast } from '@nutui/nutui';
import { showConfirmDialog,showLoadingToast } from 'vant';
import DAppError, {dapp} from '../dapp/dapp.js'
export default {
  setup() {
    const { appContext } = getCurrentInstance();
    const ajax = appContext.config.globalProperties.$ajax;
    const utils = appContext.config.globalProperties.$utils;
    const store = useStore();
    const state = reactive({
      payPrice: {
        isInit: false,
        epRatio: 0,
        tokenA: 0,
        tokenB: 0,
      },
      userInfo: store.userInfo || {},
      nowItem: {
        unitPrice: 0,
        id: 0,
      },
      radioVal: '0',
      showBottom: false,
      hasMore: true,
      infinityValue: false,
      isVisible: false,
      date: '',
      dateWeek: '',
      startDate: '',
      endDate: '',
      containerHeight: document.documentElement.clientHeight - 95,
      chainListLog: [
        // {
        //   imgUrl:
        //     '//img10.360buyimg.com/n2/s240x240_jfs/t1/210890/22/4728/163829/6163a590Eb7c6f4b5/6390526d49791cb9.jpg!q70.jpg',
        //   title: '青铜令牌',
        //   price: '388',
        //   vipPrice: '378',
        //   shopDesc: '自营',
        //   delivery: '厂商配送',
        //   shopName: '阳澄湖大闸蟹自营店>'
        // }
      ],
      // 分页
      pages: {
        page: 1,
        limit: 10,
        total: 0
      },
    });
    state.startDate = getFirstDay();
    const getChainListLog = () => {
      const par = {
        page: state.pages.page,
        date: state.date
      }
      if(!state.date){
        delete par.date;
      }
      ajax.getCardList(par).then((res: any) => {
        if(res.code == 200){
          state.chainListLog = res.data;
          state.hasMore = false;
          refresh.value = false;
        }
      }).finally(() => {
        state.infinityValue = false;
        refresh.value = false;
      }).finally(() => {
        state.infinityValue = false;
        refresh.value = false;
      });
    };
    getChainListLog();
    // 监听日期修改
    watch(()=>state.date, (newValue, oldValue) => {
      state.pages.page = 1;
      state.hasMore = true;
      getChainListLog();
    });
    state.endDate = getLastDay();
    const openSwitch = (param: any) => {
      state[`${param}`] = true;
    };
    const closeSwitch = (param: any) => {
      state[`${param}`] = false;
    };
    const setChooseValue = (param: string[]) => {
      state.date = param[3];
      state.dateWeek = param[4];
    };
    const handleScroll = () => {
    };
    onMounted(() => {
      // 可是区域高度
      state.containerHeight = document.documentElement.clientHeight - 74;
    })
    // 下拉刷新
    const refresh = ref(false);
    const refreshFun = () => {
      state.pages.page = 1;
      state.hasMore = true;
      getChainListLog();
    };
    // END 下拉刷新
    const getDesc = (item)=>{
      // 前缀变量
      let before = '+';
      if(item.before_number > item.after_number){
        before = '-';
      }
      // item.after_number保留两位小数
      return `${before}${Number(item.number).toFixed(8)}`;
    };
    const loadMore = ()=> {
      if(!state.hasMore){
        return;
      }
      getChainListLog();
    }
    const methods = {
      init(){
        ajax.getPrice().then((res: any) => {
          console.log('支付方式res: ', res);
          if(res.code == 200){
            const { epRatio, tokenA, tokenB } = res.data;
            state.payPrice = {
              isInit: true,
              epRatio: parseFloat(epRatio),
              tokenA: parseFloat(tokenA),
              tokenB: parseFloat(tokenB),
            }
          }
        });
      },
      onShowBottom: (item)=>{
        state.nowItem = item;
        state.showBottom = true;
      },
      save: function (e) {
        const toast = showLoadingToast({
          duration: 0,
          message: '',
          forbidClick: true,
        });
        const par = {
          cardId: state.nowItem.id,
          type: ~~state.radioVal,
        }
          console.log('par: ', par);
        ajax.getCardPaySign(par).then((res: any) => {
          const data = res.data
          if(res.code == 200 && data){
            console.log('api result: ', res);
            const transactions = [()=>dapp.switchEthereumChain(data.chainId)]
            if (par.type == 1) {
              transactions.push(()=>dapp.approve(data.contractEP, data.contractTokenA));
            }
            transactions.push(()=>dapp.sendTransaction(data.wallet, data.contractTokenA, data.data));
            dapp.calls(...transactions).then((data) => {
              console.log("sendTransaction: ", data)
              ajax.getCardPaySend({"hash": data.pop(), "orderId": res.data.orderId}).then((data)=>{
                toast.close();
                console.log('api result: ', data);
                if(data.code === 200){
                  showToast.success(this.$t('public.toast.jiaoyichenggong'));
                  state.showBottom = false;
                  this.$router.replace({ path:'myToken' })
                } else {
                  showToast.warn(store.public.$t('public.toast.jyfssb'));
                }
              }).catch((e)=>{
                toast.close();
                if (e instanceof DAppError) {
                  switch (e.code) {
                    case DAppError.CODE_GAS_PRICE:
                      showToast.warn(store.public.$t('public.toast.hqGASjg'));
                      break;
                    case DAppError.CODE_ESTIMATE_GAS:
                      showToast.warn(store.public.$t('public.toast.GASfhdbslbz')+"!");
                      break;
                    case DAppError.CODE_SEND_TRANSACTION:
                      showToast.warn(store.public.$t('public.toast.jyfssb'));
                      break;
                    default:
                      showToast.warn(store.public.$t('public.toast.jiaoyishibai'));
                      break;
                  }
                } else {
                  showToast.warn(store.public.$t('public.toast.jyfssb'));
                }
              });
            }).catch((e)=>{
              toast.close();
              showToast.warn(this.$t('public.toast.jiaoyishibai'));
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
      getDb(v){
        let a = state.nowItem.unitPrice * state.payPrice.tokenA;
        if(v == 1){
          return a.toFixed(4);
        }else if(v == 2){
          let b = a * state.payPrice.epRatio;
          return b.toFixed(4);
        }
      }
    }
    methods.init();
    return {
      ...toRefs(state),
      openSwitch,
      closeSwitch,
      setChooseValue,
      handleScroll,
      refreshFun,
      refresh,
      getDesc,
      loadMore,
      ...methods
    };
  }
};
</script>
<style lang="scss">
.select-data{
  min-height: 100vh;
  background: #fff;
  position: relative;
  .sd-time{
    line-height: 60px;
    padding-left: 16px;
    color:#323233
  }
  .nut-cell__value{
    font-size: 14px;
    color: #0072FF;
  }
  .date-icon{
    height: 15px;
    width: 15px;
  }
  .date-cell{
    border-radius: 40px;
  }
  .list-item{
    height: 79px;
    padding: 0 18px;
  }
  .nut-list-item{
    border-bottom: 1px solid rgba(54, 54, 68, 0.05);
    .list-item-time{
      margin-top: 10px;
    }
    &:last-child{
      border-bottom: none;
    }
  }
  .nut-list{
    .nut-list-item{
      // height: 90px;
      padding: 14px 14px 0 14px;
      border-bottom: 1px solid rgba(54, 54, 68, 0.05);
      &:last-child{
        border-bottom: none;
      }
    }
    .desc{
      margin-top:8px;
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
    .growth-value{
      font-size: 14px;
      color: #52C41A;
      text-align: right;
    }
  }
  .list-desc{
    height: 90vh;
    text-align: center;
    font-size: 14px;
    font-weight: 400;
    color: rgba(54, 54, 68, 0.5);
    line-height: 20px;
    padding-top: 10vh;
  }
.list-items_1-0 {
  background-color: rgba(255, 255, 255, 1);
  width: 100%;
  height: 121px;
  margin-bottom: 1px;
  justify-content: flex-center;
}

.group_2-0 {
  padding-left: 16px;
  border-radius: 8px;
  // width: 80px;
  height: 80px;
  margin: 20px 0 0 0px;
  & div{
    width: 80px;
    height: 80px;
  }
}
.text-group_3-0 {
  width: 112px;
  height: 80px;
  margin: 20px 0 0 8px;
}

.text_1-0 {
  width: 64px;
  height: 22px;
  overflow-wrap: break-word;
  color: rgba(19, 15, 37, 1);
  font-size: 16px;
  font-family: PingFangSC-Regular;
  
  text-align: left;
  white-space: nowrap;
  line-height: 22px;
}

.text_2-0 {
  width: 86px;
  height: 20px;
  overflow-wrap: break-word;
  color: rgba(100, 101, 102, 1);
  font-size: 14px;
  font-family: PingFangSC-Regular;
  
  text-align: left;
  white-space: nowrap;
  line-height: 20px;
  margin-top: 14px;
}

.text_3-0 {
  width: 112px;
  height: 20px;
  overflow-wrap: break-word;
  color: rgba(100, 101, 102, 1);
  font-size: 14px;
  font-family: PingFangSC-Regular;
  
  text-align: left;
  white-space: nowrap;
  line-height: 20px;
  margin-top: 4px;
}

.block_1-0 {
  // width: 56px;
  padding-right: 16px;
  text-align: right;
  height: 76px;
  margin: 20px 0px 0 0;
  overflow: hidden;
}

.text_4-0 {
  width: 39px;
  height: 22px;
  overflow-wrap: break-word;
  color: rgba(19, 15, 37, 1);
  font-size: 16px;
  font-family: PingFangSC-Regular;
  
  text-align: right;
  white-space: nowrap;
  line-height: 22px;
  margin-left: 17px;
}

.button_1-0 {
  border-radius: 8px;
  height: 25px;
  margin-top: 29px;
  text-align: right;
  .text_5-0 {
    display: inline-block;
    border-radius: 8px;
    text-align: center;
    width: 56px;
    background-color: rgba(0, 114, 255, 1);
    color: rgba(255, 255, 255, 1);
    font-size: 12px;
    line-height: 25px;
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