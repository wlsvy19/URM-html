import React from 'react'
import RuleMain from './RuleMain'
import SystemList, {WrappedSystemSearch} from './list/System-list'

import './rule.css'

const CODE = []

class System extends RuleMain {
  render() {
    return (
      <div className="urm-panel">
        <WrappedSystemSearch codeList={CODE} search={this.method.search} />
        <SystemList ref="list" codeList={CODE} edit={this.method.handleEdit} />
      </div>
    );
  }
}

export default System