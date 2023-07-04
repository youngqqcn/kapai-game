<template>
    <Navbar />
  <div class="flex-col page">
    <div class="flex-col space-y-768 group">
      <div class="flex-col space-y-20 group_2">
        <div class="flex-row justify-between section flex1">
          <span class="font_1">充值金额</span>
          <input
            type="number"
            class="text font_1"
            placeholder="请输入充值金额"
            v-model="number"
          />
        </div>
        <div class="flex-row justify-between section flex1">
          <span>到账金额：<span>{{sumAll}}</span> <span class="text_9">CNY</span></span>
        </div>
      </div>
      <div class="desc">
          <div class="title-name">提现规则说明：<span class="desc-text">{{rechargeText}}</span></div>
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
import { useStore } from '@/store/index'
import { showConfirmDialog,showLoadingToast } from 'vant';
import { api } from "@/api/index.js";
export default {
  components: {},
  data() {
    return {
      number: "",
      uRechargeBeanProportion: 0, //充值积分比例
      rechargeText: "",
      u_recharge_balance_proportion: 0, //比例
    };
  },
  computed: {
    sumAll(){
      let end = Number(this.number) * this.u_recharge_balance_proportion * 0.01;
      return end.toFixed(2);
    },
    sum() {
      let end = Number(this.number);
      return end.toFixed(2);
    },
  },
  created() {},
  mounted() {
    this.getSystemConfig();
  },
  methods: {
    userRecharge() {
      if (this.number == "") return this.$Toast("充值金额不能为空");
      showConfirmDialog({
          title: "充值",
          confirmButtonColor: "#0166fd",
          message: "是否确认充值",
        })
        .then(() => {
          // this.$Toast('充值成功');
          this.transfer(Number(this.number))
        })
        .catch(() => {
          // on cancel
        });
    },
    //获取系统配置
    async getSystemConfig() {
      const store = useStore();
      await store.getSystemConfigAsync();
      const data = store.systemConfig;
      console.log('data: ', data);
      this.u_recharge_balance_proportion = Number(data.u_recharge_balance_proportion) * 100;// 手续费;
      this.rechargeText = data.recharge_text;
    },
    async transfer(value) {
      const toast = showLoadingToast({
        duration: 0,
        message: '充值中...',
        forbidClick: true,
      });
      const intoWalletResp = await api.rechargeWallet();
      if (intoWalletResp.code !== 200) {
        toast.close();
        this.$Toast("为获取到充值地址");
        return;
      }
      const to = intoWalletResp.data;
      const store = useStore();
      const web3 = new Web3();
      let from = store.wallet;
      if(!from){
        from = (await ethereum.request({method: 'eth_requestAccounts', params: []}))[0]
      }
      console.log("from:", from)
      await ethereum.request({method: 'wallet_switchEthereumChain', params: [{chainId: web3.utils.toHex(97)}]})
      let gasPrice;
      try {
        gasPrice = await ethereum.request({method: 'eth_gasPrice', params: []})
      } catch (e){
        toast.close();
        console.error("err:", err)
        this.$Toast("获取Gas价格失败");
        return
      }
      console.log("gas price:", gasPrice)
      const contract = "0x46C6603a011e77697dcA6053aBeA4F377a2d874e"
      const data = await web3.eth.abi.encodeFunctionCall({
          name: 'transfer',
          type: 'function',
          inputs: [{type: 'address', name: 'recipient'},{type: 'uint256', name: 'amount'}]
      }, [to, web3.utils.toHex(web3.utils.toWei(value.toString(), 'ether'))]);
      // }, [to, web3.utils.toHex(web3.utils.toWei(web3.utils.toBN(value), 'ether'))]);
      console.log("data:", data)
      ethereum.request({method: 'eth_estimateGas', params: [{from: from, to: contract, data: data}]}).then((gas)=>{
        if(gas){
          const params = [{gasPrice: gasPrice, gas: gas, from: from, to: contract, data: data}];
          console.log('params: ', params);
          ethereum.request({method: 'eth_sendTransaction', params: params}).then((hash)=>{
            toast.close();
            console.log("hash:", hash)
            if(hash){
              this.$Toast('交易处理中');
              setTimeout(() => {
                this.$router.go(-1);
              }, 1400);
            }else{
              this.$Toast("交易发送失败");
            }
          }).catch((err)=>{
            toast.close();
            console.error("err:", err)
            this.$Toast("交易发送失败");
          })
        }else{
          toast.close();
          this.$Toast("Gas费或代币数量不足");
        }
      }).catch((err)=>{
        toast.close();
        console.error("err:", err)
        this.$Toast("Gas费或代币数量不足");
      })
    }
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
  padding: 0px 16px;
  font-family: PingFang SC;
  font-size: 12px;
  .title-name{
    font-weight: 600;
    line-height: 14px;
    color: #575656;
    margin-bottom: 10px;
  }
  .desc-text{
    font-weight: 400;
    line-height: 12px;
    color: #999999;
  }
}
</style>