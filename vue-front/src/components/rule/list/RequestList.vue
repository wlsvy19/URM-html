<template>
  <div class="urm-list">
    <div class="advanced-search-bar">
      <el-form label-width="135px">
        <div class="row" style="margin-top: 10px;">
          <el-form-item :label="$t('label.interfaceId')">
            <el-input v-model="sparam.interfaceId" class="search-id" @change="search"/>
          </el-form-item>
          <el-form-item :label="$t('label.interfaceType')">
            <el-select v-model="sparam.interfaceType" class="search-id" @change="search">
              <el-option value="" label="ALL"/>
              <el-option v-for="type in infTypes" :value="type.code" :label="type.name" :key="type.code"/>
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('label.requestId')">
            <el-input v-model="sparam.id" class="search-id" @change="search"/>
          </el-form-item>
          <el-form-item :label="$t('label.requestName')">
            <el-input v-model="sparam.name" class="search-name" @change="search"/>
          </el-form-item>
          <div class="search-buttons">
            <el-button @click.stop="clickTransfer()">이관</el-button>
          </div>
        </div>
        <div class="row">
          <el-form-item :label="$t('label.sourceSystem')">
            <el-input v-model="sparam.sendSystemId" class="search-id" @click.native.prevent="showSystemList('send')" readonly>
              <i slot="suffix" class="el-input__icon el-icon-close pointer" @click.stop="handleClear('sendSys')"/>
            </el-input>
          </el-form-item>
          <el-form-item :label="$t('label.sndJobCode')">
            <el-input v-model="sparam.sendJobCodeId" class="search-id" readonly>
              <i slot="suffix" class="el-input__icon el-icon-close pointer" @click.stop="handleClear('sendBiz')"/>
            </el-input>
          </el-form-item>
          <el-form-item :label="$t('label.changeStatus')">
            <el-select v-model="sparam.chgStat" class="search-id" @change="search">
              <el-option value="" label="ALL"/>
              <el-option v-for="stat in chgStats" :value="stat.code" :label="stat.name" :key="stat.code"/>
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('label.processStatus')">
            <el-select v-model="sparam.processStat" style="width: 150px" @change="search">
              <el-option value="" label="ALL"/>
              <el-option v-for="stat in procStats" :value="stat.code" :label="stat.name" :key="stat.code"/>
            </el-select>
          </el-form-item>
          <div class="search-buttons">
            <el-button @click.stop="clickEdit()">{{$t('label.add')}}</el-button>
            <el-button @click.stop="clickDelete('selected')" type="danger" plain>{{$t('label.delete')}}</el-button>
          </div>
        </div>
        <div class="row">
          <el-form-item :label="$t('label.targetSystem')">
            <el-input v-model="sparam.rcvSystemId" class="search-id" @click.native.prevent="showSystemList('rcv')" readonly>
              <i slot="suffix" class="el-input__icon el-icon-close pointer" @click.stop="handleClear('sendSys')"/>
            </el-input>
          </el-form-item>
          <el-form-item :label="$t('label.rcvJobCode')">
            <el-input v-model="sparam.rcvJobCodeId" class="search-id" readonly>
              <i slot="suffix" class="el-input__icon el-icon-close pointer" @click.stop="handleClear('sendSys')"/>
            </el-input>
          </el-form-item>
          <el-form-item :label="$t('label.lastChangeDate')">
            <el-date-picker v-model="sparam.chgDate" type="daterange" range-separator="~" start-placeholder="Start Date" end-placeholder="End Date" style="width: 220px;"/>
          </el-form-item>
          <el-form-item label="등록자" label-width="65px" class="search-check">
            <el-checkbox v-model="sparam.cRegId"/>
          </el-form-item>
          <el-form-item label="송신자" label-width="65px" class="search-check">
            <el-checkbox v-model="sparam.cSendAdminId"/>
          </el-form-item>
          <el-form-item label="수신자" label-width="65px" class="search-check">
            <el-checkbox v-model="sparam.cRcvAdminId"/>
          </el-form-item>
          <div class="search-buttons">
            <el-button @click="search">{{$t('label.search')}}</el-button>
          </div>
        </div>
      </el-form>
    </div>

    <el-table ref="table" :data="items" :height="listHeight" border class="table-striped">
      <el-table-column type="selection" width="40"/>
      <el-table-column :label="$t('label.id')" prop="id" width="150"/>
      <el-table-column :label="$t('label.changeStatus')" width="115">
        <template slot-scope="scope">
          <span :style="chgStatStyle(scope.row.chgStat)">{{getTypeStr('chgStat', scope.row.chgStat)}}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('label.processStatus')" width="130">
        <template slot-scope="scope">
          <span>{{getTypeStr('procStat', scope.row.processStat)}}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('label.interfaceType')" width="80">
        <template slot-scope="scope">
          <span>{{getTypeStr('infType', scope.row.interfaceType)}}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('label.name')" prop="name" :show-overflow-tooltip="true"/>
      <el-table-column :label="$t('label.interfaceId')" prop="interfaceId" width="135"/>
      <el-table-column :label="$t('label.sourceMethod')" width="85">
        <template slot-scope="scope">
          <span>{{getTypeStr('sysType', scope.row.sendSystemType)}}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('label.targetMethod')" width="85">
        <template slot-scope="scope">
          <span>{{getTypeStr('sysType', scope.row.rcvSystemType)}}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('label.sourceSystem')" prop="sendSystem.name" width="110" :show-overflow-tooltip="true"/>
      <el-table-column :label="$t('label.targetSystem')" prop="rcvSystem.name" width="110" :show-overflow-tooltip="true"/>
      <el-table-column :label="$t('label.lastChangeDate')" prop="chgDate" width="150"/>
      <el-table-column width="120" class-name="edit-cell operations">

        <template slot-scope="scope">
          <div>
            <el-button icon="el-icon-edit" @click.stop="clickEdit(scope.row.id)"/>
            <el-button icon="el-icon-delete" type="danger" @click.stop="clickDelete(scope.row.id)" plain/>
            <el-button icon="el-icon-refresh-left" @click.stop="viewHistory(scope.row.id)"/>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination background layout="sizes, prev, pager, next, ->, total"
      :total="paging.totalCount" :current-page="paging.curPage"
      :page-sizes="pageSizes" :page-size="sparam.size"
      @size-change="handlePageSizeChange" @current-change="handlePageCurrentChange">
    </el-pagination>

    <el-dialog :visible.sync="systemListShow" width="1300px" top="5vh" append-to-body :close-on-click-modal="false">
      <SystemList ref="sysList" :items="systems" :onlySearch="true" @search="searchSystemList" @row-dblclick="cbSystemRowClick"/>
    </el-dialog>
  </div>
</template>

<script>
import RuleList from './RuleList'

export default {
  mixins: [RuleList],
  props: {
    infTypes: {
      type: Array,
      default: function () {
        return []
      },
    },
    chgStats: {
      type: Array,
      default: function () {
        return []
      },
    },
    procStats: {
      type: Array,
      default: function () {
        return []
      },
    },
  },
  data () {
    return {
      path: 'request',
      listHeight: 'calc(100vh - 310px)',
      sparam: {
        ...this.sparam,
        page: 1,
        size: 30,
        interfaceType: '',
        chgStat: '',
        processStat: '',
        chgDate: [],
      },
      paging: {
        curPage: 1,
        totalCount: 1,
      },
      pageSizes: [15, 30, 50, 100],
      systemListShow: false,
      systems: null,
      sysType: '',
    }
  },
  methods: {
    handlePageSizeChange (val) {
      this.sparam.size = val
      this.search()
    }, // handlePageSizeChange

    handlePageCurrentChange (val) {
      this.sparam.page = val
      this.search()
    }, // handlePageCurrentChange

    clickTransfer () {
      console.log('click transfer')
    }, // clickTransfer

    viewHistory (id) {
      console.log('view history', id)
    }, // viewHistory

    handleClear (type) {
      switch (type) {
        case 'sendSys':
          this.sparam.sendSystemId = ''
          break
        case 'sendBiz':
          this.sparam.sendJobCodeId = ''
          break
        case 'rcvSys':
          this.sparam.rcvSystemId = ''
          break
        case 'rcvBiz':
          this.sparam.rcvJobCodeId = ''
          break
      }
    }, // handleClear

    showSystemList (type) {
      this.sysType = type
      if (!this.systems) {
        this.searchSystemList(() => {
          this.systemListShow = true
        })
      } else {
        this.systemListShow = true
      }
    }, // showSystemList

    searchSystemList (cbFunc) {
      let sparam = this.$refs.sysList ? this.$refs.sysList.sparam : {}
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
      if (this.sysType === 'send') {
        this.sparam.sendSystemId = row.id
      } else if (this.sysType === 'rcv') {
        this.sparam.rcvSystemId = row.id
      }
      this.systemListShow = false
    }, // cbSystemRowClick

    chgStatStyle (val) {
      let style = 'color: #fff'
      if (val === '1') {
        style = 'color: #0000FF'
      } else if (val === '2') {
        style = 'color: #9900FF'
      } else if (val === '3') {
        style = 'color: #FF0000'
      } else {
        style = 'color: #990000'
      }
      return style
    }, // chgStatStyle
  },
}
</script>

<style scoped>
.advanced-search-bar {
  border: 1px solid #aab3b3;
  margin-top: 10px;
  background-color: #e6f7ff;
}
.advanced-search-bar div.row:last-child .el-form-item--small.el-form-item {
  margin-bottom: 10px;
}
.advanced-search-bar .search-name {
  width: 180px;
}
.advanced-search-bar .search-id {
  width: 140px;
}
.advanced-search-bar .search-check {
  margin-left: 15px;
}
</style>