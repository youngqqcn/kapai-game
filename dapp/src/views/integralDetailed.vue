<template>
    <Navbar />
  <div class="integral-detailed">
    <nut-cell
      style=""
      class="date-cell"
      :showIcon="true"
      :title="date ? `${date} ${dateWeek}` : '请选择日期'"
      desc=""
      @click="openSwitch('isVisible')"
    >
      <template v-slot:link>
        <!-- <nut-switch v-model="switchChecked" /> -->
        <img class="date-icon" src="@/assets/imgs/3.png" alt="">
      </template>
    </nut-cell>
    <nut-pull-refresh v-model="refresh" @refresh="refreshFun">
      <!-- <div class="pull-block">向下拉试试吧！</div> -->
      <template v-if="shopIntegralLog">
        <template v-if="shopIntegralLog.length > 0">
          <nut-infinite-loading
              v-model="infinityValue"
              :has-more="hasMore"
              @load-more="loadMore"
          >
          <nut-cell-group>
            <template v-for="(item, index) in shopIntegralLog" :key="index">
              <nut-cell center :title="item.memo" :sub-title="item.create_time" :desc="getDesc(item)"></nut-cell>
            </template>
          </nut-cell-group>
        </nut-infinite-loading>
          <!-- <nut-list :height="90" :listData="shopIntegralLog" @scroll-bottom="handleScroll">
            <template v-slot="{ item }">
              <div class="flex">
                <div class="gradient_radius">
                  <div class="but_in">
                    {{item.number > 0 ? '转入' : '转出'}}
                  </div>
                </div>
                <div class="flex-1 growth-value">
                  {{item.number}}
                </div>
              </div>
              <div class="desc">
                <div class="desc-name">{{item.memo}}</div>
                <div class="desc-sub">{{item.create_time}}</div>
              </div>
            </template>
          </nut-list> -->
        </template>
        <div v-else class="list-desc">
          <div class="no-data">
            <img class="no-data-icon" src="@/assets/imgs/no-data.png" alt="">
            <div class="no-data-text">暂无数据</div>
          </div>
        </div>
      </template>
    </nut-pull-refresh>
    <div id="m-calendar">

    <nut-calendar
      v-model:visible="isVisible"
      :ref="(el) => calendar = el"
      :show-today="false"
      :default-value="date"
      @close="closeSwitch('isVisible')"
      @choose="setChooseValue"
      @select="selectCalendar"
      :start-date="startDate"
      :end-date="endDate"
    >
    </nut-calendar>
    </div>
  </div>
</template>
<script lang="ts">
import { ref,reactive, toRefs,onMounted,getCurrentInstance,watch } from 'vue';
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

export default {
  setup() {
    const { appContext } = getCurrentInstance();
    const ajax = appContext.config.globalProperties.$ajax;
    const refresh = ref(false);
    const calendar = ref();
    const state = reactive({
      hasMore: true,
      infinityValue: false,
      isVisible: false,
      date: '',
      dateWeek: '',
      startDate: '',
      endDate: '',
      containerHeight: document.documentElement.clientHeight - 95,
      // 分页
      pages: {
        page: 1,
        limit: 10,
        total: 0
      },
      shopIntegralLog: null
    });
    state.startDate = '2023-02-01';
    const getShopIntegralLog = () => {
      const par = {
        page: state.pages.page,
        date: state.date,
      }
      if(!state.date){
        delete par.date;
      }
      ajax.getShopIntegralLog(par).then((res: any) => {
        if(!state.shopIntegralLog || par.page === 1){
          state.shopIntegralLog = [];
        }
        if(res.code === 0){
          if(res.data.length < 20){
            state.hasMore = false;
          }
          state.pages.page++;
          state.shopIntegralLog = [...state.shopIntegralLog, ...res.data];
        }
        refresh.value = false;
      });
    };
    const created = () => {
      getShopIntegralLog();
    };
    created();
    const handleScroll = () => {
      console.log('滚动到底部了');
      // getShopIntegralLog();
    };
    onMounted(() => {
    })
    // 防止多次触发
    let isreFresh = true;
    // 下拉刷新
    const refreshFun = (value) => {
      if(isreFresh){
        isreFresh = false;
        state.pages.page = 1;
        getShopIntegralLog();
        setTimeout(() => {
          isreFresh = true;
        }, 600);
          // refresh.value = false;
      }
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
      getShopIntegralLog();
    }
    const closeSwitch = (param: any) => {
      state[`${param}`] = false;
    };
    const calendarDay = {
      date:'',
      dateWeek:'',
    }
    const setChooseValue = (param: string[]) => {
      const { date, dateWeek } = calendarDay;
      state.date = date;
      state.dateWeek = dateWeek;
      // state.date = param[3];
      // state.dateWeek = param[4];
    };
    state.endDate = new Date().format('yyyy-MM-dd');
    const openSwitch = (param: any) => {
      state[`${param}`] = true;
    };
    const selectCalendar = (param: string[]) => {
      let dayClass = 'nut-calendar__day dddf';
      if(calendarDay.date !== param[3]){
        calendarDay.date = param[3];
        calendarDay.dateWeek = param[4];
        dayClass = 'nut-calendar__day nut-calendar__day--active';
      }else{
        // 取消选中状态
        calendarDay.date = '';
        calendarDay.dateWeek = '';
      }
      const [year, month, day, date, dateWeek] = param;
      const dayStr = `${Number(day)}`;
      const monthStr2 = `${year}-${month}`;
      const content = document.getElementsByClassName('nut-calendar__content')[0];
      const monthList = content.getElementsByClassName('nut-calendar__month');
      for(let i = 0; i < monthList.length; i++){
        let el = monthList[i];
        let monthTitle:any = el.getElementsByClassName('nut-calendar__month-title')[0];
        let monthStr:String = monthTitle.innerText.replace('年', '-').replace('月', '');
        if(monthStr === monthStr2){
          const dayAll = el.getElementsByClassName('nut-calendar__days-item')[0].getElementsByClassName('nut-calendar__day');
          for(let j = 0; j < dayAll.length; j++){
            let dayEl:any = dayAll[j];
            if(dayEl.innerText === dayStr){
              dayEl.setAttribute('class', dayClass);
              break;
            }
          }
          break;
        }
      }
      // state.date = param[3];
      // state.dateWeek = param[4];
      // state.isVisible = false;
    };
    watch(()=>state.date, (newValue, oldValue) => {
      console.log('newValue: ', newValue);
      state.pages.page = 1;
      state.hasMore = true;
      getShopIntegralLog();
    });
    return {
      ...toRefs(state),
      handleScroll,
      refreshFun,
      refresh,
      getDesc,
      loadMore,
      openSwitch,
      closeSwitch,
      setChooseValue,
      selectCalendar,
      calendar
    };
  }
};
</script>
<style lang="scss">
.integral-detailed{
  &::before {
    content: "";
    display: block;
    height: 46px;
    width: 100%;
    overflow: hidden;
    visibility: hidden;
  }
  position: relative;
  padding:0px 20px 10px 20px;
  .nut-cell__value{
    font-size: 14px;
    color: #52C41A;
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
  }
  .growth-value{
    font-size: 14px;
    color: #52C41A;
    text-align: right;
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