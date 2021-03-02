<template>
  <div class="urm-panel">
    <BizList :items="listItem" @search="handleSearch"/>
  </div>
</template>

<script>
import BizList from './list/BizCodeList'

export default {
  components: {
    BizList,
  },
  data () {
    return {
      listItem: [],
    }
  },
  methods: {
    handleSearch (sparam) {
      const loading = this.$startLoading()
      console.log('search : bisuness code', sparam)
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