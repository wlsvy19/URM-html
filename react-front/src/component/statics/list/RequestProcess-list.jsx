import React from 'react'
import { Table } from 'antd'

export default class ProcessList extends React.Component {
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
          <Table.Column title="요건신청" dataIndex="stat1" />
          <Table.Column title="재작성요청" dataIndex="stat2" />
          <Table.Column title="접수완료" dataIndex="stat3" />
          <Table.Column title="반려" dataIndex="stat4" />
          <Table.Column title="요건등록완료" dataIndex="stat5" />
          <Table.Column title="대표거래테스트" dataIndex="stat6" />
          <Table.Column title="테스트중" dataIndex="stat7" />
          <Table.Column title="테스트완료" dataIndex="stat8" />
          <Table.Column title="테스트이관요청" dataIndex="stat9" />
          <Table.Column title="테스트이관" dataIndex="stat10" />
          <Table.Column title="운영이관요청" dataIndex="stat11" />
          <Table.Column title="운영이관" dataIndex="stat12" />
          <Table.Column title="TOTAL" dataIndex="count" />
        </Table>
      </div>
    );
  }
}
