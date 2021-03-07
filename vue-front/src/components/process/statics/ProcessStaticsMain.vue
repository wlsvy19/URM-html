<script>
import ProcessMain from '../ProcessMain'

export default {
  mixins: [ProcessMain],
  data () {
    return {
      sparam: {
        startDate: '',
        endDate: '',
      },
    }
  },
  methods: {
    search () {
      this.sparam.type = this.serverType
      this.sparam.infType = this.$route.params.type

      const loading = this.$startLoading()
      console.log('search process statics : ' + this.path, this.sparam)
      this.$http.get('/api/process/stat/' + this.path, {
        params: this.sparam,
      }).then(response => {
        this.$refs.list.items = response.data
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
  },
}
</script>