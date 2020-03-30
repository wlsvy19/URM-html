import React from 'react'
import { Modal } from 'antd'

export default class SubModal extends React.Component {
  state = {
    visible: false,
    childState: {}
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
  }

  render() {
    const { childState } = this.state
    
    return (
      <Modal visible={this.state.visible} width={this.props.width}
        footer={null} onCancel={this.method.handleCancel} className="urm-modal">
        {React.Children.map(this.props.children, (child) => {
          if (child.key && (child.key.endsWith('list') || child.key.endsWith('List'))) {
            let childFunc = childState[child.key] ? childState[child.key].onDbClick : undefined
            let scparam = childState[child.key] ? childState[child.key].scparam : undefined
            return React.cloneElement(child, { onDbClick: childFunc, scparam: scparam })
          }
          return child
        })}
      </Modal>
    );
  }
}
