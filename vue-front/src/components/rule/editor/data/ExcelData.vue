<template>
  <div class="urm-editor">
    <div class="editor-buttons">
      <el-button @click="clickConfirm">{{$t('label.confirm')}}</el-button>
    </div>
    <el-form :inline="true">
      <el-form-item :label="$t('label.fileName')">
        <el-input v-model="fileName" class="size-name" @click.native="changeXlFile" readonly/>
        <input type="file" ref="xlFile" accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel"
          @change="handleChangeXlFile" style="display: none;"/>
      </el-form-item>
      <el-form-item :label="$t('label.fileSelect')">
        <el-input v-model="sheetName" class="size-name"/>
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-upload2" @click.stop="getFields"/>
      </el-form-item>
    </el-form>
    <FieldTable :data="fields" :isTemplate="true"/>
  </div>
</template>
<script>
import FieldTable from './FieldTable'

export default {
  components: {
    FieldTable,
  },
  data () {
    return {
      fileName: '',
      sheetName: '',
      fields: [],
    }
  },
  methods: {
    clickConfirm () {
      this.$emit('confirm', this.fields)
    }, // clickConfirm

    getFields () {
      let files = this.$refs.xlFile.files
      if (files.length === 0) {
        return false
      }
      let sheetName = this.sheetName
      if (!sheetName || sheetName.trim().length === 0) {
        return false
      }

      let formData = new FormData()
      formData.append('sheetName', encodeURIComponent(sheetName))
      formData.append('file', files[0], files[0].name)

      const loading = this.$startLoading()
      this.$http({
        method : 'POST',
        url: '/api/data/field/excel',
        data: formData,
      }).then(response => {
        this.fields = response.data
      }).catch(error => {
        this.$handleHttpError(error)
      }).finally(() => {
        loading.close()
      })
    }, // getFields

    changeXlFile () {
      this.$refs.xlFile.click()
    }, // changeFile

    handleChangeXlFile (e) {
      this.fileName = e.target.files[0].name
    }, // handleChangeFile
  },
}
</script>