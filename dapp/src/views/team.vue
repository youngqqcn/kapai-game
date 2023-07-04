<template>
  <!-- 我的团队 -->
  <div class="team">
    <nut-pull-refresh v-model="refresh" @refresh="refreshFun">
      <div class="m-card">
        <div class="team-cell flex">
          <div class="left-text" name="地址">
            {{$t("team.dizhi")}}
          </div>
          <div class="right-text flex-1">{{ infoUser.value.wallet }}</div>
        </div>
        <div class="team-cell flex">
          <div class="left-text" name="我的等级">
            {{$t("team.myLevel")}}
            <van-tag style="transform: scale(.8);background: linear-gradient(180deg, #328CFB 0%, #14F0F0 100%);" color="transparent" type="primary">
              <span>LV{{ infoUser.value.level }}</span>
            </van-tag>
          </div>
          <div class="right-text flex-1">
            <span class="link-but" name="升级条件" @click="toPages('levelDesc')">
              {{$t("team.upgradeConditions")}}
            </span>
          </div>
        </div>
        <div class="team-cell flex">
          <div class="left-text" name="动态算力值">
            {{$t("public.dtslz")}}
          </div>
          <div class="right-text flex-1">
            <span class="link-rect">
              {{num4(Number(infoUser.value.ztPower||0)+Number(infoUser.value.teamDynamicPower||0)+Number(infoUser.value.nodePower||0))}}
            </span>
          </div>
        </div>
        <div class="team-cell flex">
          <div class="left-text" name="直推动态算力值">
            {{$t("public.ztdtslz")}}
          </div>
          <div class="right-text flex-1" @click="toPages('hashPowerParticulars')">
            <span class="link-rect">
              {{infoUser.value.ztPower}}
            </span>
            <van-icon name="arrow" color="#323233" width="8px" height="8px" style="font-size:12px"/>
          </div>
        </div>
        <div class="team-cell flex">
          <div class="left-text" name="团队动态算力值">
            {{$t("public.tddtslz")}}
          </div>
          <div class="right-text flex-1">
            <span class="link-rect">
              {{infoUser.value.teamDynamicPower}}
              <!-- 获取用户等级✖️加权比例，不可点进去下一页 -->
            </span>
          </div>
        </div>
        <div class="team-cell flex">
          <div class="left-text" name="节点算力值">
            {{$t("public.jdslz")}}
          </div>
          <div class="right-text flex-1">
            <span class="link-rect">
              {{infoUser.value.nodePower}}
            </span>
          </div>
        </div>
      </div>
      <div class="m-card">
        <div class="team-cell flex">
          <div class="left-text" name="邀请人">
            {{$t("team.yaoqingren")}}
          </div>
          <div class="right-text flex-1">
            {{infoUser.value.superior}}
          </div>
        </div>
      </div>
      <div class="m-card">
        <div class="team-cell flex">
          <div class="left-text" name="大区业绩">
            {{$t("team.dqyj")}}
          </div>
          <div class="right-text flex-1">
            {{infoUser.value.bigPower}}
          </div>
        </div>
        <div class="team-cell flex">
          <div class="left-text" name="小区业绩">
            {{$t("team.xqyj")}}
          </div>
          <div class="right-text flex-1">
            {{infoUser.value.smallPower}}
          </div>
        </div>
      </div>
      <!-- <div class="m-card">
        <div class="team-cell flex">
          <div class="left-text" name="总业绩">
            {{$t("team.zyj")}}
          </div>
          <div class="right-text flex-1">
            {{Number(infoUser.value.bigPower||0)+Number(infoUser.value.smallPower||0)}}
          </div>
        </div>
      </div> -->
      <div class="m-card">
        <div class="team-cell flex" style="padding-right:30px;">
          <div class="left-text list-title" name="我的粉丝">{{$t("team.myFans")}} ({{fensList.length}})</div>
          <div class="right-text flex-1 list-title" name="算力">
            {{$t("team.power")}}
          </div>
        </div>
        <div class="team-cell flex" v-for="it in fensList" :key="it" style="padding: 0 30px;max-height:64px;">
          <div class="flex-1 list-left">
            <div class="flex" style="width: 100%;">
              <div class="h-img">
                <img
                class="single-avatar_7"
                referrerpolicy="no-referrer"
                src="https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPngd5a497d34032b1e0e9ed23ffc4f0631ed2d1529a4beb016c233a5969bacf7af7"
              />
              </div>
              <div class="flex-1 break">
                {{it.wallet}}
              </div>
            </div>
          </div>
          <div class="right-text" style="width:100px;line-height:60px">
            {{it.power}}
          </div>
        </div>
      </div>
    </nut-pull-refresh>
  </div>
</template>

<script>
export default {
  name: 'team',
  inject:['infoUser','num4'],
  data() {
    return {
      refresh: false,
      fensList: [
        // {
        //   wallet: '地址',
        //   power: '100'
        // }
      ]
    }
  },
  created() {
    this.debounce = this.$basics.debounce();
    this.init();
  },
  methods: {
    refreshFun() {
      this.debounce(() => {
        this.init();
      }, 200);
    },
    init(){
      this.$ajax.getFans().then(res=>{
        if(res.code == 200){
          this.fensList = res.data;
        }
      }).finally(()=>{
        this.refresh = false;
      })
    },
    toPages(path){
      this.$router.push({ path })
    }
  }
}
</script>

<style lang="scss">
.team{
  background-color: #F7F8FA;
  height: calc(100vh - 44px);
  .m-card{
    margin-bottom: 10px;
    .list-left{
      display: flex;
      // 垂直居中
      align-items: center;
      .h-img{
        width:40px;
        height: 40px;
        // background: #b0b0b1;
        border-radius: 50%;
        overflow: hidden;
      }
      .break{
        word-break: break-all;font-size: 12px;
        padding-left: 16px;
        line-height: 20px;
      }
    }
  }
  .team-cell{
    background: #fff;
    padding: 12px 16px;
    position: relative;
    font-size: 14px;
    // 分割线
    &::before{
      content: '';
      position: absolute;
      left: 16px;
      right: 0;
      top: 0;
      height: 1px;
      background-color: #EBEDF0;
      transform: scaleY(0.5);
      transform-origin: 0 0;
    }
    .left-text{
      width:100px;
    }
    .right-text{
      text-align: right;
      // 换行
      word-break: break-all;
      .link-rect{
        padding-right: 4px;
        color: #130F25;
      }
    }
    .link-but{
      display: inline-flex;
      flex-direction: row;
      align-items: center;
      justify-content: center;
      vertical-align: middle;
      position: relative;
      text-decoration: none;
      outline: none;
      cursor: pointer;
      padding: 0;
      font-size: 14px;
      font-weight: 500;
      color: #FEB700;
      &:after {
        content: "";
        position: absolute;
        left: 0;
        right: 0;
        height: 0;
        bottom: 0;
        border-bottom: 1px solid #FEB700;
      }
    }
    .list-title{
      font-size: 14px;
      font-family: PingFangSC-Semibold, PingFang SC;
      font-weight: 600;
      color: #130F25;
      line-height: 20px;
    }
  }
}
</style>