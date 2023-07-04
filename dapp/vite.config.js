import { fileURLToPath, URL } from 'node:url'
import { loadEnv } from 'vite'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import importToCDN, { autoComplete } from 'vite-plugin-cdn-import'
// https://vitejs.dev/config/
const u = import.meta.url
console.log('u: ', u);
export default ({ mode }) => {
  return defineConfig({
    plugins: [
      vue(),
      importToCDN({
        modules: [
          {
            name:"vue",
            var:"Vue",
            path:"https://cdn.jsdelivr.net/npm/vue@3.2.47/dist/vue.global.min.js"
          },
          {
            name: 'vue-demi',
            var: 'VueDemi',
            path: './lib/index.iife.min.js'
          },
          {
            name:"axios",
            var:"axios",
            path:"https://cdn.jsdelivr.net/npm/axios@1.3.2/dist/axios.min.js",
          },
          {
            name:"vant",
            var:"vant",
            path:"https://cdn.jsdelivr.net/npm/vant@4.0.11/lib/vant.min.js",
            css:"https://cdn.jsdelivr.net/npm/vant@4.0.11/lib/index.min.css"
          },
          {
            name:'vue-router',
            var:'VueRouter',
            path:'https://cdn.jsdelivr.net/npm/vue-router@4.1.6/dist/vue-router.global.min.js'
          },
          {
            name:'vue-i18n',
            var:'VueI18n',
            path:'https://cdn.jsdelivr.net/npm/vue-i18n@9.2.2/dist/vue-i18n.global.min.js'
          },
          // {
          //   name:'pinia',
          //   var:'pinia',
          //   path:'https://cdn.jsdelivr.net/npm/pinia@2.0.28/dist/pinia.iife.min.js'
          // },
          // {
          //   name:'@nutui/nutui',
          //   var:'NutUI',
          //   path:'https://cdn.jsdelivr.net/npm/@nutui/nutui@4.0.1/dist/nutui.umd.min.js',
          //   css:"https://cdn.jsdelivr.net/npm/@nutui/nutui@4.0.1/dist/style.min.css"
          // },
          // autoComplete('@nutui/nutui/dist/style.css'),
        ]
      })
    ],
    // base: '/',
    base:loadEnv(mode, process.cwd()).VITE_APP_BASE_URL,
    server: {
      port: 8080,
      https: false,
      open: true,
      cors: true,
      proxy: {
        '/apis_dapp': {
          target: 'https://chain.coinscos.com/',
          changeOrigin: true,
          rewrite: (path) => path.replace(/^\/apis_dapp/, 'api/')
        },
        '/apis': {
          target: 'https://chain.coinscos.com/',
          changeOrigin: true,
          rewrite: (path) => path.replace(/^\/apis/, 'api/')
        },
      },
    },
    define: { 'process.env': {} },
    resolve: {
      alias: {
        '~': fileURLToPath(new URL('./src/assets', import.meta.url)),
        '@': fileURLToPath(new URL('./src', import.meta.url))
      }
    }
  })
}
