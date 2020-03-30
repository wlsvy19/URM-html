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
      const idRegExp = /^[0-9a-zA-Z]*$/; //Regex: alphabet or number
      
      if (!data.getFieldsValue().id || data.getFieldsValue().id.trim().length === 0) {
        message.warning('[' + locale['label.id'] + '] ' + locale['message.1015'])
        return false

      } else if (!data.getFieldsValue().id.match(idRegExp)){
        message.error('유효하지 않은 아이디입니다.')
        return false
      }
      else if (!data.getFieldsValue().name || data.getFieldsValue().name.trim().length === 0) {
        message.warning('[' + locale['label.name'] + '] ' +locale['message.1015'])
        return false

      } else if (!data.getFieldsValue().password || data.getFieldsValue().password.trim().length === 0) {
        message.warning('[' + locale['label.pass'] + '] ' +locale['message.1015'])
        return false

      } else if (data.getFieldsValue().password !== data.getFieldsValue().confirm) {
        message.error(locale['message.1016'])
        return false
      } 
      else
        return true
    },
  }

  userMethod = {
    setItem: (form, item, customSetter) => {
      if (item.id) {
        let fields = form.getFieldsValue()
        let formItem = {}
        for (let key in fields) {
          formItem[key] = item[key]
        }
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

      let $this = this
      urmsc.ajax({
        type: 'POST',
        url: 'api/' + this.props.path,
        data: JSON.stringify(saveItem),
        contentType: 'application/json; charset=UTF-8',
        success: function (res) {
          console.log(res)
          $this.setState({item: res})
        }
      })
    },

  }

  render() {
    return (
      <Modal visible={this.state.visible} width="900px"
          footer={null} onCancel={this.method.handleCancel} className="urm-modal">
        <WrappedUserEditor authList={this.props.authList} item={this.state.item} {...this.userMethod} />
      </Modal>
    );
  }
}

const UserEditorForm = (props) => {
  const { form, item } = props
  const { getFieldDecorator } = form
  const { confirm } = Modal

  // Only re-run the effect if props.item changes
  useEffect(() => {
    props.setItem(form, item)
    // eslint-disable-next-line
  }, [item]);

  let method = {
    renderOpts: () => {
      return props.authList.map((it) => <Select.Option key={it.id} value={it.id}>{it.name}</Select.Option>)
    },

    clickSave: e => {
      console.log(form.getFieldsValue(), item)
      props.save(form)
    },

    idCheckButton: () =>{
      return (
        !item.id && <Button onClick={method.idCheck} size="small" style={{ marginLeft: "5px", marginTop: "9px" } }>Check</Button>
      )
    },

    idCheck: (param) => { 
      let inputData = form.getFieldsValue() 
      urmsc.ajax({
        type: 'GET',
        url: 'api/user/check',
        data: JSON.stringify(inputData),
        success: function (data) {
          if(data === 1) { //duplicate
            confirm({
              title: 'Duplicate ID',
              content: 'Please use a other ID',
              okText: 'Yes',
              okType: 'danger',
              okButtonProps: {
                disabled: true,
              },
              cancelText: 'Cancel',
              onOk() {
                console.log('OK');
              },
              onCancel() {
                console.log('Cancel');
              },
            });
          } else{
            confirm({
              title: 'Not Duplicate ID',
              content: 'Use ID?',
              onOk() {
                console.log('OK');
              },
              onCancel() {
                console.log('Cancel');
              },
            });
          }
        },
        error: function(error) {
          console.log('ajax error')
        }
      })
    },
  } //end let method

  return (
    <div className="urm-editor">
      <div style={{ textAlign: "right", marginRight: "20px" }}>
        <Button onClick={method.clickSave}>{locale['label.save']}</Button>
      </div>

      <Form colon={false}>
        <div className="row">
          <Form.Item label={locale['label.id']} className="col-1">
            {getFieldDecorator("id")(<Input size="small" className="user-input" readOnly={item.id} />)}
            {method.idCheckButton()}
          </Form.Item>
          <Form.Item label={locale['label.name']} className="col-1">{getFieldDecorator("name")(<Input size="small" className="user-input" />)}</Form.Item>
        </div>

        <div className="row">
          <Form.Item label={locale['label.pass']}>
            {getFieldDecorator("password")(<Input.Password size="small" className="user-input" visibilityToggle={false} />)}
          </Form.Item>
          <Form.Item style={{marginLeft: "5px"}}>
            {getFieldDecorator("confirm")(<Input.Password size="small" style={{width: "420px"}} visibilityToggle={false} />)}
          </Form.Item>
        </div>

        <div className="row">
          <Form.Item label={locale['label.dept']} className="col-1">{getFieldDecorator("deptName")(<Input size="small" className="user-input" />)}</Form.Item>
          <Form.Item label={locale['label.position']} className="col-1">{getFieldDecorator("positionName")(<Input size="small" className="user-input" />)}</Form.Item>
        </div>

        <div className="row">
          <Form.Item label={locale['label.grade']} className="col-1">{getFieldDecorator("gradeName")(<Input size="small" className="user-input" />)}</Form.Item>
          <Form.Item label={locale['label.generalTel']} className="col-1">{getFieldDecorator("generalTelNo")(<Input size="small" className="user-input" />)}</Form.Item>
        </div>

        <div className="row">
          <Form.Item label={locale['label.officeTel']} className="col-1">{getFieldDecorator("officeTelNo")(<Input size="small" className="user-input" />)}</Form.Item>
          <Form.Item label={locale['label.mobile']} className="col-1">{getFieldDecorator("celNo")(<Input size="small" className="user-input" />)}</Form.Item>
        </div>

        <div className="row">
          <Form.Item label={locale['label.auth']}>
            {getFieldDecorator("authId", { initialValue: "0" })(<Select size={"small"} className="user-input">
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
