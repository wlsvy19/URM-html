import Vue from 'vue'
import VueRouter from 'vue-router'

import Data from '@/components/rule/Data'
import System from '@/components/rule/System'
import Request from '@/components/rule/Request'

import User from '@/components/manage/User'
import Biz from '@/components/manage/BizCode'

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
    
    {path: '/process1', component: ProcessStaticsDay},
    {path: '/process2', component: ProcessStaticsHour},
    
    {path: '/change1', component: ProcessStaticsDay},
    {path: '/change2', component: ProcessStaticsHour},
    
    {path: '/dlog1', component: LogRealtime},
    {path: '/dlog2', component: LogBatch},
    {path: '/dlog3', component: LogDeferred},
    
    {path: '/tlog1', component: LogRealtime},
    {path: '/tlog2', component: LogBatch},
    {path: '/tlog3', component: LogDeferred},

    {path: '/plog1', component: LogRealtime},
    {path: '/plog2', component: LogBatch},
    {path: '/plog3', component: LogDeferred},

    {path: '/dstat1', component: LogRealtime},
    {path: '/dstat2', component: LogBatch},
    {path: '/dstat3', component: LogDeferred},
    {path: '/dstat4', component: LogRealtime},
    {path: '/dstat5', component: LogBatch},
    {path: '/dstat6', component: LogDeferred},
    
    {path: '/tstat1', component: LogRealtime},
    {path: '/tstat2', component: LogBatch},
    {path: '/tstat3', component: LogDeferred},
    {path: '/tstat4', component: LogRealtime},
    {path: '/tstat5', component: LogBatch},
    {path: '/tstat6', component: LogDeferred},

    {path: '/pstat1', component: LogRealtime},
    {path: '/pstat2', component: LogBatch},
    {path: '/pstat3', component: LogDeferred},
    {path: '/pstat4', component: LogRealtime},
    {path: '/pstat5', component: LogBatch},
    {path: '/pstat6', component: LogDeferred}

  ]
})