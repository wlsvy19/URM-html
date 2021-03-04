<template>
  <div class="urm-panel">
    <UserList ref="list" :items="listItem" @search="handleSearch" @edit="handleEdit"/>

    <el-dialog :visible.sync="editorShow" width="875px">
      <UserEditor :item="editorItem" @save="handleSave"/>
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
      listItem: null,
      editorShow: false,
      editorItem: null,
    }
  },
  methods: {
    handleSearch (sparam) {
      const loading = this.$startLoading()
      console.log('search : ' + this.path, sparam)
      this.$http.get('/api/' + this.path, {
        params: sparam,
      }).then(response => {
        this.listItem = response.data
      }).catch(error => {
        this.$handleHttpError(error)
      }).finally(() => {
        loading.close()
      })
    }, // handleSearch

    handleEdit (id) {
      if (id) {
        console.log('edit : ' + id)
        this.$http.get('/api/' + this.path + '/' + id, {
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
        url: '/api/' + this.path,
        data: item,
      }).then(response => {
        this.$message.success(this.$t('message.0001'))
        item.id = response.data.id
        this.updatedItem()
      }).catch(error => {
        this.$message.warning(this.$t('message.1001') + '[' + error.response.statusText + ']')
      })
    }, // handleSave

    updatedItem () {
      this.$refs.list.handleSearch(this.$refs.list.sparam)
    }, // updatedItem

    getNewItem () {
      return {
        id: '',
        authId: '0',
      }
    }, // getNewItem
  },
}
</script>