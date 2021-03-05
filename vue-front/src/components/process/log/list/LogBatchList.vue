<template>
  <div class="urm-list">
    <el-table ref="table" :height="listHeight1" border stripe>
      <el-table-column label="인터페이스 ID" prop="interfaceId" width="130"/>
      <el-table-column label="상태" prop="status" width="65"/>
      <el-table-column label="총데이터" prop="totalCount" width="85"/>
      <el-table-column label="성공" prop="successCount" width="75"/>
      <el-table-column label="실패" prop="failCount" width="75"/>
      <el-table-column label="시작시간" prop="startTime" width="115"/>
      <el-table-column label="종료시간" prop="endTime" width="115"/>
      <el-table-column label="전송디렉토리" prop="sendFilePath"/>
      <el-table-column label="전송파일" prop="sendFileName"/>
      <el-table-column label="수신디렉토리" prop="rcvFilePath"/>
      <el-table-column label="수신파일" prop="rcvFileName"/>
    </el-table>
    <hr />
    <el-table ref="table" :height="listHeight2" border stripe>
      <el-table-column label="모델 컴포넌트 명" prop="modelName" width="150"/>
      <el-table-column label="APP ID" prop="appId" width="145"/>
      <el-table-column label="처리 내용" prop="processContent"/>
      <el-table-column label="처리 결과" prop="result" width="95"/>
      <el-table-column label="처리 시간" prop="processTime" width="120"/>
      <el-table-column label="에러내용" prop="errorContents"/>
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