import React from 'react'
import { Table, Button } from 'antd'
import { Form, Input, Select } from 'antd'

import RuleList, { RuleSearch } from './RuleList'
import RuleListButton from './RuleListButton'
import {default as urmUtils} from '../../../urm-utils'

const KINDS = urmUtils.codeKey

class DataSearch extends RuleSearch {
  method = {
    ...this.method,

    renderOpts: (key) => {
      return urmUtils.getSubListByKey(this.props.codeList, 'kind', KINDS[key])
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
            <Form.Item style={{marginLeft: "15px"}}>
              <Button onClick={this.method.clickSearch} icon="search" />
              <Button onClick={this.method.clickAdd} icon="plus" />
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
        <Table className="table-striped"
          dataSource={this.state.items} pagination={false} bordered
          size={"small"} scroll={{ y: 500 }} rowKey="id">
          <Table.Column title="ID" dataIndex="id" width="150px"/>
          <Table.Column title="Name" dataIndex="name" width="250px"/>
          <Table.Column title="Type" dataIndex="type" render={(val) => ( this.method.getTypeStr(KINDS.sysType, val) )}/>
          <Table.Column title="Register" dataIndex="regId"/>
          <Table.Column title="Operations" width="100px" render={(val) =>
            (<RuleListButton edit={e => { this.method.clickEdit(val.id) }} />)} />
        </Table>
      </div>
    );
  }
}

const WrappedDataSearch = Form.create({name:'data_search'})(DataSearch)
export default DataList
export {WrappedDataSearch}