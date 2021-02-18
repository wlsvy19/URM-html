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
    const { width, children, ...restProps } = this.props
    
    return (
      <Modal visible={this.state.visible} width={width} footer={null}
          onCancel={this.method.handleCancel} {...restProps} className="urm-modal">
        {React.Children.map(children, (child) => {
          if (child.key && childState[child.key]) {
            return React.cloneElement(child, childState[child.key])
          }
          return child
        })}
      </Modal>
    );
  }
}
