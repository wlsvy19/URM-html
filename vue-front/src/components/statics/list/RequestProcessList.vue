<template>
  <div class="urm-list">
    <el-table ref="table" :height="listHeight" border class="table-striped">
      <el-table-column label="변경일자" prop="chgDate" width="130"/>
      <el-table-column label="요건신청" prop="stat1"/>
      <el-table-column label="재작성요청" prop=""/>
      <el-table-column label="접수완료" prop=""/>
      <el-table-column label="반려" prop=""/>
      <el-table-column label="요건등록완료" prop=""/>
      <el-table-column label="대표거래테스트" prop=""/>
      <el-table-column label="테스트중" prop=""/>
      <el-table-column label="테스트완료" prop=""/>
      <el-table-column label="테스트이관요청" prop=""/>
      <el-table-column label="테스트이관" prop=""/>
      <el-table-column label="운영이관요청" prop=""/>
      <el-table-column label="운영이관" prop=""/>
      <el-table-column label="TOTAL" prop=""/>
    </el-table>
  </div>
</template>

<script>
export default {
  data () {
    return {
      listHeight: 'calc(100vh - 600px)',
    }
  }
}
</script>