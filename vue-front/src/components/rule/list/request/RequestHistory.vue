<template>
  <div class="urm-list">
    <el-table :data="data" height="500" border stripe>
      <el-table-column :label="$t('label.interfaceId')" prop="interfaceId" width="120"/>
      <el-table-column :label="$t('label.interfaceType')" prop="interfaceType" min-width="80">
        <template slot-scope="scope">
          <span>{{getTypeStr('infType', scope.row.interfaceType)}}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('label.requestName')" prop="name" :show-overflow-tooltip="true"/>
      <el-table-column :label="$t('label.changeStatus')" width="115">
        <template slot-scope="scope">
          <span>{{getTypeStr('chgStat', scope.row.chgStat)}}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('label.processStatus')" min-width="85">
        <template slot-scope="scope">
          <span>{{getTypeStr('procStat', scope.row.processStat)}}</span>
        </template>
      </el-table-column>
      <el-table-column label="업무구분" prop="jobType" min-width="85"/>
      <el-table-column label="싱크타입" prop="syncType" min-width="85"/>
      <el-table-column label="송신방식" width="100">
        <template slot-scope="scope">
          <span>{{getTypeStr('sysType', scope.row.sendSystemType)}}</span>
        </template>
      </el-table-column>
      <el-table-column label="수신방식" width="100">
        <template slot-scope="scope">
          <span>{{getTypeStr('sysType', scope.row.rcvSystemType)}}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('label.sourceSystem')" prop="sendSystem.name" width="110" :show-overflow-tooltip="true"/>
      <el-table-column label="송신업무" prop="sendJobCode.part2Name" min-width="85" :show-overflow-tooltip="true"/>
      <el-table-column :label="$t('label.targetSystem')" prop="rcvSystem.name" width="110" :show-overflow-tooltip="true"/>
      <el-table-column label="수신업무" prop="rcvJobCode.part2Name" width="85" :show-overflow-tooltip="true"/>
      <el-table-column label="2PC 여부" :formatter="getTpcYNStr" width="85"/>
      <el-table-column label="전송유형" min-width="85">
        <template slot-scope="scope">
          <span>{{getTypeStr('trType', scope.row.trType)}}</span>
        </template>
      </el-table-column>
      <el-table-column label="요청 데이터 매핑" prop="reqDataMappingId" width="140"/>
      <el-table-column label="응답 데이터 매핑" prop="resDataMappingId" width="140"/>
      <el-table-column label="송신담당자" prop="sendAdminId" width="100"/>
      <el-table-column label="수신담당자" prop="rcvAdminId" width="100"/>
      <el-table-column label="등록자" prop="regId" width="75"/>
      <el-table-column :label="$t('label.changeId')" prop="chgId"/>
      <el-table-column :label="$t('label.changeDate')" width="145" :formatter="getChgDateStr"/>
      <el-table-column label="쿼리" prop="sqlPlain" width="60" class-name="edit-cell">
        <template>
          <el-button>쿼리</el-button>
        </template>
      </el-table-column>
      <el-table-column label="기타 요청" prop="etcRemark" width="95" class-name="edit-cell">
        <template>
          <el-button>기타 요청</el-button>
        </template>
      </el-table-column>
      <el-table-column label="요청 응답" prop="eaiRemark" width="95" class-name="edit-cell">
        <template>
          <el-button>요청 응답</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
<script>
import RuleUtil from '@/components/rule/RuleUtil'

export default {
  props: {
    data : {
      type: Array,
      default: function () {
        return []
      },
    },
  },
  data () {
    return {
    }
  },
  methods: {
    getTypeStr (key, val) {
      let kind = RuleUtil.CODEKEY[key]
      let codes = this.$store.state.codes
      let obj = {}
      codes.some((code) => {
        if (code.kind === kind && code.code === val) {
          obj = code
          return true
        }
      })
      return obj.name
    }, // getTypeStr

    getTpcYNStr (row) {
      return row.tpcYN ? 'Yes' : 'No'
    }, // getYNStr

    getChgDateStr(row) {
      return this.$convertDateFormat('yyyy-MM-dd HH:mm', new Date(row.chgDate))
    }, // getDateStr
  },
}
</script>