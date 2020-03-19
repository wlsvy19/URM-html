import React, { useEffect } from 'react'
import { Button, Modal } from 'antd'
import { Form, Input, Select } from 'antd'

import RuleEditor from './RuleEditor'
import * as urmsc from '../../../urm-utils'

const KINDS = urmsc.CODEKEY

class SystemEditor extends RuleEditor {
  render() {
    return (
      <Modal visible={this.state.visible} width="1080px"
          footer={null} onCancel={this.method.handleCancel} className="urm-modal">
        <WrappedSystemEditor codeList={this.props.codeList} item={this.state.item} {...this.childMethod} />
      </Modal>
    );
  }
}

const SystemEditorForm = (props) => {
  const { form, item } = props
  const { getFieldDecorator } = form

  // Only re-run the effect if props.item changes
  useEffect(() => {
    props.setItem(form, item)
    // TODO subscribe...
    // eslint-disable-next-line
  }, [item]);

  let method = {
    renderOpts: (key) => {
      return urmsc.getSubListByKey(props.codeList, 'kind', KINDS[key])
              .map((it) => <Select.Option key={it.code} value={it.code}>{it.name}</Select.Option>)
    },
    
    clickSave: e => {
      let saveItem = props.makeRuleObj(form, item)
      props.save(saveItem)
    },
    
    renderDBSystem: () => {
      let type = form.getFieldValue('type')
      if (type === '2') {
        let dbTypeProps = {initialValue: '1'}
        let jdbcTypeProps = {initialValue: '1'}
        let dbNameProps = {}
        let dbParamsProps = {}
        
        if (item.type === '2') {
          dbTypeProps.initialValue = !item.dbType ? '1' : item.dbType
          jdbcTypeProps.initialValue = !item.jdbcType ? '1' : item.jdbcType
          dbNameProps.initialValue = !item.dbName ? '' : item.dbName
          dbParamsProps.initialValue = !item.dbParams ? '' : item.dbParams
        } else {
          let fields = form.getFieldsValue()
          if (fields.dbType) {
            item.dbType = fields.dbType
            item.jdbcType = fields.jdbcType
            item.dbName = fields.dbName
            item.dbParams = fields.dbParams
          }
        }
        
        return (
          <div>
            <div className="row">
              <Form.Item label="DBType">
                {getFieldDecorator("dbType", dbTypeProps)(<Select size={"small"} className="size-id">
                  {method.renderOpts("dbType")}
                </Select>)}
              </Form.Item>
              <Form.Item label="JDBC Type">
                {getFieldDecorator("jdbcType", jdbcTypeProps)(<Select size={"small"} className="size-id">
                  <Select.Option value="1">1</Select.Option>
                  <Select.Option value="2">2</Select.Option>
                  <Select.Option value="3">3</Select.Option>
                  <Select.Option value="4">4</Select.Option>
                </Select>)}
              </Form.Item>
              <Form.Item label="DBName">{getFieldDecorator("dbName", dbNameProps)(<Input size="small" className="size-id" />)}</Form.Item>
            </div>
            <div className="row">
              <Form.Item label="Parameter">{getFieldDecorator("dbParams", dbParamsProps)(<Input.TextArea className="urm-remark" />)}</Form.Item>
              <Form.Item label="Remark">{getFieldDecorator("remark")(<Input.TextArea className="urm-remark" />)}</Form.Item>
            </div>
          </div>
        );
      }
      return (<div className="row">
        <Form.Item label="Remark">{getFieldDecorator("remark")(<Input.TextArea className="urm-remark" />)}</Form.Item>
      </div>);
    },
  }

  return (
    <div className="urm-editor">
      <div className="editor-buttons">
        <Button onClick={method.clickSave}>Save</Button>
      </div>
      <Form colon={false}>
        <div className="row">
          <Form.Item label="System ID">{getFieldDecorator("id")(<Input size="small" className="size-id" readOnly />)}</Form.Item>
          <Form.Item label="System Code">{getFieldDecorator("code")(<Input size="small" className="size-id" />)}</Form.Item>
          <Form.Item label="System Name">{getFieldDecorator("name")(<Input size="small" style={{width: "220px"}} />)}</Form.Item>
        </div>
        <div className="row">
          <Form.Item label="Type">
            {getFieldDecorator("type", {initialValue: "1"})(<Select size={"small"} className="size-id">
              {method.renderOpts("sysType")}
            </Select>)}
          </Form.Item>
          <Form.Item label="DevType">
            {getFieldDecorator("devType", {initialValue: "1"})(<Select size={"small"} className="size-id">
              {method.renderOpts("devType")}
            </Select>)}
          </Form.Item>
        </div>
        <div className="row">
          <Form.Item label="Host">{getFieldDecorator("hostId")(<Input size="small" className="size-id" />)}</Form.Item>
          <Form.Item label="IP">{getFieldDecorator("ip")(<Input size="small" className="size-id" />)}</Form.Item>
          <Form.Item label="Port">{getFieldDecorator("port")(<Input size="small" className="size-id" />)}</Form.Item>
        </div>
        <div className="row">
          <Form.Item label="User ID">{getFieldDecorator("userId")(<Input size="small" className="size-id" />)}</Form.Item>
          <Form.Item label="Password">{getFieldDecorator("userPasswd")(<Input.Password size="small" className="size-id" visibilityToggle={false} />)}</Form.Item>
        </div>
        {method.renderDBSystem()}
      </Form>
    </div>
  );
}

const WrappedSystemEditor = Form.create({name:'system_editor'})(SystemEditorForm)
export default SystemEditor