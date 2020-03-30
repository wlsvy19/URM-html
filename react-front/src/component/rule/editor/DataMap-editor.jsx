import React, { useState, useEffect } from 'react'
import { Button, Modal, Tree } from 'antd'
import { Form, Input } from 'antd'
import { Stage, Layer, Line } from 'react-konva'

import RuleEditor from './RuleEditor'
import SubModal from '@/component/SubModal'
import CopyData from './sub/CopyData'

import * as urmsc from '@/urm-utils'

const locale = urmsc.locale

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
      
      let $this = this
      urmsc.ajax({
        type: 'POST',
        url: 'api/' + this.props.path,
        data: JSON.stringify(saveItem),
        contentType: 'application/json',
        success: function(res) {
          console.log(res)
          $this.setState({item: res})
          $this.state.confirm && $this.state.confirm(res.id)
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
  const [ expanded, setExpanded ] = useState({src: true, tgt: true})

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
    },
    
    setData: (type) => {
      let ref = dataList
      let callback = (data) => {
        urmsc.ajax({
          type: 'GET',
          url: 'api/data/' + data.id,
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
      if (!dragNode || !node.props.isLeaf) {
        console.log(node, dragNode)
        return false
      }
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
      if (item.children.mapLines) {
        let lines = item.children.mapLines.map((it, idx) => {
          let $p = document.querySelector('.map-tree > ul > li > span:first-child')
          let $src = document.querySelector('.s-'+it.sourceFieldId)
          let $tgt = document.querySelector('.t-'+it.targetFieldId)
          let pR = $p.getBoundingClientRect()
          let sR = $src.getBoundingClientRect()
          let tR = $tgt.getBoundingClientRect()
          
          return {e: {sR: sR, tR: tR}, c: {pR: pR}}
        })
        console.log(lines)
        setLines(lines)
      }
    },
    
    drawLine: (expanded) => {
      let $canvas = document.querySelector('.line-canvas')
      let cR = $canvas ? $canvas.getBoundingClientRect() : null
      return lines.map((it, idx) => {
        let sR = expanded.src ? it.e.sR : it.c.pR
        let tR = expanded.tgt ? it.e.tR : it.c.pR
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
    },
    
    onExpand: (yn, type) => {
      let res = {...expanded}
      if (type === 'src') {
        res.src = yn
      } else if (type === 'tgt') {
        res.tgt = yn
      }
      setExpanded(res)
    },
    
    setSelectNode: (node) => {
      node.props.isLeaf || setExpanded({...expanded, src: false})
      return node.props.isLeaf ? node : null
    },
    
    mappingName: () => {
      let lines = item.children.mapLines
      let srcFlds = item.sourceData.fields
      let tgtFlds = item.targetData.fields
      
      tgtFlds.forEach((it) => {
        let src = srcFlds.filter((fld) => fld.engName === it.engName)
        let exist = false
        if (src.length > 0) {
          if (lines && lines.length > 0) {
            exist = (lines.filter((line) => {
              return (line.sourceFieldId === src[0].fieldId &&
                line.targetFieldId === it.fieldId)
            }).length > 0)
          }
          
          if (!exist) {
            lines.push({
              sourceFieldId: src[0].fieldId,
              targetFieldId: it.fieldId
            })
          }
        }
      })
      item.children.mapLines = lines
      method.setMapLines()
    }
  }

  let selectNode = null

  return (
    <div className="urm-editor">
      <div className="editor-buttons">
        <Button onClick={method.clickSave} disabled={!props.isPageSave()}>{locale['label.save']}</Button>
      </div>
      <Form colon={false}>
        <div className="row">
          <Form.Item label="매핑 ID">{getFieldDecorator("id")(<Input size="small" className="size-id" readOnly />)}</Form.Item>
          <Form.Item label={locale['label.dataMapName']}>{getFieldDecorator("name")(<Input size="small" className="size-name" />)}</Form.Item>
        </div>
        
        <hr />
        
        <div className="row">
          <div className="col-2">
            <Form.Item label={locale['label.outData']} className="src-id">
              {getFieldDecorator("sourceDataId")(<Input.Search size="small" className="size-id" readOnly onSearch={vars => { method.setData('src') }} />)}
            </Form.Item>
          </div>
          <div className="col-1 center">
            <Button icon="disconnect" onClick={method.clearMapping} title={locale['label.deleteMapping']} />
            <Button icon="link" onClick={method.mappingName} title={locale['label.connectName']} />
          </div>
          <div className="col-3 row">
            <Form.Item label={locale['label.inData']} className="flex-right tgt-id">
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
            <DirectoryTree draggable defaultExpandAll onDragStart={info => { selectNode = method.setSelectNode(info.node) }}
                onExpand={(expandedKeys, {expanded, node}) => { method.onExpand(expanded, 'src') }}>
              {method.renderTreeNode('src', method.parseTreeData(item.sourceData))}
            </DirectoryTree>
          </div>
          <Stage className="col-1 line-canvas" width={200} height={300}>
            <Layer>
              {method.drawLine(expanded)}
            </Layer>
          </Stage>
          <div className="col-3 map-tree">
            <div className="row">
              <div className="col-1 title">Eng Name</div>
              <div className="col-1 title">Kor Name</div>
              <div className="col-1 title">Default Value</div>
            </div>
            <DirectoryTree draggable defaultExpandAll onDrop={info => { method.dropNode(info.node, selectNode) }}
                onExpand={(expandedKeys, {expanded, node}) => { method.onExpand(expanded, 'tgt') }}>
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