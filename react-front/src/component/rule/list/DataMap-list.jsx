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
            <Form.Item label="송신데이터">{getFieldDecorator("sourceDataId")(<Input size="small" className="search-id" />)}</Form.Item>
            <Form.Item label="수신데이터">{getFieldDecorator("targetDataId")(<Input size="small" className="search-id" />)}</Form.Item>
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
            size="small" scroll={{ y: 500 }} rowKey="id" rowSelection={this.rowSelection}>
            <Table.Column title="매핑 ID" dataIndex="id" width="150px"/>
            <Table.Column title="매핑 명" dataIndex="name" width="200px"/>
            <Table.Column title="송신데이터" dataIndex="sourceDataId"/>
            <Table.Column title="수신데이터" dataIndex="targetDataId"/>
            <Table.Column title="사용 요건 ID" dataIndex="requestId"/>
            <Table.Column title="사용 인터페이스 ID" dataIndex="interfaceId"/>
          </Table>
        </div>
      </div>
    );
  }
}

const WrappedDataMapSearch = Form.create({name:'datamap_search'})(DataMapSearch)
export default DataMapList