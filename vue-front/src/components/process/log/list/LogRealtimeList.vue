<template>
  <div class="urm-list">
    <el-table :data="items" @row-dblclick="handleRowDblclick" :height="mainHeight"
        border stripe :row-key="$randomRowKey">
      <el-table-column label="인터페이스 ID" prop="interfaceId" width="145"/>
      <el-table-column label="Global ID" prop="globalId"/>
      <el-table-column label="시작 시간" prop="startTime" width="235"/>
      <el-table-column label="종료 시간" prop="endTime" width="235"/>
      <el-table-column label="처리 시간" prop="processTime" width="135"/>
      <el-table-column label="결과" width="100">
        <template slot-scope="scope">
          {{getResult(scope.row.errorCode)}}
        </template>
      </el-table-column>
      <el-table-column label="에러코드" prop="errorCode" width="230"/>
    </el-table>

    <hr />
    <el-table :data="details" :height="detailHeight" border stripe :row-key="$randomRowKey">
      <el-table-column label="송신단계" width="85">
        <template slot-scope="scope">
          <span>{{scope.row.processSeq === 0 ? scope.row.stepIndex : ''}}</span>
        </template>
      </el-table-column>
      <el-table-column label="수신단계" width="85">
        <template slot-scope="scope">
          <span>{{scope.row.processSeq === 0 ? '' : scope.row.stepIndex}}</span>
        </template>
      </el-table-column>
      <el-table-column label="처리시스템" prop="serverName" width="155" :show-overflow-tooltip="true"/>
      <el-table-column label="모델명" prop="modelId" width="145"/>
      <el-table-column label="시스템ID" prop="appId" width="100"/>
      <el-table-column label="시작 시간" prop="startTime" width="120"/>
      <el-table-column label="종료 시간" prop="endTime" width="115"/>
      <el-table-column label="처리 시간" prop="processTime" width="115"/>
      <el-table-column label="결과" width="60">
        <template slot-scope="scope">
          {{getDetailResult(scope.row.errMessage)}}
        </template>
      </el-table-column>
      <el-table-column label="에러메시지" prop="errMessage" :show-overflow-tooltip="true"/>
      <el-table-column label="전문헤더" width="90">
        <template slot-scope="scope">
          <el-button @click="getMessage(scope.row)">보기</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :visible.sync="messageShow" width="800px" top="8vh"
      append-to-body :close-on-click-modal="false" :destroy-on-close="true">
      <RealtimeMessageList/>
    </el-dialog>
  </div>
</template>

<script>
import RealtimeMessageList from './RealtimeMessageList'

export default {
  components: {
    RealtimeMessageList,
  },
  data () {
    return {
      mainHeight: 'calc((100vh - 165px)/2)',
      detailHeight: 'calc((100vh - 165px)/2 - 50px)',
      items: [],
      details: [],
      messageShow: false,
    }
  },
  methods: {
    handleRowDblclick (row) {
      let sparam = {
        processDate: row.processDate,
        globalId: row.globalId,
      }
      this.$emit('row-dblclick', sparam)
    }, // handleRowDblclick

    getMessage (row) {
      this.messageShow = true
      console.log(row)
      // let sparam = {
      //   processDate: row.processDate,
      //   interfaceId: row.interfaceId,
      //   serialNumber: row.serialNumber,
      // }
      // const loading = this.$startLoading()
      // console.log('get online message', sparam)
      // this.$http.get('/api/process/log/online/message', {
      //   params: sparam,
      // }).then(response => {
      //   //
      // }).catch(error => {
      //   this.$handleHttpError(error)
      // }).finally(() => {
      //   loading.close()
      // })
    }, // getMessage

    getResult (val) {
      if (val !== 0) {
        return (<span style='color: #FF0000;'>실패</span>)
      }
      return (<span style='color: #000000;'>성공</span>)
    }, // getResult

    getDetailResult (val) {
      if (!val || val.length === 0) {
        return (<span style='color: #000000;'>성공</span>)
      }
      return (<span style='color: #FF0000;'>실패</span>)
    }, // getDetailResult
  },
}
</script>