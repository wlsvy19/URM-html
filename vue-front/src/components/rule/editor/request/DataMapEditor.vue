<template>
  <div class="urm-editor">
    <div class="editor-buttons">
      <el-button @click="clickSave" plain>{{$t('label.save')}}</el-button>
    </div>
    <el-form label-width="135px" :inline="true">
      <el-form-item :label="$t('label.dataMapId')">
        <el-input v-model="item.id" class="size-id" disabled/>
      </el-form-item>
      <el-form-item :label="$t('label.dataMapName')">
        <el-input v-model="item.name" class="size-name"/>
      </el-form-item>
    </el-form>
    
    <hr/>
    <div class="map-view">
      <div class="map-header">
        <div class="el-form-item el-form-item--small src">
          <label class="el-form-item__label">{{$t('label.outData')}}</label>
          <el-input v-model="item.sourceDataId" class="size-id" @click.native.prevent="showDataEditor('src')" readonly>
            <el-button slot="append" icon="el-icon-search" @click.stop="showDataList('src')"/>
          </el-input>
        </div>
        <div class="mapping-buttons">
          <el-button icon="el-icon-circle-close" @click="clearMapping"/>
          <el-button icon="el-icon-link"/>
        </div>
        <div class="el-form-item el-form-item--small tgt">
          <label class="el-form-item__label">{{$t('label.inData')}}</label>
          <el-input v-model="item.targetDataId" class="size-id" @click.native.prevent="showDataEditor('tgt')" readonly>
            <el-button slot="append" icon="el-icon-search" @click.stop="showDataList('tgt')"/>
          </el-input>
        </div>
      </div>
      <div class="map-tree">
        <div class="src">
          <div class="map-tree-header">
            <div style="width: 160px;">{{$t('label.map.item1')}}</div>
            <div style="width: 155px;">{{$t('label.map.item2')}}</div>
          </div>
          <el-tree ref="srcDataTree" class="src-data-view" :data="srcTreeData" :props="treeProps" node-key="id" :render-content="renderData"
            @scroll.native="throttleUpdateMapping"
            :render-after-expand="false" :expand-on-click-node="false" default-expand-all :indent="12"/>
        </div><!--
     --><div ref="mapCanvas" class="mapping-canvas" @keyup.delete="deleteLine" tabindex="999"/><!-- 
     --><div class="tgt">
          <div class="map-tree-header">
            <div style="width: 160px;">{{$t('label.map.item1')}}</div>
            <div style="width: 160px;">{{$t('label.map.item2')}}</div>
            <div style="width: 165px;">{{$t('label.map.defaultVal')}}</div>
          </div>
          <el-tree ref="tgtDataTree" class="tgt-data-view" :data="tgtTreeData" :props="treeProps" node-key="id" :render-content="renderData"
            @scroll.native="throttleUpdateMapping" @node-click="handleNodeClick"
            :render-after-expand="false" :expand-on-click-node="false" default-expand-all :indent="12"/>
        </div>
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
import RuleEditor from '@/components/rule/editor/RuleEditor'

import DataEditor from '@/components/rule/editor/DataEditor'
import DataList from '@/components/rule/list/DataList'

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
      this.$http.get('/api/data/' + row.id, {
      }).then(response => {
        let data = response.data
        if (this.tgtType === 'src') {
          this.item.sourceDataId = data.id
          this.item.sourceData = data
        } else if (this.tgtType === 'tgt') {
          this.item.targetDataId = data.id
          this.item.targetData = data
        }
        this.dataListShow = false
      })
    }, // cbDataRowClick

    showDataEditor (type) {
      this.tgtType = type
      if (type === 'src') {
        this.editData(this.item.sourceDataId)
      } else if (type === 'tgt') {
        this.editData(this.item.targetDataId)
      }
    },

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
        this.updateItem(data)
      }).catch(error => {
        this.$handleHttpError(error)
      })
    }, // saveData

    updateItem (item) {
      let org = this.item
      let tgt = this.tgtType
      if (tgt === 'src' && (!org.sourceDataId || org.sourceDataId.length === 0)) {
        org.sourceDataId = item.id
      } else if (tgt === 'tgt' && (!org.targetDataId || org.targetDataId.length === 0)) {
        org.targetDataId = item.id
      }
      this.searchDataList()
    }, // updateItem

    convertTreeData (data, type) {
      let tree = []
      if (data && data.id) {
        let rootData = {type: 'data', id: type, label: data.id}
        if (data.fields && data.fields.length > 0) {
          let treeData = []
          data.fields.forEach((f, idx) => {
            let tId = type + '_' + idx
            let child = {type: 'field', id: tId, label: f.engName, name: f.name, fId: f.fieldId, children: []}
            treeData.push(child)
          })
          rootData.children = treeData
        }
        tree.push(rootData)
      }
      return tree
    }, // convertTreeData

    renderData (h, tree) {
      let data = tree.data
      let icon = ''
      if (data.type === 'field') {
        icon = 'el-icon-tickets'
      } else {
        icon = 'el-icon-folder'
      }
      const iEl = (<i style="line-height: 18px; width: 18px;" class={icon}/>)
      if (data.type === 'field') {
        if (data.id.startsWith('s')) { // source
        let labelEl1 = (<span style="width: 107px" class="nowarp">{data.label}</span>)
          let labelEl2 = (<span style="width: 152px;" class="nowarp">{data.name}</span>)
          return (
            <span id={data.id} class="nowarp" style="width:100%; display: flex;" draggable="true"
              on-dragstart={(ev) => this.dragStart(ev)}
              on-dragend={(ev) => this.dragEnd(ev)}>
              {iEl}{labelEl1}{labelEl2}
            </span>
          )
        } else { // target
          let labelEl1 = (<span style="width: 110px" class="nowarp">{data.label}</span>)
          let labelEl2 = (<span style="width: 158px;" class="nowarp">{data.name}</span>)
          let labelEl3 = (<span></span>)
          this.item.mapValues.some((v, idx) => {
            if (v.fieldId === data.fId) {
              let tId = 'v_' + idx
              labelEl3 = (<span id={tId} class="nowarp">{v.defaultValue}</span>)
              return true
            }
          })
          return (
            <span id={data.id} class="nowarp droppable" style="width:100%; display: flex;"
              on-dragover={(ev) => this.dragOver(ev)}
              on-dragleave={(ev) => this.dragLeave(ev)}
              on-dragenter={(ev) => this.dragEnter(ev)}
              on-drop={(ev) => this.drop(ev)}>
              {iEl}{labelEl1}{labelEl2}{labelEl3}
            </span>
          )
        }
      } else {
        let labelEl = (<span class="nowarp">{data.label}</span>)
        return (
          <span id={data.id} class="nowarp droppable" style="width:100%; display: flex;">
            {iEl}{labelEl}
          </span>
        )
      }
    }, // renderData

    handleNodeClick () {

    }, // handleNodeClick

    clearMapping () {
      this.item.mapLines = []
    }, // clearMapping

    updateMappingLines () {

    }, // updateMappingLines

    throttleUpdateMapping: function() {//_.throttle( function () {
    //   this.updateMappingLines()
    // }, 100), // handleTreeScroll
    },

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
      console.log('computed src data')
      return this.convertTreeData(this.item.sourceData, 's')
    },
    tgtTreeData: function () {
      console.log('computed tgt data')
      return this.convertTreeData(this.item.targetData, 't')
    },
  }
}
</script>
<style scoped>
div.map-view .src {
  width: 315px;
}
div.map-view .tgt {
  width: 485px;
}

div.map-header, div.map-tree, div.map-tree-header {
  display: flex;
}
div.map-header .mapping-buttons {
  width: 200px;
  text-align: center;
}
div.map-header .mapping-buttons .el-button--small {
  margin: 0;
}

div.map-tree .src, div.map-tree .tgt {
  border: 1px solid #DCDFE6;
}
div.map-tree .src-data-view, div.map-tree .tgt-data-view {
  height: 555px;
  overflow-y: auto;
  font-size: 14px;
}
div.map-tree .mapping-canvas {
  margin-top: 28px;
  width: 200px;
  height: 555px;
}
div.map-tree .mapping-canvas:focus {
  outline: none;
}

div.map-tree-header > div {
  border-bottom: 1px solid #DCDFE6;
  background-color: #F5F7FA;
  padding: 3px 10px 5px;
}
div.map-tree-header > div:not(:last-child) {
  border-right: 1px solid #DCDFE6;
}
</style>