<template>
  <div class="urm-pannel">
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item :label="$t('label.interfaceType')">
          <el-select v-model="sparam.type" class="search-id">
            <el-option value="" label="ALL"/>
            <el-option v-for="type in infTypes" :value="type.code" :label="type.name" :key="type.code"/>
          </el-select>
        </el-form-item>
        <el-form-item label="일자 선택">
          <el-date-picker v-model="procDate" type="daterange" value-format="yyyyMMdd" range-separator="~" style="width: 220px;"/>
        </el-form-item>
      </el-form>
      <div class="search-buttons">
        <el-button @click="search">{{$t('label.search')}}</el-button>
      </div>
    </div>
    <ProcessList ref="list" :items="listItem"/>
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
    },
    procDate: {
      get: function () {
        return [this.sparam.startDate, this.sparam.endDate]
      },
      set: function (nVal) {
        this.sparam.startDate = nVal[0]
        this.sparam.endDate = nVal[1]
      },
    },
  },
  components: {
    ProcessList,
  },
  data () {
    return {
      path: '/api/stat/process/day',
      listItem: null,
      sparam: {
        type: '',
        startDate: '',
        endDate: '',
      },
    }
  },
  methods: {
    search() {
      const loading = this.$startLoading()
      this.$http.get(this.path, {
        params: this.sparam,
      }).then(response => {
        this.listItem = response.data
      }).catch(error => {
        this.$handleHttpError(error)
      }).finally(() => {
        loading.close()
      })
    },
  }
}
</script>