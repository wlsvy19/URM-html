<template>
  <div class="urm-pannel">
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item label="처리날짜">
          <el-date-picker type="daterange" start-placeholder="Start Date" end-placeholder="End Date" style="width: 220px;"/>
        </el-form-item>
        <el-form-item label="인터페이스 아이디">
          <el-input class="search-id"/>
        </el-form-item>
        <el-form-item label="총건수" >
          <el-checkbox v-model="checked"/>
        </el-form-item>
        <el-form-item label="성공건수" >
          <el-checkbox v-model="checked"/>
        </el-form-item>
        <el-form-item label="실패건수" >
          <el-checkbox v-model="checked"/>
        </el-form-item>
        <el-form-item label="평균처리시간" >
          <el-checkbox v-model="checked"/>
        </el-form-item>
      </el-form>
      <div class="search-buttons">
        <el-button @click="search">{{$t('label.search')}}</el-button>
      </div>
    </div>
    <StaticsHourList ref="list"  @edit="handleEdit"/>
  </div>
</template>

<script>
import StaticsHourList from './list/StaticsHourList'

export default {
  components: {
    StaticsHourList,
  },
  data() {
      return {
        checked: true
      }
    }
}
</script>