import React from 'react'
import RuleMain from './RuleMain'

export default class Data extends RuleMain {
  constructor (props) {
    super(props)
    this.state = {
      path: 'data',
    }
  }

  render() {
    const { DataList, DataEditor} = this.components
    
    return (
      <div className="urm-panel">
        <DataList ref="list" path={this.state.path} codeList={this.getCode()} edit={this.method.handleEdit} />
        <DataEditor ref="editor" path={this.state.path} codeList={this.getCode()} save={this.method.handleSave} />
      </div>
    );
  }
}
