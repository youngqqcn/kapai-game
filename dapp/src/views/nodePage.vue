<template>
  <!-- 节点 -->
  <div class="node-page">
    <div>
      <nut-pull-refresh v-model="refresh" @refresh="refreshFun">
        <header @click.stop>
        <span class="underline" name="权益" @click="toPages('equityPage')">
          {{ $t("nodePage.qy") }}
        </span>
          <span class="underline" name="订单" @click="toPages('nodeOrder')">
          {{ $t("nodePage.order") }}
        </span>
        </header>
        <div>
          <div class="team-cell flex">
            <div class="left-text" name="我的节点等级">
              {{ $t("nodePage.jddj") }}
            </div>
            <div class="right-text flex-1">
            <span class="link-rect">
              {{ ['', '小节点', '大节点', '超级节点'][infoUser.value.nodeLevel] }}
            </span>
            </div>
          </div>
          <div class="team-cell flex">
            <div class="left-text" name="锁仓ART">
              {{ $t("nodePage.csart") }}
            </div>
            <div class="right-text flex-1">
            <span class="link-rect">
              {{ infoUser.value.lockTokenA }}
            </span>
            </div>
          </div>
          <!-- <div class="team-cell flex">
            <div class="left-text" name="节点团队等级">
              {{$t("nodePage.tdjddj")}}
            </div>
            <div class="right-text flex-1">
              <span class="link-rect">
                V{{infoUser.value.teamNodeLevel}}
              </span>
            </div>
          </div>
          <div class="team-cell flex">
            <div class="left-text" name="团队认购份数">
              {{$t("nodePage.tdrgfs")}}
            </div>
            <div class="right-text flex-1">
              <span class="link-rect">
                {{infoUser.value.nodeCount}}
              </span>
            </div>
          </div> -->
        </div>
        <div style="margin-top: 10px;">
          <div class="token-cell" v-for="(item,i) in list" :key="i">
            <div class="token-cell_top flex">
              <div class="cell-left">
                <img src="@/assets/imgs/p0.png" alt="">
              </div>
              <div class="cell-center flex-1">
                <div class="name">{{ item.name }}</div>
                <div class="desc">
                  <div>${{ item.price }}</div>
                </div>
              </div>
              <div class="cell-right f-center">
                <div>
                  <span class="my-bottom primary" @click="onSubscribe(item)" name="认购">{{ $t('nodePage.rg') }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div style="margin-top:14px">
          <!-- <nut-pull-refresh v-model="refresh" @refresh="refreshFun"> -->
          <nut-infinite-loading
              v-model="infinityValue"
              :has-more="hasMore"
              :load-more-txt="$t('public.finished')"
              @load-more="loadMore"
          >
            <nut-cell-group>
              <template v-for="(item, index) in chainListLog" :key="index">
                <div class="m-cell">
                  <div class="m-cell__title">
                    <div class="m-cell__title__text">{{ item.memo }}</div>
                    <div class="m-cell__title__right">{{ getDesc(item) }}</div>
                  </div>
                  <div class="m-cell__content">
                    <div class="m-cell__content__text">{{ infoUser.value.wallet }}</div>
                  </div>
                  <div class="m-cell__content">
                    <div class="m-cell__content__time">{{ item.createTime }}</div>
                  </div>
                </div>
              </template>
            </nut-cell-group>
          </nut-infinite-loading>
          <!-- </nut-pull-refresh> -->
        </div>
      </nut-pull-refresh>
    </div>
  </div>
</template>

<script>
import {showLoadingToast, showToast} from 'vant';
import DAppError, {dapp} from "../dapp/dapp";

export default {
  inject: ['updateUserInfo', 'infoUser', 'num4', 'getTypeSourceName'],
  data() {
    return {
      refresh: false,
      isloading: false,
      infinityValue: false,
      hasMore: true,
      list: [],
      chainListLog: [],
      // 分页
      pages: {
        page: 0,
        total: 0
      },
    }
  },
  created() {
    this.debounce = this.$basics.debounce();
  },
  mounted() {
    this.getList()
    this.getChainListLog();
  },
  methods: {
    refreshFun() {
      this.pages.page = 0;
      this.hasMore = true;
      this.chainListLog = [];
      this.getChainListLog(true);
      this.debounce(() => {
        this.updateUserInfo();
      }, 300);
    },
    toPages(path) {
      this.$router.push({path})
    },
    loadMore() {
      if (!this.hasMore) {
        return;
      }
      this.getChainListLog();
    },
    getChainListLog(isRefresh) {
      if (this.isloading) {
        return;
      }
      if(isRefresh){
        this.getList();
      }
      this.isloading = true;
      const par = 'type=2&type=6&source=6&source=7&source=8&page=' + this.pages.page;
      this.$ajax.getLogs(par).then((res) => {
        if (res.code === 200) {
          this.pages.page++;
          this.chainListLog = this.chainListLog.concat(res.data.content);
          // 计算总页数
          this.pages.total = Math.ceil(res.data.total / res.data.pageable.size);
          if (this.pages.page >= this.pages.total) {
            this.hasMore = false;
          }
        }
      }).finally(() => {
        this.infinityValue = false;
        this.refresh = false;
        this.isloading = false;
      });
    },
    getList() {
      this.$ajax.getNodeList().then(res => {
        if (res.code == 200) {
          this.list = res.data
        }
      })
    },
    toPages(name, item) {
      this.$router.push({name, params: {item}})
    },
    onSubscribe(item, toast) {
      if (!toast) {
        toast = showLoadingToast({
          duration: 0,
          message: '',
          forbidClick: true,
        });
      }
      const that = this;
      (async function (item, toast) {
        toast.message = that.$t('public.toast.rgz') + '...';
        const res_data = await that.getNodeBuy({node: item.node})
        console.log(res_data);
        let {contract, usdt, orderId, chainId, wallet, data} = res_data;
        await dapp.switchEthereumChain(chainId);
        const is = await that.$utils.isSoldOut(item.contract, item.node);
        if (is) {
          toast.close();
          showToast(that.$t('public.toast.soldOut')); // 已售罄
          return;
        }
        const transactions = [
          () => dapp.approve(usdt, contract),
          () => dapp.sendTransaction(wallet, contract, data)
        ]
        const hashArray = await dapp.calls(...transactions);
        const res = await that.$ajax.getNodeSend({"hash": hashArray.pop(), "orderId": orderId})
        if (res) {
          return res.code === 200;
        }
        return false;
      })(item, toast).then((data) => {
        toast.close();
        if (data) {
          showToast(this.$t('public.toast.jiaoyichenggong'));
        } else {
          showToast(this.$t('public.toast.jiaoyishibai'));
        }
        this.updateUserInfo();
      }).catch((e) => {
        toast.close();
        if (e instanceof DAppError) {
          switch (e.code) {
            case DAppError.CODE_GAS_PRICE:
              showToast(this.$t('public.toast.hqGASjg'));
              break;
            case DAppError.CODE_ESTIMATE_GAS:
              showToast(this.$t('public.toast.GASfhdbslbz')+"!");
              break;
            case DAppError.CODE_SEND_TRANSACTION:
              showToast(this.$t('public.toast.jyfssb'));
              break;
            default:
              showToast(this.$t('public.toast.jiaoyishibai'));
              break;
          }
        } else {
          showToast(this.$t('public.toast.jiaoyishibai'));
        }
      });
    },
    getNodeBuy({node, nodeBuyData}) {
      return new Promise((resolve, reject) => {
        if (!nodeBuyData) {
          this.$ajax.getNodeBuy({node: node}).then(res => {
            if (res.code == 200) {
              resolve(res.data)
            } else {
              reject()
            }
          }).catch(() => {
            reject()
          })
        } else {
          resolve(nodeBuyData);
        }
      })
    },
    getDesc(item) {
      // 前缀变量
      let before = '';
      if (item.amount > 0) {
        before = '+';
      }
      item.memo = this.getTypeSourceName(item.type, item.source);
      if (item.memo) {
        item.memo = `${this.$t('public.suocang')}${item.memo} `;
      }
      // item.after_number保留两位小数
      let name = '';
      if (item.type === 2) {
        name = ` ART ${this.$t('public.token')}`
      } else if (item.type === 3) {
        name = ` SOUL ${this.$t('public.token')}`
      } else if (item.type === 6) {
        if ([8].includes(item.source)) {
          name = ` USDT ${this.$t('public.token')}`
        }
      }
      return `${before}${Number(Number(item.amount).toFixed(4))} ${name}`;
    },
  }
}
</script>

<style lang="scss" scoped>
.node-page {
  background-color: #F7F8FA;
  height: calc(100vh - 44px);

  header {
    text-align: right;
    padding: 10px 0;

    span {
      font-size: 16px;
      font-weight: 400;
      color: #576B95;
      line-height: 22px;
      margin-left: 4px;
      padding: 16px 8px;
    }
  }

  .team-cell {
    background: #fff;
    padding: 12px 16px;
    position: relative;
    font-size: 14px;
    // 分割线
    &::before {
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

    // 第一个 before
    &:first-child::before {
      display: none;
    }

    .right-text {
      text-align: right;
      // 换行
      word-break: break-all;

      .link-rect {
        padding-right: 4px;
        color: #130F25;
      }
    }
  }

  .token-cell {
    position: relative;
    background: #fff;
    padding: 16px;
    // 分割线
    &::before {
      content: '';
      position: absolute;
      left: 0;
      right: 0;
      top: 0;
      height: 1px;
      background-color: #EBEDF0;
      transform: scaleY(0.5);
      transform-origin: 0 0;
    }

    .cell-left {
      width: 80px;
      height: 80px;
      overflow: hidden;
      border-radius: 8px;
      background: #F7F8FA;

      img {
        width: 100%;
        height: 100%;
      }
    }

    .cell-center {
      padding: 16px 8px;
      position: relative;

      .name {
        font-size: 16px;
        color: #130F25;
      }

      .desc {
        font-size: 14px;
        color: #646566;
        line-height: 22px;
        margin-top: 16px;

        & div {
          white-space: nowrap;
        }
      }
    }

    .cell-right {
      position: relative;
      // width: 50px;
      text-align: right;

      .name {
        color: #130F25;
      }

      .desc {
        // position: absolute;
        right: 0;
        font-size: 14px;
        line-height: 22px;
        bottom: 24px;
        // color: #FEB700;
      }
    }

    .token-cell_top {
      height: 80px;
    }

    .token-cell_bottom {
      text-align: right;

      .right-but {
        display: inline-block;
      }

      .left-but {
        line-height: 25px;
        font-size: 12px;
        text-align: center;
        width: 80px
      }
    }
  }

  .m-cell {
    padding: 12px 16px;

    .m-cell__title {
      line-height: 26px;
      display: flex;

      .m-cell__title__text {
        font-size: 12px;
        line-height: 25px;
        display: inline-block;
        // width: 40px;
        padding: 0 8px;
        background: #FEB700;
        border-radius: 5px;
        text-align: center;
        color: #FFFFFF;
      }

      .m-cell__title__right {
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

    .m-cell__content {
      font-size: 12px;
      line-height: 17px;
      margin-top: 6px;

      .m-cell__content__time {
        color: #999999;
      }

      .m-cell__content__text {
        // 自动 换行
        word-break: break-all;
      }
    }
  }
}
</style>