import React from 'react'
import RuleMain from './RuleMain'

export default class Request extends RuleMain {
  constructor (props) {
    super(props)
    this.state = {
      path: 'request'
    }
  }
  
  getAuth = () => {
    let authList = this.props.authCode
    return authList ? authList : []
  }
  
  render() {
    const { RequestList, RequestEditor} = this.components
    
    return (
      <div className="urm-panel">
        <RequestList ref="list" path={this.state.path}
          codeList={this.getCode()} authList={this.getAuth()} edit={this.method.handleEdit} />
        <RequestEditor ref="editor" path={this.state.path}
          codeList={this.getCode()} authList={this.getAuth()} save={this.method.handleSave} />
      </div>
    );
  }
}
