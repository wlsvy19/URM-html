<template>
  <div class="urm-list">
    <el-table :data="items" @row-dblclick="handleRowDblclick" :height="mainHeight"
        border stripe :row-key="$randomRowKey">
      <el-table-column label="인터페이스 ID" prop="interfaceId" width="135"/>
      <el-table-column label="상태" width="65">
        <template slot-scope="scope">
          <span style="color: #000000" v-if="scope.row.status === 'Y'">성공</span>
          <span style="color: #FF0000" v-if="scope.row.status === 'E'">실패</span>
          <span style="color: #0000FF" v-if="scope.row.status !== 'Y' && scope.row.status !== 'E'">미완료</span>
        </template>
      </el-table-column>
      <el-table-column label="총데이터" prop="totalCount" width="85"/>
      <el-table-column label="성공" prop="successCount" width="75"/>
      <el-table-column label="실패" prop="failCount" width="75"/>
      <el-table-column label="시작시간" prop="startTime" width="115"/>
      <el-table-column label="종료시간" prop="endTime" width="115"/>
      <el-table-column label="전송디렉토리" prop="sendFilePath" :show-overflow-tooltip="true"/>
      <el-table-column label="전송파일" prop="sendFileName" :show-overflow-tooltip="true"/>
      <el-table-column label="수신디렉토리" prop="rcvFilePath" :show-overflow-tooltip="true"/>
      <el-table-column label="수신파일" prop="rcvFileName" :show-overflow-tooltip="true"/>
    </el-table>

    <hr/>
    <div>BATCH ID : {{this.batchId}}</div>
    <el-table :data="details" :height="detailHeight" border stripe :row-key="$randomRowKey">
      <el-table-column label="모델 컴포넌트 명" prop="modelName" width="150"/>
      <el-table-column label="APP ID" prop="appId" width="145"/>
      <el-table-column label="처리 내용" prop="processContent"/>
      <el-table-column label="처리 결과" prop="result" width="95"/>
      <el-table-column label="처리 시간" prop="processTime" width="120"/>
      <el-table-column label="에러내용" prop="errorContents" :show-overflow-tooltip="true"/>
    </el-table>
  </div>
</template>

<script>
import LogList from './LogList'

export default {
  mixins: [LogList],
  props: {
    items: {
      type: Array,
      default: () => [],
    },
  },
  data () {
    return {
      mainHeight: 'calc((100vh - 165px)/2)',
      detailHeight: 'calc((100vh - 165px)/2 - 50px)',
      path: 'batch',
      batchId: '',
    }
  },
}
</script>