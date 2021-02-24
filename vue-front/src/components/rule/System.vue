<template>
  <div class="urm-panel">
    <SystemList ref="list" :sysTypes="sysTypes" :devTypes="devTypes" @edit="handleEdit"/>
   
    <el-dialog :visible.sync="editorShow" width="1068px">
      <SystemEditor ref="editor" :item="editorItem" :sysTypes="sysTypes" :devTypes="devTypes" @save="handleSave"/>
    </el-dialog>
  </div>
</template> 

<script>
import RuleMain from './RuleMain'
import RuleUtil from '@/components/rule/RuleUtil'

export default {
  mixins: [RuleMain],
  data () {
    return {
      path: 'system',
    }
  },
  methods: {
    getNewItem () {
      return {
        id: '',
        type: '1',
        devType: '1',
        dbType: '1',
        jdbcType: '1',
      }
    }, // getNewItem
    initData (item) {
      let newItem = this.getNewItem()
      if (!item.fields) {
        item.fields = newItem.fields
      }
    }
  },
  computed: {
    sysTypes: function () {
      let kind = RuleUtil.CODEKEY.sysType
      return this.$store.state.codes.filter(code => (code.kind === kind))
    },
    devTypes: function () {
      let kind = RuleUtil.CODEKEY.devType
      return this.$store.state.codes.filter(code => (code.kind === kind))
    },
  },
}
</script>