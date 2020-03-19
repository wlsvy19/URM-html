import React from 'react'
import * as urmsc from '../../urm-utils'

import RequestList from './list/Request-list'
import RequestEditor from './editor/Request-editor'
import DataList from './list/Data-list'
import DataEditor from './editor/Data-editor'
import SystemList from './list/System-list'
import SystemEditor from './editor/System-editor'


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
          url: '/URM/' + this.state.path + '/' + id,
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
    
    handleSave: (data, comp) => {
      let $this = this
      urmsc.ajax({
        type: 'POST',
        url: '/URM/' + this.state.path,
        data: JSON.stringify(data),
        contentType: 'application/json; charset=UTF-8',
        success: function(res) {
          let $editor = $this.refs.editor
          console.log(res)
          $editor.setState({item: res})
          
          let $list = $this.refs.list
          $list.method._updateItems(LIST_STATE.UPDATE, res)
        }
      })
    }
  }
  
  getCode = () => {
    let codeList = this.props.commonCode
    return codeList ? codeList : []
  }
  
  components = {
    RequestList: RequestList,
    RequestEditor: RequestEditor,
    DataList: DataList,
    DataEditor: DataEditor,
    SystemList: SystemList,
    SystemEditor: SystemEditor,
  }
}
