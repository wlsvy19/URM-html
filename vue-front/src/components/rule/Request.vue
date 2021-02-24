<template>
  <div class="urm-panel">
    <RequestList ref="list" @edit="handleEdit"/>

    <el-dialog title="Editor" :visible.sync="editorShow" width="1080px">
      <RequestEditor ref="editor" :item="editorItem" @save="handleSave"/>
    </el-dialog>
  </div>
</template> 

<script>
import RuleMain from './RuleMain'

export default {
  mixins: [RuleMain],
  data () {
    return {
      path: 'request',
    }
  },
  methods: {
    getNewItem () {
      return {
        id: '',
        name: '',
        fields: [],
      }
    }, // getNewItem
    initData (item) {
      let newItem = this.getNewItem()
      if (!item.fields) {
        item.fields = newItem.fields
      }
    }
  },
}
</script>