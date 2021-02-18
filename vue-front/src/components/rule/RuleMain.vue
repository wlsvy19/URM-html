<script>
import Vue from 'vue'

import DataList from './list/DataList'
import SystemList from './list/SystemList'

Vue.component('DataList', DataList)
Vue.component('SystemList', SystemList)

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
