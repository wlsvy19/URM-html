<script>
import Vue from 'vue'

import LogBatchList from './log/list/LogBatchList'
import LogDeferredList from './log/list/LogDeferredList'
import LogRealtimeList from './log/list/LogRealtimeList'

import StaticsDayList from './statics/list/StaticsDayList'
import StaticsHourList from './statics/list/StaticsHourList'

Vue.component('LogBatchList', LogBatchList)
Vue.component('LogDeferredList', LogDeferredList)
Vue.component('LogRealtimeList', LogRealtimeList)

Vue.component('StaticsDayList', StaticsDayList)
Vue.component('StaticsHourList', StaticsHourList)

export default {
  data () {
    return {
      sparam: {
        startDate: '',
        endDate: '',
        type: '',
        interfaceId: '',
      },
      listItem: [],
    }
  },
  methods: {
    search () {
      const loading = this.$startLoading()
      this.$http.get('/api/process' + this.pageUrl, {
        params: this.sparam,
      }).then(response => {
        this.listItem = response.data
      }).catch(error => {
        this.$handleHttpError(error)
      }).finally(() => {
        loading.close()
      })
    }, // search

    searchBarStyle () {
      let type = this.$route.params.server
      if (type === 'dev') {
        return 'background-color: #8888FF;'
      } else if (type === 'test') {
        return 'background-color: #88FF88;'
      } else if (type === 'prod') {
        return 'background-color: #FF8888;'
      }
      return 'background-color: #E6F7FF;'
    }, // searchBarStyle
  },
  mounted () {
    this.sparam.type = this.serverType
  },
  computed: {
    pageTitle: function () {
      let type = this.$route.params.type
      if (type === 'realtime') {
        return '온라인'
      } else if (type === 'batch') {
        return '배치'
      } else if (type === 'deferred') {
        return '디퍼드'
      }
      return ''
    },
    serverType: function () {
      let type = this.$route.params.server
      if (type === 'dev') {
        return 1
      } else if (type === 'test') {
        return 2
      }
      return 3
    },
    dateRange: {
      get: function () {
        return [this.sparam.startDate, this.sparam.endDate]
      },
      set: function (nVal) {
        this.sparam.startDate = nVal[0]
        this.sparam.endDate = nVal[1]
      },
    },
  },
}
</script>
<style scoped>
.search-bar .el-range-editor {
  width: 220px;
}
</style>