import React from 'react'
import { connect } from 'react-redux'

import RuleMain from './RuleMain'
import DataList from './list/Data-list'
import DataEditor from './editor/Data-editor'

import { initCode } from '../../store/modules/rule'

class Data extends RuleMain {
  constructor (props) {
    super(props)
    this.state = {
      path: 'data',
    }
    this.method.setCode()
  }

  render() {
    return (
      <div className="urm-panel">
        <DataList ref="list" path={this.state.path} codeList={this.getCode()} edit={this.method.handleEdit} />
        <DataEditor ref="editor" path={this.state.path} />
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
)(Data)