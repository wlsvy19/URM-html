<template>
  <div class="urm-panel">
    <RequestList ref="list" :infTypes="infTypes" :procStats="procStats" :chgStats="chgStats"
      @edit="handleEdit"/>

    <el-dialog :visible.sync="editorShow" width="1355px">
      <RequestEditor ref="editor" :item="editorItem"
        :infTypes="infTypes" :procStats="procStats" :chgStats="chgStats"
        @save="handleSave"/>
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
      path: 'request',
    }
  },
  methods: {
    getNewItem () {
      return {
        id: '',
        interfaceType: '1',
        processStat: '1',
        chgStat: '1',
        syncType: '1',
        trType: '2',
        sendSystemType: '3',
        rcvSystemType: '3',
        sqlPlain: '',
        dbCrudType: '1',
        fileCrudType: '1',
        infFileName: '',

        sendSystem: {
          name: '',
        },
        rcvSystem: {
          name: '',
        },
        sendJobCode: {
          part2Id: '',
          part2Name: '',
        },
        rcvJobCode: {
          part2Id: '',
          part2Name: '',
        },
        sendAdmin: {
          name: '',
          positionName: '--',
        },
        rcvAdmin: {
          name: '',
          positionName: '--',
        },
      }
    }, // getNewItem
    initData (item) {
      let newItem = this.getNewItem()
      if (!item.sendSystem) {
        item.sendSystem = newItem.sendSystem
      }
      if (!item.rcvSystem) {
        item.rcvSystem = newItem.rcvSystem
      }
      if (!item.sendJobCode) {
        item.sendJobCode = newItem.sendJobCode
      }
      if (!item.rcvJobCode) {
        item.rcvJobCode = newItem.rcvJobCode
      }
      if (!item.sendAdmin) {
        item.sendAdmin = newItem.sendAdmin
      }
      if (!item.rcvAdmin) {
        item.rcvAdmin = newItem.rcvAdmin
      }
    }, // initData
  },
  computed: {
    infTypes: function () {
      let kind = RuleUtil.CODEKEY.infType
      return this.$store.state.codes.filter(code => (code.kind === kind))
    },
    chgStats: function () {
      let kind = RuleUtil.CODEKEY.chgStat
      return this.$store.state.codes.filter(code => (code.kind === kind))
    },
    procStats: function () {
      let kind = RuleUtil.CODEKEY.procStat
      return this.$store.state.codes.filter(code => (code.kind === kind))
    },
  },
}
</script>