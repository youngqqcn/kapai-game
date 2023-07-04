import { createNonceStr } from "@/utils/aes";
import { objKeySort } from "@/utils/aes";
import { objToStr } from "@/utils/aes";
import { sha1 } from "@/utils/aes";
import { md5 } from "@/utils/aes";
// import { decrypt } from "@/utils/aes";
import { encrypt } from "@/utils/aes";
function gettoken(res) {
  // 当前时间戳
  var timestamp = Date.parse(new Date()) / 1000;
  // 随机字符串
  var nonce = createNonceStr();
  if (!res) {
    res = {};
    // //模拟URL参数
    // var str = "nonce=" + nonce + "&timestamp=" + timestamp;
    // // 字符串转对象
    // var res = strToObj(str);
  }
  res.nonce = nonce;
  res.timestamp = timestamp;
  // 对象排序
  res = objKeySort(res);
  // 创建一个新对象
  var res1 = res;

  // 对象转字符串
  res = objToStr(res);

  // 拼接秘钥
  res = res + "5fbb510a4352bb01";
  // res = res + "ffba5aae5e014f80";
  // 删除&
  res = res.substr(1);
  //sha1加密
  res = sha1(res);
  // MD5加密
  res = md5(res);
  // 追加签名
  res1.sign = res;
  // 对象转字符串
  res1 = JSON.stringify(res1);
  // aes加密
  const res2 = encrypt(res1);
  return res2;
}
import { useStore } from '@/store/index'
import { showToast } from '@nutui/nutui';
import {dapp} from "../dapp/dapp.js";
import {api} from "../api";
let changeWallet = ()=>{
  let is = true;
  return ()=>{
    const ethereum = dapp.ethereum();
    if(ethereum && is){
      is = false;
      console.log('注册事件..');
      window.ethereum.on('accountsChanged', (accounts) => {
        sessionStorage.clear()
        console.log('刷新页面')
        showToast.loading();
        api.logout().then((e)=>{
          showToast.hide()
          setTimeout(()=>{
            useStore().logout();
            // window.location.reload()
          })
        }).catch(()=>{
          showToast.hide();
          useStore().logout();
        })
      });
      window.ethereum.on('chainChanged', (chainId) => ()=>{
        console.log('刷新页面')
        setTimeout(()=>{
          window.location.reload()
        })
      });
    }
  }
};
let onChangeWallet = changeWallet();
onChangeWallet();
const utils = {
  gettoken,
  async isSoldOut(contract, node){
    try {
      const owner = await dapp.requestAccounts();
      console.log("owner:", owner);
      const data = dapp.web3().eth.abi.encodeFunctionCall({
        name: 'isSoldOut',
        type: 'function',
        inputs: [{name: "node", type: "uint256"}],
      }, [node]);
      const result = await dapp.requestCall(owner, contract, data)
      const values = dapp.web3().eth.abi.decodeParameters([{type: 'bool', name: 'isSoldOut'}], result);
      console.log('isSoldOut:', values.isSoldOut);
      return values.isSoldOut;
    } catch (error) {
      console.log('isSoldOut error: ', error);
    }
    return false
  },
  getSign(originMessage, account){
    return new Promise(async(resolve, reject) => {
      const ethereum = dapp.ethereum();
      if (ethereum) {
        const sign = await dapp.signMessage(originMessage, account);
        onChangeWallet();
        resolve(sign);
      } else {
        console.log(store.public.$t('public.toast.qzAPPzfw')); // '请在app中访问'
        reject();
      }
    })
  },
  getWallet(){
    return new Promise((resolve, reject) => {
      const ethereum = dapp.ethereum();
      if (!ethereum) {
        reject(store.public.$t('public.toast.qzAPPzfw')); // '请在app中访问'
        return;
      }
      const store = useStore();
      dapp.requestAccounts()
        .then((red)=>{
          if(red){
            store.wallet = red;
            resolve(red);
          }else{
            // 提示
            showToast.warn(store.public.$t('public.toast.hqsb')); // '获取失败'
            reject(store.public.$t('public.toast.hqsb')); // '获取失败'
          }
        })
        .catch((error) => {
          if (error.code === 4001) {
            // EIP-1193 userRejectedRequest error
            console.log('Please connect to MetaMask.');
          } else {
            console.error(error);
          }
          reject(error);
        });
    })
  }
}
export { utils }
export default {
  install: (Vue) => {
    const globalProperties = Vue.config.globalProperties;
    globalProperties.$utils = utils;
  }
}