<script>
export default {
  props: {
    onlySearch: {
      type: Boolean,
      default: false,
    },
  },
  data () {
    return {
      sparam: {
        id: '',
        name: '',
      },
      pageList: {},
    }
  },

  methods: {
    search () {
      /*const loading = this.$startLoading()
      console.log('search : ' + this.pageUrl, this.sparam)
      this.$http.get(this.pageUrl, {
        params: this.sparam,
      }).then(response => {
        this.pageList = response.data
      }).catch(error => {
        this.$handleHttpError(error)
      }).finally(() => {
        loading.close()
      })*/
    },

    clickEdit () {
      console.log('edit')
    },

    clickDelete () {
      console.log('delete')
    },
  }, // methods

  mounted () {
    this.search()
  },
}
</script>