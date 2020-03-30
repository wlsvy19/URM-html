import React from 'react'
import { Table, Button } from 'antd'
import { Form, Input } from 'antd'

import RuleList, { RuleSearch } from './RuleList'
import * as urmsc from '@/urm-utils'

const locale = urmsc.locale

class DataMapSearch extends RuleSearch {
  render() {
    const { getFieldDecorator } = this.props.form

    return (
      <div className="search-bar">
        <Form colon={false}>
          <div className="row">
            <Form.Item label="ID">{getFieldDecorator("id")(<Input size="small" className="search-id" />)}</Form.Item>
            <Form.Item label="Name">{getFieldDecorator("name")(<Input size="small" className="search-name" />)}</Form.Item>
            <Form.Item className="search-buttons">
              <Button icon="search" onClick={this.method.clickSearch} title={locale['label.search']} />
              <Button icon="delete" onClick={this.method.clickDelete} title={locale['label.delete']} disabled={!this.props.canDelete()} />
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
        <WrappedDataMapSearch {...this.props} search={this.method.search} delete={this.method.clickDelete} canDelete={this.method.isPageDelete} />
        <div className="urm-list">
          <Table className="table-striped"
            dataSource={this.state.items} pagination={false} bordered
            size={"small"} scroll={{ y: 500 }} rowKey="id" rowSelection={this.rowSelection}>
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

const WrappedDataMapSearch = Form.create({name:'datamap_search'})(DataMapSearch)
export default DataMapList