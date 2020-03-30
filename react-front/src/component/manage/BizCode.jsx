import React from 'react'
import BizList from './list/BizCode-list'

export default class BizCode extends React.Component {
  render() {
    return (
      <div className="urm-panel">
        <BizList ref="list" />
      </div>
    );
  }
}
