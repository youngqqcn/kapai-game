<template>
  <!-- 数据查询 -->
  <div>
    <nut-pull-refresh v-model="refresh" @refresh="refreshFun">
      <div class="data-query">
          <div class="team-cell flex">
            <div class="left-text" name="今日ART销毁量">
              {{ $t('dataQuery.jrxl') }}
            </div>
            <div class="right-text flex-1">{{ aTodayDestroy }}</div>
          </div>
          <div class="team-cell flex">
            <div class="left-text" name="全网ART流通总量">
              {{ $t('dataQuery.qwltl_a') }}
            </div>
            <div class="right-text flex-1">
              {{ aCirculating }}
            </div>
          </div>
          <div class="team-cell flex">
            <div class="left-text" name="全网ART算力值">
              {{ $t('dataQuery.qwslz') }}
            </div>
            <div class="right-text flex-1">{{ allPower_a }}</div>
          </div>
          <div class="team-cell flex">
            <div class="left-text" name="全网ART动态算力值">
              {{ $t('dataQuery.qwttslz') }}
            </div>
            <div class="right-text flex-1">{{ aDynamicPower }}</div>
          </div>
          <div class="team-cell flex">
            <div class="left-text" name="全网ART静态算力值">
              {{ $t('dataQuery.qwjtslz') }}
            </div>
            <div class="right-text flex-1">{{ aStaticPower }}</div>
          </div>
          <div class="team-cell flex">
            <div class="left-text" name="今日SOUL销毁量">
              {{ $t('dataQuery.jrxl_b') }}
            </div>
            <div class="right-text flex-1">
              {{ bTodayDestroy }}
            </div>
          </div>
          <div class="team-cell flex">
            <div class="left-text" name="全网SOUL流通总量">
              {{ $t('dataQuery.qwltl_b') }}
            </div>
            <div class="right-text flex-1">
              {{ bCirculating }}
            </div>
          </div>
          <div class="team-cell flex">
            <div class="left-text" name="全网SOUL算力值">
              {{ $t('dataQuery.qwslz_b') }}
            </div>
            <div class="right-text flex-1">
              {{ allPower_b }}
            </div>
          </div>
          <div class="team-cell flex">
            <div class="left-text" name="全网SOUL静态算力值">
              {{ $t('dataQuery.qwjtslz_b') }}
            </div>
            <div class="right-text flex-1">
              {{ bStaticPower }}
            </div>
          </div>
          <div class="team-cell flex">
            <div class="left-text" name="全网SOUL动态算力值">
              {{ $t('dataQuery.qwttslz_b') }}
            </div>
            <div class="right-text flex-1">
              {{ bDynamicPower }}
            </div>
          </div>
      </div>
    </nut-pull-refresh>
  </div>
</template>
<script>
export default {
  data(){
    return {
      refresh: false,
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
  },
  created(){
    this.debounce = this.$basics.debounce();
    this.init();
  },
  methods:{
    refreshFun(){
      this.debounce(() => {
        this.init();
      }, 200);
    },
    init(){
      this.$ajax.getPowerInfo().then(res=>{
        if(res.code == 200){
          // 全网ART算力值 = aStaticPower + aDynamicPower
          // 所有算力值的相加 = bStaticPower + bDynamicPower
          let { aTodayDestroy, bTodayDestroy, aCirculating, bCirculating, aStaticPower, bStaticPower, aDynamicPower, bDynamicPower } = res.data;
          this.aTodayDestroy = aTodayDestroy;
          this.bTodayDestroy = bTodayDestroy;
          this.aCirculating = aCirculating;
          this.bCirculating = bCirculating;

          this.aStaticPower = Number(aStaticPower || 0);
          this.aDynamicPower = Number(aDynamicPower || 0);
          this.allPower_a = this.aStaticPower + this.aDynamicPower;

          this.bStaticPower = Number(bStaticPower || 0);
          this.bDynamicPower = Number(bDynamicPower || 0);
          this.allPower_b = this.bStaticPower + this.bDynamicPower;
        }
      }).finally(()=>{
        this.refresh = false;
      })
    }
  }
}
</script>

<style lang="scss">
.data-query{
  min-height: calc(100vh - 60px);
  .team-cell{
    position: relative;
    padding: 0 16px;
    line-height: 40px;
    // 分割线
    &::after{
      content: '';
      position: absolute;
      left: 16px;
      right: 0;
      bottom: 0;
      height: 1px;
      background-color: #EBEDF0;
      transform: scaleY(0.5);
      transform-origin: 0 0;
    }
    .left-text{
      min-width: 112px;
      color: #130F25;
      font-size: 14px;
    }
    .right-text{
      color: #333333;
      text-align: right;
    }
  }
}
</style>