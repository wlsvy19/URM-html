<template>
  <div class="urm-pannel">
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item :label="$t('label.interfaceType')">
          <el-select v-model="sparam.type" class="search-id">
            <el-option value="" label="ALL"/>
            <el-option v-for="type in infTypes" :value="type.code" :label="type.name" :key="type.code"/>
          </el-select>
        </el-form-item>
        <el-form-item label="일자 선택">
          <el-date-picker v-model="dateRange" type="monthrange" value-format="yyyyMM" range-separator="~" style="width: 220px"/>
        </el-form-item>
      </el-form>
      <div class="search-buttons">
        <el-button @click="search">{{$t('label.search')}}</el-button>
      </div>
    </div>
    <RequestProcessList :items="listItem"/>
  </div>
</template>

<script>
import StaticsMain from './StaticsMain'
import RequestProcessList from './list/RequestProcessList'

import RuleUtil from '@/components/rule/RuleUtil'

export default {
  mixins: [StaticsMain],
  components: {
    RequestProcessList,
  },
  data () {
    return {
      sparam: {
        ...this.sparam,
        type: '',
      },
      pageUrl: '/api/stat/process/month',
    }
  },
  computed: {
    infTypes: function () {
      let kind = RuleUtil.CODEKEY.infType
      return this.$store.state.codes.filter(code => (code.kind === kind))
    },
  },
}
</script>