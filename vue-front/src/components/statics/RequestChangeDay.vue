<template>
  <div class="urm-panel">
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item label="일자 선택">
          <el-date-picker v-model="dateRange" type="daterange" value-format="yyyyMMdd" class="no-suffix" :clearable="false"/>
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
      pageUrl: '/change/day',
    }
  },
  mounted () {
    let today = this.$convertDateFormat('yyyyMMdd', new Date())
    this.sparam.startDate = today
    this.sparam.endDate = today
  },
}
</script>