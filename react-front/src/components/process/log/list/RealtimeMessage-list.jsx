import React from 'react'
import { Table, Input } from 'antd'

export default class RealtimeMessageList extends React.Component {
  state = {
    items: [],
    msg: '',
  }

  render() {
    return (
      <div className="urm-list">
        <Table className="table-striped upper-list" scroll={{y: 400}}
            dataSource={this.state.items} pagination={false} bordered
            size="small" rowKey="messageIndex">
          <Table.Column title="Index" dataIndex="messageIndex" width="65px" />
          <Table.Column title="필드명" dataIndex="fieldName" />
          <Table.Column title="값" dataIndex="fieldValue" width="400px" render={(val) => ( <Input value={val} readOnly /> )} />
        </Table>

        <hr />

        <Input.TextArea value={this.state.msg} readOnly style={{height: 160}} />
      </div>
    );
  }
}