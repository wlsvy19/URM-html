import Vue from 'vue'
import VueRouter from 'vue-router'

import Data from '@/components/rule/Data'
import System from '@/components/rule/System'

Vue.use(VueRouter)

export default new VueRouter({
  mode: 'history',
  routes: [
    {path: '/data', component: Data},
    {path: '/system', component: System},
  ]
})