import React from 'react'

import RuleMain from './RuleMain'
import DataList from './list/Data-list'
import DataEditor from './editor/Data-editor'

export default class Data extends RuleMain {
  constructor (props) {
    super(props)
    this.state = {
      path: 'data',
    }
  }

  render() {
    return (
      <div className="urm-panel">
        <DataList ref="list" path={this.state.path} userInfo={this.props.userInfo}
          codeList={this.codeList()} edit={this.method.handleEdit} />
        <DataEditor ref="editor" path={this.state.path} userInfo={this.props.userInfo}
          codeList={this.codeList()} save={this.method.handleSave} />
      </div>
    );
  }
}
