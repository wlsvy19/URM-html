import Vue from 'vue'
import VueRouter from 'vue-router'

import Data from '@/components/rule/Data'
import System from '@/components/rule/System'
import Request from '@/components/rule/Request'

import User from '@/components/manage/User'
import Biz from '@/components/manage/BizCode'

import RequestProcessDay from '@/components/statics/RequestProcessDay'
import RequestProcessMonth from '@/components/statics/RequestProcessMonth'
import RequestChangeDay from '@/components/statics/RequestChangeDay'
import RequestChangeMonth from '@/components/statics/RequestChangeMonth'

import ProcessStaticsDay from '@/components/process/stat/ProcessStaticsDay'
import ProcessStaticsHour from '@/components/process/stat/ProcessStaticsHour'

import LogRealtime from '@/components/process/log/LogRealtime'
import LogBatch from '@/components/process/log/LogBatch'
import LogDeferred from '@/components/process/log/LogDeferred'

Vue.use(VueRouter)

export default new VueRouter({
  mode: 'history',
  routes: [
    {path: '/data', component: Data},
    {path: '/system', component: System},
    {path: '/request', component: Request},

    {path: '/user', component: User},
    {path: '/biz', component: Biz},

    {path: '/process/day', component: RequestProcessDay},
    {path: '/process/month', component: RequestProcessMonth},
    {path: '/change/day', component: RequestChangeDay},
    {path: '/change/month', component: RequestChangeMonth},

    {path: '/log/online/:server', component: LogRealtime},
    {path: '/log/batch/:server', component: LogBatch},
    {path: '/log/deferred/:server', component: LogDeferred},

    {path: '/stat/day/:type/:server', component: ProcessStaticsDay},
    {path: '/stat/hour/:type/:server', component: ProcessStaticsHour},
  ]
})