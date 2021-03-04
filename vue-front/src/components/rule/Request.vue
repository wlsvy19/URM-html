<template>
  <div class="urm-panel">
    <RequestList ref="list" :items="listItem" :infTypes="infTypes"
      :procStats="procStats" :chgStats="chgStats" @search="handleSearch" @edit="handleEdit"
      @show-sys-list="showSystemList" @show-biz-list="showBizList" @show-usr-list="showUserList"/>

    <el-dialog :visible.sync="editorShow" width="1355px">
      <RequestEditor :item="editorItem" :infTypes="infTypes"
        :procStats="procStats" :chgStats="chgStats" @save="handleSave"
        @show-sys-list="showSystemList" @show-biz-list="showBizList" @show-usr-list="showUserList"/>
    </el-dialog>

    <el-dialog :visible.sync="systemListShow" width="1300px" top="5vh" append-to-body :close-on-click-modal="false">
      <SystemList ref="sysList" :items="systems" :onlySearch="true" @search="searchSystemList" @row-dblclick="cbSystemRowClick"/>
    </el-dialog>

    <el-dialog :visible.sync="bizListShow" width="1300px" top="5vh" append-to-body :close-on-click-modal="false">
      <BizCodeList ref="bizList" :items="bizCodes" :onlySearch="true" @search="searchBizList" @row-dblclick="cbBizRowClick"/>
    </el-dialog>

    <el-dialog :visible.sync="userListShow" width="1300px" top="5vh" append-to-body :close-on-click-modal="false">
      <UserList ref="usrList" :items="users" :onlySearch="true" @search="searchUserList" @row-dblclick="cbUserRowClick"/>
    </el-dialog>
  </div>
</template> 

<script>
import RuleMain from './RuleMain'
import RuleUtil from '@/components/rule/RuleUtil'

import BizCodeList from '@/components/manage/list/BizCodeList'
import UserList from '@/components/manage/list/UserList'

export default {
  mixins: [RuleMain],
  components: {
    BizCodeList,
    UserList,
  },
  data () {
    return {
      path: 'request',
      systemListShow: false,
      systems: null,
      bizListShow: false,
      bizCodes: null,
      userListShow: false,
      users: null,
      cbRowFunc: () => {},
    }
  },
  methods: {
    handleSearch (sparam) { // Override
      const loading = this.$startLoading()
      console.log('search : ' + this.path, sparam)
      this.$http.get('/api/' + this.path, {
        params: sparam,
      }).then(response => {
        let pageList = response.data
        this.listItem = pageList.list
        this.$refs.list.curPage = pageList.curPage
        this.$refs.list.totalCount = pageList.totalCount
      }).catch(error => {
        this.$handleHttpError(error)
      }).finally(() => {
        loading.close()
      })
    }, // handleSearch

    getNewItem () {
      let today = new Date()
      let week = new Date()
      week.setDate(week.getDate() + 7)
      return {
        id: '',
        interfaceType: '1',
        processStat: '1',
        chgStat: '1',
        syncType: '1',
        trType: '2',
        sendSystemType: '3',
        rcvSystemType: '3',
        sqlPlain: '',
        dbCrudType: '1',
        fileCrudType: '1',
        infFileName: '',
        reqDataMappingId: '',
        resDataMappingId: '',
        testStartYMD: today,
        testEndYMD: week,

        sendSystem: { name: '' },
        rcvSystem: { name: '' },
        sendJobCode: { part2Id: '', part2Name: '' },
        rcvJobCode: { part2Id: '', part2Name: '' },
        sendAdmin: { name: '', positionName: '--' },
        rcvAdmin: { name: '', positionName: '--' },
      }
    }, // getNewItem

    initData (item) {
      let newItem = this.getNewItem()
      if (!item.sendSystem) {
        item.sendSystem = newItem.sendSystem
      }
      if (!item.rcvSystem) {
        item.rcvSystem = newItem.rcvSystem
      }
      if (!item.sendJobCode) {
        item.sendJobCode = newItem.sendJobCode
      }
      if (!item.rcvJobCode) {
        item.rcvJobCode = newItem.rcvJobCode
      }
      if (!item.sendAdmin) {
        item.sendAdmin = newItem.sendAdmin
      }
      if (!item.rcvAdmin) {
        item.rcvAdmin = newItem.rcvAdmin
      }
    }, // initData

    showSystemList (cbRowFunc, type) {
      this.cbRowFunc = cbRowFunc
      let item = this.editorItem
      if (type && !this.$refs.sysList) {
        let sType = (type === 'send') ? item.sendSystemType : item.rcvSystemType
        this.searchSystemList(() => {
          this.systemListShow = true
          this.$nextTick(() => {
            this.$refs.sysList.sparam.type = sType
          })
        }, sType)
      } else if (type && type !== this.$refs.sysList.sparam.type) {
        if (type === 'send') {
          this.$refs.sysList.sparam.type = item.sendSystemType
        } else if (type === 'rcv') {
          this.$refs.sysList.sparam.type = item.rcvSystemType
        }
        this.searchSystemList(() => {
          this.systemListShow = true
        })
      } else if (!type && !this.systems) {
        this.searchSystemList(() => {
          this.systemListShow = true
        })
      } else {
        this.systemListShow = true
      }
    }, // showSystemList

    searchSystemList (cbFunc, type) {
      let sparam = this.$refs.sysList ? this.$refs.sysList.sparam : {type: type}
      const loading = this.$startLoading()
      this.$http.get('/api/system', {
        params: sparam
      }).then(response => {
        this.systems = response.data
        typeof cbFunc === 'function' && cbFunc()
      }).catch(error => {
        this.$handleHttpError(error)
      }).finally(() => {
        loading.close()
      })
    }, // searchSystemList

    cbSystemRowClick (row) {
      this.cbRowFunc(row)
      this.systemListShow = false
    }, // cbSystemRowClick

    showBizList (cbRowFunc) {
      this.cbRowFunc = cbRowFunc
      if (!this.bizCodes) {
        this.searchBizList(() => {
          this.bizListShow = true
        })
      } else {
        this.bizListShow = true
      }
    }, // showBizList

    searchBizList (cbFunc) {
      let sparam = this.$refs.bizList ? this.$refs.bizList.sparam : {}
      const loading = this.$startLoading()
      this.$http.get('/api/code/business', {
        params: sparam
      }).then(response => {
        this.bizCodes = response.data
        typeof cbFunc === 'function' && cbFunc()
      }).catch(error => {
        this.$handleHttpError(error)
      }).finally(() => {
        loading.close()
      })
    }, // searchBizList

    cbBizRowClick (row) {
      this.cbRowFunc(row)
      this.bizListShow = false
    }, // cbBizRowClick

    showUserList (cbRowFunc) {
      this.cbRowFunc = cbRowFunc
      if (!this.users) {
        this.searchUserList(() => {
          this.userListShow = true
        })
      } else {
        this.userListShow = true
      }
    }, // showUserList

    searchUserList (cbFunc) {
      let sparam = this.$refs.usrList ? this.$refs.usrList.sparam : {}
      const loading = this.$startLoading()
      this.$http.get('/api/user', {
        params: sparam
      }).then(response => {
        this.users = response.data
        typeof cbFunc === 'function' && cbFunc()
      }).catch(error => {
        this.$handleHttpError(error)
      }).finally(() => {
        loading.close()
      })
    }, // searchUserList

    cbUserRowClick (row) {
      this.cbRowFunc(row)
      this.userListShow = false
    }, // cbUserRowClick
  },
  computed: {
    infTypes: function () {
      let kind = RuleUtil.CODEKEY.infType
      return this.$store.state.codes.filter(code => (code.kind === kind))
    },
    chgStats: function () {
      let kind = RuleUtil.CODEKEY.chgStat
      return this.$store.state.codes.filter(code => (code.kind === kind))
    },
    procStats: function () {
      let kind = RuleUtil.CODEKEY.procStat
      return this.$store.state.codes.filter(code => (code.kind === kind))
    },
  },
}
</script>