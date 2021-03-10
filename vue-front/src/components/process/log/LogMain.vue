<script>
export default {
  data () {
    return {
      sparam: {
        processDate: '',
        startTime: '0000',
        endTime: '2359',
      },
      listItems: {},
    }
  },
  methods: {
    search () {
      this.sparam.type = this.$route.params.server

      const loading = this.$startLoading()
      console.log('search log : ' + this.path, this.sparam)
      this.$http.get('/api/process/log/' + this.path, {
        params: this.sparam,
      }).then(response => {
        let item = this.listItems[this.$route.path]
        if (item) {
          this.listItems[this.$route.path] = response.data
        } else {
          this.$set(this.listItems, this.$route.path, response.data)
        }
      }).catch(error => {
        this.$handleHttpError(error)
      }).finally(() => {
        loading.close()
      })
    }, // search
  },
  computed: {
    searchBarStyle: function () {
      let type = this.$route.params.server
      if (type === 'dev') {
        return 'background-color: #8888FF;'
      } else if (type === 'test') {
        return 'background-color: #88FF88;'
      } else if (type === 'pro') {
        return 'background-color: #FF8888;'
      }
      return 'background-color: #E6F7FF;'
    },
    listItem: function () {
      let item = this.listItems[this.$route.path]
      return item ? item : []
    },
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
.search-bar .el-date-editor--date {
  width: 120px;
}
.search-bar .el-date-editor--timerange {
  width: 140px;
}
</style>