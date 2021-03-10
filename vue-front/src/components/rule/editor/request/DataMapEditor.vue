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
          <el-tooltip :content="$t('label.deleteMapping')" placement="top" :open-delay="500" :enterable="false">
            <el-button @click="clearMapping"><i class="el-icon-brush" style="transform: rotate(195deg);"/></el-button>
          </el-tooltip>
          <el-tooltip :content="$t('label.connectName')" placement="top" :open-delay="500" :enterable="false">
            <el-button icon="el-icon-link" @click="mappingByName"/>
          </el-tooltip>
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
import _ from 'lodash'
import Raphael from 'raphael'

import RuleEditor from '@/components/rule/editor/RuleEditor'

import DataEditor from '@/components/rule/editor/DataEditor'
import DataList from '@/components/rule/list/DataList'

const NOR_COLOR = '#0000FF'
const BLUR_COLOR = '#00FFFF'
const SEL_COLOR = '#FF0000'

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

      dragNodeId: null,
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
        this.item.mapLines = []
        this.$nextTick(() => {
          this.updateMappingLines()
        })
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
    }, // showDataEditor

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
        let rootData = {type: 'data', id: type + '-data', label: data.id}
        if (data.fields && data.fields.length > 0) {
          let treeData = []
          data.fields.forEach((f) => {
            let tId = type + '-' + f.fieldId
            let child = {type: 'field', id: tId, fId: f.fieldId, field: f, isEdit: false}
            this.item.mapValues.some((v) => {
              if (v.fieldId === data.fId) {
                child.value = v.defaultValue
              }
            })
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
        let f = data.field
        if (data.id.startsWith('s')) { // source
          let labelEl1 = (<span style="width: 109px;" class="nowarp">{f.engName}</span>)
          let labelEl2 = (<span style="width: 141px;" class="nowarp">{f.name}</span>)
          return (
            <span id={data.id} class="nowarp" style="width: 100%; height: 22px; display: flex;" draggable="true"
                on-dragstart={(ev) => this.dragStart(ev)}
                on-dragend={(ev) => this.dragEnd(ev)}>
              {iEl}{labelEl1}{labelEl2}
            </span>
          )
        } else { // target
          let labelEl1 = (<span style="width: 112px;" class="nowarp">{f.engName}</span>)
          let labelEl2 = (<span style="width: 161px;" class="nowarp">{f.name}</span>)
          let labelEl3 = (
            <span style="width: 144px;" class="nowarp" on-click={(ev) => this.clickValueData(ev, data)}>
              <span v-show={!data.isEdit}>{data.value}</span>
              <span v-show={data.isEdit}>
                <el-input v-model={data.value} class="tree-node-input"
                  nativeOnFocusout={() => this.changeValueData(data)}/>
              </span>
            </span>
          )
          return (
            <span id={data.id} class="nowarp droppable" style="width: 100%; height: 22px; display: flex;"
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
          <span id={data.id} class="nowarp droppable" style="width: 100%; display: flex;">
            {iEl}{labelEl}
          </span>
        )
      }
    }, // renderData

    dragStart (ev) {
      console.log('dragStart')
      this.dragNodeId = ev.target.id
      ev.dataTransfer.setData('text', this.dragNodeId)
      ev.target.style.color = '#409EFF'
      ev.target.closest('div.el-tree-node__content').style.backgroundColor = '#ffffd0'
    }, // dragStart

    dragEnd (ev) {
      console.log('dragEnd')
      ev.preventDefault()
      ev.target.style.color = ''
      ev.target.closest('div.el-tree-node__content').style.backgroundColor = ''
      this.dragNodeId = null
    }, // dragEnd

    dragEnter (ev) {
      let dropNodeId = ev.target.id
      !dropNodeId && (dropNodeId = ev.target.closest('span.droppable').id)
      let sData = this.$refs.srcDataTree.getNode(this.dragNodeId).data
      let tData = this.$refs.tgtDataTree.getNode(dropNodeId).data
      let droppable = (sData.type === 'field' && tData.type === 'field') ? true : false
      if (!droppable) {
        return
      }
      ev.preventDefault()
      ev.stopPropagation()
      ev.target.style.color = '#409EFF'
      ev.target.closest('div.el-tree-node__content').style.backgroundColor = '#ffffd0'
    }, // dragEnter

    dragOver (ev) {
      let dropNodeId = ev.target.id
      !dropNodeId && (dropNodeId = ev.target.closest('span.droppable').id)
      let sData = this.$refs.srcDataTree.getNode(this.dragNodeId).data
      let tData = this.$refs.tgtDataTree.getNode(dropNodeId).data
      let droppable = (sData.type === 'field' && tData.type === 'field') ? true : false
      if (!droppable) {
        return
      }
      ev.preventDefault()
      ev.stopPropagation()
      ev.target.style.color = '#409EFF'
      ev.target.closest('div.el-tree-node__content').style.backgroundColor = '#ffffd0'
    }, // dragOver

    dragLeave (ev) {
      ev.preventDefault()
      ev.stopPropagation()
      ev.target.style.color = ''
      ev.target.closest('div.el-tree-node__content').style.backgroundColor = ''
    }, // dragLeave

    drop (ev) {
      ev.preventDefault()
      ev.stopPropagation()
      ev.target.style.color = ''
      ev.target.closest('div.el-tree-node__content').style.backgroundColor = ''

      let dragNodeId = ev.dataTransfer.getData('text')
      let dropNodeId = ev.target.id
      !dropNodeId && (dropNodeId = ev.target.closest('span.droppable').id)
      let sData = this.$refs.srcDataTree.getNode(dragNodeId).data
      let tData = this.$refs.tgtDataTree.getNode(dropNodeId).data
      let isOk = this.addMappingData(sData, tData)
      if (isOk) {
        this.drawMappingLine(dragNodeId, dropNodeId)
      }
    }, // drop

    clickValueData (ev, data) {
      ev.stopPropagation()
      data.isEdit = true
      this.$nextTick(() => {
        this.$refs.tgtDataTree.$el.querySelector('#' + data.id + ' input').focus()
      })
    }, // clickValueData

    changeValueData (data) {
      console.log('in')
      this.item.mapValues.some((v) => {
        if (v.fieldId === data.fId) {
          v.defaultValue = data.value
        }
      })
      data.isEdit = false
    }, // changeValueData

    addMappingData(sData, tData) {
      console.log('addMappingData', sData, tData)
      let isOk = false
      let isExist = false
      this.item.mapLines.some((line) => {
        if (line.sourceFieldId === sData.fId && line.targetFieldId === tData.fId) {
          isExist = true
        }
        if (isExist) {
          return true // break
        }
      })
      if (isExist) {
        let mapping = sData.fId + ' to ' + tData.fId
        this.$message({type: 'warning', message: 'already exist mapping [' + mapping + ']'})
        isOk = false
      } else {
        this.item.mapLines.push({ sourceFieldId: sData.fId, targetFieldId: tData.fId })
        isOk = true
      }
      return isOk
    }, // addMappingData

    handleNodeClick (data) {
      console.log('handleNodeClick', data)
      this.selectedNode = data
    }, // handleNodeClick

    mappingByName () {
      let lines = this.item.mapLines
      let srcFlds = this.item.sourceData.fields
      let tgtFlds = this.item.targetData.fields
      
      tgtFlds.forEach((tFld) => {
        let src = srcFlds.find((sFld) => (sFld.engName === tFld.engName))
        let filteredLines = []
        if (src) {
          if (lines && lines.length > 0) {
            filteredLines = lines.filter((line) =>
              (line.sourceFieldId === src.fieldId && line.targetFieldId === tFld.fieldId))
          }
          
          if (filteredLines.length === 0) {
            lines.push({
              sourceFieldId: src.fieldId,
              targetFieldId: tFld.fieldId
            })
          }
        }
      })
      this.updateMappingLines()
    }, // mappingByName

    clearMapping () {
      let confirmProp = {
        confirmButtonText: 'YES',
        cancelButtonText: 'NO',
        closeOnClickModal: false,
        dangerouslyUseHTMLString: false,
        type: 'error',
      }
      this.$confirm('매핑을 전부 삭제하시겠습니까?', confirmProp).then(() => {
        this.item.mapLines = []
        this.mapPaper.clear()
      }).catch(() => {})
    }, // clearMapping

    updateMappingLines () {
      let newDiv = document.createElement('div')
      let newPaper = Raphael(newDiv, 200, 555)
      this.item.mapLines.forEach((line) => {
        let tgtNodeId = 't-' + line.targetFieldId
        try {
          let srcNodeId = 's-' + line.sourceFieldId
          this.drawMappingLine(srcNodeId, tgtNodeId, newPaper)
        } catch (err) {
          console.error(err)
        }
      })
      this.mapPaper && this.mapPaper.clear()
      let mapCanvas = this.$refs.mapCanvas
      while (mapCanvas.hasChildNodes()) {
        mapCanvas.removeChild(mapCanvas.firstChild)
      }
      mapCanvas.appendChild(newDiv.firstChild)
      this.mapPaper = newPaper
    }, // updateMappingLines

    drawMappingLine (srcNodeId, tgtNodeId, paper) {
      !paper && (paper = this.mapPaper)
      const canvasY = this.getElTop(this.$refs.mapCanvas)
      let srcDataTree = this.$refs.srcDataTree
      let tgtDataTree = this.$refs.tgtDataTree
      let srcNode = srcDataTree.getNode(srcNodeId)
      let tgtNode = tgtDataTree.getNode(tgtNodeId)
      let srcNodeEl = srcDataTree.$el.querySelector('#' + srcNodeId)
      let tgtNodeEl = tgtDataTree.$el.querySelector('#' + tgtNodeId)
      if (!srcNodeEl || !tgtNodeEl) {
        return
      }
      let sInfo = this.getLineInfo(srcDataTree, srcNode, srcNodeEl, canvasY)
      let tInfo = this.getLineInfo(tgtDataTree, tgtNode, tgtNodeEl, canvasY)

      let centerY = sInfo.y + ((tInfo.y - sInfo.y) / 2)
      let pathStr = 'M0,' + sInfo.y + ' Q50,' + sInfo.y + ',100,' + centerY + ' Q150,' + tInfo.y + ',200,' + tInfo.y
      let line = paper.path(pathStr)
      let lineColor = (sInfo.isBlur || tInfo.isBlur) ? BLUR_COLOR : NOR_COLOR
      line.attr({'stroke': lineColor, 'stroke-width': 3})
      line.node.style.cursor = 'pointer'

      line.linedata = {target: tgtNode.data, source: srcNode.data}
      line.selected = false
      line.click(function () {
        if (this.attr('stroke') === BLUR_COLOR) {
          return
        }
        this.selected ? this.attr({'stroke': lineColor}) : this.attr({'stroke': SEL_COLOR})
        this.selected = !this.selected
      })
    }, // drawMappingLine

    throttleUpdateMapping: _.throttle(function () {
      this.updateMappingLines()
    }, 100), // handleTreeScroll

    deleteLine () {
      let delItems = []
      this.mapPaper.forEach((el) => {
        el.selected && delItems.push(el)
      })
      while (delItems.length > 0) {
        let item = delItems.shift()
        let sData = item.linedata.source, tData = item.linedata.target
        item.remove() // delete line
        this.item.mapLines.some((line, idx) => {
          if (line.sourceFieldId === sData.fId && line.targetFieldId === tData.fId) {
            this.item.mapLines.splice(idx, 1)
            return true
          }
        })
      }
    }, // deleteLine

    getElTop (el) {
      let box = el.getBoundingClientRect()
      let body = document.body
      let docEl = document.documentElement
      let scrollTop = window.pageYOffset || docEl.scrollTop || body.scrollTop
      let clientTop = docEl.clientTop || body.clientTop || 0
      let top  = box.top +  scrollTop - clientTop
      return Math.round(top)
    }, // getElTop

    getLineInfo (dataTree, node, nodeEl, canvasY) {
      let isBlur = false
      while (node) {
        if (nodeEl.offsetHeight !== 0 || nodeEl.offsetWidth !== 0) {
          break
        }
        node = node.parent
        nodeEl = dataTree.$el.querySelector('#' + node.key)
        isBlur = true
        if (!nodeEl) {
          console.error('does not exist node element - ' + node.key)
          break
        }
      }
      let y = this.getElTop(nodeEl) - canvasY + 8
      if (y < 0 || y > 600) {
        y = y < 0 ? 0 : 600
        isBlur = true
      }
      return {y: y, isBlur: isBlur}
    }, // getLineInfo

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
    srcTreeData () {
      return this.convertTreeData(this.item.sourceData, 's')
    },
    tgtTreeData () {
      return this.convertTreeData(this.item.targetData, 't')
    },
  },
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