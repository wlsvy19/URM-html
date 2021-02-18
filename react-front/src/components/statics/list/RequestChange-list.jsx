import React from 'react'
import { Table } from 'antd'

export default class ChangeList extends React.Component {
  state = {
    items: [],
  }

  render() {
    return (
      <div className="urm-list">
        <Table className="table-striped"
            dataSource={this.state.items} pagination={false} bordered
            size="small" rowKey="chgDate">
          <Table.Column title="변경일자" dataIndex="chgDate" width="130px"/>
          <Table.ColumnGroup title="변경요청">
            <Table.Column title="온라인" dataIndex="stat1o" />
            <Table.Column title="배치" dataIndex="stat1b" />
            <Table.Column title="디퍼드" dataIndex="stat1d" />
            <Table.Column title="TOTAL" dataIndex="stat1" />
          </Table.ColumnGroup>
          <Table.ColumnGroup title="변경완료">
            <Table.Column title="온라인" dataIndex="stat2o" />
            <Table.Column title="배치" dataIndex="stat2b" />
            <Table.Column title="디퍼드" dataIndex="stat2d" />
            <Table.Column title="TOTAL" dataIndex="stat2" />
          </Table.ColumnGroup>
          <Table.ColumnGroup title="삭제요청">
            <Table.Column title="온라인" dataIndex="stat3o" />
            <Table.Column title="배치" dataIndex="stat3b" />
            <Table.Column title="디퍼드" dataIndex="stat3d" />
            <Table.Column title="TOTAL" dataIndex="stat3" />
          </Table.ColumnGroup>
          <Table.ColumnGroup title="삭제완료">
            <Table.Column title="온라인" dataIndex="stat4o" />
            <Table.Column title="배치" dataIndex="stat4b" />
            <Table.Column title="디퍼드" dataIndex="stat4d" />
            <Table.Column title="TOTAL" dataIndex="stat4" />
          </Table.ColumnGroup>
        </Table>
      </div>
    );
  }
}
