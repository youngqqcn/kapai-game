<template>
<div class="navbar">
  <nut-navbar class="m-navbar" @on-click-back="back" @on-click-title="title" :title="title">
    <template #left v-if="isBack">
       <svg class="nut-icon" xmlns="http://www.w3.org/2000/svg" color="" viewBox="0 0 1024 1024" aria-labelledby="" role="presentation"><path d="M657.92 983.04c-14.336 0-28.672-5.632-39.936-16.384L211.968 560.64c-10.752-10.752-16.384-25.088-16.384-39.936 0-14.848 6.144-29.184 16.384-39.936L617.984 74.752c22.016-22.016 57.856-22.016 79.872 0s22.016 57.856 0 79.872l-366.08 366.08 366.08 366.08c22.016 22.016 22.016 57.856 0 79.872-11.264 11.264-25.6 16.384-39.936 16.384z" fill="currentColor" fill-opacity="0.9"></path></svg>
    </template>
    <template #right>
      <slot name="right"></slot>
    </template>
  </nut-navbar>
</div>
</template>

<script>
import { toRefs } from 'vue';
import { useRouter,useRoute } from 'vue-router'
let router,route;
export default {
  name: 'Navbar',
  props: {
    isBack:{
      type: Boolean,
      default: true
    },
    title: {
      type: String,
      default: ''
    },
    back: {
      type: Function,
      default: () => {
        router.go(-1)
      }
    },
    titleClick: {
      type: Function,
      default: () => {

      }
    }
  },
  setup(props) {
    router = useRouter();
    route = useRoute();
    let { title, back, titleClick,isBack } = toRefs(props);
    if(!isBack){
      back = ()=>{}
    }
    return {
      title,
      back,
      titleClick
    };
  }
}
</script>

<style lang="scss">
body{
  .m-navbar{
    position: fixed;
    top: 0;
    width:100%;
    padding: 0;
    margin-bottom: 0;
    .nut-navbar__left{
      color: #000;
    }
  }
  .navbar{
    height: 44px;
    .nut-navbar__title{
      --nut-font-size-2: 17px;
      --nut-navbar-title-font-color: #000;
    }
  }
}
</style>