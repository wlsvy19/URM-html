<template>
  <div class="urm-panel">
    <DataList ref="list" :items="listItem" @search="handleSearch" @edit="handleEdit"/>

    <el-dialog :visible.sync="editorShow" width="1080px">
      <DataEditor :item="editorItem" @save="handleSave"/>
    </el-dialog>
  </div>
</template>

<script>
import RuleMain from './RuleMain'

export default {
  mixins: [RuleMain],
  data () {
    return {
      path: 'data',
    }
  },
  methods: {
    getNewItem () {
      return {
        id: '',
        fields: [],
      }
    }, // getNewItem
    initData (item) {
      let newItem = this.getNewItem()
      if (!item.fields) {
        item.fields = newItem.fields
      }
    }, // initData
  },
}
</script>