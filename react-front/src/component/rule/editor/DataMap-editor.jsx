import React, { useState, useEffect } from 'react'
import { Button, Modal, Tree } from 'antd'
import { Form, Input } from 'antd'
import { Stage, Layer, Line } from 'react-konva'

import RuleEditor from './RuleEditor'
import SubModal from '../SubModal'
import DataList from '../list/Data-list'

import * as urmsc from '../../../urm-utils'

class DataMap extends RuleEditor {
  customMethod = {
    validator: (data) => {
      if (!data.name || data.name.trim().length === 0) {
        return false
      }
      return true
    }
  }

  render() {
    return (
      <Modal visible={this.state.visible} onCancel={this.method.handleCancel} width="980px"
        footer={null} >
        <WrappedDataMapEditor codeList={this.props.codeList} item={this.state.item} {...this.childMethod} />
      </Modal>
    );
  }
}

const DataMapEditor = (props) => {
  const { form, item } = props
  const { getFieldDecorator } = props.form
  const { DirectoryTree, TreeNode } = Tree
  const [ lines, setLines ] = useState([])
  const [ values, setValues ] = useState([])

  // Only re-run the effect if props.item changes
  useEffect(() => {
    props.setItem(form, item)
    method.setMapLines()
    if (item.mapValues) setValues(item.mapValues)
    // TODO subscribe...
    // eslint-disable-next-line
  }, [item]);
  
  useEffect(() => {
    console.log('value change')
  }, [values]);
  
  let dataList = null

  let method = {
    clickSave: e => {
      let children = {
        mapLines: item.mapLines,
        mapValues: values,
      }
      console.log(form.getFieldsValue(), item)
      //props.save(form, children, method.makeSaveItem)
    },
    
    clearMapping: e => {
      item.mapLines = []
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
          let title = <div className="inline"><div style={{width: 80}}>{field.engName}</div><div style={{width: 80}}>{field.name}</div></div>
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
      let lines = item.mapLines
      let exist = false
      if (lines && lines.length > 0) {
        exist = (lines.filter((it) => {
          return (it.sourceFieldId === dragNode.props.data.fieldId &&
            it.targetFieldId === node.props.data.fieldId)
        }).length > 0)
      } else if (!lines) {
        lines = []
      }
      
      if (!exist) {
        lines.push({
          sourceFieldId: dragNode.props.data.fieldId,
          targetFieldId: node.props.data.fieldId
        })
      }
      item.mapLines = lines
    },
    
    setData: (type) => {
      let ref = dataList
      urmsc.ajax({
        type: 'GET',
        url: '/URM/data',
        success: function(res) {
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
          
          ref.setState({visible: true})
          ref.method.changeChildState({items: res, onDbClick: callback})
        }
      })
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
          nodeProp.className = 'n-' + field.fieldId
          if (type === 'tgt') {
            nodeProp.title = (<div className="inline" onClick={e => { method.editDefault(e, field) }}>
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
      tmp.forEach((it, idx) => {
        if (it.fieldId === fieldId) {
          tmp.splice(idx, 1)
        }
      })
      
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
      let rect = $canvas.getBoundingClientRect()
      if (item.mapLines) {
        let lines =item.mapLines.map((it, idx) => {
          return <Line key={idx} x={rect.left} y={rect.top + 10} points={[0, 0, 100, 0]} storke="black"/>
        })
        setLines(lines)
      }
    },
    
    drawLine: () => {
      console.log(lines)
      return lines
    }
  }

  

  let selectNode = null

  return (
    <div className="urm-editor">
      <div style={{textAlign: "right", marginRight: "20px"}}>
        <Button onClick={method.clickSave}>Save</Button>
      </div>
      <Form colon={false}>
        <div className="row">
          <Form.Item label="Mapping ID">{getFieldDecorator("id")(<Input size="small" className="size-id" readOnly />)}</Form.Item>
          <Form.Item label="Mapping Name">{getFieldDecorator("name")(<Input size="small" className="size-name" />)}</Form.Item>
        </div>
        <div className="row">
          <Form.Item label="SourceDataId" className="col-2">
            {getFieldDecorator("sourceDataId")(<Input.Search size="small" className="size-id" readOnly onSearch={vars => { method.setData('src') }} />)}
          </Form.Item>
          <div className="col-1">
            <Button onClick={method.clearMapping} icon="disconnect" />
            <Button icon="shrink" />
          </div>
          <Form.Item label="TargetDataId" className="col-2">
            {getFieldDecorator("targetDataId")(<Input.Search size="small" className="size-id" readOnly onSearch={vars => { method.setData('tgt') }} />)}
          </Form.Item>
        </div>
        <div className="row">
          <div className="col-2 map-tree">
            <div className="row">
              <div className="col-1 title">Eng Name</div>
              <div className="col-1 title">Kor Name</div>
            </div>
            <DirectoryTree draggable onDragStart={info => { selectNode = info.node }} defaultExpandAll >
              {method.renderTreeNode('src', method.parseTreeData(item.sourceData))}
            </DirectoryTree>
          </div>
          <Stage className="col-1 line-canvas" width={200} height={300}>
            <Layer>
              <Line key={0} x={0} y={10} points={[0, 0, 100, 0]} storke="black"/>
            </Layer>
          </Stage>
          <div className="col-3 map-tree">
            <div className="row">
              <div className="col-1 title">Eng Name</div>
              <div className="col-1 title">Kor Name</div>
              <div className="col-1 title">Default Value</div>
            </div>
            <DirectoryTree draggable onDrop={info => { method.dropNode(info.node, selectNode) }} defaultExpandAll >
              {method.renderTreeNode('tgt', method.parseTreeData(item.targetData))}
            </DirectoryTree>
          </div>
        </div>
      </Form>
      
      <SubModal ref={(list) => { dataList = list }} width="950px"
        render={() => (<DataList ref="child" path="data" codeList={props.codeList} />)} />
    </div>
  );
}

const WrappedDataMapEditor = Form.create({name:'data_map_editor'})(DataMapEditor)
export default DataMap