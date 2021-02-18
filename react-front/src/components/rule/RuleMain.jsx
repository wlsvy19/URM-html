import React from 'react'
import { message } from 'antd'
import * as urmsc from '@/urm-utils'

const locale = urmsc.locale
const LIST_STATE = urmsc.LIST_STATE

export default class RuleMain extends React.Component {
  method = {
    handleEdit: (id) => {
      if (id) {
        let _editor = this.refs.editor
        urmsc.ajax({
          type: 'GET',
          url: 'api/' + this.state.path + '/' + id,
          success: function(res) {
            console.log(res)
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
          // let res = JSON.parse(xhr.response)
          message.warning('Update Fail. ' + xhr.statusText)
        }
      })
    }
  }
  
  codeList = () => {
    let codeList = this.props.commonCode
    return codeList ? codeList : []
  }
}
