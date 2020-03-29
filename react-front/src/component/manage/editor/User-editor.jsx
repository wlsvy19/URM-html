import React, { useEffect } from 'react'
import { Button, Modal, message, Form, Input, Select } from 'antd'


import * as urmsc from '../../../urm-utils'

class Usereditor extends React.Component {

  constructor(props){
    super(props);
  }

  state = {
    visible: false,
    item: {},
    isIdValid : false,
    readOnly: false,
    data: this.props.data
  }

  method = {
    handleCancel: e => {
      this.setState({ visible: false })
    },

    validator: (data) => { //click save button
      console.log(data)
      const idRegExp = /^[0-9a-zA-Z]*$/; //Regex: alphabet or number
      //const idRegex = /^[A-z0-9]*$/; //Regex: alphabet or number
      console.log(data.getFieldsValue().id)
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
    
    isId: (data) =>{
      if(this.state.data === 1){       
        return( 
          <WrappedUserEditor authList={this.props.authList} item={this.state.item} {...this.userMethod} />
        ) 
      }else{
        return(
          <WrappedUserAddForm authList={this.props.authList} item={this.state.item} {...this.userMethod} />
        )
      }

    }
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

        {this.method.isId()}

        {/* <WrappedUserEditor authList={this.props.authList} item={this.state.item} {...this.userMethod} /> */}
        {/* <WrappedUserEditor2 authList={this.props.authList} item={this.state.item} {...this.userMethod} />
         */}
      </Modal>
    );
  }
}

const UserAddForm = (props) => {
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
        <div>
          <Form.Item>{getFieldDecorator("idCheck")(<Button onClick={method.idCheck} size="small" style={{ marginLeft: "8px", marginTop: "9px" } }>Check</Button>)}</Form.Item>          
        </div>
      )
    },

    idCheck: (param) => { 
      let inputData = form.getFieldsValue() 
      urmsc.ajax({
        type: 'GET',
        url: '/URM/user/check',
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
        <Button onClick={method.clickSave}>Save</Button>
      </div>

      <Form colon={false}>
        <div className="row">
          <Form.Item label="ID" >{getFieldDecorator("id")(<Input size="small" className="user-input" />)}</Form.Item>
          {method.idCheckButton()}
          {/* <Form.Item>{getFieldDecorator("idCheck")(<Button onClick={method.idCheck} size="small" style={{ marginLeft: "8px", marginTop: "9px" } }>Check</Button>)}</Form.Item>
           */}
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

const UserEditor = (props) => {
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
        <div>
          <Form.Item>{getFieldDecorator("idCheck")(<Button onClick={method.idCheck} size="small" style={{ marginLeft: "8px", marginTop: "9px" } }>Check</Button>)}</Form.Item>          
        </div>
      )
    },

  } //end let method

  return (
    <div className="urm-editor">
      <div style={{ textAlign: "right", marginRight: "20px" }}>
        <Button onClick={method.clickSave}>Save</Button>
      </div>

      <Form colon={false}>
        <div className="row">
          <Form.Item label="ID" >{getFieldDecorator("id")(<Input size="small" className="user-input" readOnly />)}</Form.Item>
          <Form.Item label="Name" style={{ marginLeft: "60px" }}>{getFieldDecorator("name")(<Input size="small" className="user-input" />)}</Form.Item>
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

const WrappedUserAddForm = Form.create({ name: 'user_editor' })(UserAddForm)

const WrappedUserEditor = Form.create({ name: 'user_editor' })(UserEditor)
export default Usereditor
