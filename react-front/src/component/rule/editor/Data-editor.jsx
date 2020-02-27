import React, { useState, useEffect } from 'react'
import { Button, Modal, Table } from 'antd'
import { Form, Input, Select } from 'antd'

import RuleEditor from './RuleEditor'
import {default as urmUtils} from '../../../urm-utils'

class DataEditor extends RuleEditor {
  constructor(props) {
    super(props)
    this.state.path = 'data'
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
      <Modal visible={this.state.visible} onCancel={this.method.handleCancel} width="1080px" footer={null} >
        <WrappedDataEditor item={this.state.item} {...this.childMethod} />
      </Modal>
    );
  }
}

const DataEditorForm = (props) => {
  const { form, item } = props
  const { getFieldDecorator } = props.form
  const [ fields, setFields ] = useState([])
  //TODO dbinfo remain...

  // Only re-run the effect if props.item changes
  useEffect(() => {
    props.setItem(form, item)
    if (item.fields) setFields(item.fields)
    // TODO subscribe...
    // eslint-disable-next-line
  }, [item]);

  let method = {
    clickSave: e => {
      console.log(form.getFieldsValue(), item)
      //props.save(form, fields, method.makeSaveItem)
    },
    
    clickAppend: e => {
      let items = Object.assign([], fields)
      items.push({
        sno: (fields.length + 1),
        type: 'C',
        nullable: true,
        keyYN: false,
        sqlYN: true,
      })
      setFields(items)
    },
    
    renderYesOrNo: (val) => {
      return (
        <Select value={urmUtils.convertYN(val)}>
          <Select.Option key="y" value="Y">Yes</Select.Option>
          <Select.Option key="n" value="N">No</Select.Option>
        </Select>
      );
    },
  }

  return (
    <div className="urm-editor">
      <div style={{textAlign: "right", marginRight: "20px"}}>
        <Button onClick={method.clickSave}>Save</Button>
      </div>
      <Form colon={false}>
        <div className="row">
          <Form.Item label="Data ID">{getFieldDecorator("id")(<Input size="small" className="size-id" readOnly />)}</Form.Item>
          <Form.Item label="Data Name">{getFieldDecorator("name")(<Input size="small" style={{width: "220px"}} />)}</Form.Item>
        </div>
      </Form>
      <div>
        <div><Button onClick={method.clickAppend}>Append</Button></div>
        <Table className="table-striped"
          dataSource={fields} pagination={false} bordered
          size={"small"} scroll={{ y: 500 }} rowKey="sno">
          <Table.Column title="Index" dataIndex="sno" width="55px"/>
          <Table.Column title="ENG Name" dataIndex="engName" width="150px" render={(val) => <Input size="small" value={val} />}/>
          <Table.Column title="KOR Name" dataIndex="name" width="150px" render={(val) => <Input size="small" value={val} />}/>
          <Table.Column title="Type" dataIndex="type" width="110px" render={(val) => (
            <Select value={val}>
              <Select.Option key="c" value="C">Character</Select.Option>
              <Select.Option key="n" value="N">Number</Select.Option>
              <Select.Option key="d" value="D">Date</Select.Option>
              <Select.Option key="b" value="B">Binary</Select.Option>
            </Select>
          )}/>
          <Table.Column title="DateFormat" dataIndex="dateFormat" render={(val) => <Input size="small" value={val} />}/>
          <Table.Column title="Length" dataIndex="length" render={(val) => <Input size="small" value={val} />}/>
          <Table.Column title="Nullable" dataIndex="nullable" width="75px" render={(val) => method.renderYesOrNo(val)}/>
          <Table.Column title="IsKey" dataIndex="keyYN" width="70px" render={(val) => method.renderYesOrNo(val)}/>
          <Table.Column title="IsSql" dataIndex="sqlYN" width="70px" render={(val) => method.renderYesOrNo(val)}/>
        </Table>
      </div>
    </div>
  );
}

const WrappedDataEditor = Form.create({name:'data_editor'})(DataEditorForm)
export default DataEditor