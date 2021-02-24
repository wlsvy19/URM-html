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
      if (!this.validator()) {
        return
      }
      this.$emit('save', this.item)
    }, // clickSave

    validator () {
      let item = this.item
      if (!item.name || item.name.trim().length === 0) {
        this.$message({message: this.$t('message.1003'), type: 'warning'})
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