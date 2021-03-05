<script>
import Vue from 'vue'

import DataList from './list/DataList'
import SystemList from './list/SystemList'
import RequestList from './list/RequestList'

import DataEditor from './editor/DataEditor'
import SystemEditor from './editor/SystemEditor'
import RequestEditor from './editor/RequestEditor'

Vue.component('DataList', DataList)
Vue.component('SystemList', SystemList)
Vue.component('RequestList', RequestList)

Vue.component('DataEditor', DataEditor)
Vue.component('SystemEditor', SystemEditor)
Vue.component('RequestEditor', RequestEditor)

export default {
  data () {
    return {
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
    }, // search

    handleEdit (id) {
      if (id) {
        console.log('edit : ' + id)
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
        this.$message({message: this.$t('message.0001'), type: 'success'})
        item.id = response.data.id
        this.handleSearch(this.$refs.list.sparam)
      }).catch(error => {
        this.$handleHttpError(error)
      })
    }, // handleSave
  }
}
</script>
