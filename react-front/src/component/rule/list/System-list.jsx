import React from 'react'
import { Table, Button } from 'antd'
import { Form, Input, Select } from 'antd'

import RuleList, { RuleSearch } from './RuleList'
import * as urmsc from '../../../urm-utils'

const locale = urmsc.locale

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
            <Form.Item label={locale['label.systemId']}>{getFieldDecorator("id")(<Input size="small" className="search-id" />)}</Form.Item>
            <Form.Item label={locale['label.systemName']}>{getFieldDecorator("name")(<Input size="small" className="search-name" />)}</Form.Item>
            <Form.Item label={locale['label.host']}>{getFieldDecorator("hostId")(<Input size="small" className="search-id" />)}</Form.Item>
            <Form.Item label={locale['label.systemType']}>
              {getFieldDecorator("type", {initialValue: ""})(<Select size={"small"} className="search-id">
                <Select.Option value="">ALL</Select.Option>
                {this.method.renderOpts("sysType")}
              </Select>)}
            </Form.Item>
            <Form.Item label={locale['label.devType']}>
              {getFieldDecorator("devType", {initialValue: ""})(<Select size={"small"} className="search-id">
                <Select.Option value="">ALL</Select.Option>
                {this.method.renderOpts("devType")}
              </Select>)}
            </Form.Item>
            <Form.Item className="search-buttons">
              <Button icon="search" onClick={this.method.clickSearch} title={locale['label.search']} />
              {this.method.renderButton(
                <div className="inline">
                  <Button icon="plus" onClick={this.method.clickAdd} title={locale['label.add']} />
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
          <Table.Column title={locale['label.systemId']} dataIndex="id" width="130px"/>
          <Table.Column title={locale['label.systemName']} dataIndex="name" width="180px"/>
          <Table.Column title={locale['label.systemType']} dataIndex="type" render={(val) => ( this.method.getTypeStr('sysType', val) )}/>
          <Table.Column title={locale['label.devType']} dataIndex="devType" render={(val) =>  ( this.method.getTypeStr('devType', val) )}/>
          <Table.Column title={locale['label.host']} dataIndex="hostId" width="120px"/>
          <Table.Column title={locale['label.ip']} dataIndex="ip"/>
          <Table.Column title={locale['label.port']} dataIndex="port"/>
          <Table.Column title={locale['label.dbType']} dataIndex="dbType" render={(val) =>  ( this.method.getTypeStr('dbType', val) )}/>
          <Table.Column title={locale['label.dbName']} dataIndex="dbName"/>
          {this.method.renderButton(
            <Table.Column title="Operations" className="operations" width="90px" render={(val) => 
              (<Button icon="edit" onClick={e => { this.method.clickEdit(val.id) }} title={locale['label.modify']} />)} />
          )}
        </Table>
      </div>
    );
  }
}

const WrappedSystemSearch = Form.create({name:'system_search'})(SystemSearch)
export default SystemList