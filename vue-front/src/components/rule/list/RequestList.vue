<template>
  <div class="urm-list">
    <div class="advanced-search-bar">
      <el-form label-width="135px">
        <div class="row">
          <el-form-item :label="$t('label.interfaceId')">
            <el-input v-model="sparam.id" class="search-id"/>
          </el-form-item>
          <el-form-item :label="$t('label.interfaceType')">
            <el-select v-model="sparam.type">
              <el-option value="" label="All" class="search-id"/>
              <!--interfaceType options-->
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('label.requestId')">
            <el-input v-model="sparam.id" class="search-id"/>
          </el-form-item>
          <el-form-item :label="$t('label.requestName')">
            <el-input v-model="sparam.id" class="search-id"/>
          </el-form-item>
        </div>
        <div class="row">
          <el-form-item :label="$t('label.sourceSystem')">
            <el-input v-model="sparam.id" class="search-id"/>
          </el-form-item>
          <el-form-item :label="$t('label.sndJobCode')">
            <el-input v-model="sparam.id" class="search-id"/>
          </el-form-item>
          <el-form-item :label="$t('label.changeStatus')">
            <el-select v-model="sparam.type">
              <el-option value="" label="All" class="search-id"/>
              <!--changeStatus options-->
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('label.processStatus')">
            <el-select v-model="sparam.type">
              <el-option value="" label="All" class="search-id"/>
              <!--processStatus options-->
            </el-select>
          </el-form-item>
        </div>
        <div class="row">
          <el-form-item :label="$t('label.targetSystem')">
            <el-input v-model="sparam.id" class="search-id"/>
          </el-form-item>
          <el-form-item :label="$t('label.rcvJobCode')">
            <el-input v-model="sparam.id" class="search-id"/>
          </el-form-item>
          <el-form-item :label="$t('label.lastChangeDate')">
            <el-date-picker v-model="value2" type="daterange" align="right" start-placeholder="Start Date" end-placeholder="End Date" default-value="2010-10-01"/>
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
      <el-table-column :label="$t('label.id')" prop="id" width="150"/>
      <el-table-column :label="$t('label.changeStatus')" prop="chgStat" width="125"/>
      <el-table-column :label="$t('label.processStatus')" prop="processStat" width="130"/>
      <el-table-column :label="$t('label.interfaceType')" prop="interfaceType" width="80"/>
      <el-table-column :label="$t('label.name')" prop="name" width="150"/>
      <el-table-column :label="$t('label.interfaceId')" prop="interfaceId" width="150"/>
      <el-table-column :label="$t('label.sourceMethod')" prop="sendSystemType" width="150"/>
      <el-table-column :label="$t('label.targetMethod')" prop="rcvSystemType" width="150"/>
      <el-table-column :label="$t('label.sourceSystem')" prop="sendSystem.name" width="150"/>
      <el-table-column :label="$t('label.targetSystem')" prop="rcvSystem.name" width="150"/>
      <el-table-column :label="$t('label.lastChangeDate')" prop="chgDate" width="150"/>
    </el-table>
    <el-pagination :page-size="20" :pager-count="11" layout="prev, pager, next" :total="100"/>
  </div>
</template>

<script>
import RuleList from './RuleList'

export default {
  mixins: [RuleList],
  data () {
    return {
      path: 'request',
      sparam: {
        ...this.sparam,
        type: '',
      },
    }
  },
  methods: {
    clickUsed (id) {
      console.log('used', id)
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