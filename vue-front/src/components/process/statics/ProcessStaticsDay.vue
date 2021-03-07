<template>
  <div class="urm-panel">
    <div class="urm-header">일자별 {{pageTitle}} 거래처리통계</div>
    <div class="search-bar" :style="searchBarStyle()">
      <el-form :inline="true">
        <el-form-item label="처리날짜">
          <el-date-picker v-model="dateRange" type="daterange" value-format="yyyyMMdd" class="no-suffix" :clearable="false"/>
        </el-form-item>
        <el-form-item label="인터페이스 아이디">
          <el-input v-model="sparam.interfaceId" class="search-id" @change="search"/>
        </el-form-item>
      </el-form>
      <div class="search-buttons">
        <el-button @click="search">{{$t('label.search')}}</el-button>
      </div>
    </div>

    <StaticsDayList ref="list" :key="$route.path"/>
  </div>
</template>

<script>
import ProcessStaticsMain from './ProcessStaticsMain'

export default {
  mixins: [ProcessStaticsMain],
  data () {
    return {
      path: 'day',
    }
  },
  mounted () {
    let today = this.$convertDateFormat('yyyyMMdd', new Date())
    this.sparam.startDate = today
    this.sparam.endDate = today
  },
  computed: {
    dateRange: {
      get: function () {
        return [this.sparam.startDate, this.sparam.endDate]
      },
      set: function (nVal) {
        this.sparam.startDate = nVal[0]
        this.sparam.endDate = nVal[1]
      },
    },
  },
}
</script>