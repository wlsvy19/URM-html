import React from 'react'
import RuleMain from './RuleMain'

class System extends RuleMain {
  constructor (props) {
    super(props)
    this.state = {
      path: 'system',
    }
  }

  render() {
    const { SystemList, SystemEditor} = this.components
    
    return (
      <div className="urm-panel" style={{minWidth: "1100px"}}>
        <SystemList ref="list" path={this.state.path} codeList={this.getCode()} edit={this.method.handleEdit} />
        <SystemEditor ref="editor" path={this.state.path} codeList={this.getCode()} save={this.method.handleSave} />
      </div>
    );
  }
}

export default System