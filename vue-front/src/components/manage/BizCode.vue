<template>
  <div class="urm-panel">
    <BizCodeList :items="listItem" @search="handleSearch"/>
  </div>
</template>

<script>
import BizCodeList from './list/BizCodeList'

export default {
  components: {
    BizCodeList,
  },
  data () {
    return {
      listItem: [],
    }
  },
  methods: {
    handleSearch (sparam) {
      const loading = this.$startLoading()
      console.log('search : business code', sparam)
      this.$http.get('/api/code/business', {
        params: sparam,
      }).then(response => {
        this.listItem = response.data
      }).catch(error => {
        this.$handleHttpError(error)
      }).finally(() => {
        loading.close()
      })
    }
  },
}
</script>