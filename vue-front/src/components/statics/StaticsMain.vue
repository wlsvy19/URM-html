<script>
export default {
  data () {
    return {
      sparam: {
        startDate: '',
        endDate: '',
      },
      listItem: null,
    }
  },
  methods: {
    search() {
      const loading = this.$startLoading()
      this.$http.get(this.pageUrl, {
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
  mounted () {
    let today = new Date()
    this.sparam.startDate = today
    this.sparam.endDate = today
  },
  computed: {
    dateRange: {
      get: function () {
        return [this.sparam.startDate, this.sparam.endDate]
      },
      set: function (nVal) {
        this.sparam.startDate = nVal[0]
        this.sparam.endDate = nVal[1]
      }
    }
  },
}
</script>