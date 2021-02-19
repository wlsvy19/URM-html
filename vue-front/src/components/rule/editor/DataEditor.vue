<template>
  <div class="urm-editor">
    <div class="editor-buttons">
      <el-button @click="downloadExcel" plain>Excel 내려받기</el-button>
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
          <el-button icon="el-icon-document-copy" @click.stop="copyData" :disabled="existField" plain/>
        </div>
        <div class="data-row-buttons">
          <el-button icon="el-icon-circle-plus-outline" @click.stop="clickAppend" plain/>
          <el-button icon="el-icon-remove-outline" @click.stop="clickRemove" type="danger" plain/>
        </div>
      </div>
      <el-table ref="fieldTable" :data="item.fields" border class="table-striped">
        <el-table-column type="selection" width="40"/>
        <el-table-column :label="$t('label.index')" prop="sno" width="70" align="center"/>
        <el-table-column :label="$t('label.fieldName')" width="150" class-name="edit-cell">
          <template slot-scope="scope">
            <el-input v-model="scope.row.engName"/>
          </template>
        </el-table-column>
        <el-table-column :label="$t('label.fieldLocalName')" width="150" class-name="edit-cell">
          <template slot-scope="scope">
            <el-input v-model="scope.row.name"/>
          </template>
        </el-table-column>
        <el-table-column :label="$t('label.fieldType')" width="120" class-name="edit-cell">
          <template slot-scope="scope">
            <el-select v-model="scope.row.type">
              <el-option value="C" label="Character"/>
              <el-option value="N" label="Number"/>
              <el-option value="D" label="Date"/>
              <el-option value="B" label="Binary"/>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column :label="$t('label.dFormat')" class-name="edit-cell">
          <template slot-scope="scope">
            <el-input v-model="scope.row.dateFormat"/>
          </template>
        </el-table-column>
        <el-table-column :label="$t('label.len')" width="70" class-name="edit-cell">
          <template slot-scope="scope">
            <el-input v-model="scope.row.length"/>
          </template>
        </el-table-column>
        <el-table-column :label="$t('label.nullable')" width="75" class-name="edit-cell">
          <template slot-scope="scope">
            <YesNoSelect :data="scope.row.nullable"/>
          </template>
        </el-table-column>
        <el-table-column :label="$t('label.isKey')" width="85" class-name="edit-cell">
          <template slot-scope="scope">
            <YesNoSelect :data="scope.row.keyYN"/>
          </template>
        </el-table-column>
        <el-table-column :label="$t('label.isSQL')" width="120" class-name="edit-cell">
          <template slot-scope="scope">
            <YesNoSelect :data="scope.row.sqlYN"/>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script>
import RuleEditor from './RuleEditor'
import YesNoSelect from './YesNoSelect'

export default {
  mixins: [RuleEditor],
  components: {
    YesNoSelect,
  },
  data () {
    return {
    }
  },

  methods: {
    downloadExcel () {
      console.log('download')
    }, // downloadExcel

    addByExcel () {
      console.log('add by excel')
    }, // addByExcel
    
    addByQuery () {
      console.log('add by query')
    },

    copyData () {
      console.log('copy data')
    },

    clickAppend () {
      console.log('append', this.item)
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
        this.$message({
          message: 'Please select an item to remove.',
          type: 'warning',
        })
        return
      }
      let fields = this.item.fields
      checkedList.forEach((chkItem) => {
        fields.some((it, idx) => {
          if (it.sno === chkItem.sno) {
            fields.splice(idx, 1)
            return true // break
          }
        })
      })
      this.refreshIndex()
    }, // clickRemove

    refreshIndex () {
      let fields = this.item.fields
      fields.forEach((it, idx) => {
        it.sno = idx + 1
      })
    }, // refreshIndex

    customValidator () {
      let item = this.item
      if (!item.fields || item.fields.length === 0) {
        this.$message({type: 'error', message: 'please add fields'})
        return false
      }
      return true
    }, // customValidator
  }, // methods

  computed: {
    existField: function () {
      return this.item.fields && this.item.fields.length > 0
    }
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