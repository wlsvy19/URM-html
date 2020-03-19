import React, { useEffect, useState } from 'react'
import { Button, Modal, Form, Input, Select, message } from 'antd'
import { Link } from 'react-router-dom';
import * as urmsc from '../../../urm-utils'

class Usereditor extends React.Component {

  constructor(props) {
    super(props);
  }

  state = {
    visible: false,
    item: {},
  }

  method = {
    handleCancel: e => {
      this.setState({ visible: false })
    },


    validator: (data) => {
      console.log('validator()')

      if (!data.id || data.id.trim().length === 0) {
        console.log('1')
        return false
      }
      if (!data.name || data.name.trim().length === 0) {
        console.log('2')
        return false
      }
      if (!data.password || data.password.trim().length === 0) {
        console.log('3')
        return false
      }
      if (!data.confirm || data.confirm.trim().length === 0) {
        console.log('4')
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
        if (customSetter) customSetter(formItem, item)
        form.setFieldsValue(formItem)
      } else {
        form.resetFields()
      }
    },

    save: (form, children, customSetter) => {
      let data = form.getFieldsValue()
      let saveItem = {}

      if (customSetter) customSetter(saveItem, data, children)
      if (!this.method.validator(data)) {
        return false
      }

      urmsc.ajax({
        type: 'POST',
        url: '/URM/' + this.props.path,
        data: JSON.stringify(data),
        contentType: 'application/json',
        success: function (res) {
          console.log(this.response)
        }
      })
    },

  }

  render() {
    return (
      <Modal visible={this.state.visible} width="1200px" footer={null} onCancel={this.method.handleCancel} className="urm-modal">
        <WrappedUserEditor authList={this.props.authList} item={this.state.item} {...this.userMethod} />
      </Modal>
    );
  }
}

const UserEditorForm = (props) => {
  const { form, item } = props
  const { getFieldDecorator } = form

  const { password, setPassword } = useState('')
  const { passwordCheck, setPasswordCheck } = useState('')
  const { setPasswordError } = useState(false)
  const { confirm } = Modal

  // Only re-run the effect if props.item changes
  useEffect(() => {
    props.setItem(form, item, method.initialSetter)
    // eslint-disable-next-line
  }, [item]);

  let method = {
    // renderOpts: (key) => {
    //   return urmsc.getSubListByKey(props.authList, 'kind', urmsc.CODEKEY[key]) //list, key, value
    //     .map((it) => <Select.Option key={it.code} value={it.code}>{it.name}</Select.Option>)
    // },
    renderOpts: () => {
      return props.authList.map((it) => <Select.Option key={it.id}>{it.name}</Select.Option>)
    },

    clickSave: e => {
      console.log(form.getFieldsValue(), item)

      const inputId = form.getFieldsValue().id
      const inputName = form.getFieldsValue().name
      const inputPassword = form.getFieldsValue().password

      console.log(inputId)
      console.log(inputName)
      console.log(inputPassword)

      if (!inputId) {
        message.warning('Please enter your ID')
      } else if (!inputName) {
        message.warning('Please enter your Name')
      } else if (!inputPassword) {
        message.warning('Please enter your Password')
      } else if (password !== passwordCheck) {
        message.error('Password do not match!')
      }

      props.save(form)
    },

    idCheck: (param) => { //param
      console.log('param: ', param)
      urmsc.ajax({
        type: 'POST',
        url: '/URM/check',
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
  const onChangePassword = (e) => {
    setPassword(e.target.value);
  }

  const onChangePasswordChk = (e) => {
    setPasswordError(e.target.value !== password)
    setPasswordCheck(e.target.value);
  }

   

  return (
    <div className="urm-editor">
      <div style={{ textAlign: "right", marginRight: "20px" }}>
        <Button onClick={method.clickSave}>Save</Button>
      </div>

      <Form colon={false}>
        <div className="row">
          <Form.Item label="ID" >{getFieldDecorator("id")(<Input size="small" style={{ width: "230px" }} />)}</Form.Item>
          <Button onClick={method.idCheck} size="small" style={{ marginLeft: "8px", marginTop: "9px" }}>Check</Button>
          <Form.Item label="Name">{getFieldDecorator("name")(<Input size="small" style={{ width: "230px" }} />)}</Form.Item>
        </div>

        <div className="row">
          <Form.Item label="Password">{getFieldDecorator("password")(<Input.Password onChange={onChangePassword} size="small" style={{ width: "230px" }} />)}</Form.Item>
          <Form.Item label="Check Password" style={{ marginLeft: "60px" }}>{getFieldDecorator("confirm")(<Input.Password onChange={onChangePasswordChk} size="small" style={{ width: "230px" }} />)}</Form.Item>
        </div>

        <div className="row">
          <Form.Item label="Dept">{getFieldDecorator("dept")(<Input size="small" style={{ width: "230px" }} />)}</Form.Item>
          <Form.Item label="Position" style={{ marginLeft: "60px" }}>{getFieldDecorator("position")(<Input size="small" style={{ width: "230px" }} />)}</Form.Item>
        </div>

        <div className="row">
          <Form.Item label="Grade">{getFieldDecorator("grade")(<Input size="small" style={{ width: "230px" }} />)}</Form.Item>
          <Form.Item label="General Tel" style={{ marginLeft: "60px" }}>{getFieldDecorator("dept")(<Input size="small" style={{ width: "230px" }} />)}</Form.Item>
        </div>

        <div className="row">
          <Form.Item label="Office Tel">{getFieldDecorator("position")(<Input size="small" style={{ width: "230px" }} />)}</Form.Item>
          <Form.Item label="Mobile" style={{ marginLeft: "60px" }}>{getFieldDecorator("grade")(<Input size="small" style={{ width: "230px" }} />)}</Form.Item>
        </div>

        <div className="row">
          <Form.Item label="Auth Code">
            {getFieldDecorator("auth", { initialValue: "URM ADMIN" })(<Select size={"small"} style={{ width: "230px" }}>
              {method.renderOpts("")}
            </Select>)}
          </Form.Item>
        </div>

      </Form>
    </div>
  )
}

const WrappedUserEditor = Form.create({ name: 'user_editor' })(UserEditorForm)
export default Usereditor
