import React from 'react'
import { Table } from 'antd'

export default class StaticsHourList extends React.Component {
  state = {
    items: [],
    filters: [],
  }

  getRowKey = (record) => {
    return this.state.filters.indexOf(record)
  }

  render() {
    return (
      <div className="urm-list">
        <Table className="table-striped"
            dataSource={this.state.filters} pagination={false} bordered
            size="small" rowKey={this.getRowKey}>
          <Table.Column title="처리날짜" dataIndex="processDate" />
          <Table.Column title="인터페이스ID" dataIndex="interfaceId" width="150px" />
          <Table.Column title="항목" dataIndex="countType" />
          <Table.Column title="00" dataIndex="hour00" />
          <Table.Column title="01" dataIndex="hour01" />
          <Table.Column title="02" dataIndex="hour02" />
          <Table.Column title="03" dataIndex="hour03" />
          <Table.Column title="04" dataIndex="hour04" />
          <Table.Column title="05" dataIndex="hour05" />
          <Table.Column title="06" dataIndex="hour06" />
          <Table.Column title="07" dataIndex="hour07" />
          <Table.Column title="08" dataIndex="hour08" />
          <Table.Column title="09" dataIndex="hour09" />
          <Table.Column title="10" dataIndex="hour10" />
          <Table.Column title="11" dataIndex="hour11" />
          <Table.Column title="12" dataIndex="hour12" />
          <Table.Column title="13" dataIndex="hour13" />
          <Table.Column title="14" dataIndex="hour14" />
          <Table.Column title="15" dataIndex="hour15" />
          <Table.Column title="16" dataIndex="hour16" />
          <Table.Column title="17" dataIndex="hour17" />
          <Table.Column title="18" dataIndex="hour18" />
          <Table.Column title="19" dataIndex="hour19" />
          <Table.Column title="20" dataIndex="hour20" />
          <Table.Column title="21" dataIndex="hour21" />
          <Table.Column title="22" dataIndex="hour22" />
          <Table.Column title="23" dataIndex="hour23" />
        </Table>
      </div>
    );
  }
}