<template>
  <el-form :inline="true">
    <el-form-item :label="$t('label.fileName')">
      <input type="file" ref="excelFile" class="size-id" readonly/>
    </el-form-item>
    <el-form-item :label="$t('label.fileSelect')">
      <el-input v-model="sparam.sheetName" class="size-name" readonly/>
    </el-form-item>
    <el-form-item label="">
      <el-button icon="el-icon-upload" @click.stop="getFields"/>
    </el-form-item>
  </el-form>
</template>
<script>
export default {
  data () {
    return {
      sheetName: '',
    }
  },
  methods: {
    getFields () {
      let files = this.$refs.excelFile.files
      let formData = new FormData()
      formData.append('sheetName', encodeURIComponent(this.sheetName))
      formData.append('file', files[0], files[0].name)

      this.$emit('get', formData)
    }, // getFields
  },
}
</script>