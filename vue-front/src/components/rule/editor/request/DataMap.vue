<template>
  <div class="urm-editor">
    <div class="editor-buttons">
      <el-button @click="clickSave" plain>{{$t('label.save')}}</el-button>
    </div>
    <el-form label-width="135px" :inline="true">
      <el-form-item :label="$t('label.dataMapId')">
        <el-input v-model="item.id" class="size-id" readonly/>
      </el-form-item>
      <el-form-item :label="$t('label.dataMapName')">
        <el-input v-model="item.name" class="size-name"/>
      </el-form-item>
    </el-form>
    
    <hr/>
    <div>
      <div class="map-header">
        <div>
          <el-form-item :label="$t('label.outData')">
            <el-input v-model="item.sourceDataId" class="size-id" readonly>
              <i slot="suffix" class="el-input__icon el-icon-search pointer" @click.stop="showDataList('src')"/>
            </el-input>
          </el-form-item>
        </div>
        <div>
          <el-button icon="el-icon-link"/>
          <el-button icon="el-icon-circle-close" @click="clearMapping"/>
        </div>
        <div>
          <el-form-item :label="$t('label.inData')">
            <el-input v-model="item.targetDataId" class="size-id" readonly>
              <i slot="suffix" class="el-input__icon el-icon-search pointer" @click.stop="showDataList('tgt')"/>
            </el-input>
          </el-form-item>
        </div>
      </div>
      <div class="map-tree">
        <el-tree ref="srcDataTree" class="src-data-view" :data="srcTreeData" :props="treeProps" node-key="id" :render-content="renderData"
          @scroll.native="throttleUpdateMapping"
          :render-after-expand="false" :expand-on-click-node="false" default-expand-all :indent="12"/><!--
     --><div ref="mapCanvas" class="mapping-canvas" @keyup.delete="deleteLine" tabindex="999"/><!-- 
     --><el-tree ref="tgtDataTree" class="tgt-data-view" :data="tgtTreeData" :props="treeProps" node-key="id" :render-content="renderData"
          @scroll.native="throttleUpdateMapping" @node-click="handleNodeClick" 
          :render-after-expand="false" :expand-on-click-node="false" default-expand-all :indent="12"/>
      </div>
    </div>

    <el-dialog :visible.sync="dataListShow" width="980px" top="5vh" append-to-body :close-on-click-modal="false">
      <DataList ref="dataList" :items="datas"
        @search="searchDataList" @edit="editData" @row-dblclick="cbDataRowClick"/>
    </el-dialog>

    <el-dialog :visible.sync="dataEditorShow" width="1080px" top="8vh" append-to-body :close-on-click-modal="false">
      <DataEditor :item="data" @save="saveData"/>
    </el-dialog>
  </div>
</template>
<script>
import RuleEditor from '@/component/rule/editor/RuleEditor'

import DataEditor from '@/component/rule/list/DataEditor'
import DataList from '@/component/rule/list/DataList'

export default {
  mixins: [RuleEditor],
  components: {
    DataList,
    DataEditor,
  },

  data () {
    return {
      tgtType: '',
      dataListShow: false,
      datas: null,
      dataEditorShow: false,
      data: null,
      treeProps: {
        children: 'children',
        label: 'label',
      },
    }
  },

  methods: {
    showDataList (type) {
      this.tgtType = type
      if (!this.datas) {
        this.searchDataList(() => {
          this.dataListShow = true
        })
      } else {
        this.dataListShow = true
      }
    }, // showDataList

    searchDataList (cbFunc) {
      let sparam = this.$refs.dataList ? this.$refs.dataList.sparam : {}
      const loading = this.$startLoading()
      this.$http.get('/api/data', {
        params: sparam,
      }).then(response => {
        this.datas = response.data
        typeof cbFunc === 'function' && cbFunc()
      }).catch(error => {
        this.$handleHttpError(error)
      }).finally(() => {
        loading.close()
      })
    }, // searchDataList

    cbDataRowClick (row) {
      if (this.tgtType === 'src') {
        this.item.sourceDataId = row.id
        this.item.sourceData = row
      } else if (this.tgtType === 'tgt') {
        this.item.targetDataId = row.id
        this.item.targetData = row
      }
      this.dataListShow = false
    }, // cbDataRowClick

    editData (id) {
      let newItem = { id: '', fields: [] }
      if (id) {
        console.log('edit data : ' + id)
        this.$http.get('/api/data/' + id, {
        }).then(response => {
          this.data = response.data
          if (!this.data.fields) {
            this.data.fields = newItem.fields
          }
          this.dataEditorShow = true
        })
      } else {
        this.data = newItem
        this.dataEditorShow = true
      }
    }, // editData

    saveData (data) {
      console.log('save data', data)
      this.$http({
        method : 'POST',
        url: '/api/data',
        data: data,
      }).then(response => {
        this.$message({message: this.$t('message.0001'), type: 'success'})
        data.id = response.data.id
        this.searchDataList()
      }).catch(error => {
        this.$handleHttpError(error)
      })
    }, // saveData

    convertTreeData (data, type) {
      let rootData = {type: 'data', id: type, label: src.id}
      let treeData = []
      if (data && data.length > 0) {
        data.fields.forEach((f, idx) => {
          let fLabel = '<div>' + f.engName + '</div><div>' + f.name + '</div>'
          let tId = type + '_' + idx
          let child = {type: 'field', id: tId, label: fLabel, children: []}
          treeData.push(child)
        })
      }
      rootData.children = treeData
      return rootData
    }, // convertTreeData

    clearMapping () {
      this.item.mapLines = []
    }, // clearMapping

    updateMappingLines () {

    }, // updateMappingLines

    throttleUpdateMapping: _.throttle( function () {
      this.updateMappingLines()
    }, 100), // handleTreeScroll

    customValidator () {
      let item = this.item
      if (!item.sourceDataId || item.sourceDataId.length === 0) {
        this.$message({type: 'error', message: 'please add fields'})
        return false
      }
      if (!item.targetDataId || item.targetDataId.length === 0) {
        this.$message({type: 'error', message: 'please add fields'})
        return false
      }
      return true
    }, // customValidator
  }, // methods

  computed: {
    srcTreeData: function () {
      return this.convertTreeData(this.item.sourceData, 's')
    },
    tgtTreeData: function () {
      return this.convertTreeData(this.item.targetData, 't')
    },
  }
}
</script>
<style scoped>
div.map-header {
  display: flex;
}
div.map-header .el-button--small {
  margin: 0;
}
</style>