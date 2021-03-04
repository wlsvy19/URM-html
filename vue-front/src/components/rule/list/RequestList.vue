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
            <el-input v-model="sparam.sendJobCodeId" class="search-id" @click.native.prevent="showBizList('send')" readonly>
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
              <i slot="suffix" class="el-input__icon el-icon-close pointer" @click.stop="handleClear('rcvSys')"/>
            </el-input>
          </el-form-item>
          <el-form-item :label="$t('label.rcvJobCode')">
            <el-input v-model="sparam.rcvJobCodeId" class="search-id" @click.native.prevent="showBizList('rcv')" readonly>
              <i slot="suffix" class="el-input__icon el-icon-close pointer" @click.stop="handleClear('rcvBiz')"/>
            </el-input>
          </el-form-item>
          <el-form-item :label="$t('label.lastChangeDate')">
            <el-date-picker v-model="sparam.chgDate" type="daterange" range-separator="~"
              start-placeholder="Start Date" end-placeholder="End Date" style="width: 220px;"/>
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
      <el-table-column :label="$t('label.processStatus')" min-width="85">
        <template slot-scope="scope">
          <span>{{getTypeStr('procStat', scope.row.processStat)}}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('label.interfaceType')" min-width="70" :show-overflow-tooltip="true">
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
      <el-table-column :label="$t('label.lastChangeDate')" width="145">
        <template slot-scope="scope">
          <span>{{getDateStr(scope.row.chgDate, 'yyyy-MM-dd HH:mm')}}</span>
        </template>
      </el-table-column>
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
      :total="totalCount" :current-page="curPage"
      :page-sizes="pageSizes" :page-size="sparam.size"
      @size-change="handlePageSizeChange" @current-change="handlePageCurrentChange">
    </el-pagination>

    <el-dialog :visible.sync="transferShow" width="1200px" top="8vh"
      append-to-body :close-on-click-modal="false" :destroy-on-close="true">
      <RequestTransfer ref="transfer" @confirm="handleConfirm" @show-usr-list="showUserList"/>
    </el-dialog>

    <el-dialog :visible.sync="historyShow" width="1300px" top="8vh"
      append-to-body :close-on-click-modal="false" :destroy-on-close="true">
      <RequestHistory :data="history"/>
    </el-dialog>
  </div>
</template>

<script>
import RuleList from './RuleList'

import RequestHistory from './request/RequestHistory'
import RequestTransfer from './request/RequestTransfer'

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
  components: {
    RequestHistory,
    RequestTransfer,
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
        sendSystemId: '',
        rcvSystemId: '',
        sendJobCodeId: '',
        rcvJobCodeId: '',
      },
      curPage: 1,
      totalCount: 1,
      pageSizes: [15, 30, 50, 100],
      tgtType: '',
      transferShow: false,
      historyShow: false,
      history: null,
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
      this.transferShow = true
    }, // clickTransfer

    handleConfirm (list) {
      const loading = this.$startLoading()
      this.$http({
        method : 'POST',
        url: '/api/request/transfer',
        data: list,
      }).then(response => {
        let res = response.data
        this.$message.success(res + ' 의 요건이 성공적으로 이관되었습니다.')
        this.$refs.transfer.getRequests()
        this.search()
      }).catch(error => {
        this.$handleHttpError(error)
      }).finally(() => {
        loading.close()
      })
    }, // handleConfirm

    viewHistory (id) {
      const loading = this.$startLoading()
      this.$http.get('/api/request/history', {
        params: {id: id},
      }).then(response => {
        this.history = response.data
        this.historyShow = true
      }).catch(error => {
        this.$handleHttpError(error)
      }).finally(() => {
        loading.close()
      })
    }, // viewHistory

    handleClear (type) {
      if (type === 'sendSys') {
        this.sparam.sendSystemId = ''
      } else if (type === 'sendBiz') {
        this.sparam.sendJobCodeId = ''
      } else if (type === 'rcvSys') {
        this.sparam.rcvSystemId = ''
      } else if (type === 'rcvBiz') {
        this.sparam.rcvJobCodeId = ''
      }
    }, // handleClear

    showSystemList (type) {
      this.tgtType = type
      this.$emit('show-sys-list', this.cbSystemRowClick)
    }, // showSystemList

    cbSystemRowClick (row) {
      if (this.tgtType === 'send') {
        this.sparam.sendSystemId = row.id
      } else if (this.tgtType === 'rcv') {
        this.sparam.rcvSystemId = row.id
      }
    }, // cbSystemRowClick

    showBizList (type) {
      this.tgtType = type
      this.$emit('show-biz-list', this.cbBizRowClick)
    }, // showBizList

    cbBizRowClick (row) {
      if (this.tgtType === 'send') {
        this.sparam.sendJobCodeId = row.id
      } else if (this.tgtType === 'rcv') {
        this.sparam.rcvJobCodeId = row.id
      }
    }, // cbBizRowClick

    showUserList (param) {
      if (typeof param === 'function') {
        this.$emit('show-usr-list', param)
      }
    }, // showUserList

    chgStatStyle (val) {
      if (val === '1') {
        return 'color: #0000FF'
      } else if (val === '2') {
        return 'color: #9900FF'
      } else if (val === '3') {
        return 'color: #FF0000'
      } else {
        return 'color: #990000'
      }
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