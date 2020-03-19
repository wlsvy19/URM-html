import React, { useState } from 'react'
import { Button, Modal, Table } from 'antd'
import { Form, Input, Select } from 'antd'

import SubModal from '../../SubModal'
import SystemList from '../../list/System-list'

import * as urmsc from '../../../../urm-utils'

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
      props.confirm(fields)
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
      let param = {
        systemId: form.getFieldValue('systemId'),
        type: form.getFieldValue('type'),
        query: form.getFieldValue('query'),
      }
      urmsc.ajax({
        type: 'GET',
        url: '/URM/data/field/query',
        data: param,
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
        <Button onClick={method.clickConfirm}>Confirm</Button>
      </div>
      <Form colon={false}>
        <div className="row">
          <Form.Item label="System ID">
            {getFieldDecorator("systemId")(<Input.Search size="small" className="size-id" readOnly onSearch={method.setSystem} />)}
          </Form.Item>
          <Form.Item label="Type">{getFieldDecorator("type", {initialValue: 0})(
            <Select size="small" className="size-id">
              <Select.Option key="0" value={0}>Table</Select.Option>
              <Select.Option key="1" value={1}>Query</Select.Option>
            </Select>)}
          </Form.Item>
          <Form.Item className="flex-right"><Button onClick={method.getStructure}>Get Structure</Button></Form.Item>
        </div>
        <div className="row">
          <Form.Item className="data-query" extra={<p>테이블 명을 넣어주세요. [TABLENAME] 또는 [OWNER].[TABLENAME] 또는 [DATABASE].[OWNER].[TABLENAME]<br/>TB_ABC_TABLE 또는 EAIOWN.TB_ABC_TABLE 또는 UD_MCI01.EAIOWN.TB_ABC_TABLE</p>}>
            {getFieldDecorator("query")(<Input.TextArea />)}
          </Form.Item>
        </div>
        
        <hr />
        
        <Table className="table-striped" rowClassName="editable-row"
          dataSource={fields} pagination={false} bordered
          size={"small"} scroll={{ y: 500 }} rowKey="sno">
          <Table.Column title="Index" dataIndex="sno" width="55px" align="center"/>
          <Table.Column title="ENG Name" dataIndex="engName" width="150px"
            render={(val, record, idx) => <Form.Item>{getFieldDecorator("fields["+idx+"].engName", {initialValue: val})(<Input size="small" />)}</Form.Item>}/>
          <Table.Column title="KOR Name" dataIndex="name" width="150px"
            render={(val, record, idx) => <Form.Item>{getFieldDecorator("fields["+idx+"].name", {initialValue: val})(<Input size="small" />)}</Form.Item>}/>
          <Table.Column title="Type" dataIndex="type" width="110px"
            render={(val, record, idx) => <Form.Item>{getFieldDecorator("fields["+idx+"].type", {initialValue: val})(
              <Select size="small">
                <Select.Option key="c" value="C">Character</Select.Option>
                <Select.Option key="n" value="N">Number</Select.Option>
                <Select.Option key="d" value="D">Date</Select.Option>
                <Select.Option key="b" value="B">Binary</Select.Option>
              </Select>)}
            </Form.Item>}/>
          <Table.Column title="DateFormat" dataIndex="dateFormat"
            render={(val, record, idx) => <Form.Item>{getFieldDecorator("fields["+idx+"].dateFormat", {initialValue: val})(<Input size="small" />)}</Form.Item>}/>
          <Table.Column title="Length" dataIndex="length"
            render={(val, record, idx) => <Form.Item>{getFieldDecorator("fields["+idx+"].length", {initialValue: val})(<Input size="small" />)}</Form.Item>}/>
          <Table.Column title="Nullable" dataIndex="nullable" width="75px" render={(val, record, idx) => method.renderYesOrNo(val, 'fields['+idx+'].nullable')}/>
          <Table.Column title="IsKey" dataIndex="keyYN" width="70px" render={(val, record, idx) => method.renderYesOrNo(val, 'fields['+idx+'].keyYN')}/>
          <Table.Column title="IsSql" dataIndex="sqlYN" width="70px" render={(val, record, idx) => method.renderYesOrNo(val, 'fields['+idx+'].sqlYN')}/>
        </Table>
      </Form>
            
      <SubModal ref={(list) => { sysList = list }} width="1120px">
        <SystemList key="list" path="system" codeList={props.codeList} onlySearch={true} />
      </SubModal>
    </div>
  );
}

const WrappedQueryEditor = Form.create({name:'query_editor'})(QueryEditorForm)
export default QueryEditor