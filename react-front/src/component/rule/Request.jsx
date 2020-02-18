import React from 'react'
import RuleMain from './RuleMain'
import RequestList, {WrappedRequestSearch} from './list/Request-list'
import RequestEditor from './editor/Request-editor'

const CODE = []

class Request extends RuleMain {
  constructor (props) {
    super(props)
    this.state = {
      path : 'request',
    }
    this.method.setCode(CODE)
  }

  render() {
    return (
      <div className="urm-panel">
        <WrappedRequestSearch ref="searchBar" codeList={CODE} search={this.method.search} add={this.method.handleAdd} />
        <RequestList ref="list" codeList={CODE} edit={this.method.handleEdit} />
        <RequestEditor ref="editor" codeList={CODE} />
      </div>
    );
  }
}

export default Request