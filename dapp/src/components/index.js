import navbar from './navbar.vue';
const arr = [navbar];
export default {
  install(Vue) {
    arr.forEach((item) => {
      Vue.component(item.name, item);
    });
  }
};