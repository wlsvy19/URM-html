import React, { useEffect } from 'react'
import { Button, Modal } from 'antd'
import { Form, Input, Select } from 'antd'

import RuleEditor from './RuleEditor'
import * as urmsc from '../../../urm-utils'

const locale = urmsc.locale

const KINDS = urmsc.CODEKEY

class SystemEditor extends RuleEditor {
  render() {
    return (
      <Modal visible={this.state.visible} width="1068px"
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
    item.subRule = {
      dbType: item.dbType,
      jdbcType: item.jdbcType,
      dbName: item.dbName,
      dbParams: item.dbParams
    }
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
      
      saveItem.dbType = undefined
      saveItem.jdbcType = undefined
      saveItem.dbName = undefined
      saveItem.dbParams = undefined
      if (saveItem.type === '2') {
        let children = item.subRule
        saveItem.dbType = children.dbType
        saveItem.jdbcType = children.jdbcType
        saveItem.dbName = children.dbName
        saveItem.dbParams = children.dbParams
      }
      
      props.save(saveItem)
    },
    
    renderDBSystem: () => {
      let type = form.getFieldValue('type')
      let children = item.subRule
      if (children && type === '2') {
        let dbTypeProps = {initialValue: !children.dbType ? '1' : children.dbType}
        let jdbcTypeProps = {initialValue: !children.jdbcType ? '1' : children.jdbcType}
        let dbNameProps = {initialValue: !children.dbName ? '' : children.dbName}
        let dbParamsProps = {initialValue: !children.dbParams ? '' : children.dbParams}
        
        if (item.type !== '2') {
          let fields = form.getFieldsValue()
          if (fields.dbType) {
            children.dbType = fields.dbType
            children.jdbcType = fields.jdbcType
            children.dbName = fields.dbName
            children.dbParams = fields.dbParams
          }
        }
        
        return (
          <div>
            <div className="row">
              <Form.Item label={locale['label.dbType']}>
                {getFieldDecorator("dbType", dbTypeProps)(<Select size="small" className="size-id">
                  {method.renderOpts("dbType")}
                </Select>)}
              </Form.Item>
              <Form.Item label={locale['label.jdbcType']}>
                {getFieldDecorator("jdbcType", jdbcTypeProps)(<Select size="small" className="size-id">
                  <Select.Option value="1">1</Select.Option>
                  <Select.Option value="2">2</Select.Option>
                  <Select.Option value="3">3</Select.Option>
                  <Select.Option value="4">4</Select.Option>
                </Select>)}
              </Form.Item>
              <Form.Item label={locale['label.dbName']}>{getFieldDecorator("dbName", dbNameProps)(<Input size="small" className="size-name" />)}</Form.Item>
            </div>
            <div className="row">
              <Form.Item label={locale['label.connectParam']}>{getFieldDecorator("dbParams", dbParamsProps)(<Input.TextArea className="urm-remark" />)}</Form.Item>
            </div>
          </div>
        );
      }
      return undefined
    },

  }

  return (
    <div className="urm-editor">
      <div className="editor-buttons">
        <Button onClick={method.clickSave} disabled={!props.isPageSave()}>{locale['label.save']}</Button>
      </div>
      <Form colon={false}>
        <div className="row">
          <Form.Item label={locale['label.systemId']}>{getFieldDecorator("id")(<Input size="small" className="size-id" readOnly />)}</Form.Item>
          <Form.Item label={locale['label.systemCd']}>{getFieldDecorator("code")(<Input size="small" className="size-id" />)}</Form.Item>
          <Form.Item label={locale['label.systemName']}>{getFieldDecorator("name")(<Input size="small" className="size-name" />)}</Form.Item>
        </div>
        <div className="row">
          <Form.Item label={locale['label.systemType']}>
            {getFieldDecorator("type", {initialValue: "1"})(<Select size="small" className="size-id">
              {method.renderOpts("sysType")}
            </Select>)}
          </Form.Item>
          <Form.Item label={locale['label.devType']}>
            {getFieldDecorator("devType", {initialValue: "1"})(<Select size="small" className="size-id">
              {method.renderOpts("devType")}
            </Select>)}
          </Form.Item>
        </div>
        <div className="row">
          <Form.Item label={locale['label.host']}>{getFieldDecorator("hostId")(<Input size="small" className="size-id" />)}</Form.Item>
          <Form.Item label={locale['label.ip']}>{getFieldDecorator("ip")(<Input size="small" className="size-id" />)}</Form.Item>
          <Form.Item label={locale['label.port']}>{getFieldDecorator("port")(<Input size="small" className="size-name" />)}</Form.Item>
        </div>
        <div className="row">
          <Form.Item label={locale['label.id']}>{getFieldDecorator("userId")(<Input size="small" className="size-id" />)}</Form.Item>
          <Form.Item label={locale['label.pass']}>{getFieldDecorator("userPasswd")(<Input.Password size="small" className="size-id" visibilityToggle={false} />)}</Form.Item>
        </div>
        {method.renderDBSystem()}
        <div className="row">
          <Form.Item label={locale['label.etcRemark']}>{getFieldDecorator("remark")(<Input.TextArea className="urm-remark" />)}</Form.Item>
        </div>
      </Form>
    </div>
  );
}

const WrappedSystemEditor = Form.create({name:'system_editor'})(SystemEditorForm)
export default SystemEditor