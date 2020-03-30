import React from 'react'
import { message } from 'antd'
import * as urmsc from '@/urm-utils'

const locale = urmsc.locale
const LIST_STATE = urmsc.LIST_STATE

export default class RuleMain extends React.Component {
  componentDidMount() {
    console.log('mounted')
  }

  method = {
    handleEdit: (id) => {
      if (id) {
        let $this = this
        urmsc.ajax({
          type: 'GET',
          url: 'api/' + this.state.path + '/' + id,
          success: function(res) {
            let $editor = $this.refs.editor
            console.log(res)
            $editor.setState({visible: true, item: res})
          }
        })
      } else {
        this.refs.editor.setState({visible: true, item: {}})
      }
    },
    
    handleSave: (data) => {
      let $this = this
      urmsc.ajax({
        type: 'POST',
        url: 'api/' + this.state.path,
        data: JSON.stringify(data),
        contentType: 'application/json; charset=UTF-8',
        success: function(res) {
          let $editor = $this.refs.editor
          console.log(res)
          $editor.setState({item: res})
          
          let $list = $this.refs.list
          $list.method._updateItems(LIST_STATE.UPDATE, res)
          message.success(locale['message.0001'])
        }
      })
    }
  }
  
  codeList = () => {
    let codeList = this.props.commonCode
    return codeList ? codeList : []
  }
}
