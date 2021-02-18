import React, { useState, useEffect } from 'react'
import { Button, Modal, Tree, message } from 'antd'
import { Form, Input } from 'antd'
import { Stage, Layer, Line } from 'react-konva'

import RuleEditor from './RuleEditor'
import SubModal from '@/components/SubModal'
import DataList from '@/components/rule/list/Data-list'
import DataEditor from './Data-editor'

import * as urmsc from '@/urm-utils'

const locale = urmsc.locale
const LIST_STATE = urmsc.LIST_STATE

class DataMap extends RuleEditor {
  state = {
    ...this.state,
    confirm: undefined
  }

  shouldComponentUpdate(nextProps, nextState) {
    if (this.state.visible !== nextState.visible) {
      return true
    }
    if (JSON.stringify(this.state.item) !== JSON.stringify(nextState.item)) {
      return true
    }
    return false
  }

  childMethod = {
    ...this.childMethod,

    save: (saveItem) => {
      console.log(saveItem)
      if (!this.method.validator(saveItem)) {
        return false
      }
      
      let _this = this
      urmsc.ajax({
        type: 'POST',
        url: 'api/' + this.props.path,
        data: JSON.stringify(saveItem),
        contentType: 'application/json',
        success: function(res) {
          console.log(res)
          _this.setState({item: res})
          _this.state.confirm && _this.state.confirm(res.id)
          message.success(locale['message.0001'])
        },
        error: function(xhr) {
          message.warning('Update Fail. ' + xhr.statusText)
        }
      })
    },
  }
    

  render() {
    return (
      <Modal visible={this.state.visible} width="1040px" destroyOnClose
          footer={null} onCancel={this.method.handleCancel} className="urm-modal">
        <WrappedDataMapEditor codeList={this.props.codeList} userInfo={this.props.userInfo}
          item={this.state.item} {...this.childMethod} />
      </Modal>
    );
  }
}

const DataMapEditor = (props) => {
  const { form, item } = props
  const { getFieldDecorator } = form
  const { DirectoryTree, TreeNode } = Tree
  const [ lines, setLines ] = useState([])
  const [ values, setValues ] = useState([])
  const [ expandStatus, setExpandStatus ] = useState({ expanded: {src: true, tgt: true}, keys: {src: ['d-0'], tgt: ['d-0']} })

  useEffect(() => {
    props.setItem(form, item)
    setValues(item.mapValues ? item.mapValues : [])
    item.subRule = { mapLines: (item.mapLines ? item.mapLines : [])}
    method.setMapLines()
    console.log(lines)
    window.addEventListener('keydown', function(e) {
      let selected = item.subRule.selectedLine
      if (e.keyCode === 46 && selected) {
        let mapLines = item.subRule.mapLines
        for (let i = mapLines.length-1; i >= 0; i--) {
          let line = mapLines[i]
          if (line.sourceFieldId === selected.sId && line.targetFieldId === selected.tId) {
            mapLines.splice(i, 1)
          }
        }
        item.subRule.selectedLine = null
        method.setMapLines()
      }
    })
    // eslint-disable-next-line
  }, [item]); // Only re-run the effect if props.item changes

  useEffect(() => {
    let $el = document.querySelectorAll('.map-tree > .ant-tree')
    $el.forEach((it) => {
      it.onscroll = function(e) {
        method.setMapLines()
      }
    })
    method.setMapLines()
    // eslint-disable-next-line
  }, [expandStatus]); // Only re-run the effect if expandStatus changes

  let dataList = null
  let dataEdit = null
  let subRef = null

  let method = {
    clickSave: e => {
      item.subRule.mapValues = values
      let saveItem = method.makeMapObj()
      props.save(saveItem)
    },
    
    makeMapObj: () => {
      let saveItem = props.makeRuleObj(form, item);
      saveItem.mapLines = item.subRule.mapLines
      saveItem.mapValues = item.subRule.mapValues
      
      return saveItem
    },
    
    clearMapping: e => {
      item.subRule.mapLines = []
      setLines([])
    },
    
    setData: (type) => {
      let callback = (data) => {
        urmsc.ajax({
          type: 'GET',
          url: 'api/data/' + data.id,
          success: function(res) {
            let status = {...expandStatus}
            status.keys[type] = ['d-0']
            status.expanded[type] = true
            setExpandStatus(status)
            if (type === 'src') {
              item.sourceData = res
              form.setFieldsValue({sourceDataId: data.id})
            } else if (type === 'tgt') {
              item.targetData = res
              form.setFieldsValue({targetDataId: data.id})
            }
            item.subRule.mapLines = []
            setLines([])
          },
        })
        dataList.setState({visible: false})
      }
      
      let childState = {
        list: {onDbClick: callback, edit: method.editData(dataEdit)}
      }
      dataList.setState({visible: true, childState: childState})
    },
    
    getData: (type) => {
      let id = type === 'src' ? form.getFieldValue('sourceDataId') : form.getFieldValue('targetDataId')
      if (!id) return false
      
      urmsc.ajax({
        type: 'GET',
        url: 'api/data/' + id,
        success: function(res) {
          dataEdit.setState({visible: true, item: res})
        }
      })
    },
    
    editData: (editor) => (id) => {
      if (id) {
        urmsc.ajax({
          type: 'GET',
          url: 'api/data/' + id,
          success: function(res) {
            editor.setState({visible: true, item: res})
          }
        })
      } else {
        editor.setState({visible: true, item: {}})
      }
    },
    
    saveData: (data) => {
      urmsc.ajax({
        type: 'POST',
        url: 'api/data',
        data: JSON.stringify(data),
        contentType: 'application/json; charset=UTF-8',
        success: function(res) {
          console.log(res)
          dataEdit.setState({item: res})
          message.success(locale['message.0001'])
          
          subRef.method._updateItems(LIST_STATE.UPDATE, res)
          message.success(locale['message.0001'])
        },
        error: function(xhr) {
          message.warning('Update Fail. ' + xhr.statusText)
        }
      })
    },
    
    parseTreeData: (data) => {
      let treeData = []
      if (data) {
        let parent = {
          title: data.id,
          key: 'd-0',
          data: data,
        }
        parent.children = data.fields.map((field, idx) => {
          let title = <div className={"inline s-"+field.fieldId}>
            <div style={{width: 100}}>{field.engName}</div>
            <div style={{width: 150}}>{field.name}</div>
          </div>
          return {
            title: title,
            key: 'f-' + idx,
            data: field,
          }
        })
        treeData.push(parent)
      }
      return treeData
    },
    
    dropNode: (node, dragNode) => {
      if (!dragNode || !node.props.isLeaf) {
        return false
      }
      let _lines = item.subRule.mapLines
      let exist = false
      
      if (_lines && _lines.length > 0) {
        exist = (_lines.filter((it) => {
          return (it.sourceFieldId === dragNode.props.data.fieldId &&
            it.targetFieldId === node.props.data.fieldId)
        }).length > 0)
      }
      
      if (!exist) {
        _lines.push({
          sourceFieldId: dragNode.props.data.fieldId,
          targetFieldId: node.props.data.fieldId
        })
        console.log('push line', node)
      }
      item.subRule.mapLines = _lines
      
      method.setMapLines()
    },
    
    renderTreeNode: (type, treeData, isChild) => {
      return treeData.map((data, idx) => {
        let nodeProp = {
          key: data.key,
          title: data.title,
          data: data.data,
        }
        if (isChild) {
          let field = data.data
          nodeProp.isLeaf = true
          if (type === 'tgt') {
            nodeProp.title = (<div className={"inline t-"+field.fieldId} onClick={e => { method.editDefault(e, field) }}>
              <div key={0} style={{width: 100}}>{field.engName}</div>
              <div key={1} style={{width: 150}}>{field.name}</div>
              <div key={2} className="dVal">
                {method.renderDVal(field.fieldId)}
              </div>
            </div>)
          }
        }
        if (data.children && data.children.length) {
          return (
            <TreeNode {...nodeProp}>
              {method.renderTreeNode(type, data.children, true)}
            </TreeNode>
          );
        }
        return <TreeNode {...nodeProp} />
      })
    },
    
    renderDVal: (fieldId) => {
      let index = 0
      return values.filter((it, idx) => {
        index = idx
        return it.fieldId === fieldId
      }).map((it) => <span key={index} className="org">{it.defaultValue}</span>)
    },
    
    editDefault: (e, data) => {
      let $el = e.target
      let $p = document.querySelector('.edit')
      if ($p) {
        $p.classList.remove('edit')

        let $input = $p.querySelector('input')
        let val = $input.value
        method.saveDefault($input.dataset.fieldId, val)
        $p.removeChild($input)
        
        let $span = $p.querySelector('.org')
        if ($span) $span.classList.remove('hide')
      }
      
      if ($el.classList.contains('dVal') || $el.classList.contains('org')) {
        if ($el.classList.contains('org')) {
          $el = $el.parentNode
        }
        let isEdit = $el.classList.contains('edit')
        if (!isEdit) {
          $el.classList.add('edit')

          let $span = $el.querySelector('span.org')
          let val = ''
          if ($span) {
            val = $span.innerText
            $span.classList.add('hide')
          }
          let $input = document.createElement('input')
          $input.value = val
          $input.dataset.fieldId = data.fieldId
          $el.appendChild($input)
          $input.focus()
        }
      }
    },
    
    saveDefault: (fieldId, val) => {
      let tmp = [...values]
      for (let i = tmp.length-1; i >= 0; i--) {
        let it = tmp[i]
        if (it.fieldId === fieldId) {
          tmp.splice(i, 1)
          break
        }
      }
      
      if (val.trim().length > 0) {
        tmp.push({
          fieldId: fieldId,
          defaultValue: val
        })
      }
      setValues(tmp)
    },
    
    setMapLines: () => {
      let expanded = expandStatus.expanded
      if (item.subRule.mapLines) {
        let _lines = item.subRule.mapLines.map((it, idx) => {
          let $sP = document.querySelector('.map-tree.src > ul > li > span:first-child')
          let $tP = document.querySelector('.map-tree.tgt > ul > li > span:first-child')
          let $src = document.querySelector('.s-'+it.sourceFieldId)
          let $tgt = document.querySelector('.t-'+it.targetFieldId)
          
          let sPR = $sP.getBoundingClientRect()
          let tPR = $tP.getBoundingClientRect()
          let sR = expanded.src ? $src.getBoundingClientRect() : sPR
          let tR = expanded.tgt ? $tgt.getBoundingClientRect() : tPR
          
          return {sR: sR, tR: tR, sId: it.sourceFieldId, tId: it.targetFieldId}
        })
        setLines(_lines)
      }
    },
    
    drawLine: () => {
      let $canvas = document.querySelector('.line-canvas')
      if (!$canvas) return undefined
      let cR = $canvas.getBoundingClientRect()
      return lines.map((it, idx) => {
        let sR = it.sR
        let tR = it.tR
        let y1 = sR.top-cR.top+sR.height/2
        let y2 = tR.top-cR.top+tR.height/2
        
        let lineProps = {
          key: idx,
          stroke: '#0000ff',
          onMouseEnter: e => {
            let container = e.target.getStage().container()
            container.style.cursor = 'pointer'
          },
          onMouseLeave: e => {
            let container = e.target.getStage().container()
            container.style.cursor = 'default'
          },
          onClick: e => {
            if (y1 > 50 && y2 > 50)  method.selectLine(it, e)
          }
        }
        
        if (y1 < 34) {
          y1 = 34
          lineProps.stroke = '#00ffff'
        }
        if (y2 < 34) {
          y2 = 34
          lineProps.stroke = '#00ffff'
        }
        
        let selected = item.subRule ? item.subRule.selectedLine : undefined
        if (selected && selected.sId === it.sId && selected.tId === it.tId) {
          lineProps.stroke = '#ff0000'
        }
        
        let points = [0, y1, 200, y2]
        
        if (y1 !== y2) {
          points = [0, y1, 100, y1, 100, y2, 200, y2]
          lineProps.bezier = true
        }
        
        return <Line points={points} {...lineProps} />
      })
    },
    
    selectLine: (line, e) => {
      item.subRule.selectedLine = line
      method.setMapLines()
    },
    
    onExpand: (type, yn, keys) => {
      let status = {...expandStatus}
      status.keys[type] = keys
      status.expanded[type] = yn
      console.log(status)
      setExpandStatus(status)
    },
    
    setSelectNode: (node) => {
      console.log(node)
      if (!node.props.isLeaf) {
        let status = {...expandStatus}
        status.expanded.src = false
        setExpandStatus(status)
      }
      return node.props.isLeaf ? node : null
    },
    
    mappingName: () => {
      let _lines = item.subRule.mapLines
      let srcFlds = item.sourceData.fields
      let tgtFlds = item.targetData.fields
      
      tgtFlds.forEach((it) => {
        let src = srcFlds.filter((fld) => fld.engName === it.engName)
        let exist = false
        if (src.length > 0) {
          if (_lines && lines.length > 0) {
            exist = (_lines.filter((line) => {
              return (line.sourceFieldId === src[0].fieldId &&
                line.targetFieldId === it.fieldId)
            }).length > 0)
          }
          
          if (!exist) {
            _lines.push({
              sourceFieldId: src[0].fieldId,
              targetFieldId: it.fieldId
            })
          }
        }
      })
      item.subRule.mapLines = _lines
      method.setMapLines()
    },
  }

  let selectNode = null

  return (
    <div className="urm-editor">
      <div className="editor-buttons">
        <Button onClick={method.clickSave} disabled={!props.isPageSave()}>{locale['label.save']}</Button>
      </div>
      <Form colon={false}>
        <div className="row">
          <Form.Item label={locale['label.dataMapId']}>{getFieldDecorator("id")(<Input size="small" className="size-id" readOnly />)}</Form.Item>
          <Form.Item label={locale['label.dataMapName']}>{getFieldDecorator("name")(<Input size="small" className="size-name" />)}</Form.Item>
        </div>
        
        <hr />
        
        <div className="row">
          <div className="col-2">
            <Form.Item label={locale['label.outData']} className="data-id">
              {getFieldDecorator("sourceDataId")(<Input.Search size="small" className="size-id" readOnly
                onClick={e => { method.getData('src') }} onSearch={vars => { method.setData('src') }} />)}
            </Form.Item>
          </div>
          <div className="center" style={{width: 200}}>
            <Button icon="link" onClick={method.mappingName} title={locale['label.connectName']} />
            <Button icon="disconnect" onClick={method.clearMapping} title={locale['label.deleteMapping']} />
          </div>
          <div className="col-3 row">
            <Form.Item label={locale['label.inData']} className="data-id">
              {getFieldDecorator("targetDataId")(<Input.Search size="small" className="size-id" readOnly
                onClick={e => { method.getData('tgt') }} onSearch={vars => { method.setData('tgt') }} />)}
            </Form.Item>
          </div>
        </div>
        <div className="row">
          <div className="map-tree src">
            <div className="row">
              <div className="col-1 title">{locale['label.map.item1']}</div>
              <div className="col-1 title">{locale['label.map.item2']}</div>
            </div>
            <DirectoryTree draggable expandedKeys={expandStatus.keys.src}
                onDragStart={info => { selectNode = method.setSelectNode(info.node) }}
                onExpand={(expandedKeys, {expanded, node}) => { method.onExpand('src', expanded, expandedKeys) }}>
              {method.renderTreeNode('src', method.parseTreeData(item.sourceData))}
            </DirectoryTree>
          </div>
          <Stage className="line-canvas" width={200} height={584}>
            <Layer>
              {method.drawLine()}
            </Layer>
          </Stage>
          <div className="map-tree tgt">
            <div className="row">
              <div className="col-1 title">{locale['label.map.item1']}</div>
              <div className="col-1 title">{locale['label.map.item2']}</div>
              <div className="col-1 title">{locale['label.map.defaultVal']}</div>
            </div>
            <DirectoryTree draggable expandedKeys={expandStatus.keys.tgt}
                onDrop={info => { method.dropNode(info.node, selectNode) }}
                onExpand={(expandedKeys, {expanded, node}) => { method.onExpand('tgt', expanded, expandedKeys) }}>
              {method.renderTreeNode('tgt', method.parseTreeData(item.targetData))}
            </DirectoryTree>
          </div>
        </div>
      </Form>
      
      <SubModal ref={(list) => { dataList = list }} width="980px" bodyStyle={{maxHeight: 600, overflow: 'auto'}}>
        <DataList key="list" ref={(list) => { subRef = list }} path="data" userInfo={props.userInfo} codeList={props.codeList} />
      </SubModal>
      
      <DataEditor ref={(edit) => { dataEdit = edit }} path="data" userInfo={props.userInfo}
        codeList={props.codeList} save={method.saveData} />
    </div>
  );
}

const WrappedDataMapEditor = Form.create({name:'datamap_editor'})(DataMapEditor)
export default DataMap