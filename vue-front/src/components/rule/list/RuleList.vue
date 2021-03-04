<script>
import RuleUtil from '@/components/rule/RuleUtil'

export default {
  props: {
    onlySearch: {
      type: Boolean,
      default: false,
    },
    items: {
      type: Array,
      default: function () {
        return []
      },
    },
  },
  data () {
    return {
      listHeight: 'calc(100vh - 165px)',
      sparam: {
        id: '',
        name: '',
      },
    }
  },

  methods: {
    search () {
      this.$emit('search', this.sparam)
    }, // handleSearch

    clickEdit (id) {
      this.$emit('edit', id)
    }, // clickEdit

    clickDelete (key) {
      let ids = []
      if (key === 'selected') {
        ids = this.$refs.table.selection.map((row) => row.id)
        if (ids.length <= 0) {
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
      this.$confirm('정말 삭제 하시겠습니까?', confirmProp).then(() => {
        const loading = this.$startLoading()
        this.$http({
          method : 'POST',
          url: '/api/' + this.path + '/delete',
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
      }).catch(() => {})
    }, // clickDelete

    handleRowDblclick (row) {
      this.$emit('row-dblclick', row)
    }, // handleRowDblclick

    getTypeStr (key, val) {
      let kind = RuleUtil.CODEKEY[key]
      let codes = this.$store.state.codes
      let obj = {}
      codes.some((code) => {
        if (code.kind === kind && code.code === val) {
          obj = code
          return true
        }
      })
      return obj.name
    }, // getTypeStr

    getDateStr(val, format) {
      return this.$convertDateFormat(format, new Date(val))
    }, // getDateStr
  }, // methods

  mounted () {
    if (!this.items) {
      this.search()
    }
  },
}
</script>