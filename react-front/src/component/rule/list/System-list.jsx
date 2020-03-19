import React from 'react'
import { Table, Button } from 'antd'
import { Form, Input, Select } from 'antd'

import RuleList, { RuleSearch } from './RuleList'
import * as urmsc from '../../../urm-utils'

class SystemSearch extends RuleSearch {
  method = {
    ...this.method,

    renderOpts: (key) => {
      return urmsc.getSubListByKey(this.props.codeList, 'kind', urmsc.CODEKEY[key])
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
            <Form.Item label="Host">{getFieldDecorator("hostId")(<Input size="small" className="search-id" />)}</Form.Item>
            <Form.Item label="Type">
              {getFieldDecorator("type", {initialValue: ""})(<Select size={"small"} className="search-id">
                <Select.Option value="">ALL</Select.Option>
                {this.method.renderOpts("sysType")}
              </Select>)}
            </Form.Item>
            <Form.Item label="DevType">
              {getFieldDecorator("devType", {initialValue: ""})(<Select size={"small"} className="search-id">
                <Select.Option value="">ALL</Select.Option>
                {this.method.renderOpts("devType")}
              </Select>)}
            </Form.Item>
            <Form.Item className="search-buttons">
              <Button icon="search" onClick={this.method.clickSearch} />
              {this.method.renderButton(
                <div className="inline">
                  <Button icon="plus" onClick={this.method.clickAdd} />
                </div>
              )}
            </Form.Item>
          </div>
        </Form>
      </div>
    );
  }
}

class SystemList extends RuleList {
  render() {
    return (
      <div className="urm-list">
        <WrappedSystemSearch {...this.props} search={this.method.search} />
        <Table className="table-striped"
          dataSource={this.state.items} pagination={false} bordered
          size={"small"} scroll={{ y: 500 }} rowKey="id"
          onRow={this.onRow}>
          <Table.Column title="ID" dataIndex="id" width="130px"/>
          <Table.Column title="Name" dataIndex="name" width="180px"/>
          <Table.Column title="Type" dataIndex="type" render={(val) => ( this.method.getTypeStr('sysType', val) )}/>
          <Table.Column title="DevType" dataIndex="devType" render={(val) =>  ( this.method.getTypeStr('devType', val) )}/>
          <Table.Column title="Host" dataIndex="hostId" width="120px"/>
          <Table.Column title="IP" dataIndex="ip"/>
          <Table.Column title="port" dataIndex="port"/>
          <Table.Column title="DBType" dataIndex="dbType" render={(val) =>  ( this.method.getTypeStr('dbType', val) )}/>
          <Table.Column title="DBName" dataIndex="dbName"/>
          {this.method.renderButton(
            <Table.Column title="Operations" className="operations" width="90px" render={(val) => 
              (<Button icon="edit" onClick={e => { this.method.clickEdit(val.id) }} />)} />
          )}
        </Table>
      </div>
    );
  }
}

const WrappedSystemSearch = Form.create({name:'system_search'})(SystemSearch)
export default SystemList