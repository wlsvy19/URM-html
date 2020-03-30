import React, { useState } from 'react'
import { Button, Modal, Table } from 'antd'
import { Form, Input, Select, Upload } from 'antd'

import * as urmsc from '@/urm-utils'

const locale = urmsc.locale

class ExcelEditor extends React.Component {
  state = {
    visible: false,
  }

  shouldComponentUpdate(nextProps, nextState) {
    if (this.state.visible !== nextState.visible) {
      return true
    }
    return false
  }

  method = {
    handleCancel: e => {
      this.setState({visible: false})
    },
  }

  childMethod = {
    confirm: (fields) => {
      console.log(fields)
    },
  }

  render() {
    return (
      <Modal visible={this.state.visible} width="1000px"
        footer={null} onCancel={this.method.handleCancel} className="urm-modal">
        <WrappedExcelEditor {...this.childMethod} />
      </Modal>
    );
  }
}

const ExcelEditorForm = (props) => {
  const { form } = props
  const { getFieldDecorator } = props.form
  const [ fields, setFields ] = useState([])

  let method = {
    clickConfirm: e => {
      props.confirm(fields)
    },
    
    preventAction: (file) => {
      return false
    },
    
    uploadFile: e => {
      return e && e.file
    },
    
    getFields: e => {
      // application/vnd.openxmlformats-officedocument.spreadsheetml.sheet : xlsx
      let file = form.getFieldValue('fileName')
      let sheetName = form.getFieldValue('sheetName')
      
      let formData = new FormData();
      formData.append('sheetName', encodeURIComponent(sheetName))
      formData.append('file', file, file.name)
      
      urmsc.ajax({
        type: 'POST',
        url: 'api/data/field/excel',
        data: formData,
        contentType: false,
        success: function(res) {
          setFields(res)
        }
      })
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

  return (
    <div className="urm-editor">
      <div className="editor-buttons">
        <Button onClick={method.clickConfirm}>{locale['label.confirm']}</Button>
      </div>
      <Form colon={false}>
        <div className="row">
          <Form.Item label={locale['label.fileName']}>
            {getFieldDecorator('fileName', {
              valuePropName: 'file', getValueFromEvent: method.uploadFile
            })(<Upload beforeUpload={method.preventAction} className="urm-upload" accept="application/vnd.ms-excel">
              <Button icon="select" size="small" />
            </Upload>)}
          </Form.Item>
          <Form.Item label={locale['label.fileSelect']}>{getFieldDecorator("sheetName")(<Input size="small" style={{width: "220px"}} />)}</Form.Item>
          <Form.Item><Button icon="upload" size="small" onClick={method.getFields} title={locale['label.excelUpload']} /></Form.Item>
        </div>
        
        <hr />
        
        <Table className="table-striped" rowClassName="editable-row"
          dataSource={fields} pagination={false} bordered
          size={"small"} scroll={{ y: 500 }} rowKey="sno">
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
    </div>
  );
}

const WrappedExcelEditor = Form.create({name:'excel_editor'})(ExcelEditorForm)
export default ExcelEditor