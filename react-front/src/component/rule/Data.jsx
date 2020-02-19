import React from 'react'
import RuleMain from './RuleMain'
import DataList, {WrappedDataSearch} from './list/Data-list'

import './rule.css'

const CODE = []

class Data extends RuleMain {
  constructor (props) {
    super(props)
    this.state = {
      path : 'data',
    }
    //this.method.setCode(CODE)
  }

  render() {
    return (
      <div className="urm-panel">
        <WrappedDataSearch codeList={CODE} search={this.method.search} />
        <DataList ref="list" codeList={CODE} edit={this.method.handleEdit} />
      </div>
    );
  }
}

export default Data