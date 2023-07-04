<template>
  <div class="node-order">
    <nut-pull-refresh v-model="refresh" @refresh="refreshFun">
      <nut-infinite-loading
          v-model="infinityValue"
          :has-more="hasMore"
          :load-more-txt="$t('public.finished')"
          @load-more="loadMore"
          vshow="hasMore || chainListLog.length > 0"
      >
      <nut-cell-group style="min-height:100px">
        <template v-for="(item, index) in chainListLog" :key="index">
          <div class="m-cell">
            <div class="m-cell__title">
              <div class="m-cell__title__text">{{item.name}}</div>
              <div class="m-cell__title__right">{{getDesc(item)}}</div>
            </div>
            <div class="m-cell__content">
              <div class="m-cell__content__text">{{infoUser.value.wallet}}</div>
            </div>
            <div class="m-cell__content">
              <div class="m-cell__content__time">{{item.createTime}}</div>
            </div>
          </div>
        </template>
      </nut-cell-group>
      </nut-infinite-loading>
      <div class="no-data" v-if="!refresh && chainListLog.length == 0">
          <img class="no-data-icon" src="@/assets/imgs/no-data.png" alt="">
          <div class="no-data-text" name="暂无数据">
            {{$t('public.zwsj')}}
          </div>
      </div>
    </nut-pull-refresh>
  </div>
</template>

<script>
export default {
  inject:['infoUser','num4','getTypeSourceName'],
  data(){
    return{
      isloading: false,
      refresh: false,
      infinityValue: false,
      hasMore: true,
      chainListLog: [],
      // 分页
      pages: {
        page: 0,
        total: 0
      },
    }
  },
  mounted(){
    this.getChainListLog();
  },
  methods:{
    loadMore(){
      if(!this.hasMore){
        return;
      }
      this.getChainListLog();
    },
    getChainListLog(){
      if(this.isloading){
        return;
      }
      this.isloading = true;
      const par = '&page='+this.pages.page;
      this.$ajax.getNodeOrder(par).then((res) => {
        if(res.code == 200){
          this.chainListLog = res.data.content;
          this.refresh = false;
          this.pages.page++;
          this.pages.total = Math.ceil(res.data.total / res.data.pageable.size);
          if(this.pages.page >= this.pages.total){
            this.hasMore = false;
          }
        }
      }).finally(() => {
        this.infinityValue = false;
        this.refresh = false;
        this.isloading = false;
      })
    },
    getDesc(item){
      // 前缀变量
      let before = '-';
      // if(item.price > 0){
      //   before = '+';
      // }
      return `${before}${Number(Number(item.price).toFixed(8))} USDT`;
    },
    refreshFun(){
      this.pages.page = 0;
      this.hasMore = true;
      this.getChainListLog();
    }
  }
}
</script>

<style lang="scss" scoped>
.node-order{
  .no-data{
    min-height: 80vh;
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
        // background: #FEB700;
        border-radius: 5px;
        text-align: center;
        // color: #FFFFFF;
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
}
</style>