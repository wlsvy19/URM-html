import React from 'react'
import { Table, Button, Form } from 'antd'

import RuleList, { RuleSearch } from './RuleList'
import * as urmsc from '@/urm-utils'

const locale = urmsc.locale

class DataMapSearch extends RuleSearch {
  render() {
    return (
      <div className={'search-bar ' + this.props.path}>
        <div className="row">
          <Form.Item className="search-buttons">
            <Button onClick={this.method.clickSearch}>다시 읽기</Button>
            <Button type="danger" onClick={this.method.clickDelete} disabled={!this.props.canDelete()}>{locale['label.delete']}</Button>
          </Form.Item>
        </div>
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
              size="small" scroll={{ y: 500 }} rowKey="id"
              rowSelection={this.rowSelection} onRow={this.onRow}>
            <Table.Column title="매핑 ID" dataIndex="id" width="135px"/>
            <Table.Column title="매핑 명" dataIndex="name" width="180px"/>
            <Table.Column title="송신데이터" dataIndex="sourceDataId" width="130px"/>
            <Table.Column title="수신데이터" dataIndex="targetDataId" width="130px"/>
            <Table.Column title="사용 요건 ID" dataIndex="requestId" width="145px"/>
            <Table.Column title="사용 요건 명" dataIndex="requestName"/>
            <Table.Column title="사용 인터페이스 ID" dataIndex="interfaceId" width="165px"/>
          </Table>
        </div>
      </div>
    );
  }
}

const WrappedDataMapSearch = Form.create({name:'datamap_search'})(DataMapSearch)
export default DataMapList