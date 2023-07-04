/**
 * 基础工具
 */

/*eslint no-extend-native: ["error", { "exceptions": ["Date","String","Object","Function"] }]*/
import dayjs from 'dayjs';

// 过滤器
/**
 * 日期格式化
 * @param {String} fmt
 * @returns String
 */
Date.prototype.format = function(fmt) {
  fmt = fmt.replace('yyyy','YYYY').replace('dd','DD');
  return dayjs(this).format(fmt);
};
String.prototype.urlQuery=function(){
  let url=this.split('?');
  let param = {path:'',query:{}};
  if(url[1]){
    let paramArr = url[1].split('&');
    let kv = null;
    paramArr.map(it=>{
      if(it){
        kv = it.split('=');
        param.query[kv[0]] = kv[1]||'';
      }
    })
  }
  param.path=url[0];
  return param
}
// END 过滤器

// 实用函数

/**
 * 点击一次，调用next允许继续执行
 * @returns _par time=下次可点击的时间、id=根据id缓存状态
 */
var clickOne = () => {
  const nextStare = {}; // 状态
  let timeId = null;
  const next = (id = 0) => {
    clearTimeout(timeId);
    nextStare['k' + id] = false;
  };
  let key = null;
  return function(fn, _par = { time: 600 }) {
    key = 'k' + (_par.id || 0);
    if (!nextStare[key]) {
      nextStare[key] = true;
      fn();
    }
    if (_par.time) {
      clearTimeout(timeId);
      timeId = setTimeout(() => {
        next(_par.id);
      }, _par.time);
    }
    return next;
  };
};
/**
 * 点击一次，调用next允许继续执行
 * @returns _par time=下次可点击的时间、id=根据id缓存状态
 */
Function.prototype.clickOne = clickOne;
// 函数防抖
var debounce = () => {
  let timeId = null;
  return function(fn, time = 10) {
    clearTimeout(timeId);
    timeId = setTimeout(() => {
      fn();
    }, time);
  };
};
// 函数节流
var throttle=()=>{
  var pre = Date.now();
  return function(fn,wait){
      var now = Date.now();
      if( now - pre >= wait){
          fn();
          pre = Date.now();
      }
  }
}
const isObject = (val) => val !== null && typeof val === "object";
const isFunction = (val) => typeof val === "function";
const isDate = (val) => val instanceof Date;
const isString = (val) => typeof val === "string";
// END ----
const basics = {
  throttle: throttle,
  debounce: debounce,
  clickOne: clickOne,
  isString: isString,
  isPromise: (val) => {
    return isObject(val) && isFunction(val.then) && isFunction(val.catch);
  }
};
export default {
  install(Vue) {
    console.log('Vue: ', Vue);
    Vue.config.globalProperties.$basics = basics;
  }
}
export {basics};
/* window.onstorage = function(e) {
  if (e.key === 'miniPathBack') {
    _vue.$router.go(-1);
  }
  console.log(e.key + ' 键已经从 ' + e.oldValue + ' 改变为 ' + e.newValue + '.');
};
 */
