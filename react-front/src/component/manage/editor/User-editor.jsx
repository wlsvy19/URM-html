import React, { useEffect } from 'react'
import { Button, Modal, message } from 'antd'
import { Form, Input, Select } from 'antd'

import * as urmsc from '../../../urm-utils'

class Usereditor extends React.Component {
  state = {
    visible: false,
    item: {},
  }

  method = {
    handleCancel: e => {
      this.setState({ visible: false })
    },

    validator: (data) => {
      if (!data.id || data.id.trim().length === 0) {
        message.warning('Please enter your ID')
        return false
      } else { //TODO 정규식 체크
      }
      if (!data.name || data.name.trim().length === 0) {
        message.warning('Please enter your Name')
        return false
      }
      if (!data.password || data.password.trim().length === 0) {
        message.warning('Please enter your Password')
        return false
      } else if (data.password !== data.confirm) {
        message.error('Password do not match!')
        return false
      }

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
      console.log(saveItem)
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

    idCheck: (param) => { //param
      console.log('param: ', param)
      urmsc.ajax({
        type: 'POST',
        url: '/URM/user/check',
        data: param,
        success: function (id) {
          console.log('data', id)
        },
      })
  
      var data = 1;
      if (data === 1) {
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

  }

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
          <Form.Item label="Mobile" style={{ marginLeft: "60px" }}>{getFieldDecorator("grade")(<Input size="small" className="user-input" />)}</Form.Item>
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
