import React from 'react'
import { message } from 'antd'

import UserList from './list/User-list'
import UserEditor from './editor/User-editor'
import * as urmsc from '@/urm-utils'

const locale = urmsc.locale
const LIST_STATE = urmsc.LIST_STATE

export default class User extends React.Component {
  constructor(props) {
    super(props)
    this.state = {
      path: 'user'
    }
  }

  method = {
    handleEdit: (id) => {
      if (id) {
        let _editor = this.refs.editor
        urmsc.ajax({
          type: 'GET',
          url: 'api/' + this.state.path + '/' + id,
          success: function(res) {
            _editor.setState({visible: true, item: res})
          }
        })
      } else {
        this.refs.editor.setState({visible: true, item: {}})
      }
    },
    
    handleSave: (data) => {
      let _editor = this.refs.editor
      let _list = this.refs.list
      urmsc.ajax({
        type: 'POST',
        url: 'api/' + this.state.path,
        data: JSON.stringify(data),
        contentType: 'application/json; charset=UTF-8',
        success: function(res) {
          console.log(res)
          _editor.setState({item: res})
          
          _list.method._updateItems(LIST_STATE.UPDATE, res)
          message.success(locale['message.0001'])
        },
        error: function(xhr) {
          message.warning(locale['message.1001'] + ' [' + xhr.statusText + ']')
        }
      })
    }
  }
  
  authList = () => {
    let authList = this.props.authCode
    return authList ? authList : []
  }
  
  render() {
    return (
      <div className="urm-panel">
        <UserList ref="list" path={this.state.path} userInfo={this.props.userInfo}
          authList={this.authList()} edit={this.method.handleEdit} />
        <UserEditor ref="editor" path={this.state.path} userInfo={this.props.userInfo}
          authList={this.authList()} save={this.method.handleSave} />
      </div>
    );
  }
}
