import {ajax, ajaxDapp} from "@/utils/ajax"
import {utils} from "@/utils"
import {networkConfig} from '@/config/networkConfig'
const api = {
  // 登录
   onSign: (params = {}) => ajaxDapp.post(`/api/auth/sign`, params),
   logout: (params = {}) => ajaxDapp.get(`/api/auth/logout`, params),
   getCardList: (params = {}) => ajaxDapp.get(`/card/list`, params),

  getUserInfo: (params = {}) => ajaxDapp.get(`/wallet/info`, params),  // 获取用户信息
  getPrice: (params = {}) => ajaxDapp.get(`/token/price`, params), // 获取代币价格
  getCardPaySign: (params = {}) => ajaxDapp.post(`/card/mold/data`, params), // 铸造支付前签名
  getCardPaySend: (params = {}) => ajaxDapp.post(`/card/mold/send`, params), // 铸造支付
  getLogs: (params = '') => ajaxDapp.get(`/wallet/logs?${params}`), // 日志（列表数据）
  payWallet: (params = {}) => ajaxDapp.post(`/wallet/withdraw`, params), // 转入钱包
  getFans: (params = {}) => ajaxDapp.get(`/wallet/fans`, params), // 我的粉丝
  getCards: (params = {}) => ajaxDapp.get(`/wallet/cards`, params), // 我的令牌
  getWalletBind: (params = {}) => ajaxDapp.post(`/wallet/bind`, params), //  绑定邀请人钱包地址
  getPowerInfo: (params = {}) => ajaxDapp.get(`/power/info`, params), //  获取算力信息
  getWalletCheckIn: (params = {}) => ajaxDapp.get(`/wallet/checkIn?${params}`, ), //  绑定邀请人钱包地址

  getNodeList: (params = {}) => ajaxDapp.get(`/node/list`, params), // 获取节点列表
  getNodeOrder: (params = '') => ajaxDapp.get(`/node/orders?${params}`), // 获取节点订单列表
  getNodeBuy: (params = {}) => ajaxDapp.post(`/node/buy`, params), // 节点签名
  getNodeSend: (params = {}) => ajaxDapp.post(`/node/send`, params), // 提交购买交易













    // 获取用户信息
    // getUserInfo: (params = {}) => ajax.post(`/Api/api/userInfo`, {param: utils.gettoken(params)}),
    // PSG变化记录
    getChainListLog: (params = {}) => ajax.post(`/Api/api/chainListLog`, {param: utils.gettoken(params)}),
    // 绿色积分明细
    getShopIntegralLog: (params = {}) => ajax.post(`/Api/api/shopIntegralLog`, {param: utils.gettoken(params)}),
    // 转入钱包
    upChain: (params = {}) => ajax.post(`/Api/api/upChain`, {param: utils.gettoken(params)}),
    // 合伙人积分明细
    partnerChainIntegral: (params = {}) => {
      if(networkConfig.dev){
        console.log('加密前: ', params);
      }
      return ajax.post(`/Api/api/partnerChainIntegralLog`, {param: utils.gettoken(params)})
    },
    upChain2: (params = {}) => {
      return ajaxDapp.post(`/dapp/withdraw`, params,{
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        }
      })
    },
    getSign: (params = {}) => ajaxDapp.get(`/auth/signMessage?wallet=${params.wallet}`),//?wallet=钱包地址
    // getSign: (params = {}) => ajaxDapp.get(`/dapp/sign?wallet=${params.wallet}`),//?wallet=钱包地址
    // getSign: (params = {}) => ajax.get(`http://8.135.112.23:8080/dapp/sign?${params.wallet}`),
    rechargeWallet:()=>ajaxDapp.get("/dapp/recharge/wallet"),
    // 提现
    upChainDapp: (params = {}) => {
      return ajaxDapp.post(`/dapp/withdraw/usdt`, params,{
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        }
      })
    },

}
export default {
    install: (Vue) => {
      const globalProperties = Vue.config.globalProperties;
      globalProperties.$ajax = api;
    }
};
export {api}
//获取默认配置
export function getSystemConfig(params = {}) {
  console.log('params: ', params);
  return ajax.post(`/Api/api/getSystemConfig`, {param: utils.gettoken(params)});
}