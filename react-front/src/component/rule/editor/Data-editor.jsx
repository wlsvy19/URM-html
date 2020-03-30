import React, { useState, useEffect } from 'react'
import { Button, Modal, Table, message } from 'antd'
import { Form, Input, Select } from 'antd'

import RuleEditor from './RuleEditor'
import SubModal from '@/component/SubModal'
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
    item.children = {}
    // TODO subscribe...
    // eslint-disable-next-line
  }, [item]);

  let excel = null
  let query = null
  let copyList = null
  let usedList = null

  let method = {
    clickSave: e => {
      item.children.fields = fields
      let saveItem = method.makeDataObj()
      
      if (saveItem.id && saveItem.id.length > 0) {
        method.checkData(saveItem)
      } else {
        props.save(saveItem)
      }
    },
    
    makeDataObj: () => {
      let saveItem = props.makeRuleObj(form, item)
      saveItem.fields = item.children.fields
      
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
        saveItem.channelCodd = 'EAI'
        props.save(saveItem)
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
      let selectedKeys = item.children.selectedKeys
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
          url: 'api/field/' + data.id,
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
      return (
        <Form.Item>{getFieldDecorator(dataIndex, {initialValue: urmsc.convertYN(val)})(
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
      item.children.selectedKeys = tmp
    },
    onSelectAll: (selected, selectedRows, changeRows) => {
      let tmp = []
      selectedRows.forEach((it) => {
        tmp.push(it.sno)
      })
      console.log(tmp)
      item.children.selectedKeys = tmp
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
        <Table className="table-striped" rowClassName="editable-row"
          dataSource={fields} pagination={false} bordered
          size={"small"} scroll={{ y: 500 }} rowKey="sno" rowSelection={rowSelection}>
          <Table.Column title={locale['label.index']} dataIndex="sno" width="65px" align="center"/>
          <Table.Column title={locale['label.fieldName']} dataIndex="engName" width="150px"
            render={(val, record, idx) => <Form.Item>{getFieldDecorator("fields["+idx+"].engName", {initialValue: val})(<Input size="small" />)}</Form.Item>}/>
          <Table.Column title={locale['label.fieldLocalName']} dataIndex="name" width="150px"
            render={(val, record, idx) => <Form.Item>{getFieldDecorator("fields["+idx+"].name", {initialValue: val})(<Input size="small" />)}</Form.Item>}/>
          <Table.Column title={locale['label.fieldType']} dataIndex="type" width="110px"
            render={(val, record, idx) => <Form.Item>{getFieldDecorator("fields["+idx+"].type", {initialValue: val})(
              <Select size="small">
                <Select.Option key="c" value="C">Character</Select.Option>
                <Select.Option key="n" value="N">Number</Select.Option>
                <Select.Option key="d" value="D">Date</Select.Option>
                <Select.Option key="b" value="B">Binary</Select.Option>
              </Select>)}
            </Form.Item>}/>
          <Table.Column title={locale['label.dFormat']} dataIndex="dateFormat"
            render={(val, record, idx) => <Form.Item>{getFieldDecorator("fields["+idx+"].dateFormat", {initialValue: val})(<Input size="small" />)}</Form.Item>}/>
          <Table.Column title={locale['label.len']} dataIndex="length"
            render={(val, record, idx) => <Form.Item>{getFieldDecorator("fields["+idx+"].length", {initialValue: val})(<Input size="small" />)}</Form.Item>}/>
          <Table.Column title={locale['label.nullable']} dataIndex="nullable" width="75px" render={(val, record, idx) => method.renderYesOrNo(val, 'fields['+idx+'].nullable')}/>
          <Table.Column title={locale['label.isKey']} dataIndex="keyYN" width="85px" render={(val, record, idx) => method.renderYesOrNo(val, 'fields['+idx+'].keyYN')}/>
          <Table.Column title={locale['label.isSQL']} dataIndex="sqlYN" width="100px" render={(val, record, idx) => method.renderYesOrNo(val, 'fields['+idx+'].sqlYN')}/>
        </Table>
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