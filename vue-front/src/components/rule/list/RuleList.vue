<script>
import RuleUtil from '@/components/rule/RuleUtil'

export default {
  props: {
    onlySearch: {
      type: Boolean,
      default: false,
    },
  },
  data () {
    return {
      listHeight: 'calc(100vh - 270px)',
      sparam: {
        id: '',
        name: '',
      },
      items: [],
    }
  },

  methods: {
    search () {
      const loading = this.$startLoading()
      console.log('search : ' + this.path, this.sparam)
      this.$http.get('/api/' + this.path, {
        params: this.sparam,
      }).then(response => {
        this.items = response.data
      }).catch(error => {
        this.$handleHttpError(error)
      }).finally(() => {
        loading.close()
      })
    },

    clickEdit (id) {
      this.$emit('edit', id)
    },

    clickDelete (key) {
      let ids = []
      if (key === 'selected') {
        let checkedList = this.$refs.fieldTable.selection
        if (checkedList.length <= 0) {
          this.$message({message: this.$t('message.1004'), type: 'warning'})
          return
        }
      } else {
        ids.push(key)
      }
      let confirmProp = {
        confirmButtonText: 'YES',
        cancelButtonText: 'NO',
        closeOnClickModal: false,
        dangerouslyUseHTMLString: false,
        type: 'error',
      }
      this.$confirm('정말 삭제 하시겠습니까?', 'Remove', confirmProp).then(() => {
        const loading = this.$startLoading()
        let url = '/api/' + this.path + '/delete'
        this.$http({
          method : 'POST',
          url: url,
          data: ids,
        }).then(response => {
          let res = response.data
          if (res.code === 0) {
            this.$message({message: 'delete ' + this.path.toUpperCase() + ' [ ' + res.obj + ' ]', type: 'success'})
            this.search()
          } else if (res.code === 1) {
            this.$message({message: '삭제 실패하였습니다. - ' + res.message + ' \n 영향도를 확인하여 주세요.', type: 'warning'})
          } else {
            this.$message({message: res.message, type: 'warning'})
          }
        }).catch(error => {
          this.$handleHttpError(error)
        }).finally(() => {
          loading.close()
        })
      }).catch(() => {
        this.$message({type: 'info', message: 'Delete canceled'})
      })
    },

    getTypeStr (key, val) {
      let kind = RuleUtil.CODEKEY[key]
      let codes = this.$store.state.codes
      let obj = {}
      codes.some((code) => {
        if (code.kind === kind && code.code === val) {
          obj = code
          return code
        }
      })
      return obj.name
    }
  }, // methods

  mounted () {
    this.search()
  },
}
</script>