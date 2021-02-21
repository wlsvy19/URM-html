<template>
  <el-form :inline="true">
    <el-form-item :label="$t('label.fileName')">
      <el-input v-model="sparam.fileName" class="size-id" readonly/>
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
      sparam: {
        fileName: '',
        sheetName: '',
      }
    }
  },
  methods: {
    getFields () {
      this.$emit('get', this.sparam)
    }, // getFields
  },
}
</script>