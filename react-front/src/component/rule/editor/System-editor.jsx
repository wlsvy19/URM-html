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
        let dbTypeProps = {initialValue: !item.dbType ? '1' : item.dbType}
        let jdbcTypeProps = {initialValue: !item.jdbcType ? '1' : item.jdbcType}
        let dbNameProps = {initialValue: !item.dbName ? '' : item.dbName}
        
        if (item.type !== '2') {
          let fields = form.getFieldsValue()
          if (fields.dbType) {
            item.dbType = fields.dbType
            item.jdbcType = fields.jdbcType
            item.dbName = fields.dbName
          }
        }
        
        return (
          <div className="row">
            <Form.Item label={locale['label.dbType']}>
              {getFieldDecorator("dbType", dbTypeProps)(<Select size={"small"} className="size-id">
                {method.renderOpts("dbType")}
              </Select>)}
            </Form.Item>
            <Form.Item label={locale['label.jdbcType']}>
              {getFieldDecorator("jdbcType", jdbcTypeProps)(<Select size={"small"} className="size-id">
                <Select.Option value="1">1</Select.Option>
                <Select.Option value="2">2</Select.Option>
                <Select.Option value="3">3</Select.Option>
                <Select.Option value="4">4</Select.Option>
              </Select>)}
            </Form.Item>
            <Form.Item label={locale['label.dbName']}>{getFieldDecorator("dbName", dbNameProps)(<Input size="small" className="size-id" />)}</Form.Item>
          </div>
        );
      }
      return undefined
    },

    renderDBSystem2: () => {
      let type = form.getFieldValue('type')
      if (type === '2') {
        let dbParamsProps = {}
        
        if (item.type === '2') {
          dbParamsProps.initialValue = !item.dbParams ? '' : item.dbParams
        } else {
          let fields = form.getFieldsValue()
          if (fields.dbType) {
            item.dbParams = fields.dbParams
          }
        }
        
        return (
          <Form.Item label={locale['label.connectParam']}>{getFieldDecorator("dbParams", dbParamsProps)(<Input.TextArea className="urm-remark" />)}</Form.Item>
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
          <Form.Item label={locale['label.systemName']}>{getFieldDecorator("name")(<Input size="small" style={{width: "220px"}} />)}</Form.Item>
        </div>
        <div className="row">
          <Form.Item label={locale['label.systemType']}>
            {getFieldDecorator("type", {initialValue: "1"})(<Select size={"small"} className="size-id">
              {method.renderOpts("sysType")}
            </Select>)}
          </Form.Item>
          <Form.Item label={locale['label.devType']}>
            {getFieldDecorator("devType", {initialValue: "1"})(<Select size={"small"} className="size-id">
              {method.renderOpts("devType")}
            </Select>)}
          </Form.Item>
        </div>
        <div className="row">
          <Form.Item label={locale['label.host']}>{getFieldDecorator("hostId")(<Input size="small" className="size-id" />)}</Form.Item>
          <Form.Item label={locale['label.ip']}>{getFieldDecorator("ip")(<Input size="small" className="size-id" />)}</Form.Item>
          <Form.Item label={locale['label.port']}>{getFieldDecorator("port")(<Input size="small" className="size-id" />)}</Form.Item>
        </div>
        <div className="row">
          <Form.Item label={locale['label.id']}>{getFieldDecorator("userId")(<Input size="small" className="size-id" />)}</Form.Item>
          <Form.Item label={locale['label.pass']}>{getFieldDecorator("userPasswd")(<Input.Password size="small" className="size-id" visibilityToggle={false} />)}</Form.Item>
        </div>
        {method.renderDBSystem()}
        <div className="row">
          {method.renderDBSystem2()}
          <Form.Item label={locale['etcRemark']}>{getFieldDecorator("remark")(<Input.TextArea className="urm-remark" />)}</Form.Item>
        </div>
      </Form>
    </div>
  );
}

const WrappedSystemEditor = Form.create({name:'system_editor'})(SystemEditorForm)
export default SystemEditor