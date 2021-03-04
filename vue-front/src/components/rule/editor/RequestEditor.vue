<template>
  <div class="urm-editor">
    <div class="editor-buttons">
      <el-button @click="clickSave" plain>{{$t('label.save')}}</el-button>
    </div>
    <el-form label-width="135px">
      <div class="row">
        <el-form-item :label="$t('label.requestId')">
          <el-input v-model="item.id" class="size-id" readonly/>
        </el-form-item>
        <el-form-item :label="$t('label.requestName')">
          <el-input v-model="item.name" class="size-default"/>
        </el-form-item>
      </div>

      <div class="row">
        <el-form-item :label="$t('label.interfaceType')">
          <el-select v-model="item.interfaceType" class="size-id" @change="handleChangeInfType">
            <el-option v-for="type in infTypes" :value="type.code" :label="type.name" :key="type.code"/>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('label.processStatus')">
          <el-select v-model="item.processStat" class="size-id">
            <el-option v-for="stat in procStats" :value="stat.code" :label="stat.name" :key="stat.code"/>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('label.changeStatus')">
          <el-select v-model="item.chgStat" class="size-id">
            <el-option v-for="stat in chgStats" :value="stat.code" :label="stat.name" :key="stat.code"/>
          </el-select>
        </el-form-item>
      </div>

      <div class="row">
        <el-form-item :label="$t('label.syncType')">
          <el-select v-model="item.syncType" class="size-id" @change="handleChangeSyncType">
            <el-option v-for="type in syncTypes" :value="type.code" :label="type.name" :key="type.code"/>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('label.interfaceId')">
          <el-input v-model="item.interfaceId" class="size-id"/>
        </el-form-item>
        <el-form-item :label="$t('label.eaiYn')">
          <el-checkbox v-model="item.eaiYN"/>
        </el-form-item>
        <el-form-item :label="$t('label.2pcYn')">
          <el-checkbox v-model="item.tpcYN"/>
        </el-form-item>
      </div>

      <div class="row">
        <el-form-item :label="$t('label.jobType')">
          <el-input v-model="item.jobType" class="size-id"/>
        </el-form-item>
        <el-form-item :label="$t('label.trType')">
          <el-radio-group v-model="item.trType" class="size-id">
            <el-radio v-for="type in trTypes" :label="type.code" :key="type.code">{{type.name}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item :label="$t('label.testDate')">
          <el-date-picker v-model="testDate" type="daterange" class="size-name"
            value-format="yyyyMMdd" :clearable="false"/>
        </el-form-item>
      </div>

      <div class="row">
        <el-form-item :label="$t('label.isMapping')">
          <el-checkbox v-model="item.dataMapYN"/>
        </el-form-item>
        <div class="row" v-show="item.dataMapYN">
          <el-form-item label="Request">
            <el-input v-model="item.reqDataMappingId" class="size-id" readonly>
              <i slot="suffix" class="el-input__icon el-icon-close pointer" @click.stop="handleClear('req')"/>
            </el-input>
          </el-form-item>
          <el-form-item label="Response">
            <el-input v-model="item.resDataMappingId" class="size-id" readonly>
              <i slot="suffix" class="el-input__icon el-icon-close pointer" @click.stop="handleClear('res')"/>
            </el-input>
          </el-form-item>
        </div>
      </div>
      <hr/>
      <div class="row">
        <div>
          <div class="row">
            <el-form-item :label="$t('label.sourceMethod')">
              <el-select v-model="item.sendSystemType" class="size-id">
                <el-option v-for="type in sysTypes" :value="type.code" :label="type.name" :key="type.code"/>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('label.sourceSystem')">
              <el-input v-model="item.sendSystem.name" class="size-id" @click.native="showSystemList('send')" readonly/>
            </el-form-item>
          </div>
          <div class="row">
            <el-form-item :label="serviceLabel('send')">
              <el-input v-model="item.sendServerName" class="size-id" />
            </el-form-item>
            <el-form-item :label="$t('label.sourceInfo')">
              <el-input v-model="item.sendDbInfo" class="size-id" />
            </el-form-item>
          </div>
          <!-- DB -->
          <div class="row" v-show="item.sendSystemType === '2'">
            <el-form-item :label="$t('label.sourceQuery')">
              <el-input v-model="item.sqlPlain" style="width: 390px;" readonly/>
            </el-form-item>
          </div>
          <div class="row">
            <el-form-item :label="$t('label.sndJobCode')">
              <el-input v-model="jobCodeName.send" class="size-id" @click.native="showBizList('send')" readonly/>
            </el-form-item>
            <el-form-item :label="$t('label.sourceAdmin')">
              <el-input v-model="adminName.send" class="size-id" @click.native="showUserList('send')" readonly/>
            </el-form-item>
          </div>
        </div>

        <div>
          <div class="row">
            <el-form-item :label="$t('label.targetMethod')">
              <el-select v-model="item.rcvSystemType" class="size-id">
                <el-option v-for="type in sysTypes" :value="type.code" :label="type.name" :key="type.code"/>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('label.targetSystem')">
              <el-input v-model="item.rcvSystem.name" class="size-id" @click.native="showSystemList('rcv')" readonly/>
            </el-form-item>
          </div>
          <div class="row">
            <el-form-item :label="serviceLabel('rcv')">
              <el-input v-model="item.rcvServerName" class="size-id" />
            </el-form-item>
            <el-form-item :label="$t('label.targetInfo')">
              <el-input v-model="item.rcvDbInfo" class="size-id" />
            </el-form-item>
          </div>
          <!-- File -->
          <div class="row" v-if="item.rcvSystemType === '1'">
            <el-form-item :label="$t('label.targetFileCRUD')">
              <el-select v-model="item.fileCrudType" class="size-id">
                <el-option v-for="type in fileCrudTypes" :value="type.code" :label="type.name" :key="type.code"/>
              </el-select>
            </el-form-item>
          </div>
          <!-- DB -->
          <div class="row" v-if="item.rcvSystemType === '2'">
            <el-form-item :label="$t('label.targetDBCRUD')">
              <el-select v-model="item.dbCrudType" class="size-name">
                <el-option v-for="type in dbCrudTypes" :value="type.code" :label="type.name" :key="type.code"/>
              </el-select>
            </el-form-item>
          </div>
          <div class="row">
            <el-form-item :label="$t('label.rcvJobCode')">
              <el-input v-model="jobCodeName.rcv" class="size-id" @click.native="showBizList('rcv')" readonly/>
            </el-form-item>
            <el-form-item :label="$t('label.targetAdmin')">
              <el-input v-model="adminName.rcv" class="size-id" @click.native="showUserList('rcv')" readonly/>
            </el-form-item>
          </div>
        </div>
      </div>

      <hr/>
      <div class="row">
        <el-form-item :label="$t('label.etcRequest')">
          <el-input type="textarea" v-model="item.etcRemark" class="size-default"/>
        </el-form-item>
        <el-form-item :label="$t('label.eaiRequest')">
          <el-input type="textarea" v-model="item.eaiRemark" class="size-default"/>
        </el-form-item>
      </div>

      <hr/>
      <div class="row">
        <el-form-item :label="$t('label.attachmentFile')">
          <el-input v-model="item.infFileName" class="size-default" @click.native="changeInfFile" readonly>
            <i slot="suffix" class="el-input__icon el-icon-download pointer" @click.stop="downloadInfFile"/>
          </el-input>
          <input type="file" ref="infFile" @change="handleChangeInfFile" style="display: none;"/>
        </el-form-item>
      </div>
    </el-form>
  </div>
</template>

<script>
import RuleEditor from './RuleEditor'
import RuleUtil from '@/components/rule/RuleUtil'

export default {
  mixins: [RuleEditor],
  props: {
    infTypes: {
      type: Array,
      default: function () {
        return []
      },
    },
    chgStats: {
      type: Array,
      default: function () {
        return []
      },
    },
    procStats: {
      type: Array,
      default: function () {
        return []
      },
    },
  },
  data() {
    return {
      tgtType: '',
      userListShow: false,
      users: null,
    }
  },
  methods: {
    customValidator () {
      let item = this.item
      if (!item.sendSystemId || item.sendSystemId.length === 0) {
        this.$message({message: this.$t('message.1008'), type: 'warning'})
        return false
      }
      if (!item.rcvSystemId || item.rcvSystemId.length === 0) {
        this.$message({message: this.$t('message.1009'), type: 'warning'})
        return false
      }
      if (!item.sendJobCodeId || item.sendJobCodeId.length === 0) {
        this.$message({message: this.$t('message.1019'), type: 'warning'})
        return false
      }
      if (!item.rcvJobCodeId || item.rcvJobCodeId.length === 0) {
        this.$message({message: this.$t('message.1019'), type: 'warning'})
        return false
      }

      if (!item.sendAdminId || item.sendAdminId.length === 0) {
        //item.sendAdminId = this.props.userInfo.id
      }
      if (!item.rcvAdminId || item.rcvAdminId.length === 0) {
        //item.rcvAdminId = this.props.userInfo.id
      }

      if (!item.dataMapYN) {
        item.reqDataMappingId = null
        item.resDataMappingId = null
      }
      if (item.sendSystemType !== '2') {
        item.sqlPlain = null
        item.sql = null
      }
      if (item.rcvSystemType !== '1') {
        item.fileCrudType = null
      }
      if (item.rcvSystemType !== '2') {
        item.dbCrudType = null
      }

      /*if( !urmsc.isSelectAuth('prcStat', data.processStat, this.props.userInfo.auth) ) {
        message.warning(locale['message.1010']);
        return false
      }
      if( !urmsc.isSelectAuth('chgStat', data.chgStat, this.props.userInfo.auth) ) {
        message.warning('\'변경 신청\' 으로 수정합니다.');
        this.setState({ item: {...this.state.item, chgStat: 0} })
        return false
      }*/
      return true
    }, // customValidator

    handleChangeInfType (val) {
      switch (val) {
        case '1':
          this.item.sendSystemType = '3'
          this.item.rcvSystemType = '3'
          this.item.tpcYN = false
          this.item.trType = '2'
          this.item.dataMapYN = false
          break
        case '2':
          this.item.sendSystemType = '3'
          this.item.rcvSystemType = '3'
          this.item.tpcYN = true
          this.item.dataMapYN = false
          break
        case '3':
          this.item.sendSystemType = '3'
          this.item.rcvSystemType = '3'
          this.item.tpcYN = true
          this.item.dataMapYN = true
          break
      }
    }, // handleChangeInfType

    handleChangeSyncType (val) {
      switch (val) {
        case '2':
          this.item.trType = '1'
          break
        default:
          this.item.trType = '2'
          break
      }
    }, // handleChangeSyncType

    changeInfFile () {
      this.$refs.infFile.click()
    }, // changeInfFile

    handleChangeInfFile (e) {
      let file = e.target.files[0]

      let formData = new FormData()
      formData.append('file', file, file.name)

      const loading = this.$startLoading()
      this.$http({
        method : 'POST',
        url: '/api/request/upload',
        data: formData,
      }).then(response => {
        this.$message({message: this.$t('message.0007') + ' - ' + response.data, type: 'success'})
        this.item.infFileName = file.name
      }).catch(error => {
        this.$handleHttpError(error)
      }).finally(() => {
        loading.close()
      })
    }, // handleChangeInfFile

    downloadInfFile () {
      let fileName = this.item.infFileName
      if (!fileName || fileName.length === 0) return
      window.open('/api/request/download?fileName='+fileName, '_blank')
    }, // downloadInfFile

    handleClear (type) {
      switch (type) {
        case 'req':
          this.item.reqDataMappingId = null
          break
        case 'res':
          this.item.resDataMappingId = null
          break
      }
    }, // handleClear

    showSystemList (type) {
      this.tgtType = type
      this.$emit('show-sys-list', this.cbSystemRowClick, type)
    }, // showSystemList

    cbSystemRowClick (row) {
      if (this.tgtType === 'send') {
        this.item.sendSystemId = row.id
        this.item.sendSystem.name = row.name
      } else if (this.tgtType === 'rcv') {
        this.item.rcvSystemId = row.id
        this.item.rcvSystem.name = row.name
      }
    }, // cbSystemRowClick

    showBizList (type) {
      this.tgtType = type
      this.$emit('show-biz-list', this.cbBizRowClick)
    }, // showBizList

    cbBizRowClick (row) {
      if (this.tgtType === 'send') {
        this.item.sendJobCodeId = row.id
        this.item.sendJobCode.part2Id = row.part2Id
        this.item.sendJobCode.part2Name = row.part2Name
      } else if (this.tgtType === 'rcv') {
        this.item.rcvJobCodeId = row.id
        this.item.rcvJobCode.part2Id = row.part2Id
        this.item.rcvJobCode.part2Name = row.part2Name
      }
    }, // cbUserRowClick

    showUserList (type) {
      this.tgtType = type
      this.$emit('show-usr-list', this.cbUserRowClick)
    }, // showUserList

    cbUserRowClick (row) {
      if (this.tgtType === 'send') {
        this.item.sendAdminId = row.id
        this.item.sendAdmin.name = row.name
        this.item.sendAdmin.positionName = (row.positionName ? row.positionName : '--')
      } else if (this.tgtType === 'rcv') {
        this.item.rcvAdminId = row.id
        this.item.rcvAdmin.name = row.name
        this.item.rcvAdmin.positionName = (row.positionName ? row.positionName : '--')
      }
    }, // cbUserRowClick
  },
  computed: {
    syncTypes: function () {
      let kind = RuleUtil.CODEKEY.syncType
      return this.$store.state.codes.filter(code => (code.kind === kind))
    },
    trTypes: function () {
      let kind = RuleUtil.CODEKEY.trType
      return this.$store.state.codes.filter(code => (code.kind === kind))
    },
    sysTypes: function () {
      let kind = RuleUtil.CODEKEY.sysType
      return this.$store.state.codes.filter(code => (code.kind === kind))
    },
    dbCrudTypes: function () {
      let kind = RuleUtil.CODEKEY.dbCrudType
      return this.$store.state.codes.filter(code => (code.kind === kind))
    },
    fileCrudTypes: function () {
      let kind = RuleUtil.CODEKEY.fileCrudType
      return this.$store.state.codes.filter(code => (code.kind === kind))
    },
    serviceLabel () {
      return (type) => {
        if (type === 'send') {
          switch (this.item.sendSystemType) {
            case '1': return this.$t('label.sourceFile')
            case '2': return this.$t('label.sourceTable')
            default: return this.$t('label.sourceService')
          }
        } else if (type === 'rcv') {
          switch (this.item.rcvSystemType) {
            case '1': return this.$t('label.targetFile')
            case '2': return this.$t('label.targetTable')
            default: return this.$t('label.targetService')
          }
        }
      }
    },
    jobCodeName: function () {
      return {
        send: this.item.sendJobCode.part2Name + '(' + this.item.sendJobCode.part2Id + ')',
        rcv: this.item.rcvJobCode.part2Name + '(' + this.item.rcvJobCode.part2Id + ')',
      }
    },
    adminName: function () {
      return {
        send: this.item.sendAdmin.name + '(' + this.item.sendAdmin.positionName + ')',
        rcv: this.item.rcvAdmin.name + '(' + this.item.rcvAdmin.positionName + ')',
      }
    },
    testDate: {
      get: function () {
        return [this.item.testStartYMD, this.item.testEndYMD]
      },
      set: function (nVal) {
        this.item.testStartYMD = nVal[0]
        this.item.testEndYMD = nVal[1]
      },
    },
  },
}
</script>
<style scoped>
.size-default {
  width: 485px;
}
</style>