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
  methods: {
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
  computed: {
    serverType: function () {
      let type = this.$route.params.server
      if (type === 'dev') {
        return 1
      } else if (type === 'test') {
        return 2
      }
      return 3
    },
  },
}
</script>