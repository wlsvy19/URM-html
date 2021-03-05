<template>
  <div class="urm-list">
    <el-table ref="table" :height="listHeight1" border stripe>
      <el-table-column label="인터페이스 ID" prop="interfaceId" width="145"/>
      <el-table-column label="Global ID" prop="globalId"/>
      <el-table-column label="시작 시간" prop="startTime" width="235"/>
      <el-table-column label="종료 시간" prop="endTime" width="235"/>
      <el-table-column label="처리 시간" prop="processTime" width="135"/>
      <el-table-column label="결과" width="100"/>
      <el-table-column label="에러코드" prop="errorCode" width="230"/>
    </el-table>
    <hr />
    <el-table ref="table" :height="listHeight2" border stripe>
      <el-table-column label="송신단계" width="85"/>
      <el-table-column label="수신단계" width="85"/>
      <el-table-column label="처리시스템" prop="serverName" width="155"/>
      <el-table-column label="모델명" prop="modelId" width="145"/>
      <el-table-column label="시스템ID" prop="appId" width="100"/>
      <el-table-column label="시작 시간" prop="startTime" width="120"/>
      <el-table-column label="종료 시간" prop="endTime" width="115"/>
      <el-table-column label="처리 시간" prop="processTime" width="115"/>
      <el-table-column label="결과" width="60"/>
      <el-table-column label="에러메시지" prop="errMessage"/>
      <el-table-column label="전문헤더" prop="header" width="90"/>
    </el-table>
  </div>
</template>

<script>
export default {
  data () {
    return {
      listHeight1: 'calc(100vh - 600px)',
      listHeight2: 'calc(100vh - 700px)',
    }
  }
}
</script>