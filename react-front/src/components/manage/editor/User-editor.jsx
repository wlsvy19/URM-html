import React, { useEffect } from 'react'
import { Button, Modal, message } from 'antd'
import { Form, Input, Select } from 'antd'

import * as urmsc from '@/urm-utils'

const locale = urmsc.locale

class UserEditor extends React.Component {
  state = {
    visible: false,
    item: {}
  }

  method = {
    handleCancel: e => {
      this.setState({ visible: false })
    },

    validator: (data) => {
      if (!data.id || data.id.trim().length === 0) {
        message.warning('[' + locale['label.id'] + '] ' + locale['message.1015'])
        return false
      } else if (!data.name || data.name.trim().length === 0) {
        message.warning('[' + locale['label.name'] + '] ' +locale['message.1015'])
        return false
      } else if (!this.state.item.id || data.password) {
        if (!data.password || data.password.trim().length === 0) {
          message.warning('[' + locale['label.pass'] + '] ' +locale['message.1015'])
          return false
        } else if (data.password !== data.confirm) {
          message.error(locale['message.1016'])
          return false
        }
      }
      return true
    },
  }

  userMethod = {
    setItem: (form, item) => {
      if (item.id) {
        let fields = form.getFieldsValue()
        let formItem = {}
        for (let key in fields) {
          formItem[key] = item[key]
        }
        formItem.password = ''
        form.setFieldsValue(formItem)
      } else {
        form.resetFields()
      }
    },

    save: (saveItem) => {
      console.log('saveItem : ', saveItem)
      if (!this.method.validator(saveItem)) {
        return false
      }

      this.props.save(saveItem)
    },

    isPageSave: () => {
      return urmsc.isPageEdit(this.props.path, 'save', this.props.userInfo.auth)
    }
  }

  render() {
    return (
      <Modal visible={this.state.visible} width="875px"
          footer={null} onCancel={this.method.handleCancel} className="urm-modal">
        <WrappedUserEditor authList={this.props.authList} item={this.state.item} {...this.userMethod} />
      </Modal>
    );
  }
}

const UserEditorForm = (props) => {
  const { form, item } = props
  const { getFieldDecorator } = form

  useEffect(() => {
    props.setItem(form, item)
    // eslint-disable-next-line
  }, [item]); // Only re-run the effect if props.item changes

  let chkId = null
  let modal = null

  let method = {
    renderOpts: () => {
      return props.authList.map((it) => <Select.Option key={it.id} value={it.id}>{it.name}</Select.Option>)
    },

    clickSave: e => {
      props.save(form.getFieldsValue())
    },

    popupCheckID: () => {
      if (item.id) return false
      let content = <div className="row">
        <Input ref={input => { chkId = input }} size="small" style={{width: 220}} />
        <Button size="small" onClick={method.checkID}>{locale['label.idConfirm']}</Button>
      </div>

      modal = Modal.confirm({
        title: locale['label.modifyUserInfo'],
        content: content,
        width: 390,
        cancelText: 'NO',
        okText: locale['label.idUse'],
        okButtonProps: {
          disabled: true,
          size: 'small',
        },
        cancelButtonProps: {
          size: 'small',
        },
        onOk() {
          form.setFieldsValue({id: chkId.state.value})
        }
      });
    },
    
    checkID: () => {
      // eslint-disable-next-line
      const idRegExp = /^[0-9a-zA-Z_\-\.]*$/; //Regex: alphabet or number
      modal.update({okButtonProps: {
        disabled: true,
        size: 'small',
      }})
      
      let id = chkId.state.value
      if (!id || id.trim().length === 0) {
        return false
      } else if (!id.match(idRegExp)){
        message.error('유효하지 않은 아이디입니다.')
        return false
      }

      urmsc.ajax({
        type: 'GET',
        url: 'api/user/check',
        data: JSON.stringify({id: id}),
        success: function (data) {
          if(data > 0) { //duplicate
            message.warning(locale['message.2002'])
          } else {
            modal.update({okButtonProps: {
              disabled: false,
              size: 'small',
            }})
            message.warning(locale['message.2001'])
          }
        }
      })
    },
  } //end let method

  return (
    <div className="urm-editor">
      <div className="editor-buttons">
        <Button onClick={method.clickSave} disabled={!props.isPageSave()}>{locale['label.save']}</Button>
      </div>

      <Form colon={false}>
        <div className="row">
          <Form.Item label={locale['label.id']}>
            {getFieldDecorator("id")(<Input size="small" className="user-input" readOnly onClick={method.popupCheckID} />)}
          </Form.Item>
          <Form.Item label={locale['label.name']}>{getFieldDecorator("name")(<Input size="small" className="user-input" />)}</Form.Item>
        </div>

        <div className="row">
          <Form.Item label={locale['label.pass']}>
            {getFieldDecorator("password")(<Input.Password size="small" className="user-input" visibilityToggle={false} />)}
          </Form.Item>
          <Form.Item style={{marginLeft: 5}}>
            {getFieldDecorator("confirm")(<Input.Password size="small" style={{width: 375}} visibilityToggle={false} />)}
          </Form.Item>
        </div>

        <div className="row">
          <Form.Item label={locale['label.dept']}>{getFieldDecorator("deptName")(<Input size="small" className="user-input" />)}</Form.Item>
          <Form.Item label={locale['label.position']}>{getFieldDecorator("positionName")(<Input size="small" className="user-input" />)}</Form.Item>
        </div>

        <div className="row">
          <Form.Item label={locale['label.grade']}>{getFieldDecorator("gradeName")(<Input size="small" className="user-input" />)}</Form.Item>
          <Form.Item label={locale['label.generalTel']}>{getFieldDecorator("generalTelNo")(<Input size="small" className="user-input" />)}</Form.Item>
        </div>

        <div className="row">
          <Form.Item label={locale['label.officeTel']}>{getFieldDecorator("officeTelNo")(<Input size="small" className="user-input" />)}</Form.Item>
          <Form.Item label={locale['label.mobile']}>{getFieldDecorator("celNo")(<Input size="small" className="user-input" />)}</Form.Item>
        </div>

        <div className="row">
          <Form.Item label={locale['label.auth']}>
            {getFieldDecorator("authId", { initialValue: "0" })(<Select size="small" className="user-input">
              {method.renderOpts()}
            </Select>)}
          </Form.Item>
        </div>

      </Form>
    </div>
  )
}

const WrappedUserEditor = Form.create({ name: 'user_editor' })(UserEditorForm)
export default UserEditor
