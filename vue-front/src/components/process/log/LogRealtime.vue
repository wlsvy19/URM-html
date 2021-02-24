<template>
  <div class="urm-pannel">
    <p>온라인 거래처리로그</p>
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item label="처리날짜">
          <el-date-picker type="daterange" start-placeholder="Start Date" end-placeholder="End Date" style="width: 220px;"/>
        </el-form-item>
        <el-form-item label="인터페이스 아이디">
          <el-input class="search-id"/>
        </el-form-item>
        <el-form-item label="GlobalID">
          <el-input class="search-id"/>
        </el-form-item>
        <el-form-item label="에러만">
          <el-checkbox/>
        </el-form-item>
      </el-form>
      <div class="search-buttons">
        <el-button @click="search">{{$t('label.search')}}</el-button>
      </div>
    </div>
    <LogRealtimeList ref="list" @edit="handleEdit"/>
  </div>
</template>

<script>
import LogRealtimeList from './list/LogRealtimeList'

export default {
  components: {
    LogRealtimeList,
  }
}
</script>