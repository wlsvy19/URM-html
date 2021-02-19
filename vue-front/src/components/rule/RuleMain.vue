<script>
import Vue from 'vue'

import DataList from './list/DataList'
import SystemList from './list/SystemList'
import RequestList from './list/RequestList'


import DataEditor from './editor/DataEditor'

Vue.component('DataList', DataList)
Vue.component('SystemList', SystemList)
Vue.component('RequestList', RequestList)


Vue.component('DataEditor', DataEditor)

export default {
  data () {
    return {
      editorShow: false,
      editorItem: null,
    }
  },
  methods: {
    handleEdit (id) {
      console.log('edit', id)
      if (id) {
        this.$http.get('/api/' + this.path + '/' + id, {
        }).then(response => {
          this.editorItem = response.data
          this.initData(this.editorItem)
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
        let data = response.data
        this.$message({message: this.$t('message.0001'), type: 'success'})
        item.id = data.id
        this.updatedItem(item)
      }).catch(error => {
        this.$handleHttpError(error)
      })
    }, // handleSave

    updatedItem () {
      this.$refs.list.search()
    }, // updatedItem
  }
}
</script>
