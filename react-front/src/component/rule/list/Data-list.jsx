import React from 'react'
import { Table, Button } from 'antd'
import { Form, Input, Select } from 'antd'
import moment from 'moment'

import RuleList, { RuleSearch } from './RuleList'
import RuleListButton from './RuleListButton'
import * as urmsc from '../../../urm-utils'

const KINDS = urmsc.CODEKEY

class DataSearch extends RuleSearch {
  method = {
    ...this.method,

    renderOpts: (key) => {
      return urmsc.getSubListByKey(this.props.codeList, 'kind', KINDS[key])
              .map((it) => <Select.Option key={it.code} value={it.code}>{it.name}</Select.Option>)
    },
  }

  render() {
    const { getFieldDecorator } = this.props.form

    return (
      <div className="search-bar">
        <Form colon={false}>
          <div className="row">
            <Form.Item label="ID">{getFieldDecorator("id")(<Input size="small" className="search-id" />)}</Form.Item>
            <Form.Item label="Name">{getFieldDecorator("name")(<Input size="small" className="search-name" />)}</Form.Item>
            <Form.Item label="Type">
              {getFieldDecorator("type", {initialValue: ""})(<Select size={"small"} className="search-id">
                <Select.Option value="">ALL</Select.Option>
                {this.method.renderOpts("dataType")}
              </Select>)}
            </Form.Item>
            <Form.Item className="search-buttons">
              <Button onClick={this.method.clickSearch} icon="search" />
              {this.method.renderButton(
                <div className="inline">
                  <Button onClick={this.method.clickAdd} icon="plus" />
                  <Button icon="delete" />
                </div>
              )}
            </Form.Item>
          </div>
        </Form>
      </div>
    );
  }
}

class DataList extends RuleList {
  render() {
    return (
      <div className="urm-list">
        <WrappedDataSearch  {...this.props} search={this.method.search} />
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
          <Table.Column title="Name" dataIndex="name" width="250px"/>
          <Table.Column title="Type" dataIndex="type" render={(val) => ( this.method.getTypeStr(KINDS.sysType, val) )} />
          <Table.Column title="Register" dataIndex="regId"/>
          <Table.Column title="RegDate" dataIndex="regDate" render={(val) => ( moment(val).format('YYYY-MM-DD HH:mm') )} />
          {this.method.renderButton(
            <Table.Column title="Operations" className="operations" width="100px" render={(val) =>
              (<RuleListButton edit={e => { this.method.clickEdit(val.id) }} delete={e => { this.method.clickDelete([val.id]) }} />)} />
          )}
        </Table>
      </div>
    );
  }
}

const WrappedDataSearch = Form.create({name:'data_search'})(DataSearch)
export default DataList