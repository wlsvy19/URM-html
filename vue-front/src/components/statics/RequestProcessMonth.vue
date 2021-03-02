<template>
  <div class="urm-pannel">
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item :label="$t('label.interfaceType')">
          <el-select v-model="sparam.interfaceType" class="search-id">
            <el-option value="" label="ALL"/>
            <el-option v-for="type in infTypes" :value="type.code" :label="type.name" :key="type.code"/>
          </el-select>
        </el-form-item>
        <el-form-item label="일자 선택">
          <el-date-picker v-model="sparam.chgDate" type="monthrange" range-separator="~" start-placeholder="Start month" end-placeholder="End month" style="width: 220px"/>
        </el-form-item>
      </el-form>
      <div class="search-buttons">
        <el-button @click="search">{{$t('label.search')}}</el-button>
      </div>
    </div>
    <ProcessList ref="list"/>
  </div>
</template>



<script>
import ProcessList from './list/RequestProcessList'
import RuleUtil from '@/components/rule/RuleUtil'

export default {
  computed: {
    infTypes: function () {
      let kind = RuleUtil.CODEKEY.infType
      return this.$store.state.codes.filter(code => (code.kind === kind))
    }
  },
  data () {
    return {
      path: '/api/stat/process/month',
      sparam: {
        ...this.sparam,
        type: '',
        chgDate: [],
      },
    }
  },
  components: {
    ProcessList,
  },
  methods: {
    search() {
      const loading = this.$startLoading()
      console.log(loading)
  
      console.log('search : ' + this.path, this.sparam)
      this.$http.get(this.path, {
        params: this.sparam,
      }).then(response => {
        console.log('response.data:' + response.data)
      }).catch(error => {
        this.$handleHttpError('에러로그'+error)
      }).finally(() => {
        loading.close()
      })
    }
  }
}
</script>