import React, { useState, useEffect } from 'react'
import { Button, Modal, Tree } from 'antd'
import { Form, Input } from 'antd'
import { Stage, Layer, Line } from 'react-konva'

import RuleEditor from './RuleEditor'
import SubModal from '../SubModal'
import CopyData from './sub/CopyData'

import * as urmsc from '../../../urm-utils'

class DataMap extends RuleEditor {
  childMethod = {
    ...this.childMethod,

    save: (saveItem) => {
      console.log(saveItem)
      if (!this.method.validator(saveItem)) {
        return false
      }
      
      console.log('save')
      return false
      
      let $this = this
      urmsc.ajax({
        type: 'POST',
        url: '/URM/' + this.props.path,
        data: JSON.stringify(saveItem),
        contentType: 'application/json',
        success: function(res) {
          console.log(res)
          $this.setState({item: res})
        }
      })
    },
  }
    

  render() {
    return (
      <Modal visible={this.state.visible} width="1000px" 
          footer={null} onCancel={this.method.handleCancel} className="urm-modal">
        <WrappedDataMapEditor codeList={this.props.codeList} item={this.state.item} {...this.childMethod} />
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

  // Only re-run the effect if props.item changes
  useEffect(() => {
    props.setItem(form, item)
    setValues(item.mapValues ? item.mapValues : [])
    item.children = { mapLines: (item.mapLines ? item.mapLines : [])}
    method.setMapLines()
    // TODO subscribe...
    // eslint-disable-next-line
  }, [item]);

  let dataList = null

  let method = {
    clickSave: e => {
      item.children.mapValues = values
      let saveItem = method.makeMapObj()
      props.save(saveItem)
    },
    
    makeMapObj: () => {
      let saveItem = props.makeRuleObj(form, item);
      saveItem.mapLines = item.children.mapLines
      saveItem.mapValues = item.children.mapValues
      
      return saveItem
    },
    
    clearMapping: e => {
      item.children.mapLines = []
      method.setMapLines()
      method.drawLine()
    },
    
    setData: (type) => {
      let ref = dataList
      let callback = (data) => {
        urmsc.ajax({
          type: 'GET',
          url: '/URM/data/' + data.id,
          success: function(result) {
            if (type === 'src') {
              item.sourceData = result
              form.setFieldsValue({sourceDataId: data.id})
            } else if (type === 'tgt') {
              item.targetData = result
              form.setFieldsValue({targetDataId: data.id})
            }
          }
        })
        ref.setState({visible: false})
      }
      
      let childState = {
        list: {onDbClick: callback}
      }
      ref.setState({visible: true, childState: childState})
    },
    
    parseTreeData: (data) => {
      let treeData = []
      if (data) {
        let parent = {
          title: data.id,
          key: '0',
          data: data,
        }
        parent.children = data.fields.map((field, idx) => {
          let title = <div className={"inline s-"+field.fieldId}><div style={{width: 80}}>{field.engName}</div><div style={{width: 80}}>{field.name}</div></div>
          return {
            title: title,
            key: '0-' + idx,
            data: field,
          }
        })
        treeData.push(parent)
      }
      return treeData
    },
    
    dropNode: (node, dragNode) => {
      let lines = item.children.mapLines
      let exist = false
      
      if (lines && lines.length > 0) {
        exist = (lines.filter((it) => {
          return (it.sourceFieldId === dragNode.props.data.fieldId &&
            it.targetFieldId === node.props.data.fieldId)
        }).length > 0)
      }
      
      if (!exist) {
        lines.push({
          sourceFieldId: dragNode.props.data.fieldId,
          targetFieldId: node.props.data.fieldId
        })
        console.log('push line', node)
      }
      item.children.mapLines = lines
      
      method.setMapLines()
      method.drawLine()
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
              <div key={0} style={{width: 80}}>{field.engName}</div>
              <div key={1} style={{width: 120}}>{field.name}</div>
              <div key={2} className="dVal">
                {method.renderDVal(field.fieldId)}
              </div>
            </div>)
          }
        }
        if (data.children && data.children.length) {
          return (
            <TreeNode {...nodeProp} >
              {method.renderTreeNode(type, data.children, true)}
            </TreeNode>
          );
        }
        return <TreeNode {...nodeProp} />
      })
    },
    
    renderDVal: (fieldId) => {
      return values.filter(it => it.fieldId === fieldId).map((it) => <span className="org">{it.defaultValue}</span>)
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
      
      if ($el.classList.contains('dVal')) {
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
      let $canvas = document.querySelector('.line-canvas')
      let cR = $canvas.getBoundingClientRect()
      if (item.children.mapLines) {
        let lines = item.children.mapLines.map((it, idx) => {
          let $src = document.querySelector('.s-'+it.sourceFieldId)
          let $tgt = document.querySelector('.t-'+it.targetFieldId)
          let sR = $src.getBoundingClientRect()
          let tR = $tgt.getBoundingClientRect()
          
          let y1 = sR.top-cR.top+sR.height/2
          let y2 = tR.top-cR.top+tR.height/2
          
          let points = [0, y1, 200, y2]
          let lineProps = {
            key: idx,
            stroke: '#0000ff'
          }
          
          if (y1 !== y2) {
            points = [0, y1, 100, y1, 100, y2, 200, y2]
            lineProps.bezier = true
          }
          
          return <Line points={points} {...lineProps} />
        })
        setLines(lines)
      }
    },
    
    drawLine: () => {
      console.log(lines)
      return lines
    },
    
    onExpand: () => {
      console.log('expand')
      method.setMapLines()
      method.drawLine()
    }
  }

  let selectNode = null

  return (
    <div className="urm-editor">
      <div className="editor-buttons">
        <Button onClick={method.clickSave}>Save</Button>
      </div>
      <Form colon={false}>
        <div className="row">
          <Form.Item label="Mapping ID">{getFieldDecorator("id")(<Input size="small" className="size-id" readOnly />)}</Form.Item>
          <Form.Item label="Mapping Name">{getFieldDecorator("name")(<Input size="small" className="size-name" />)}</Form.Item>
        </div>
        
        <hr />
        
        <div className="row">
          <div className="col-2">
            <Form.Item label="Source" className="src-id">
              {getFieldDecorator("sourceDataId")(<Input.Search size="small" className="size-id" readOnly onSearch={vars => { method.setData('src') }} />)}
            </Form.Item>
          </div>
          <div className="col-1 center">
            <Button icon="disconnect" onClick={method.clearMapping} />
            <Button icon="link" />
          </div>
          <div className="col-3 row">
            <Form.Item label="Target" className="flex-right tgt-id">
              {getFieldDecorator("targetDataId")(<Input.Search size="small" className="size-id" readOnly onSearch={vars => { method.setData('tgt') }} />)}
            </Form.Item>
          </div>
        </div>
        <div className="row">
          <div className="col-2 map-tree">
            <div className="row">
              <div className="col-1 title">Eng Name</div>
              <div className="col-1 title">Kor Name</div>
            </div>
            <DirectoryTree draggable defaultExpandAll onDragStart={info => { selectNode = info.node }}>
              {method.renderTreeNode('src', method.parseTreeData(item.sourceData))}
            </DirectoryTree>
          </div>
          <Stage className="col-1 line-canvas" width={200} height={300}>
            <Layer>
              {method.drawLine()}
            </Layer>
          </Stage>
          <div className="col-3 map-tree">
            <div className="row">
              <div className="col-1 title">Eng Name</div>
              <div className="col-1 title">Kor Name</div>
              <div className="col-1 title">Default Value</div>
            </div>
            <DirectoryTree draggable defaultExpandAll onDrop={info => { method.dropNode(info.node, selectNode) }}>
              {method.renderTreeNode('tgt', method.parseTreeData(item.targetData))}
            </DirectoryTree>
          </div>
        </div>
      </Form>
      
      <SubModal ref={(list) => { dataList = list }} width="980px">
        <CopyData key="list" codeList={props.codeList} />
      </SubModal>
    </div>
  );
}

const WrappedDataMapEditor = Form.create({name:'datamap_editor'})(DataMapEditor)
export default DataMap