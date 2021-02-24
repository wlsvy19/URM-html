<template>
  <div class="urm-panel">
    <UserList ref="list" @edit="handleEdit"/>

    <el-dialog :visible.sync="editorShow" width="875px">
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
      editorShow: false,
      editorItem: null,
    }
  },
  methods: {
    getNewItem () {
      return {
        id: '',
        authId: '0',
      }
    }, // getNewItem

    handleEdit (id) {
      console.log('edit', id)
      if (id) {
        this.$http.get('/api/user/' + id, {
        }).then(response => {
          this.editorItem = response.data
          this.editorItem.password = ''
          this.editorShow = true
        })
      } else {
        this.editorItem = this.getNewItem()
        this.editorShow = true
      }
    }, // handleEdit

    handleSave (item) {
      console.log('save', item)
      this.$http({
        method : 'POST',
        url: '/api/user',
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