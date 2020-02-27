import React from 'react'
import RuleMain from './RuleMain'
import SystemList, {WrappedSystemSearch} from './list/System-list'
import SystemEditor from './editor/System-editor'

import './rule.css'

const CODE = []

class System extends RuleMain {
  constructor (props) {
    super(props)
    this.state = {
      path : 'system',
    }
    this.method.setCode(CODE)
  }

  render() {
    return (
      <div className="urm-panel">
        <WrappedSystemSearch ref="searchBar" codeList={CODE} search={this.method.search} add={this.method.handleAdd} />
        <SystemList ref="list" codeList={CODE} edit={this.method.handleEdit} operation={true} />
        <SystemEditor ref="editor" codeList={CODE} />
      </div>
    );
  }
}

export default System