import React from 'react'
import BizList from './list/BusinessCode-list'

class BusinessCode extends React.Component {
  render() {
    return (
      <div className="urm-panel">
        <BizList ref="list" />
      </div>
    );
  }
}

export default BusinessCode