import Vue from 'vue'
import Vuex from 'vuex'

import ElementUI  from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import 'element-ui/lib/theme-chalk/display.css'

import VueI18n from 'vue-i18n'
import enLocale from 'element-ui/lib/locale/lang/en'
import koLocale from 'element-ui/lib/locale/lang/ko'
import ElementLocale from 'element-ui/lib/locale'

import koMessage from './locale/ko.json'

import App from '@/App.vue'
import router from '@/router'

import axios from 'axios'

Vue.use(Vuex)

Vue.use(VueI18n)
Vue.use(ElementUI, {
  size: 'small',
})

const messages = {
  en: {
    ...enLocale,
  },
  ko: {
    ...koMessage,
    ...koLocale,
  },
}

const i18n = new VueI18n({
  locale: 'en', // set locale
  messages, // set locale messages
})
ElementLocale.i18n((key, value) => i18n.t(key, value))

const store = new Vuex.Store({
  state: {
    codes: [],
    auths: [],
  },
})

Vue.prototype.$http = axios

Vue.prototype.$handleHttpError = function (err) {
  if (err.response) {
    console.log('handleHttpError err.response', err.response)
    let httpErr = '[' + err.response.status + ' : ' + err.response.statusText + '] '
    if (err.response.data.message) {
      httpErr += (' - ' + err.response.data.message)
    }
    this.$message({message: httpErr, type: 'error'})
    if (err.response.status === 401) { // not exist session
      self.location = '/login.page'
    }
  } else if (err.request) {
    console.log('handleHttpError err.request', err.request)
    let config = err.config
    let requestInfo = config.method + ' ' + config.url
    this.$message({message: requestInfo + ' ' + err.data, type: 'error'})
  } else {
    console.log('handleHttpError other ')
    console.log('Error', err.message);
    this.$message({message: err.message, type: 'error'})
  }
} // $handleHttpError

Vue.prototype.$randomRowKey = function (row) {
  row.__randomKey__ = row.__randomKey__ || Math.random()
  return row.__randomKey__
} // $randomRowKey

Vue.prototype.$startLoading = function () {
  const loading = this.$loading({
    lock: true,
    text: 'Loading',
    spinner: 'el-icon-loading',
    background: 'rgba(0, 0, 0, 0.3)',
  })
  return loading
} // $startLoading

Vue.prototype.$convertDateFormat  = function (format, date) {
  let val = !date ? new Date() : date
  let res = format
  if (format.indexOf('yyyy') > -1) {
    let yyyy = val.getFullYear()
    res = res.replace('yyyy', yyyy)
  }
  if (format.indexOf('MM') > -1) {
    let MM = val.getMonth() < 9 ? '0' + (val.getMonth() + 1) : (val.getMonth() + 1) // getMonth() is zero-based
    res = res.replace('MM', MM)
  }
  if (format.indexOf('dd') > -1) {
    let dd = val.getDate() < 10 ? '0' + val.getDate() : val.getDate()
    res = res.replace('dd', dd)
  }
  if (format.indexOf('HH') > -1) {
    let HH = val.getHours() < 10 ? '0' + val.getHours() : val.getHours()
    res = res.replace('HH', HH)
  }
  if (format.indexOf('mm') > -1) {
    let mm = val.getMinutes() < 10 ? '0' + val.getMinutes() : val.getMinutes()
    res = res.replace('mm', mm)
  }
  if (format.indexOf('ss') > -1) {
    let ss = val.getSeconds() < 10 ? '0' + val.getSeconds() : val.getSeconds()
    res = res.replace('ss', ss)
  }
  return res
}, // $convertDateFormat 

Vue.prototype.$reloadConfig = function () {
  console.log('reload config')
  // set locale
  this.$i18n.locale = 'ko'

  let url = '/api/code'
  // set common
  this.$http.get(url + '/common', {
  }).then(response => {
    this.$store.state.codes = response.data
  }).catch(error => {
    throw error
  })

  // set auth
  this.$http.get(url + '/auth', {
  }).then(response => {
    this.$store.state.auths = response.data
  }).catch(error => {
    throw error
  })
} // reloadConfig

Vue.prototype.$checkLogin = function () {
  console.log('check login')
  this.$store.state.user = {id: 'eai', name: 'eai', authId: '0'}
  // this.$http.get('/api/check/login', {
  // }).then(response => {
  //   this.$store.state.user = response.data
  // }).catch(error => {
  //   throw error
  // })
} // checkLogin

new Vue({
  render: h => h(App),
  store,
  router,
  i18n,
}).$mount('#app')
