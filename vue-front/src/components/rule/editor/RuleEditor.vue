<script>
export default {
  props: {
    item: {
      type: Object,
      default: function () {
        return {}
      },
    }
  },
  methods: {
    clickSave () {
      this.$emit('save', this.item)
    }, // clickSave

    validator () {
      let item = this.item
      if (!item.name || item.name.trim().length === 0) {
        this.$message({type: 'error', message: this.$t('message.1003')})
        return false
      }
      if (this.customValidator && !this.customValidator()) {
        return false
      }
      return true
    }, // validator
  }, // methods
}
</script>