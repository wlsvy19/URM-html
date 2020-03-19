import React from 'react'

import * as urmsc from '../../urm-utils'
import UserList from './list/User-list'
import UserEditor from './editor/User-editor'

const AUTH = []

class User extends React.Component {
  constructor(props) {
    super(props)
    this.state = {
      path: 'user'
    }
    this.method.setCode()
  }

  componentDidMount() {
    console.log('mounted')
  }

  method = {
    setCode: () => {
      if (AUTH.length === 0) {
        urmsc.ajax({
          type: 'GET',
          url:  '/URM/code/auth',
          success: function(res) {
            res.forEach((it) => {
              AUTH.push(it)
            })
          },
        })
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
            $editor.setState({visible: true, item: obj})
          }
        })
      } else {
        this.refs.editor.setState({visible: true, item: {}})
      }
    },
  }

  render() {
    return (
      <div className="urm-panel" >
        <UserList ref="list" path={this.state.path} authList={AUTH} edit={this.method.handleEdit} />
        <UserEditor ref="editor" path={this.state.path} authList={AUTH} />
      </div>
    );
  }
}

export default User

