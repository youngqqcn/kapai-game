<template>
  <div>
    <!-- 算力明细 -->
    <div class="h-select-data">
      <div class="sd-time" @click="openSwitch('isVisible')">
        <span style="font-size: 14px;">{{date?date:$t('public.qxz')}}</span>
        <span style="color:rgba(50, 50, 51, 0.4)" v-show="date" @click.stop="date=''">
          <van-icon name="clear" style="font-size:12px;padding: 4px 8px;" />
        </span>
        <span :style="{paddingLeft:(date?0:8)+'px'}">
          <van-icon name="arrow-down" color="#323233" width="8px" height="8px" style="font-size:12px" />
        </span>
      </div>
      <div class="flex-1">
        <template v-if="chainListLog">
          <template v-if="chainListLog.length > 0">
            <nut-pull-refresh v-model="refresh" @refresh="refreshFun">
              <nut-infinite-loading
                  v-model="infinityValue"
                  :has-more="hasMore"
                  @load-more="loadMore"
              >
              <nut-cell-group>
                <template v-for="(item, index) in chainListLog" :key="index">
                  <nut-cell center :title="$t('public.ztsl')" :sub-title="item.createTime" :desc="getDesc(item)"></nut-cell>
                </template>
              </nut-cell-group>
            </nut-infinite-loading>
            </nut-pull-refresh>
          </template>
          <div v-else class="list-desc">
            <div class="no-data">
            <img class="no-data-icon" src="@/assets/imgs/no-data.png" alt="">
            <div class="no-data-text">暂无数据</div>
          </div>
          </div>
        </template>
      </div>
    </div>
    <nut-calendar
      v-model:visible="isVisible"
      _default-value="date"
      @close="closeSwitch('isVisible')"
      @choose="setChooseValue"
      :start-date="startDate"
      _end-date="endDate"
    >
    </nut-calendar>
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
export default {
  setup() {
    const { appContext } = getCurrentInstance();
    const ajax = appContext.config.globalProperties.$ajax;
    const state = reactive({
      hasMore: true,
      infinityValue: false,
      isVisible: false,
      date: '',
      dateWeek: '',
      startDate: '',
      endDate: '',
      containerHeight: document.documentElement.clientHeight - 95,
      chainListLog: [] as any[],
      // 分页
      pages: {
        page: 0,
        limit: 10,
        total: 0
      },
    });
    state.startDate = getFirstDay();
    const getChainListLog = ()=> {
      let par = 'type=1&source=2&page='+state.pages.page;
      // let par = '&type=2&type=3&type=5&source=1&source=2&page='+state.pages.page;
      if(state.date){
        par += '&beforeDate='+state.date;
      }
      ajax.getLogs(par).then((res: any) => {
        if(!state.chainListLog || state.pages.page === 0){
          state.chainListLog = [];
        }
        if(res.code === 200) {
          state.pages.page++;
          // state.chainListLog = [...state.chainListLog,res.data.content];
          state.chainListLog.push(...res.data.content);
          // 计算总页数
          state.pages.total = Math.ceil(res.data.total / res.data.pageable.size);
          if(state.pages.page >= state.pages.total){
            state.hasMore = false;
          }
        }
      }).finally(() => {
        state.infinityValue = false;
        refresh.value = false;
      })
    };
    getChainListLog(true);
    // 监听日期修改
    watch(()=>state.date, (newValue, oldValue) => {
      state.pages.page = 0;
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
      const [yyyy, mm, dd] = param;
      let m = mm;
      if (mm.length === 1) {
        m = `0${mm}`;
      }
      state.date = `${yyyy}-${m}-${dd}`;
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
      return item.amount;
    };
    const loadMore = ()=> {
      if(!state.hasMore){
        return;
      }
      getChainListLog();
    }
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
    };
  }
};
</script>
<style lang="scss">
.h-select-data{
  height: calc(100vh - 44px);
  background: #F7F8FA;
  position: relative;
  .sd-time{
    line-height: 26px;
    padding-top: 8px;
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
}
</style>