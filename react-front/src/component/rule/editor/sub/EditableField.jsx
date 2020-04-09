import React from 'react'
import { Table, Form, Input, Select } from 'antd'

import * as urmsc from '@/urm-utils'

const locale = urmsc.locale

const EditableField = (props) => {
  const { form, item, ...restProps } = props
  const { getFieldDecorator } = form

  let method = {
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

  return (
    <Table className="table-striped" rowClassName="editable-row"
      dataSource={item} pagination={false} bordered
      size="small" scroll={{ y: 500 }} rowKey="sno" {...restProps}>
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
      <Table.Column title={locale['label.isSQL']} dataIndex="sqlYN" width="110px" render={(val, record, idx) => method.renderYesOrNo(val, 'fields['+idx+'].sqlYN')}/>
    </Table>
  );
}

export default EditableField