import React, { useState, useEffect } from 'react'
import { Button, Modal, Tree } from 'antd'
import { Form, Input } from 'antd'

import RuleEditor from '../RuleEditor'
//import {default as urmUtils} from '../../../urm-utils'

class DataMap extends RuleEditor {
  constructor(props) {
    super(props)
    this.state.path = 'datamap'
  }
  
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
      <Modal visible={this.state.visible} onCancel={this.method.handleCancel} width="980px" footer={null} >
        <WrappedDataMapEditor item={this.state.item} {...this.childMethod} />
      </Modal>
    );
  }
}

const DataMapEditor = (props) => {
  const { form, item } = props
  const { getFieldDecorator } = props.form
  const { DirectoryTree, TreeNode } = Tree

  // Only re-run the effect if props.item changes
  useEffect(() => {
    props.setItem(form, item)
    // TODO subscribe...
    // eslint-disable-next-line
  }, [item]);

  let method = {
    clickSave: e => {
      console.log(form.getFieldsValue(), item)
      //props.save(form, fields, method.makeSaveItem)
    },
    
    parseTreeData: (data) => {
      let treeData = []
      if (data) {
        data.forEach((p, pidx) => {
          let parent = {
            title: p.id,
            key: pidx,
          }
          parent.children =
            p.fields.map((child, cidx) => {
              let title = child.engName + child.name
              return {
                title: title,
                key: pidx + '-' + cidx,
              }
            })
          treeData.push(parent)
        })
      }
      return treeData
    },
    
    renderTreeNode: (treeData) => {
      return treeData.map((data) => {
        if (data.children && data.children.length) {
          return (
            <TreeNode key={data.key} title={data.title}>
              {method.renderTreeNode(data.children)}
            </TreeNode>
          );
        }
        return <TreeNode key={data.key} title={data.title} />
      })
    },
  }

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
          <Form.Item label="SourceDataId">
            {getFieldDecorator("sourceDataId")(<Input.Search size="small" className="size-id" readOnly />)}
          </Form.Item>
          <Form.Item label="TargetDataId">
            {getFieldDecorator("targetDataId")(<Input.Search size="small" className="size-id" readOnly />)}
          </Form.Item>
        </div>
      </Form>
      <div>
        <div className="half-row">
          <DirectoryTree defaultExpandAll>
            {method.renderTreeNode(method.parseTreeData(item.sourceData))}
          </DirectoryTree>
        </div>
        <div className="half-row">
          <DirectoryTree defaultExpandAll>
            {method.renderTreeNode(method.parseTreeData(item.targetData))}
          </DirectoryTree>
        </div>
      </div>
    </div>
  );
}

const WrappedDataMapEditor = Form.create({name:'data_map_editor'})(DataMapEditor)
export default DataMap