<template>
  <div class="urm-list">
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item label="담당자">
          <el-input v-model="manager" class="search-id" @click.native.prevent="showUserList('mng')" readonly/>
        </el-form-item>
        <el-form-item label="이관자">
          <el-input v-model="transferor" class="search-id" @click.native.prevent="showUserList('trans')" readonly/>
        </el-form-item>
      </el-form>
      <div class="search-buttons">
        <el-button @click="clickConfirm">이관</el-button>
      </div>
    </div>

    <el-table ref="reqList" :data="items" height="500" border stripe>
      <el-table-column type="selection" width="40"/>
      <el-table-column :label="$t('label.requestId')" prop="id" width="145"/>
      <el-table-column :label="$t('label.requestName')" prop="name"/>
      <el-table-column :label="$t('label.interfaceType')" width="95" :formatter="getInfTypeStr"/>
      <el-table-column :label="$t('label.interfaceId')" prop="interfaceId" width="120"/>
      <el-table-column :label="$t('label.sourceSystem')" prop="sendSystem.name" width="110" :show-overflow-tooltip="true"/>
      <el-table-column :label="$t('label.targetSystem')" prop="rcvSystem.name" width="110" :show-overflow-tooltip="true"/>
      <el-table-column label="등록자" prop="regId" width="110"/>
      <el-table-column label="송신자" prop="sendAdminId" width="110"/>
      <el-table-column label="수신자" prop="rcvAdminId" width="110"/>
    </el-table>
  </div>
</template>
<script>
import RuleUtil from '@/components/rule/RuleUtil'

export default {
  data () {
    return {
      tgtType: '',
      manager: '',
      transferor: '',
      items: [],
    }
  },
  methods: {
    getRequests (id) {
      const loading = this.$startLoading()
      this.$http.get('/api/request/list', {
        params: {loginUser: id},
      }).then(response => {
        this.items = response.data
      }).catch(error => {
        this.$handleHttpError(error)
      }).finally(() => {
        loading.close()
      })
    }, // getRequests

    clickConfirm () {
      if (!this.manager || this.manager.length === 0) {
        this.$message.warning('담당자가 선택되지 않았습니다.')
        return false
      }
      if (!this.transferor || this.transferor.length === 0) {
        this.$message.warning('이관자가 선택되지 않았습니다.')
        return false
      }

      let checkedList = this.$refs.reqList.selection
      if (checkedList.length <= 0) {
        this.$message.warning('선택된 요건이 없습니다.')
        return
      }
      let confirmProp = {
        confirmButtonText: 'YES',
        cancelButtonText: 'NO',
        closeOnClickModal: false,
        dangerouslyUseHTMLString: false,
        type: 'error',
      }
      this.$confirm(checkedList.length + ' 의 요건이 선택되었습니다. 이관하시겠습니까?', confirmProp).then(() => {
        checkedList.forEach((req) => {
          req.regId = this.transferor
          req.sendAdminId = this.transferor
          req.rcvAdminId = this.transferor
        })
        this.$emit('confirm', checkedList)
      }).catch(() => {
        this.$message.info('취소되었습니다.')
      })
    },

    showUserList (type) {
      this.tgtType = type
      this.$emit('show-usr-list', this.cbUserRowClick)
    }, // showUserList

    cbUserRowClick (row) {
      if (this.tgtType === 'mng') {
        this.manager = row.id
        this.getRequests(row.id)
      } else if (this.tgtType === 'trans') {
        this.transferor = row.id
      }
    }, // cbUserRowClick

    getInfTypeStr (row) {
      let kind = RuleUtil.CODEKEY.infType
      let codes = this.$store.state.codes
      let obj = {}
      codes.some((code) => {
        if (code.kind === kind && code.code === row.interfaceType) {
          obj = code
          return true
        }
      })
      return obj.name
    }, // getTypeStr
  },
}
</script>