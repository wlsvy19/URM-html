import React, { useEffect } from 'react'
import { Button, Modal, Form, Input, Select } from 'antd'
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
  }

  childMethod = {
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
  }

  render() {
    return (
      <Modal visible={this.state.visible} width="1200px"
          footer={null} onCancel={this.method.handleCancel} className="urm-modal">
        <WrappedUserEditor authList={this.props.authList} item={this.state.item} {...this.childMethod} />
      </Modal>
    );
  }
}


const UserEditorForm = (props) => {

  const { form, item } = props
  const { getFieldDecorator } = form

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
      return props.authList.map((it) => <Select.Option key={it.id} value={it.name}>{it.name}</Select.Option>)
    },

    clickSave: e => {
      console.log(form.getFieldsValue(), item)
      //props.save(form, fields, method.makeSaveItem)
    },
  }

  return (
    <div className="urm-editor">
      <div style={{ textAlign: "right", marginRight: "20px" }}>
        <Button onClick={method.clickSave}>Save</Button>
      </div>

      <Form colon={false}>
        <div className="row">
          <Form.Item label="ID">{getFieldDecorator("id")(<Input size="small" style={{ width: "220px" }} />)}</Form.Item>
          <Form.Item label="Name">{getFieldDecorator("name")(<Input size="small" style={{ width: "220px" }} />)}</Form.Item>
        </div>

        <div className="row">
          <Form.Item label="Password">{getFieldDecorator("passwd")(<Input.Password size="small" style={{ width: "230px" }} />)}</Form.Item>
          <Form.Item label="Check Password">{getFieldDecorator("chpasswd")(<Input.Password size="small" style={{ width: "230px" }} />)}</Form.Item>
        </div>

        <div className="row">
          <Form.Item label="Dept">{getFieldDecorator("dept")(<Input size="small" style={{ width: "230px" }} />)}</Form.Item>
          <Form.Item label="Position">{getFieldDecorator("position")(<Input size="small" style={{ width: "230px" }} />)}</Form.Item>
          <Form.Item label="Grade">{getFieldDecorator("grade")(<Input size="small" style={{ width: "230px" }} />)}</Form.Item>
        </div>

        <div className="row">
          <Form.Item label="General Tel">{getFieldDecorator("dept")(<Input size="small" style={{ width: "230px" }} />)}</Form.Item>
          <Form.Item label="Office Tel">{getFieldDecorator("position")(<Input size="small" style={{ width: "230px" }} />)}</Form.Item>
          <Form.Item label="Mobile">{getFieldDecorator("grade")(<Input size="small" style={{ width: "230px" }} />)}</Form.Item>
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
