<template>
  <div class="urm-panel">
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item label="μΌμ μ ν">
          <el-date-picker v-model="dateRange" type="monthrange" value-format="yyyyMM" class="no-suffix" :clearable="false"/>
        </el-form-item>
      </el-form>
      <div class="search-buttons">
        <el-button @click="search">{{$t('label.search')}}</el-button>
      </div>
    </div>

    <RequestChangeList ref="list" :items="listItem"/>
  </div>
</template>

<script>
import StaticsMain from './StaticsMain'

export default {
  mixins: [StaticsMain],
  data () {
    return {
      pageUrl: '/change/month',
    }
  },
  mounted () {
    let today = this.$convertDateFormat('yyyyMM')
    this.sparam.startDate = today
    this.sparam.endDate = today
  },
}
</script>