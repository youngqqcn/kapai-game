<template>
  <div class="level-page">
    <div v-html="levelDesc"></div>
  </div>
</template>

<script>
export default {
  name: 'EquityPage',
  data(){
    return {
      levelDesc: ''
    }
  },
  mounted(){
    this.levelDesc = this.$useStore.public.levelDesc;
  },
  methods:{
  }
}
</script>

<style lang="scss" scoped>
.level-page{
  padding: 16px;
  height: calc(100vh - 44px);
  font-size: 14px;
  color: #333;
}
</style>