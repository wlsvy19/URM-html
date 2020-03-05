import React from 'react'
import { Modal } from 'antd'

class SubModal extends React.Component {
  state = {
    visible: false
  }

  shouldComponentUpdate(nextProps, nextState) {
    if (this.state.visible !== nextState.visible) {
      return true
    }
    return false
  }
  
  method = {
    handleCancel: e => {
      this.setState({visible: false})
    },
    
    changeChildState: (chg) => {
      if (this.refs.child) {
        this.refs.child.setState(chg)
      }
    }
  }
  
//        {this.props.render ? this.props.render() : (<this.prop.component />)}
  render() {//console.log(this.props.children, this)
    return (
      <Modal visible={this.state.visible} width={this.props.width}
        footer={null} onCancel={this.method.handleCancel} className="urm-modal">
        {this.props.children}
      </Modal>
    );
  }
}

export default SubModal