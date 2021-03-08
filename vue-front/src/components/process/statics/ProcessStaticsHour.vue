<template>
  <div class="urm-panel">
    <div class="urm-header">시간별 {{pageTitle}} 거래처리통계</div>
    <div class="search-bar" :style="searchBarStyle()">
      <el-form :inline="true">
        <el-form-item label="처리날짜">
          <el-date-picker v-model="startDate" value-format="yyyyMMdd" :clearable="false"/>
          <el-select v-model="startTime" style="width: 70px;">
            <el-option v-for="(n, i) in 24" :value="getHourStr(i)" :label="getHourStr(i) + '시'" :key="i"/>
          </el-select><!--
        -->~<!--
        --><el-date-picker v-model="endDate" value-format="yyyyMMdd" :clearable="false"/>
          <el-select v-model="endTime" style="width: 70px;">
            <el-option v-for="(n, i) in 24" :value="getHourStr(i)" :label="getHourStr(i) + '시'" :key="i"/>
          </el-select>
        </el-form-item>
        <el-form-item label="인터페이스 아이디">
          <el-input v-model="sparam.interfaceId" class="search-id" @change="search"/>
        </el-form-item>
        <el-form-item label="총건수" >
          <el-checkbox v-model="filter.total" @change="handleChangeFilter('t')"/>
        </el-form-item>
        <el-form-item v-if="isDevOnline" label="시뮬레이션건수" >
          <el-checkbox v-model="filter.sim" @change="handleChangeFilter('sim')"/>
        </el-form-item>
        <el-form-item label="성공건수" >
          <el-checkbox v-model="filter.success" @change="handleChangeFilter('s')"/>
        </el-form-item>
        <el-form-item label="실패건수" >
          <el-checkbox v-model="filter.fail" @change="handleChangeFilter('f')"/>
        </el-form-item>
        <el-form-item label="평균처리시간" >
          <el-checkbox v-model="filter.avg" @change="handleChangeFilter('a')"/>
        </el-form-item>
      </el-form>
      <div class="search-buttons">
        <el-button @click="search">{{$t('label.search')}}</el-button>
      </div>
    </div>
    <StaticsHourList ref="list" :key="$route.path"/>
  </div>
</template>

<script>
import ProcessStaticsMain from './ProcessStaticsMain'

export default {
  mixins: [ProcessStaticsMain],
  data() {
    return {
      filter: {
        total: true,
        success: true,
        fail: true,
        avg: true,
        sim: true,
      },
      path: 'hour',
    }
  },
  methods: {
    handleChangeFilter (type) {
      console.log(type)
    }, // handleChangeFilter
    getHourStr (val) {
      return val < 10 ? '0' + val : val
    }, // getHourStr

  },
  mounted () {
    let today = this.$convertDateFormat('yyyyMMdd', new Date())
    this.sparam.startDate = today + '00'
    this.sparam.endDate = today + '23'
  },
  computed: {
    startDate: {
      get: function () {
        return this.sparam.startDate.substring(0,8)
      },
      set: function (nVal) {
        this.sparam.startDate = nVal + this.startTime
      },
    },
    startTime: {
      get: function () {
        return this.sparam.startDate.substring(8,10)
      },
      set: function (nVal) {
        this.sparam.startDate = this.startDate + nVal
      },
    },
    endDate: {
      get: function () {
        return this.sparam.endDate.substring(0,8)
      },
      set: function (nVal) {
        this.sparam.endDate = nVal + this.endTime
      },
    },
    endTime: {
      get: function () {
        return this.sparam.endDate.substring(8,10)
      },
      set: function (nVal) {
        this.sparam.endDate = this.endDate + nVal
      },
    },
    isDevOnline: function () {
      let type = this.$route.params.server
      let infType = this.$route.params.type
      if (type === 'dev' && infType == 'realtime') {
        return true
      } else 
        return false;
    },
  },
}
</script>
<style scoped>
.search-bar .el-date-editor--date {
  width: 140px;
}
</style>