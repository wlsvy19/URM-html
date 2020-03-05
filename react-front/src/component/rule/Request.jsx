import React from 'react'
import { connect } from 'react-redux'

import RuleMain from './RuleMain'
import RequestList from './list/Request-list'
import RequestEditor from './editor/Request-editor'

import { initCode } from '../../store/modules/rule'

class Request extends RuleMain {
  constructor (props) {
    super(props)
    this.state = {
      path: 'request'
    }
    this.method.setCode()
  }

  render() {
    return (
      <div className="urm-panel">
        <RequestList ref="list" path={this.state.path} codeList={this.getCode()} edit={this.method.handleEdit} />
        <RequestEditor ref="editor" path={this.state.path} codeList={this.getCode()} />
      </div>
    );
  }
}

const mapStateToProps = ({ rule }) => ({
  commonCode: rule.commonCode,
})

const mapDispatchToProps = dispatch => ({
  initCode: (code) => dispatch(initCode(code)),
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Request)