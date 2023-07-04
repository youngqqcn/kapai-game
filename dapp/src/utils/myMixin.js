const myMixin = {
  created: function () {
  },
  methods: {
    getAssetsFile: (url) => {
      return new URL(`../assets/images/${url}`, import.meta.url).href
   }
  }
}
export default myMixin