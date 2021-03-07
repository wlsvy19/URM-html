<template>
  <div class="urm-panel">
    <DataList ref="list" :items="listItem"
      @search="handleSearch" @edit="handleEdit" @show-used="handleShowUsed"/>

    <el-dialog :visible.sync="editorShow" width="1080px">
      <DataEditor :item="editorItem" @save="handleSave" @show-used="handleShowUsed"/>
    </el-dialog>

    <el-dialog :visible.sync="usedListShow" width="900px" top="8vh"
      append-to-body :close-on-click-modal="false" :destroy-on-close="true">
      <UsedData :data="usedList" :byEditor="byEditor" @confirm="handleConfirm"/>
    </el-dialog>
  </div>
</template>

<script>
import RuleMain from './RuleMain'

import UsedData from './list/data/UsedData'

export default {
  mixins: [RuleMain],
  components: {
    UsedData,
  },
  data () {
    return {
      path: 'data',
      usedListShow: false,
      usedList: null,
      byEditor: false,
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

    handleShowUsed (id, byEditor) {
      this.byEditor = byEditor
      const loading = this.$startLoading()
      this.$http.get('/api/data/used', {
        params: {id: id},
      }).then(response => {
        if (byEditor) {
          let list = response.data
          if (list && list.length > 0) {
            this.usedList = list
            this.usedListShow = true
          } else {
            this.handleSave(this.editorItem)
          }
        } else {
          this.usedList = response.data
          this.usedListShow = true
        }
      }).catch(error => {
        this.$handleHttpError(error)
      }).finally(() => {
        loading.close()
      })
    }, // clickUsed

    handleConfirm () {
      if (this.byEditor) {
        this.editorItem.channelCode = 'EAI'
        this.handleSave(this.editorItem)
      }
      this.usedListShow = false
    }, // handleConfirm
  },
}
</script>