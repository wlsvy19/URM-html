<template>
  <div class="urm-list">
    <div class="advanced-search-bar">
      <el-form label-width="135px">
        <div class="row" style="margin-top: 10px;">
          <el-form-item :label="$t('label.interfaceId')">
            <el-input v-model="sparam.interfaceId" class="search-id"/>
          </el-form-item>
          <el-form-item :label="$t('label.interfaceType')">
            <el-select v-model="sparam.interfaceType" class="search-id">
              <el-option value="" label="ALL"/>
              <el-option v-for="type in infTypes" :value="type.code" :label="type.name" :key="type.code"/>
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('label.requestId')">
            <el-input v-model="sparam.id" class="search-id"/>
          </el-form-item>
          <el-form-item :label="$t('label.requestName')">
            <el-input v-model="sparam.name" class="search-name"/>
          </el-form-item>
          <div class="search-buttons">
            <el-button @click.stop="clickTransfer">이관</el-button>
          </div>
        </div>
        <div class="row">
          <el-form-item :label="$t('label.sourceSystem')">
            <el-input v-model="sparam.sendSystemId" class="search-id"/>
          </el-form-item>
          <el-form-item :label="$t('label.sndJobCode')">
            <el-input v-model="sparam.sendJobCodeId" class="search-id"/>
          </el-form-item>
          <el-form-item :label="$t('label.changeStatus')">
            <el-select v-model="sparam.chgStat" class="search-id">
              <el-option value="" label="ALL"/>
              <el-option v-for="stat in chgStats" :value="stat.code" :label="stat.name" :key="stat.code"/>
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('label.processStatus')">
            <el-select v-model="sparam.processStat" style="width: 150px">
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
            <el-input v-model="sparam.rcvSystemId" class="search-id"/>
          </el-form-item>
          <el-form-item :label="$t('label.rcvJobCode')">
            <el-input v-model="sparam.rcvJobCodeId" class="search-id"/>
          </el-form-item>
          <el-form-item :label="$t('label.lastChangeDate')">
            <el-date-picker v-model="sparam.chgDate" type="daterange"
              start-placeholder="Start Date" end-placeholder="End Date" style="width: 220px;"/>
          </el-form-item>
          <div class="row">
            <el-form-item label="등록자" label-width="65px">
              <el-checkbox v-model="sparam.cRegId"/>
            </el-form-item>
            <el-form-item label="송신자" label-width="65px">
              <el-checkbox v-model="sparam.cSendAdminId"/>
            </el-form-item>
            <el-form-item label="수신자" label-width="65px">
              <el-checkbox v-model="sparam.cRcvAdminId"/>
            </el-form-item>
          </div>
          <div class="search-buttons">
            <el-button @click.stop="search">{{$t('label.search')}}</el-button>
          </div>
        </div>
      </el-form>
    </div>

    <el-table :data="items" :height="listHeight" border class="table-striped">
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
      <el-table-column width="125" class-name="edit-cell operations">
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

    <el-dialog title="System List" :visible.sync="systemListShow" width="95%" top="8vh" append-to-body :close-on-click-modal="false">
      <SystemList :onlySearch="true" @row-dblclick="cbSystemRowClick"/>
    </el-dialog>
  </div>
</template>

<script>
import RuleList from './RuleList'
import RuleUtil from '@/components/rule/RuleUtil'

export default {
  mixins: [RuleList],
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
    }
  },
  methods: {
    search () {
      const loading = this.$startLoading()
      console.log('search : ' + this.path, this.sparam)
      this.$http.get('/api/' + this.path, {
        params: this.sparam,
      }).then(response => {
        let pageList = response.data
        this.items = pageList.list
        this.paging = {
          curPage: pageList.curPage,
          totalCount: pageList.totalCount,
        }
      }).catch(error => {
        this.$handleHttpError(error)
      }).finally(() => {
        loading.close()
      })
    }, // Override search

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

    cbSystemRowClick (row) {
      console.log(row)

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

<style scoped>
.advanced-search-bar {
  border: 1px solid #aab3b3;
  margin-top: 10px;
  background-color: #e6f7ff;
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