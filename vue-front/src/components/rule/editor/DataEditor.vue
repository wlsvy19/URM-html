<template>
  <div class="urm-editor">
    <div class="editor-buttons">
      <el-button @click="downloadExcel" :disabled="isNew" plain>Excel 내려받기</el-button>
      <el-button @click="clickSave" plain>{{$t('label.save')}}</el-button>
    </div>
    <el-form label-width="135px" :inline="true">
      <el-form-item :label="$t('label.dataId')">
        <el-input v-model="item.id" class="size-id" readonly/>
      </el-form-item>
      <el-form-item :label="$t('label.dataName')">
        <el-input v-model="item.name" class="size-name"/>
      </el-form-item>
    </el-form>
    
    <hr/>
    <div>
      <div class="data-header">
        <div>
          <el-button icon="el-icon-document" @click.stop="addByExcel" :disabled="existField" plain/>
          <el-button icon="el-icon-coin" @click.stop="addByQuery" :disabled="existField" plain/>
          <el-button icon="el-icon-copy-document" @click.stop="copyData" :disabled="existField" plain/>
        </div>
        <div class="data-row-buttons">
          <el-button icon="el-icon-circle-plus-outline" @click.stop="clickAppend" plain/>
          <el-button icon="el-icon-remove-outline" @click.stop="clickRemove" type="danger" plain/>
        </div>
      </div>
      <FieldTable ref="fieldTable" :data="item.fields"/>
    </div>

    <el-dialog :visible.sync="excelDataShow" width="1000px" top="8vh"
      append-to-body :close-on-click-modal="false" :destroy-on-close="true">
      <ExcelData @confirm="handleConfirm"/>
    </el-dialog>

    <el-dialog :visible.sync="queryDataShow" width="1000px" top="8vh"
      append-to-body :close-on-click-modal="false" :destroy-on-close="true">
      <QueryData @confirm="handleConfirm"/>
    </el-dialog>

    <el-dialog :visible.sync="copyDataShow" width="1000px" top="8vh"
      append-to-body :close-on-click-modal="false" :destroy-on-close="true">
      <CopyData @confirm="handleConfirm"/>
    </el-dialog>
  </div>
</template>
<script>
import RuleEditor from './RuleEditor'

import FieldTable from './data/FieldTable'
import ExcelData from './data/ExcelData'
import QueryData from './data/QueryData'
import CopyData from './data/CopyData'

export default {
  mixins: [RuleEditor],
  components: {
    FieldTable,
    ExcelData,
    QueryData,
    CopyData,
  },
  data () {
    return {
      excelDataShow: false,
      queryDataShow: false,
      copyDataShow: false,
    }
  },

  methods: {
    downloadExcel () {
      window.open('api/data/excel/download?dataId=' + this.item.id, '_blank')
    }, // downloadExcel

    addByExcel () {
      this.excelDataShow = true
    }, // addByExcel
    
    addByQuery () {
      this.queryDataShow = true
    },

    copyData () {
      this.copyDataShow = true
    },

    clickAppend () {
      let fields = this.item.fields
      fields.push({
        sno: (fields.length + 1),
        type: 'C',
        nullable: true,
        keyYN: false,
        sqlYN: true,
      })
    }, // clickAppend
    
    clickRemove () {
      let checkedList = this.$refs.fieldTable.selection
      if (checkedList.length <= 0) {
        this.$message.warning(this.$t('message.1005'))
        return
      }
      let fields = this.item.fields
      checkedList.forEach((chkItem) => {
        fields.some((f, idx) => {
          if (f.sno === chkItem.sno) {
            fields.splice(idx, 1)
            return true // break
          }
        })
      })
      this.refreshIndex()
    }, // clickRemove

    refreshIndex () {
      let fields = this.item.fields
      fields.forEach((f, idx) => {
        f.sno = idx + 1
      })
    }, // refreshIndex

    handleConfirm (fields) {
      console.log(fields)
      this.item.fields = fields
      this.excelDataShow = false
      this.queryDataShow = false
      this.copyDataShow = false
    }, // handleConfirm

    customValidator () {
      let item = this.item
      if (!item.fields || item.fields.length === 0) {
        this.$message({type: 'error', message: 'please add fields'})
        return false
      }
      if (!this.isNew) {
        this.$emit('show-used', item.id, true)
        return false
      }
      return true
    }, // customValidator
  }, // methods

  computed: {
    isNew: function () {
      return !this.item.id || this.item.id.length === 0
    },
    existField: function () {
      return this.item.fields && this.item.fields.length > 0
    },
  }
}
</script>
<style scoped>
div.data-header {
  display: flex;
}
div.data-header .el-button--small {
  margin: 0;
}
div.data-header > .data-row-buttons {
  margin-left: auto;
}
</style>