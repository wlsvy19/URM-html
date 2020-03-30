import React from 'react'

import RuleMain from './RuleMain'
import SystemList from './list/System-list'
import SystemEditor from './editor/System-editor'

class System extends RuleMain {
  constructor (props) {
    super(props)
    this.state = {
      path: 'system',
    }
  }

  render() {
    return (
      <div className="urm-panel" style={{minWidth: "1100px"}}>
        <SystemList ref="list" path={this.state.path} userInfo={this.props.userInfo}
          codeList={this.codeList()} edit={this.method.handleEdit} />
        <SystemEditor ref="editor" path={this.state.path} userInfo={this.props.userInfo}
          codeList={this.codeList()} save={this.method.handleSave} />
      </div>
    );
  }
}

export default System