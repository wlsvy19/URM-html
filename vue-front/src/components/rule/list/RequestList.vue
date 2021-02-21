<template>
  <div class="urm-list">
    <div class="advanced-search-bar">
      <el-form label-width="135px">
        <div class="row">
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
              <el-option v-for="stat in chgStats" :value="stat.code" :label="stat.name" :key="stat.code"/>
            </el-select>
          </el-form-item>
        </div>
        <div class="row">
          <el-form-item :label="$t('label.targetSystem')">
            <el-input v-model="sparam.rcvSystemId" class="search-id"/>
          </el-form-item>
          <el-form-item :label="$t('label.rcvJobCode')">
            <el-input v-model="sparam.rcvJobCodeId" class="search-id"/>
          </el-form-item>
          <el-form-item :label="$t('label.lastChangeDate')">
            <el-date-picker v-model="chgDate" type="daterange" align="right" start-placeholder="Start Date" end-placeholder="End Date" default-value="2010-10-01"/>
          </el-form-item>
          <el-form-item label="등록자">
            <el-checkbox/>
          </el-form-item>
          <el-form-item label="송신자">
            <el-checkbox/>
          </el-form-item>
          <el-form-item label="수신자">
            <el-checkbox/>
          </el-form-item>
        </div>
      </el-form>
    </div>

    <el-table :data="items" border class="table-striped">
      <el-table-column :label="$t('label.id')" prop="id" width="145"/>
      <el-table-column :label="$t('label.changeStatus')" width="115">
        <template slot-scope="scope">
          <span>{{getTypeStr('chgStat', scope.row.chgStat)}}</span>
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
      <el-table-column :label="$t('label.name')" prop="name"/>
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
      <el-table-column :label="$t('label.sourceSystem')" prop="sendSystem.name" width="110"/>
      <el-table-column :label="$t('label.targetSystem')" prop="rcvSystem.name" width="110"/>
      <el-table-column :label="$t('label.lastChangeDate')" prop="chgDate" width="150"/>
      <el-table-column>
        <template slot-scope="scope">
          <div>
            <el-button @click.stop="clickEdit(scope.row.id)"/>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination background layout="sizes, prev, pager, next, ->, total"
      :total="paging.totalCount" :current-page="paging.curPage"
      :page-sizes="pageSizes" :page-size="sparam.size"
      @size-change="handlePageSizeChange" @current-change="handlePageCurrentChange">
    </el-pagination>
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
      sparam: {
        ...this.sparam,
        page: 1,
        size: 30,
        type: '',
      },
      paging: {
        curPage: 1,
        totalCount: 1,
      },
      pageSizes: ['15', '30', '50', '100'],
      chgDate: [],
    }
  },
  methods: {
    search () {
      console.log('search', this.sparam)
    }, // Override search

    handlePageSizeChange () {

    }, // handlePageSizeChange

    handlePageCurrentChange () {
      
    }, // handlePageCurrentChange
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