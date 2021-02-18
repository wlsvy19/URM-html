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
      items: [],
    }
  },

  methods: {
    search () {
      //const loading = this.$startLoading()
      console.log('search : ' + this.path, this.sparam)
      this.items = [{
        id: 'test',
        name: 'test',
      }]
      /*this.$http.get(this.pageUrl, {
        params: this.sparam,
      }).then(response => {
        this.pageList = response.data
      }).catch(error => {
        this.$handleHttpError(error)
      }).finally(() => {
        loading.close()
      })*/
    },

    clickEdit (id) {
      this.$emit('edit', id)
    },

    clickDelete (id) {
      console.log('delete', id)
    },

    getTypeStr (key, val) {
      console.log(key, val)
      return val
    }
  }, // methods

  mounted () {
    this.search()
  },
}
</script>