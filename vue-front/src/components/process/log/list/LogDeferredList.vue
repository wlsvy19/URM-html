<template>
  <div class="urm-list">
  <el-tabs v-model="log" @tab-click="handleClick">
    <el-tab-pane label="로그데이터" name="first">
      <el-table ref="table" :height="listHeight" border class="table-striped">
        <el-table-column label="인터페이스 ID" prop="interfaceId" width="250"/>
        <el-table-column label="시작 시간" prop="startTime"/>
        <el-table-column label="종료 시간" prop="endTime"/>
        <el-table-column label="총 건수" prop="totalCount"/>
        <el-table-column label="성공 건수" prop="successCount"/>
        <el-table-column label="실패 건수" prop="failCount"/>
      </el-table>

      <hr />

      <el-table ref="table" :height="listHeight" border class="table-striped">
        <el-table-column label="인터페이스ID" prop="interfaceId" width="215"/>
        <el-table-column label="시스템" prop="appId" width="215"/>
        <el-table-column label="처리일자" prop="processDate"/>
        <el-table-column label="시작 시간" prop="startTime"/>
        <el-table-column label="종료 시간" prop="endTime"/>
        <el-table-column label="총 건수" prop="totalCount"/>
        <el-table-column label="성공 건수" prop="successCount"/>
        <el-table-column label="실패 건수" prop="failCount"/>
      </el-table>
    </el-tab-pane>
    <el-tab-pane label="오류데이터" name="second">
      <el-table ref="table" :height="listHeight" border class="table-striped">
        <el-table-column label="인터페이스 ID" prop="interfaceId" width="145"/>
        <el-table-column label="처리 일자" prop="processDate" width="110"/>
        <el-table-column label="에러 시간" prop="messageTime" width="120"/>
        <el-table-column label="시스템" prop="appId" width="145"/>
        <el-table-column label="오류데이터" prop="errorContents"/>
        <el-table-column label="자세한에러" prop="errorDataContents"/>
      </el-table>
    </el-tab-pane>
  </el-tabs>


  </div>
</template>

<script>
export default {
  data () {
    return {
      listHeight: 'calc(100vh - 900px)',
      log: 'first',
    }
  }
}
</script>