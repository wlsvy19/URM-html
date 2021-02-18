<script>
export default {
  prop: {
    onlySearch: {
      type: Boolean,
      default: false,
    },
  },
  data () {
    return {
      sparam: {
        page: 1,
        size: 30,
        id: '',
        name: '',
      }
    }
  }
}
</script>