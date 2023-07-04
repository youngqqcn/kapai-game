<template>
  <Navbar />
  <div class="partner">
    <nut-pull-refresh v-model="refresh" @refresh="refreshFun">
      <div class="m-card" :style="{backgroundImage:`url('${cardImg}')`}">
        <div style="padding: 0 24px;">
            <div class="m-card_title">YYK：(待释放)
            </div>
            <div class="m-card_body">
            </div>
            <div class="m-card_footer flex">
              <div class="flex items-center" style="width:100%;">
                <div class="flex-1"></div>
                <div>
                  {{userInfo.partner_chain_frozen_number}}
                  <!-- {{userInfo.integral}} -->
                </div>
              </div>
            </div>
          </div>
      </div>
      <nut-infinite-loading
            v-model="infinityValue"
            :has-more="hasMore"
            @load-more="loadMore"
        >
            <!-- <div class="infiniteLi" v-for="(item, index) in defultList" :key="index">{{item}}</div> -->
            <div v-if="defultList.length">
              <nut-cell-group v-if="defultList.length">
                <template v-for="(item, index) in defultList" :key="index">
                  <nut-cell center :title="item.memo" :sub-title="item.create_time" :desc="getDesc(item)"></nut-cell>
                </template>
              </nut-cell-group>
            </div>
            <template v-else>
                <div class="no-data">
                  <img class="no-data-icon" src="@/assets/imgs/no-data.png" alt="">
                  <div class="no-data-text">暂无数据</div>
                </div>
            </template>
        </nut-infinite-loading>
    </nut-pull-refresh>
  </div>
</template>

<script>
import cardImg from '@/assets/imgs/card2.png'
import { useStore } from '@/store/index'
export default {
  data() {
    return {
      cardImg,
      userInfo: {
        integral: 0,
        partner_chain_frozen_number: 0
      },
      refresh: false,
      infinityValue: false,
      hasMore: true,
      defultList: [],
      pages: {
        page: 1,
        limit: 10,
        total: 0
      },
    };
  },
  created() {
  },
  mounted() {
    this.$nextTick(()=>{
      this.getUserInfo();
      this.partnerChainIntegral();
    })
  },
  methods: {
    getUserInfo() {
      const store = useStore();
      if(store.userInfo){
        this.userInfo = store.userInfo;
        return;
      }
      this.$ajax.getUserInfo().then((res) => {
        if(res.code === 0) {
          this.userInfo = res.data
          store.userInfo = res.data;
        }
      });
    },
    getDesc(item){
      // 前缀变量
      let before = '+';
      if(item.before_number > item.after_number){
        before = '-';
      }
      // item.after_number保留两位小数
      return `${before}${Number(item.number).toFixed(8)}`;
    },
    // 下拉刷新
    refreshFun() {
      this.pages.page = 1;
      this.partnerChainIntegral();
    },
    partnerChainIntegral() {
      let par = {
          page: this.pages.page,
        }
      this.$ajax.partnerChainIntegral(par)
        .then((res) => {
          if(par.page == 1){
            this.defultList = [];
          }
          if (res.code === 0) {
            this.defultList = [...this.defultList,...res.data];
            if(res.data.length < 20){
              this.hasMore = false;
            }
          }
        }).finally(() => {
          this.refresh = false;
          this.infinityValue = false;
        })
    },
    // 加载更多
    loadMore() {
      this.partnerChainIntegral();
    },
  },
}
</script>
<style lang="scss">
.partner{
  padding:0 16px;
  .nut-cell__value{
    font-size: 14px;
    color: #52C41A;
  }
  .m-card{
    display: inline-block;
    // min-height: 176px;
    height: calc(100vw * 0.4);
    max-width: 700px;
    max-height: 365px;
    // padding: 24px;
    width: 100%;
    background-size: 100% 100%;
    text-align: left;
    color: rgba(78, 77, 77, 0.4);
    .m-card_title{
      margin-top: 24px;
      font-size: 16px;
      font-weight: 500;
      color: rgba(54, 52, 52, 0.4);
      line-height: 14px;
    }
    .m-card_body{
      margin-top: 24px;
      height: 40px;
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
  }
}
</style>