<template>
  <div class="urm-pannel">
    <p>일자별 {{getInfType()}} 거래처리통계</p>
    <div class="search-bar" :style="procStatStyle(this.$route.params)">
      <el-form :inline="true">
        <el-form-item label="처리날짜">
          <el-date-picker type="daterange" start-placeholder="Start Date" end-placeholder="End Date" style="width: 220px;"/>
        </el-form-item>
        <el-form-item label="인터페이스 아이디">
          <el-input class="search-id"/>
        </el-form-item>
      </el-form>
      <div class="search-buttons">
        <el-button @click="search">{{$t('label.search')}}</el-button>
      </div>
    </div>
    <StaticsDayList ref="list" @edit="handleEdit"/>
  </div>
</template>

<script>
import StaticsDayList from './list/StaticsDayList'

export default {
  components: {
    StaticsDayList,
  },
  methods: {
    procStatStyle (val) {
      let type = val.server
      let style= ''
      if(type === 'dev') {
        style = 'backgroundColor: #8888ff'
      }else if(val.server === 'test') {
        style = 'backgroundColor: #88ff88'
      } else if(val.server === 'prod') {
        style = 'backgroundColor: #ff8888'
      }
      return style
    }, // proLogStyle
    getInfType () {
        let infType = ''
        let type = this.$route.params.type
        if(type === 'online') {
          infType = '온라인'
        } else if(type === 'batch') {
            infType = '배치'
        } else if(type === 'deferred') {
            infType = '디퍼드'
        }
        return infType
    }, // getInfType
  }
}
</script>