import React, { useState } from 'react'
import { Button, Modal, message } from 'antd'
import { Form, Input, Select } from 'antd'

import EditableField from './EditableField'
import SubModal from '@/component/SubModal'
import SystemList from '@/component/rule/list/System-list'

import * as urmsc from '@/urm-utils'

const locale = urmsc.locale

class QueryEditor extends React.Component {
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
        <WrappedQueryEditor codeList={this.props.codeList} {...this.childMethod} />
      </Modal>
    );
  }
}

const QueryEditorForm = (props) => {
  const { form } = props
  const { getFieldDecorator } = props.form
  const [ fields, setFields ] = useState([])

  let sysList = null

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
    
    setSystem: (type) => {
      let ref = sysList
      let callback = (system) => {
        console.log(form)
        form.setFieldsValue({systemId: system.id})
        ref.setState({visible: false})
      }
      
      let childState = {
        list: { onDbClick: callback, scparam: {type: '2'} }
      }
      ref.setState({visible: true, childState: childState})
    },

    getStructure: () => {
      let systemId = form.getFieldValue('systemId')
      let type = form.getFieldValue('type')
      let query = form.getFieldValue('query')
      if(!systemId || systemId.trim().length === 0) {
        message.warning("시스템을 선택하여 주세요.")
      }
      if(!query || query.trim().length === 0) {
        if(type === 0) {
          message.warning("테이블 명을 넣어주세요.")
        } else {
          message.warning("SELECT 문을 넣어주세요.")
        }
        return
      }
      let param = {
        systemId: systemId,
        type: type,
        query: query,
      }
      urmsc.ajax({
        type: 'GET',
        url: 'api/data/field/query',
        data: param,
        success: function(res) {
          setFields(res)
        },
        error: function(xhr) {
          message.warning(xhr.statusText)
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
          <Form.Item label="시스템">
            {getFieldDecorator("systemId")(<Input.Search size="small" className="size-id" readOnly onSearch={method.setSystem} />)}
          </Form.Item>
          <Form.Item label="Type">{getFieldDecorator("type", {initialValue: 0})(
            <Select size="small" className="size-id">
              <Select.Option key="0" value={0}>Table</Select.Option>
              <Select.Option key="1" value={1}>Query</Select.Option>
            </Select>)}
          </Form.Item>
          <Form.Item className="flex-right"><Button onClick={method.getStructure}>구조 가져오기</Button></Form.Item>
        </div>
        <div className="row">
          <Form.Item className="data-query" extra={<p>테이블 명을 넣어주세요. [TABLENAME] 또는 [OWNER].[TABLENAME] 또는 [DATABASE].[OWNER].[TABLENAME]<br/>TB_ABC_TABLE 또는 EAIOWN.TB_ABC_TABLE 또는 UD_MCI01.EAIOWN.TB_ABC_TABLE</p>}>
            {getFieldDecorator("query")(<Input.TextArea />)}
          </Form.Item>
        </div>
        
        <hr />
        
        <EditableField form={props.form} item={fields} />
      </Form>
            
      <SubModal ref={(list) => { sysList = list }} width="1280px">
        <SystemList key="list" path="system" codeList={props.codeList} onlySearch={true} />
      </SubModal>
    </div>
  );
}

const WrappedQueryEditor = Form.create({name:'query_editor'})(QueryEditorForm)
export default QueryEditor