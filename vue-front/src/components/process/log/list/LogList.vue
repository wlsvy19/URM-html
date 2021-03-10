<script>
export default {
  data () {
    return {
      details: []
    }
  },
  methods: {
    handleRowDblclick (row) {
      this.batchId = row.batchId
      let sparam = {
        type: this.$route.params.server,
        batchId: row.batchId,
      }

      // get details
      const loading = this.$startLoading()
      console.log('get ' + this.path + ' details', sparam)
      this.$http.get('/api/process/log/' + this.path + '/detail', {
        params: sparam,
      }).then(response => {
        this.details = response.data
      }).catch(error => {
        this.$handleHttpError(error)
      }).finally(() => {
        loading.close()
      })
    }, // handleRowDblclick
  }
}
</script>