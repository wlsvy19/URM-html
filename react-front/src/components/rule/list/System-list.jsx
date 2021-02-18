import React from 'react'
import { Table, Button } from 'antd'
import { Form, Input, Select } from 'antd'

import RuleList, { RuleSearch } from './RuleList'
import * as urmsc from '@/urm-utils'

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
      <div className={'search-bar ' + this.props.path}>
        <Form colon={false}>
          <div className="row">
            <Form.Item label={locale['label.systemId']}>{getFieldDecorator("id")(<Input size="small" className="search-id" />)}</Form.Item>
            <Form.Item label={locale['label.systemName']}>{getFieldDecorator("name")(<Input size="small" className="search-name" />)}</Form.Item>
            <Form.Item label={locale['label.host']}>{getFieldDecorator("hostId")(<Input size="small" className="search-id" />)}</Form.Item>
            <Form.Item label={locale['label.systemType']}>
              {getFieldDecorator("type", {initialValue: ""})(<Select size="small" className="search-id">
                <Select.Option value="">ALL</Select.Option>
                {this.method.renderOpts("sysType")}
              </Select>)}
            </Form.Item>
            <Form.Item label={locale['label.devType']}>
              {getFieldDecorator("devType", {initialValue: ""})(<Select size="small" className="search-id">
                <Select.Option value="">ALL</Select.Option>
                {this.method.renderOpts("devType")}
              </Select>)}
            </Form.Item>
            <Form.Item className="search-buttons">
              <Button size="small" onClick={this.method.clickSearch}>{locale['label.search']}</Button>
              {this.method.renderButton(
                <div className="inline">
                  <Button size="small" onClick={this.method.clickAdd}>{locale['label.add']}</Button>
                  <Button size="small" type="danger" onClick={this.method.clickDelete} disabled={!this.props.canDelete()}>{locale['label.delete']}</Button>
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
        <WrappedSystemSearch {...this.props} search={this.method.search} delete={this.method.clickDelete} canDelete={this.method.isPageDelete} />
        <Table className="table-striped"
          dataSource={this.state.items} pagination={false} bordered
          size="small" /*scroll={{ y: 500 }}*/ rowKey="id"
          onRow={this.onRow}>
          <Table.Column title={locale['label.systemId']} dataIndex="id" width="130px"/>
          <Table.Column title={locale['label.systemName']} dataIndex="name" ellipsis />
          <Table.Column title={locale['label.systemType']} dataIndex="type" width="100px" render={(val) => ( this.method.getTypeStr('sysType', val) )}/>
          <Table.Column title={locale['label.devType']} dataIndex="devType" width="90px" render={(val) =>  ( this.method.getTypeStr('devType', val) )}/>
          <Table.Column title={locale['label.host']} dataIndex="hostId" width="165px" ellipsis />
          <Table.Column title={locale['label.ip']} dataIndex="ip" width="165px" ellipsis />
          <Table.Column title={locale['label.port']} dataIndex="port" width="85px" />
          <Table.Column title={locale['label.dbType']} dataIndex="dbType" width="95px" render={(val) =>  ( this.method.getTypeStr('dbType', val) )}/>
          <Table.Column title={locale['label.dbName']} dataIndex="dbName" width="155px" ellipsis />
          {this.method.renderButton(
            <Table.Column className="operations" width="85px" render={(val) => 
              (<div>
                <Button icon="edit" onClick={e => { this.method.clickEdit(val.id) }} title={locale['label.modify']} />
                <Button icon="delete" type="danger" onClick={e => { this.method.clickDelete([val.id]) }} title={locale['label.delete']} disabled={!this.method.isPageDelete()} />
              </div>)}
            />
          )}
        </Table>
      </div>
    );
  }
}

const WrappedSystemSearch = Form.create({name:'system_search'})(SystemSearch)
export default SystemList