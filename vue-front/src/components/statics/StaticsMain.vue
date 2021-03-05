<script>
import Vue from 'vue'

import RequestChangeList from './list/RequestChangeList'
import RequestProcessList from './list/RequestProcessList'

Vue.component('RequestChangeList', RequestChangeList)
Vue.component('RequestProcessList', RequestProcessList)

import RuleUtil from '@/components/rule/RuleUtil'

export default {
  data () {
    return {
      sparam: {
        startDate: '',
        endDate: '',
      },
      listItem: [],
    }
  },
  methods: {
    search () {
      const loading = this.$startLoading()
      this.$http.get('/api/stat' + this.pageUrl, {
        params: this.sparam,
      }).then(response => {
        this.listItem = response.data
      }).catch(error => {
        this.$handleHttpError(error)
      }).finally(() => {
        loading.close()
      })
    }, // search
  },
  computed: {
    dateRange: {
      get: function () {
        return [this.sparam.startDate, this.sparam.endDate]
      },
      set: function (nVal) {
        this.sparam.startDate = nVal[0]
        this.sparam.endDate = nVal[1]
      },
    },
    infTypes: function () {
      let kind = RuleUtil.CODEKEY.infType
      return this.$store.state.codes.filter(code => (code.kind === kind))
    },
  },
}
</script>
<style scoped>
.search-bar .el-range-editor {
  width: 220px;
}
</style>