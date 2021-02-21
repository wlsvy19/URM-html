<template>
  <div class="urm-panel">
    <UserList ref="list" @edit="handleEdit"/>

    <el-dialog title="Editor" :visible.sync="editorShow" width="1080px">
      <UserEditor ref="editor" :item="editorItem" @save="handleSave"/>
    </el-dialog>
  </div>
</template>

<script>
import UserList from './list/UserList'
import UserEditor from './editor/UserEditor'

export default {
  components: {
    UserList,
    UserEditor,
  },
  data () {
    return {
      path: 'user',
    }
  },
  methods: {
    handleEdit (id) {
      console.log('edit', id)
      if (id) {
        this.$http.get('/api/' + this.path + '/' + id, {
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
        url: '/api/' + this.path,
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