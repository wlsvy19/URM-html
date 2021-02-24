<template>
  <div class="urm-pannel">
    <p>배치 거래처리로그</p>
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item label="처리날짜">
          <el-date-picker type="daterange" start-placeholder="Start Date" end-placeholder="End Date" style="width: 220px;"/>
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
    <LogBatchList ref="list" @edit="handleEdit"/>
  </div>
</template>

<script>
import LogBatchList from './list/LogBatchList'

export default {
  components: {
    LogBatchList,
  },
  data () {
    return {
      radio: 'T'
    }
  }
}
</script>