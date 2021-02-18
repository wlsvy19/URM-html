import React, { useState } from 'react'
import { Button, Modal } from 'antd'
import { Form, Input, Select, Upload } from 'antd'

import EditableField from './EditableField'
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
    handleCancel: (form) => {
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
      <Modal visible={this.state.visible} width="1000px" zIndex="1001" destroyOnClose
        footer={null} onCancel={e => { this.method.handleCancel() }} className="urm-modal">
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
      let saveItem = method.makeFieldObj()
      props.confirm(saveItem)
    },
    
    makeFieldObj: () => {
      let saveItem = [...fields]
      
      let fieldForm = form.getFieldValue('fields')
      saveItem.forEach((it, idx) => {
        let field = fieldForm[idx]
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
    
    preventAction: (file) => {
      return false
    },
    
    uploadFile: e => {
      return e && [e.file]
    },
    
    getFields: e => {
      // application/vnd.openxmlformats-officedocument.spreadsheetml.sheet : xlsx
      let file = form.getFieldValue('fileName')
      let sheetName = form.getFieldValue('sheetName')
      
      if (!sheetName || sheetName.trim().length === 0) {
        return false
      }
      
      let formData = new FormData();
      formData.append('sheetName', encodeURIComponent(sheetName))
      formData.append('file', file[0], file[0].name)
      
      urmsc.ajax({
        type: 'POST',
        url: 'api/data/field/excel',
        data: formData,
        contentType: false,
        success: function(res) {
          setFields(res)
        },
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
              valuePropName: 'fileList', getValueFromEvent: method.uploadFile
            })(<Upload beforeUpload={method.preventAction} className="urm-upload" accept="application/vnd.ms-excel">
              <Button icon="select" size="small" />
            </Upload>)}
          </Form.Item>
          <Form.Item label={locale['label.fileSelect']}>{getFieldDecorator("sheetName")(<Input size="small" style={{width: "220px"}} />)}</Form.Item>
          <Form.Item><Button icon="upload" size="small" onClick={method.getFields} title={locale['label.excelUpload']} /></Form.Item>
        </div>
        
        <hr />
        
        <EditableField form={props.form} item={fields} />
      </Form>
    </div>
  );
}

const WrappedExcelEditor = Form.create({name:'excel_editor'})(ExcelEditorForm)
export default ExcelEditor