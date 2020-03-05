import React from 'react'
import { Table, Button } from 'antd'
import { Form, Input } from 'antd'

import RuleList, { RuleSearch } from './RuleList'

class DataMapSearch extends RuleSearch {
  render() {
    const { getFieldDecorator } = this.props.form

    return (
      <div className="search-bar">
        <Form colon={false}>
          <div className="row">
            <Form.Item label="ID">{getFieldDecorator("id")(<Input size="small" className="search-id" />)}</Form.Item>
            <Form.Item label="Name">{getFieldDecorator("name")(<Input size="small" className="search-name" />)}</Form.Item>
            <Form.Item label="SourceDataId">
              {getFieldDecorator("sourceDataId")(<Input size="small" className="search-id" readOnly allowClear />)}
            </Form.Item>
            <Form.Item label="TargetDataId">
              {getFieldDecorator("targetDataId")(<Input size="small" className="search-id" readOnly allowClear />)}
            </Form.Item>
            <Form.Item className="search-buttons">
              <Button onClick={this.method.clickSearch} icon="search" />
              <Button icon="delete" />
            </Form.Item>
          </div>
        </Form>
      </div>
    );
  }
}

class DataMapList extends RuleList {
  render() {
    return (
      <div>
        <WrappedDataMapSearch {...this.props} search={this.method.search} />
        <div className="urm-list">
          <Table className="table-striped"
            dataSource={this.state.items} pagination={false} bordered
            size={"small"} scroll={{ y: 500 }} rowKey="id"
            onRow={(record, index) => {
              return {
                onDoubleClick: e => { if (this.state.onDbClick) this.state.onDbClick(record) }
              }
            }
          }>
            <Table.Column title="ID" dataIndex="id" width="150px"/>
            <Table.Column title="Name" dataIndex="name" width="200px"/>
            <Table.Column title="SourceDataId" dataIndex="sourceDataId"/>
            <Table.Column title="TargetDataId" dataIndex="targetDataId"/>
            <Table.Column title="UsedRequestId" dataIndex="requestId"/>
            <Table.Column title="UsedInterfaceId" dataIndex="interfaceId"/>
          </Table>
        </div>
      </div>
    );
  }
}

const WrappedDataMapSearch = Form.create({name:'data_map_search'})(DataMapSearch)
export default DataMapList