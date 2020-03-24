import React, { useEffect } from 'react'
import { Button, Modal, message } from 'antd'
import { Form, Input, Select } from 'antd'

import * as urmsc from '../../../urm-utils'

class Usereditor extends React.Component {
  state = {
    visible: false,
    item: {},
    isIdValid : false,
  }

  method = {
    handleCancel: e => {
      this.setState({ visible: false })
    },

    validator: (data) => {
      const idRegExp = /^[0-9a-zA-Z]*$/; //Regex: alphabet or number
      //const idRegex = /^[A-z0-9]*$/; //Regex: alphabet or number

      if (!data.getFieldsValue().id || data.getFieldsValue().id.trim().length === 0) {
        message.warning('Please enter your ID')
        return false

      } else if (!data.getFieldsValue().id.match(idRegExp)){
        message.error('ID only use alphabet or number')
        return false
      }
      else if (!data.getFieldsValue().name || data.getFieldsValue().name.trim().length === 0) {
        message.warning('Please enter your Name')
        return false

      } else if (!data.getFieldsValue().password || data.getFieldsValue().password.trim().length === 0) {
        message.warning('Please enter your Password')
        return false

      } else if (data.getFieldsValue().password !== data.getFieldsValue().confirm) {
        message.error('Password do not match!')
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
      console.log('saveItem : ',saveItem)
      if (!this.method.validator(saveItem)) {
        return false
      }

      urmsc.ajax({
        type: 'POST',
        url: '/URM/' + this.props.path,
        data: JSON.stringify(saveItem),
        contentType: 'application/json; charset=UTF-8',
        success: function (res) {
          console.log(res)
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

    idCheck: (param) => { 
      console.log('넘어가는 data: ', form.getFieldValue('id'))
      //id를 jsonstringfy
      urmsc.ajax({
        type: 'POST',
        url: '/URM/user/check',
        data: JSON.stringify(form.getFieldValue('id')),
        contentType: 'application/json; charset=UTF-8',
        success: function (data) {
          console.log('data', data)
        },
        error: function(error) {
          console.log('ajax error')
        }
      })

      var data = 1;
      if (data === 2) {
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
      } else
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
    },
  } //end let method

  return (
    <div className="urm-editor">
      <div style={{ textAlign: "right", marginRight: "20px" }}>
        <Button onClick={method.clickSave}>Save</Button>
      </div>

      <Form colon={false}>
        <div className="row">
          <Form.Item label="ID" >{getFieldDecorator("id")(<Input size="small" className="user-input" />)}</Form.Item>
          <Button onClick={method.idCheck} size="small" style={{ marginLeft: "8px", marginTop: "9px" }}>Check</Button>
          <Form.Item label="Name">{getFieldDecorator("name")(<Input size="small" className="user-input" />)}</Form.Item>
        </div>

        <div className="row">
          <Form.Item label="Password">{getFieldDecorator("password")(<Input.Password size="small" className="user-input" />)}</Form.Item>
          <Form.Item label="Check Password" style={{ marginLeft: "60px" }}>{getFieldDecorator("confirm")(<Input.Password size="small" style={{ width: "230px" }} />)}</Form.Item>
        </div>

        <div className="row">
          <Form.Item label="Dept">{getFieldDecorator("dept")(<Input size="small" className="user-input" />)}</Form.Item>
          <Form.Item label="Position" style={{ marginLeft: "60px" }}>{getFieldDecorator("position")(<Input size="small" className="user-input" />)}</Form.Item>
        </div>

        <div className="row">
          <Form.Item label="Grade">{getFieldDecorator("grade")(<Input size="small" className="user-input" />)}</Form.Item>
          <Form.Item label="General Tel" style={{ marginLeft: "60px" }}>{getFieldDecorator("dept")(<Input size="small" className="user-input" />)}</Form.Item>
        </div>

        <div className="row">
          <Form.Item label="Office Tel">{getFieldDecorator("position")(<Input size="small" className="user-input" />)}</Form.Item>
          <Form.Item label="Mobile" style={{ marginLeft: "60px" }}>{getFieldDecorator("celNo")(<Input size="small" className="user-input" />)}</Form.Item>
        </div>

        <div className="row">
          <Form.Item label="Auth Code">
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
export default Usereditor
