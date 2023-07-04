<template>
    <Navbar style="top:0;left:0;width:100%;" />
  <div class="flex-col page">
    <div class="flex-col space-y-768 group">
      <div class="flex-col space-y-20 group_2">
        <div class="flex-row justify-between section flex1">
          <span class="font_1">提现数量</span>
          <input
            type="number"
            class="text font_1"
            placeholder="请输入绿色积分数量"
            v-model="number"
          />
        </div>
        <div class="flex-col section_2" style="padding-bottom: 6px">
          <!-- <div class="flex-row justify-between group_3">
            <span class="font_1 text_2">到账方式</span>
            <span class="font_2 text_3">USDT</span>
          </div>
          <div class="flex-row justify-between group_3">
            <span class="font_1 text_4">当前比例</span>
            <span class="font_2 text_5">1:{{ uBeanWithdrawalProportion }}</span>
          </div> -->
          <div class="flex-row justify-between group_3">
            <span class="font_1 text_4">提现时间</span>
            <span class="font_2 text_5">T+1</span>
          </div>
          <div class="flex-row justify-between group_3" style="border: none">
            <span class="font_1 text_4">提现手续费</span>
            <span class="font_2 text_5">{{withdrawalService}}%</span>
          </div>
        </div>
        <div class="desc">
          <div class="title-name">提现规则说明</div>
          <div class="desc-text">{{withdrawalText}}</div>
        </div>
      </div>
      <div class="flex-row justify-between section_3">
        <div
          class="flex-row group_5"
          style="
            width: 200px;
            overflow-x: scroll;
            height: 40px;
            padding-top: 14px;
          "
        >
          <span class="text_6">合计:</span>
          <span class="text_7">{{ sum }}</span>
          <span class="text_9">USDT</span>
        </div>
        <div
          class="flex-col items-center text-wrapper clickFeedback"
          @click="userRecharge"
        >
          <span class="text_8">确认</span>
        </div>
      </div>
    </div>
  </div>
</template>
  
  <script>
import { userWithdrawal } from "@/api/user.js";
import { getSystemConfig } from "@/api/index.js";
import { showConfirmDialog,showLoadingToast } from 'vant';
import { useStore } from '@/store/index'
import { showToast } from '@nutui/nutui';
export default {
  components: {},
  data() {
    return {
      number: "",
      uBeanWithdrawalProportion: 1, //积分提现比例
      withdrawalService:0,//提现手续费
      u_withdrawal_proportion: 1, //提现比例
      withdrawalText: "", //提现规则
    };
  },
  computed: {
    sum() {
      let end = this.number;// * this.uBeanWithdrawalProportion;
      end = Number(end);
      isNaN(end) && (end = 0);
      end = end / this.u_withdrawal_proportion;
      //手续费
      const ws = this.withdrawalService * 0.01;
      end = end - end * ws;
      return end.toFixed(2);
    },
  },
  mounted() {
    this.getSystemConfig();
  },
  methods: {
    upChain() {
      const store = useStore();
      if(!store.wallet) {
        showToast.warn('请先连接钱包');
        return;
      }
      this.isLoading = true;
      const toast = showLoadingToast({
        duration: 0,
        message: '提现中...',
        forbidClick: true,
      });
      this.getSign().then(async (personal_sign)=>{
        console.log('personal_sign: ', personal_sign);
        const params = [personal_sign, store.wallet];
        console.log('参数: ', params);
        const sign = await ethereum.request({
          method:'personal_sign',
          params: params
        });
        console.log('sign: ', sign);
        const par = {
          wallet: store.wallet,
          value: this.number,
          sign
        }
        this.$ajax.upChainDapp(par).then((res) => {
          console.log(`==${res}==`);
          toast.close();
          if(res.code === 200) {
            showToast.success('提现成功');
            setTimeout(() => {
              this.$router.go(-1);
            }, 1400);
          }else{
            // showToast.warn(res.message||'提现失败');
          }
        }).catch((err)=>{
          console.error('err: ', err);
          showToast.warn('提现失败!');
          toast.close();
        }).finally(()=>{
          this.isLoading = false;
        })
      }).catch((err)=>{
        toast.close();
        console.error('err: ', err);
        showToast.warn('签名失败');
      }).finally(()=>{
        this.isLoading = false;
      })
    },
    getSign() {
      const store = useStore();
      return new Promise((resolve, reject) => {
        this.$ajax.getSign({wallet:store.wallet}).then((res) => {
          if(res.code === 200) {
            resolve(res.data);
          }else{
            reject(res.msg);
          }
        }).catch((err)=>{
          reject(err);
        });
      })
    },
    userRecharge() {
      if (this.number == "") return this.$Toast("提现金额不能为空");
      showConfirmDialog({
          title: "提现",
          confirmButtonColor: "#0166fd",
          message: "是否确认提现",
        })
        .then(() => {
          this.upChain();
        })
        .catch((e) => {
          console.error('e: ', e);
          // on cancel
        });
    },
    //获取系统配置
    async getSystemConfig() {
      const store = useStore();
      await store.getSystemConfigAsync();
      const data = store.systemConfig;
      console.log('data: ', data);
      this.uBeanWithdrawalProportion = data.uBeanWithdrawalProportion;
      this.u_withdrawal_proportion = Number(data.u_withdrawal_proportion);
      this.withdrawalService = Number(data.withdrawal_service) * 100;// 提现手续费
      this.withdrawalText = data.withdrawal_text;
    },
  },
};
</script>
  
  <style lang="scss" scoped>
input {
  text-align: right;
  border: none;
}
input::-webkit-input-placeholder {
  color: #999999;
}
input::-moz-placeholder {
  /* Mozilla Firefox 19+ */
  color: #999999;
}
input:-moz-placeholder {
  /* Mozilla Firefox 4 to 18 */
  color: #999999;
}
input:-ms-input-placeholder {
  /* Internet Explorer 10-11 */
  color: #999999;
}
.page {
  &::before {
    content: "";
    display: block;
    height: 40px;
    width: 100%;
    overflow: hidden;
    visibility: hidden;
  }
  background-color: #f5f5f5;
  width: 100%;
  overflow-y: auto;
  overflow-x: hidden;
  height: 100%;
  .space-y-768 {
    & > *:not(:first-child) {
    }
    .space-y-20 {
      & > *:not(:first-child) {
      }
      .section {
        background-color: #ffffff;
        border-radius: 10px;
        height: 56px;
        padding: 0 18px;
        margin-bottom: 14px;
        .text {
          color: #999999;
        }
      }
      .section_2 {
        padding-left: 20px;
        padding-right: 19px;
        background-color: #ffffff;
        border-radius: 10px;
        .group_3 {
          padding: 18px 0 13px;
          border-bottom: solid 0.5px #f5f5f5;
          .text_2 {
            line-height: 12.25px;
          }
          .text_3 {
            margin: 1px 0;
          }
        }
        .group_4 {
          padding: 13px 0 13px 1.5px;
          .text_4 {
            line-height: 12.25px;
          }
          .text_5 {
            margin: 1px 0;
          }
        }
        .font_2 {
          font-size: 13px;
          font-family: PingFang SC;
          line-height: 9.75px;
          color: #999999;
        }
      }
      .font_1 {
        font-size: 13px;
        font-family: PingFang SC;
        line-height: 12px;
        color: #333333;
      }
    }
    .group_2 {
      padding: 15.5px 15px 0;
    }
    .section_3 {
      padding: 10px 15px 10px 20.5px;
      background-color: #ffffff;
      position: fixed;
      bottom: 0;
      width: 100%;
      .group_5 {
        align-self: center;
        .text_6 {
          color: #333333;
          font-size: 16px;
          font-family: PingFang SC;
          font-weight: 600;
          line-height: 15px;
        }
        .text_7 {
          margin-left: 4px;
          color: #0166fd;
          font-size: 18px;
          font-family: PingFang SC;
          font-weight: 600;
          line-height: 13.25px;
        }
        .text_9 {
          margin: 2.5px 0 2px 4px;
          color: #333333;
          font-size: 14px;
          font-family: PingFang SC;
          font-weight: 600;
          line-height: 10.5px;
        }
      }
      .text-wrapper {
        padding: 15px 0;
        background-color: #0166fd;
        border-radius: 5px;
        width: 136px;
        height: 44px;
        .text_8 {
          color: #ffffff;
          font-size: 15px;
          font-family: PingFang SC;
          font-weight: 600;
          line-height: 14px;
        }
      }
    }
  }
  .group {
    overflow-y: auto;
  }
}
.desc{
  padding: 10px 16px;
  .title-name{
    font-size: 14px;
    font-family: PingFang SC;
    font-weight: 600;
    line-height: 14px;
    color: #575656;
    margin-bottom: 10px;
  }
  .desc-text{
    font-size: 12px;
    font-family: PingFang SC;
    font-weight: 400;
    line-height: 12px;
    color: #999999;
  }
}
</style>