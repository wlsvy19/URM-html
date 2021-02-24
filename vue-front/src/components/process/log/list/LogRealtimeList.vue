<template>
  <div class="urm-list">
    <el-table ref="table" :height="listHeight" border class="table-striped">
      <el-table-column label="인터페이스 ID" prop="interfaceId" width="145"/>
      <el-table-column label="Global ID" prop="globalId"/>
      <el-table-column label="시작 시간" prop="startTime" width="235"/>
      <el-table-column label="종료 시간" prop="endTime" width="235"/>
      <el-table-column label="처리 시간" prop="processTime" width="135"/>
      <el-table-column label="결과" width="100"/>
      <el-table-column label="에러코드" prop="errorCode" width="230"/>
    </el-table>

    <hr />

    <el-table ref="table" :height="listHeight" border class="table-striped">
      <el-table-column label="송신단계" width="85"/>
      <el-table-column label="수신단계" width="85"/>
      <el-table-column label="처리시스템" prop="serverName" width="155"/>
      <el-table-column label="모델명" prop="modelId" width="145"/>
      <el-table-column label="시스템ID" prop="appId" wdith="130"/>
      <el-table-column label="시작 시간" prop="startTime" wdith="115"/>
      <el-table-column label="종료 시간" prop="endTime" wdith="115"/>
      <el-table-column label="처리 시간" prop="processTime" wdith="115"/>
      <el-table-column label="결과" wdith="30"/>
      <el-table-column label="에러메시지" prop="errMessage" width="1000"/>
      <el-table-column label="전문헤더" prop="header" wdith="90"/>
    </el-table>
  </div>
</template>

<script>
export default {
  data () {
    return {
      listHeight: 'calc(100vh - 900px)',
    }
  }
}
</script>