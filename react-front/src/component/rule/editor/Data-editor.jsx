import React, { useState, useEffect } from 'react'
import { Button, Modal, message } from 'antd'
import { Form, Input, Select } from 'antd'

import RuleEditor from './RuleEditor'
import SubModal from '@/component/SubModal'
import EditableField from './sub/EditableField'
import ExcelData from './sub/ExcelData'
import QueryData from './sub/QueryData'
import CopyData from './sub/CopyData'
import UsedData from '@/component/rule/list/sub/UsedData'

import * as urmsc from '@/urm-utils'

const locale = urmsc.locale

class DataEditor extends RuleEditor {
  customMethod = {
    validator: (data) => {
      if (!data.fields || data.fields.length === 0) {
        message.warning('please add fields')
        return false
      }
      return true
    }
  }

  render() {
    return (
      <Modal visible={this.state.visible} width="1080px"
          footer={null} onCancel={this.method.handleCancel} className="urm-modal">
        <WrappedDataEditor codeList={this.props.codeList} item={this.state.item} {...this.childMethod} />
      </Modal>
    );
  }
}

const DataEditorForm = (props) => {
  const { form, item } = props
  const { getFieldDecorator } = form
  const [ fields, setFields ] = useState([])

  // Only re-run the effect if props.item changes
  useEffect(() => {
    props.setItem(form, item)
    setFields(item.fields ? item.fields : [])
    item.subRule = {}
    // TODO subscribe...
    // eslint-disable-next-line
  }, [item]);

  let excel = null
  let query = null
  let copyList = null
  let usedList = null

  let method = {
    clickSave: e => {
      item.subRule.fields = fields
      let saveItem = method.makeDataObj()
      
      if (saveItem.id && saveItem.id.length > 0) {
        method.checkData(saveItem)
      } else {
        props.save(saveItem)
      }
    },
    
    makeDataObj: () => {
      let saveItem = props.makeRuleObj(form, item)
      saveItem.fields = item.subRule.fields
      
      let data = form.getFieldsValue()
      saveItem.fields.forEach((it, idx) => {
        let field = data.fields[idx]
        for (let key in field) {
          if (key.endsWith('YN') || key === 'nullable') {
            it[key] = urmsc.convertYN(field[key])
          } else {
            it[key] = (key in field) ? field[key] : it[key]
          }
        }
      })
      
      return saveItem
    },
    
    checkData: (saveItem) => {
      let id = saveItem.id
      let callback = () => {
        saveItem.channelCode = 'EAI'
        props.save(saveItem)
        usedList.setState({visible: false})
      }
      let childState = {
        list: {onDbClick: callback, scparam: {id: id}}
      }
      
      urmsc.ajax({
        type: 'GET',
        url: 'api/data/used',
        data: {id: id},
        success: function(list) {
          if (list && list.length > 0) {
            usedList.setState({visible: true, childState: childState})
          } else {
            props.save(saveItem)
          }
        },
      })
    },
    
    clickAppend: e => {
      let items = [...fields]
      items.push({
        sno: (fields.length + 1),
        type: 'C',
        nullable: true,
        keyYN: false,
        sqlYN: true,
      })
      setFields(items)
    },
    
    clickRemove: e => {
      let selectedKeys = item.subRule.selectedKeys
      if (selectedKeys.length === 0) {
        message.warning(locale['message.1005'])
        return false
      }
      let tmp = []
      fields.forEach((it) => {
        if (selectedKeys.indexOf(it.sno) === -1) {
          let obj = {
            ...it,
            sno: tmp.length + 1
          }
          tmp.push(obj)
        }
      })
      setFields(tmp)
      item.subRule.selectedKeys = []
    },
    
    addByExcel: () => {
      excel.childMethod.confirm = (fields) => {
        setFields(fields)
        excel.setState({visible: false})
      }
      excel.setState({visible: true})
    },
    
    addByQuery: () => {
      query.childMethod.confirm = (fields) => {
        setFields(fields)
        query.setState({visible: false})
      }
      query.setState({visible: true})
    },
    
    copyData: () => {
      let ref = copyList
      let callback = (data) => {
        urmsc.ajax({
          type: 'GET',
          url: 'api/data/field/' + data.id,
          success: function(fields) {
            setFields(fields ? fields : [])
          }
        })
        ref.setState({visible: false})
      }
      
      let childState = {
        list: {onDbClick: callback}
      }
      ref.setState({visible: true, childState: childState})
    },
    
    renderYesOrNo: (val, dataIndex) => {
      let yn = urmsc.convertYN(val)
      return (
        <Form.Item>{getFieldDecorator(dataIndex, {initialValue: yn})(
          <Select size="small">
            <Select.Option key="y" value="Y">Yes</Select.Option>
            <Select.Option key="n" value="N">No</Select.Option>
          </Select>)}
        </Form.Item>
      );
    },
  }
  
  let rowSelection = {
    columnWidth: 40,
    onSelect: (record, selected, selectedRows, nativeEvent) => {
      let tmp = []
      selectedRows.forEach((it) => {
        tmp.push(it.sno)
      })
      item.subRule.selectedKeys = tmp
    },
    onSelectAll: (selected, selectedRows, changeRows) => {
      let tmp = []
      selectedRows.forEach((it) => {
        tmp.push(it.sno)
      })
      item.subRule.selectedKeys = tmp
    },
  }
  
  return (
    <div className="urm-editor">
      <div className="editor-buttons">
        <Button onClick={method.clickSave} disabled={!props.isPageSave()}>{locale['label.save']}</Button>
      </div>
      <Form colon={false}>
        <div className="row">
          <Form.Item label={locale['label.dataId']}>{getFieldDecorator("id")(<Input size="small" className="size-id" readOnly />)}</Form.Item>
          <Form.Item label={locale['label.dataName']}>{getFieldDecorator("name")(<Input size="small" style={{width: "220px"}} />)}</Form.Item>
        </div>
        
        <hr />
        
        <div className="row">
          <div>
            <Button icon="file-excel" onClick={method.addByExcel} disabled={fields.length > 0} title="Excel 로 등록" />
            <Button icon="database" onClick={method.addByQuery} disabled={fields.length > 0} title="Query 로 등록" />
            <Button icon="copy" onClick={method.copyData} disabled={fields.length > 0} title="데이터구조 복사" />
          </div>
          <div className="flex-right">
            <Button icon="plus-square" onClick={method.clickAppend} title="Field 추가" />
            <Button icon="minus-square" onClick={method.clickRemove} title={locale['label.delete']} />
          </div>
        </div>
        <EditableField form={props.form} item={fields} rowSelection={rowSelection} />
      </Form>
      
      <ExcelData ref={(editor) => { excel = editor }} />
      
      <QueryData ref={(editor) => { query = editor }} codeList={props.codeList} />
      
      <SubModal ref={(list) => { copyList = list }} width="1000px">
        <CopyData key="list" codeList={props.codeList} />
      </SubModal>
      
      <SubModal ref={(list) => { usedList = list }} width="900px">
        <UsedData key="list" onlySearch={false} />
      </SubModal>
    </div>
  );
}

const WrappedDataEditor = Form.create({name:'data_editor'})(DataEditorForm)
export default DataEditor