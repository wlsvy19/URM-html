import React, { useEffect } from 'react'
import { Button, Modal } from 'antd'
import { Form, Input, Select } from 'antd'

import RuleEditor from './RuleEditor'
import {default as urmUtils} from '../../../urm-utils'

const KINDS = urmUtils.codeKey

class SystemEditor extends RuleEditor {
  constructor(props) {
    super(props)
    this.state.path = 'system'
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
        <WrappedSystemEditor codeList={this.props.codeList} item={this.state.item} {...this.childMethod} />
      </Modal>
    );
  }
}

const SystemEditorForm = (props) => {
  const { form, item } = props
  const { getFieldDecorator } = form
  //TODO dbinfo remain...

  // Only re-run the effect if props.item changes
  useEffect(() => {
    props.setItem(form, item)
    // TODO subscribe...
    // eslint-disable-next-line
  }, [item]);

  let method = {
    renderOpts: (key) => {
      return urmUtils.getSubListByKey(props.codeList, 'kind', KINDS[key])
              .map((it) => <Select.Option key={it.code} value={it.code}>{it.name}</Select.Option>)
    },
    
    clickSave: e => {
      console.log(form.getFieldsValue())
      props.save(form)
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
            </div>
          </div>
        );
      }
      return undefined
    },
  }

  return (
    <div className="urm-editor">
      <div style={{textAlign: "right", marginRight: "20px"}}>
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
          <Form.Item label="Password">{getFieldDecorator("password")(<Input.Password size="small" className="size-id" visibilityToggle={false} />)}</Form.Item>
        </div>
        {method.renderDBSystem()}
        <div className="row">
          <Form.Item label="Remark">{getFieldDecorator("remark")(<Input.TextArea className="urm-remark" />)}</Form.Item>
        </div>
      </Form>
    </div>
  );
}

const WrappedSystemEditor = Form.create({name:'system_editor'})(SystemEditorForm)
export default SystemEditor