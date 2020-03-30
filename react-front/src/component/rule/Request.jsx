import React from 'react'

import RuleMain from './RuleMain'
import RequestList from './list/Request-list'
import RequestEditor from './editor/Request-editor'

export default class Request extends RuleMain {
  constructor (props) {
    super(props)
    this.state = {
      path: 'request'
    }
  }
  
  authList = () => {
    let authList = this.props.authCode
    return authList ? authList : []
  }
  
  render() {
    return (
      <div className="urm-panel">
        <RequestList ref="list" path={this.state.path} userInfo={this.props.userInfo}
          codeList={this.codeList()} authList={this.authList()} edit={this.method.handleEdit} />
        <RequestEditor ref="editor" path={this.state.path} userInfo={this.props.userInfo}
          codeList={this.codeList()} authList={this.authList()} save={this.method.handleSave} />
      </div>
    );
  }
}
