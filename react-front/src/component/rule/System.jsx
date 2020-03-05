import React from 'react'
import { connect } from 'react-redux'

import RuleMain from './RuleMain'
import SystemList from './list/System-list'
import SystemEditor from './editor/System-editor'

import { initCode } from '../../store/modules/rule'

class System extends RuleMain {
  constructor (props) {
    super(props)
    this.state = {
      path: 'system',
    }
    this.method.setCode()
  }

  render() {
    return (
      <div className="urm-panel" style={{minWidth: "1100px"}}>
        <SystemList ref="list" path={this.state.path} codeList={this.getCode()} edit={this.method.handleEdit} />
        <SystemEditor ref="editor" path={this.state.path} codeList={this.getCode()} />
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
)(System)