<template>
  <div class="urm-editor">
    <div class="editor-buttons">
      <el-button @click="clickConfirm">{{$t('label.confirm')}}</el-button>
    </div>
    <el-form :inline="true">
      <div class="row">
        <el-form-item label="시스템">
          <el-input v-model="sparam.systemId" class="size-id" @click.native="showSystemList" readonly/>
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
    <FieldTable :data="fields" :isTemplate="true"/>

    <el-dialog :visible.sync="systemListShow" width="1300px" top="5vh" append-to-body :close-on-click-modal="false">
      <SystemList ref="sysList" :items="systems" :onlySearch="true" @search="searchSystemList" @row-dblclick="cbSystemRowClick"/>
    </el-dialog>
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
      sparam: {
        systemId: '',
        type: 0,
        query: '',
      },
      fields: [],
      systems: null,
      systemListShow: false,
    }
  },
  methods: {
    clickConfirm () {
      this.$emit('confirm', this.fields)
    },

    getStructure () {
      let systemId = this.sparam.systemId
      if(!systemId || systemId.trim().length === 0) {
        this.$message({message: "시스템을 선택하여 주세요.", type: 'warning'})
        return
      }
      let query = this.sparam.query
      if(!query || query.trim().length === 0) {
        if(this.sparam.type === 0) {
          this.$message({message: "테이블 명을 넣어주세요.", type: 'warning'})
        } else {
          this.$message({message: "SELECT 문을 넣어주세요.", type: 'warning'})
        }
        return
      }

      const loading = this.$startLoading()
      this.$http.get('/api/data/field/query', {
        params: this.sparam,
      }).then(response => {
        this.fields = response.data
      }).catch(error => {
        this.$handleHttpError(error)
      }).finally(() => {
        loading.close()
      })
    }, // getStructure

    showSystemList () {
      if (!this.systems) {
        this.searchSystemList(() => {
          this.systemListShow = true
        })
      } else {
        this.systemListShow = true
      }
    }, // showSystemList

    searchSystemList (cbFunc) {
      let sparam = this.$refs.sysList ? this.$refs.sysList.sparam : {}
      const loading = this.$startLoading()
      this.$http.get('/api/system', {
        params: sparam
      }).then(response => {
        this.systems = response.data
        typeof cbFunc === 'function' && cbFunc()
      }).catch(error => {
        this.$handleHttpError(error)
      }).finally(() => {
        loading.close()
      })
    }, // searchSystemList

    cbSystemRowClick (row) {
      this.sparam.systemId = row.id
      this.systemListShow = false
    }, // cbSystemRowClick
  }
}
</script>