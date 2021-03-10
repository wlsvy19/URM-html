<template>
  <div class="urm-panel">
    <div class="urm-header">디퍼드 거래처리로그</div>
    <div class="search-bar" :style="searchBarStyle">
      <el-form :inline="true">
        <el-form-item label="처리날짜">
          <el-date-picker v-model="sparam.processDate" value-format="yyyyMMdd" class="no-suffix" :clearable="false"/>
          <el-time-picker is-range v-model="timeRange" format="HH:mm" value-format="HHmm" class="no-suffix" :clearable="false"/>
        </el-form-item>
        <el-form-item label="인터페이스 아이디">
          <el-input v-model="sparam.interfaceId" class="search-id" @change="search"/>
        </el-form-item>
      </el-form>
      <div class="search-buttons">
        <el-button @click="search">{{$t('label.search')}}</el-button>
      </div>
    </div>

    <LogDeferredList :items="listItem"/>
  </div>
</template>

<script>
import LogMain from './LogMain'

import LogDeferredList from './list/LogDeferredList'

export default {
  mixins: [LogMain],
  components: {
    LogDeferredList,
  },
  data () {
    return {
      sparam: {
        ...this.sparam,
        processDate: '',
        startTime: '0000',
        endTime: '2359',
      },
      path: 'deferred',
    }
  },
  mounted () {
    this.sparam.processDate = this.$convertDateFormat('yyyyMMdd')
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