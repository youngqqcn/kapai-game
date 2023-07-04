<template>
  <div class="equity-page">
    <div v-html="equityDesc"></div>
  </div>
</template>

<script>
export default {
  name: 'EquityPage',
  data(){
    return {
      equityDesc: ''
    }
  },
  mounted(){
    this.equityDesc = this.$useStore.public.equityDesc;
  },
  methods:{
  }
}
</script>

<style lang="scss" scoped>
.equity-page{
  padding: 16px;
  height: calc(100vh - 44px);
  font-size: 14px;
  color: #333;
}
</style>