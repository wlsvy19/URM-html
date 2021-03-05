<template>
  <div class="urm-panel">
    <div class="urm-header">일자별 {{pageTitle}} 거래처리통계</div>
    <div class="search-bar" :style="searchBarStyle()">
      <el-form :inline="true">
        <el-form-item label="처리날짜">
          <el-date-picker v-model="dateRange" type="daterange" value-format="yyyyMMdd" :clearable="false"/>
        </el-form-item>
        <el-form-item label="인터페이스 아이디">
          <el-input class="search-id"/>
        </el-form-item>
      </el-form>
      <div class="search-buttons">
        <el-button @click="search">{{$t('label.search')}}</el-button>
      </div>
    </div>

    <StaticsDayList :items="listItem"/>
  </div>
</template>

<script>
import ProcessMain from '../ProcessMain'

export default {
  mixins: [ProcessMain],
  data () {
    return {
      sparam: {
        ...this.sparam,
        infType: '',
      },
      pageUrl: '/stat/day',
    }
  },
  mounted () {
    let today = this.$convertDateFormat('yyyyMMdd', new Date())
    this.sparam.startDate = today
    this.sparam.endDate = today
    this.sparam.infType = this.$route.params.type
  },
}
</script>