import React from 'react'
import { Button } from 'antd'

class UserListButton extends React.Component {
  render() {
    return (
      <div>
        <Button onClick={this.props.edit} icon="edit" />
        <Button onClick={this.props.delete} icon="delete" />
      </div>
    );
  }
}

export default UserListButton