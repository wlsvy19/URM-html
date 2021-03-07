<script>
import ProcessMain from '../ProcessMain'

export default {
  mixins: [ProcessMain],
  data () {
    return {
      sparam: {
        processDate: '',
        startTime: '0000',
        endTime: '2359',
      },
    }
  },
  methods: {
    search () {
      this.sparam.type = this.serverType

      const loading = this.$startLoading()
      console.log('search log : ' + this.path, this.sparam)
      this.$http.get('/api/process/log/' + this.path, {
        params: this.sparam,
      }).then(response => {
        this.$refs.list.items = response.data
      }).catch(error => {
        this.$handleHttpError(error)
      }).finally(() => {
        loading.close()
      })
    }, // search

    getDetails (sparam) {
      sparam.type = this.serverType

      const loading = this.$startLoading()
      console.log('get ' + this.path + ' details', sparam)
      this.$http.get('/api/process/log/' + this.path + '/detail', {
        params: sparam,
      }).then(response => {
        this.$refs.details = response.data
      }).catch(error => {
        this.$handleHttpError(error)
      }).finally(() => {
        loading.close()
      })
    }, // getDetails
  },
  computed: {
    timeRange: {
      get: function () {
        return [this.sparam.startTime, this.sparam.endTime]
      },
      set: function (nVal) {
        this.sparam.startTime = nVal[0]
        this.sparam.endTime = nVal[1]
      },
    },
  },
}
</script>
<style scoped>
.search-bar .el-date-editor--date, .search-bar .el-date-editor--timerange {
  width: 140px;
}
</style>