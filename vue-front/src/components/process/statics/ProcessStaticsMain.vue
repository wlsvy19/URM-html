<script>
export default {
  data () {
    return {
      sparam: {
        startDate: '',
        endDate: '',
      },
      listItems: {},
    }
  },
  methods: {
    search () {
      this.sparam.type = this.$route.params.server
      this.sparam.infType = this.$route.params.type

      const loading = this.$startLoading()
      console.log('search process statics : ' + this.path, this.sparam)
      this.$http.get('/api/process/stat/' + this.path, {
        params: this.sparam,
      }).then(response => {
        if (this.listItem) {
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
      return item ? item : null
    },
  },
}
</script>