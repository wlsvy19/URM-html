<script>
import Vue from 'vue'

import DataList from './list/DataList'
import SystemList from './list/SystemList'
import RequestList from './list/RequestList'


Vue.component('DataList', DataList)
Vue.component('SystemList', SystemList)
Vue.component('RequestList', RequestList)


export default {
  methods: {
    handleEdit (id) {
      console.log('edit', id)
    }, // handleEdit

    handleSave (item) {
      console.log('save', item)
    }, // handleSave
  }
}
</script>
