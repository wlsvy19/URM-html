<template>
  <div class="urm-panel">
    <BizList ref="list" @edit="handleEdit"/>
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
    }
  },
  methods: {
    handleEdit (id) {
      console.log('edit', id)
      if (id) {
        this.$http.get('/api/code/business/' + id, {
        }).then(response => {
          this.editorItem = response.data
          this.editorShow = true
        })
      } else {
        this.editorItem = {}
        this.editorShow = true
      }
    }, // handleEdit

    handleSave (item) {
      console.log('save', item)
      this.$http({
        method : 'POST',
        url: '/api/code/business',
        data: item,
      }).then(response => {
        let data = response.data
        this.$message({message: this.$t('message.0001'), type: 'success'})
        item.id = data.id
        this.updatedItem(item)
      }).catch(error => {
        this.$message({
          message: this.$t('message.1001') + '[' + error.response.statusText + ']',
          type: 'warning',
        })
      })
    }, // handleSave

    updatedItem () {
      this.$refs.list.search()
    }, // updatedItem
  },
}
</script>