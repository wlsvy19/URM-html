import React from 'react'
import { Modal } from 'antd'

class RuleModal extends React.Component {
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
      this.refs.child.setState(chg)
    }
  }
  
  render() {
    return (
      <Modal visible={this.state.visible} width={this.props.width} footer={null} onCancel={this.method.handleCancel}>
        {this.props.render ? this.props.render() : (<this.prop.component />)}
      </Modal>
    );
  }
}

export default RuleModal