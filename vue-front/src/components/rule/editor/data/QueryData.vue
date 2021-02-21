<template>
  <el-form>
    <div class="row">
      <el-form-item label="시스템">
        <el-input v-model="sparam.systemId" class="size-id" readonly/>
      </el-form-item>
      <el-form-item label="Type">
        <el-select v-model="sparam.type" class="size-id">
          <el-option :value="0" label="Table"/>
          <el-option :value="1" label="Query"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click.stop="getStructure">구조 가져오기</el-button>
      </el-form-item>
    </div>
    <div class="row">
      <el-form-item label="시스템">
        <el-input type="textarea" v-model="sparam.query" class="data-query"/>
      </el-form-item>
    </div>
  </el-form>
</template>
<script>
export default {
  data () {
    return {
      sparam: {
        systemId: '',
        type: 0,
        query: '',
      }
    }
  },
  methods: {
    getStructure () {
      let systemId = this.sparam.systemId
      if(!systemId || systemId.trim().length === 0) {
        this.$message({message: "시스템을 선택하여 주세요.", type: 'warning'})
        return
      }
      let query = this.sparam.query
      if(!query || query.trim().length === 0) {
        if(type === 0) {
          this.$message({message: "테이블 명을 넣어주세요.", type: 'warning'})
        } else {
          this.$message({message: "SELECT 문을 넣어주세요.", type: 'warning'})
        }
        return
      }
      this.$emit('get', this.sparam)
    }
  }
}
</script>