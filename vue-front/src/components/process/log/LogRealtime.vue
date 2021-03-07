<template>
  <div class="urm-panel">
    <div class="urm-header">온라인 거래처리로그</div>
    <div class="search-bar" :style="searchBarStyle()">
      <el-form :inline="true">
        <el-form-item label="처리날짜">
          <el-date-picker v-model="sparam.processDate" value-format="yyyyMMdd" class="datepicker" :clearable="false"/>
          <el-time-picker is-range v-model="timeRange" value-format="HHmmss" class="no-suffix" :clearable="false"/>
        </el-form-item>
        <el-form-item label="인터페이스 아이디">
          <el-input v-model="sparam.interfaceId" class="search-id" @change="search"/>
        </el-form-item>
        <el-form-item label="GlobalID">
          <el-input v-model="sparam.globalId" class="search-id" @change="search"/>
        </el-form-item>
        <el-form-item label="에러만">
          <el-checkbox v-model="sparam.error" @change="search"/>
        </el-form-item>
      </el-form>
      <div class="search-buttons">
        <el-button @click="search">{{$t('label.search')}}</el-button>
      </div>
    </div>
    <LogRealtimeList ref="list" :key="$route.path" @row-dblclick="getDetails"/>
  </div>
</template>

<script>
import LogMain from './LogMain'

export default {
  mixins: [LogMain],
  data () {
    return {
      sparam: {
        ...this.sparam,
        processDate: '',
        startTime: '',
        endTime: '',
        error: false,
      },
      pageUrl: '/log/realtime',
    }
  },
  mounted () {
    let now = new Date()
    let today = this.$convertDateFormat('yyyyMMdd', now)
    let hour = this.$convertDateFormat('HH', now)
    this.sparam.processDate = today
    this.sparam.startTime = hour+'0000'
    this.sparam.endTime = hour+'5959'
  },
  computed: {
    timeRange: {
      get: function () {
        return [this.sparam.startTime, this.sparam.endTime]
      },
      set: function (nVal) {
        this.sparam.startTime = nVal[0]
        this.sparam.endTime = nVal[1]
      },
    },
  },
}
</script>
<style scoped>
.search-bar .el-date-editor--date {
  width: 140px;
}
.search-bar .el-date-editor--timerange {
  width: 185px;
}
</style>