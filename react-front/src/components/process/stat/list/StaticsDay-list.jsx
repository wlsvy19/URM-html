import React from 'react'
import { Table } from 'antd'

export default class StaticsDayList extends React.Component {
  state = {
    items: [],
  }
  
  renderSim = () => {
    let type = this.props.match.params.path.charAt(0)
    let infType = this.props.match.params.path.charAt(5)
    if (type === 'd' && infType === '4') {
      return <Table.Column title="시뮬레이션건수" dataIndex="simCount" />
    }
    return undefined
  }
  
  getRowKey = (record) => {
    return this.state.items.indexOf(record)
  }

  render() {
    return (
      <div className="urm-list">
        <Table className="table-striped"
            dataSource={this.state.items} pagination={false} bordered
            size="small" rowKey={this.getRowKey}>
          <Table.Column title="처리날짜" dataIndex="processDate" />
          <Table.Column title="인터페이스ID" dataIndex="interfaceId" width="200px" />
          <Table.Column title="총건수" dataIndex="totalCount" />
          {this.renderSim()}
          <Table.Column title="성공건수" dataIndex="successCount" />
          <Table.Column title="실패건수" dataIndex="failCount" />
          <Table.Column title="평균처리시간" dataIndex="processTime" />
        </Table>
      </div>
    );
  }
}