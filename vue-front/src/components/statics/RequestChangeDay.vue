<template>
  <div class="urm-pannel">
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item label="일자 선택">
          <el-date-picker v-model="dateRange" type="daterange" value-format="yyyyMMdd" range-separator="~" style="width: 220px;"/>
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
import RequestChangeList from './list/RequestChangeList'

export default {
  mixins: [StaticsMain],
  components: {
    RequestChangeList,
  },
  data () {
    return {
      pageUrl: '/api/stat/change/day',
    }
  },
}
</script>