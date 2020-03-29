import React from 'react'

import * as urmsc from '../../urm-utils'
import UserList from './list/User-list'
import UserEditor from './editor/User-editor'

class User extends React.Component {
  constructor(props) {
    super(props)
    this.state = {
      path: 'user'
    }
  }

  method = {
    handleAdd: (id) => {
      //edit
      console.log('path: ' + this.state.path)
      if (id) {
        let $this = this
        urmsc.ajax({
          type: 'GET',
          url: '/URM/' + this.state.path + '/' + id,
          
          success: function(obj) {
            let $editor = $this.refs.editor
            console.log(obj)
            $editor.setState({visible: true, item: obj, readOnly:false, data:0})
          }
        })
      } else {
        this.refs.editor.setState({visible: true, item: {}})
      }
    },

    handleEdit: (id) => {
      //edit
      console.log('path: ' + this.state.path)
      if (id) {
        let $this = this
        urmsc.ajax({
          type: 'GET',
          url: '/URM/' + this.state.path + '/' + id,
          
          success: function(obj) {
            let $editor = $this.refs.editor
            console.log(obj)
            $editor.setState({visible: true, item: obj, readOnly:false, data:1})
  
          }
        })
      } else {
        this.refs.editor.setState({visible: true, item: {}})
      }
    },

  }
  
  getAuth = () => {
    let authList = this.props.authCode
    return authList ? authList : []
  }
  
  render() {
    return (
      <div className="urm-panel" >
        <UserList ref="list" path={this.state.path} authList={this.getAuth()} add={this.method.handleAdd} edit={this.method.handleEdit}/>
        <UserEditor ref="editor" path={this.state.path} authList={this.getAuth()} />
      </div>
    );
  }
}

export default User

