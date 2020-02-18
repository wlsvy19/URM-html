import React from 'react'
import { Table, Button } from 'antd'
import { Form, Input, Select } from 'antd'

import RuleListButton from './RuleListButton'
import {default as urmUtils} from '../../../urm-utils'

const KINDS = urmUtils.codeKey

class SystemSearch extends React.Component {
  componentDidMount() {
    //this.props.search()
  }
  
  method = {
    clickSearch: e => {
      this.props.search(this.props.form.getFieldsValue())
    },
    
    clickAdd: e => {
      this.props.add()
    },

    renderOpts: (key) => {
      return urmUtils.getSubListByKey(this.props.codeList, "kind", KINDS[key])
              .map((it) => <Select.Option key={it.code} value={it.code}>{it.name}</Select.Option>)
    },
  }

  render() {
    const { getFieldDecorator } = this.props.form

    return (
      <div className="search-bar">
        <div className="row">
          <Form.Item label="ID" colon={false}>{getFieldDecorator("id")(<Input size="small" className="search-id" />)}</Form.Item>
          <Form.Item label="Name" colon={false}>{getFieldDecorator("name")(<Input size="small" className="search-name" />)}</Form.Item>
          <Form.Item label="Host" colon={false}>{getFieldDecorator("hostId")(<Input size="small" className="search-id" />)}</Form.Item>
          <Form.Item label="Type" colon={false}>
            {getFieldDecorator("type", {initialValue: ""})(<Select size={"small"} className="search-id">
              <Select.Option value="">ALL</Select.Option>
              {this.method.renderOpts("sysType")}
            </Select>)}
          </Form.Item>
          <Form.Item label="DevType" colon={false}>
            {getFieldDecorator("devType", {initialValue: ""})(<Select size={"small"} className="search-id">
              <Select.Option value="">ALL</Select.Option>
              {this.method.renderOpts("devType")}
            </Select>)}
          </Form.Item>
          <Form.Item style={{marginLeft: "15px"}}>
            <Button onClick={this.method.clickSearch} icon="search" />
            <Button onClick={this.method.clickAdd} icon="plus" />
          </Form.Item>
        </div>
      </div>
    );
  }
}

class SystemList extends React.Component {
  state = {
    items: [],
  }

  method = {
    getTypeStr: (kind, key) => {
      let obj = {}
      let list = this.props.codeList
      for(let i=0; i < list.length; i++) {
        let it = list[i]
        if (it.kind === kind && it.code === key) {
          obj = it
          break
        }
      }
      return obj.name
    },

    clickEdit: (id) => {
      this.props.edit(id)
    },
    
    handleResize: (e, { size }) => {
      this.setState(({ columns }) => {
        const nextColumns = [...columns]
        return { columns: nextColumns }
      })
    }
  }

  render() {
    return (
      <div className="urm-list">
        <Table dataSource={this.state.items} pagination={false} bordered size={"small"} scroll={{ y: 500 }} className="table-striped">
          <Table.Column title="ID" dataIndex="id" width="130px"/>
          <Table.Column title="Name" dataIndex="name"/>
          <Table.Column title="Type" dataIndex="type" render={(val) => ( this.method.getTypeStr(KINDS.sysType, val) )}/>
          <Table.Column title="DevType" dataIndex="devType" render={(val) =>  ( this.method.getTypeStr(KINDS.devType, val) )}/>
          <Table.Column title="Host" dataIndex="hostId" width="120px"/>
          <Table.Column title="IP" dataIndex="ip"/>
          <Table.Column title="port" dataIndex="port"/>
          <Table.Column title="DBType" dataIndex="dbType" render={(val) =>  ( this.method.getTypeStr(KINDS.dbType, val) )}/>
          <Table.Column title="DBName" dataIndex="dbName"/>
          <Table.Column title="Operations" width="100px" render={(val) => (
            <RuleListButton edit={e => { this.method.clickEdit(val.id) }} />
          )}/>
        </Table>
      </div>
    );
  }
}

const WrappedSystemSearch = Form.create({name:'system_search'})(SystemSearch)
export default SystemList
export {WrappedSystemSearch}