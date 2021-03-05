<template>
  <div class="urm-panel">
    <div class="urm-header">배치 거래처리로그</div>
    <div class="search-bar" :style="searchBarStyle()">
      <el-form :inline="true">
        <el-form-item label="처리날짜">
          <el-date-picker v-model="dateRange" type="daterange" value-format="yyyyMMdd" :clearable="false"/>
        </el-form-item>
        <el-form-item label="인터페이스 아이디">
          <el-input class="search-id"/>
        </el-form-item>
        <el-form-item label="배치 아이디">
          <el-input class="search-id"/>
        </el-form-item>
        <el-form-item :label="$t('label.trType')">
          <el-radio-group v-model="radio">
            <el-radio label="T">전체</el-radio>
            <el-radio label="E">실패</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div class="search-buttons">
        <el-button @click="search">{{$t('label.search')}}</el-button>
      </div>
    </div>
    <LogBatchList :items="listItem"/>
  </div>
</template>

<script>
import ProcessMain from '../ProcessMain'

export default {
  mixins: [ProcessMain],
  data () {
    return {
      pageUrl: '/log/batch',
      radio: 'T'
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