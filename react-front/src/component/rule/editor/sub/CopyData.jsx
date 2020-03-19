import React from 'react'
import { Button, Table } from 'antd'
import { Form, Input, Select } from 'antd'
import moment from 'moment'

import * as urmsc from '../../../../urm-utils'

class CopySearch extends React.Component {
  componentDidMount() {
    this.props.search()
  }
  
  method = {
    clickSearch: e => {
      this.props.search(this.props.form.getFieldsValue())
    },
    
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
            <Form.Item label="Type">
              {getFieldDecorator("type", {initialValue: ""})(<Select size={"small"} className="search-id">
                <Select.Option value="">ALL</Select.Option>
                {this.method.renderOpts("dataType")}
              </Select>)}
            </Form.Item>
            <Form.Item className="search-buttons">
              <Button icon="search" onClick={this.method.clickSearch} />
            </Form.Item>
          </div>
        </Form>
      </div>
    );
  }
}

class CopyList extends React.Component {
  state = {
    items: [],
    fields: [],
  }

  shouldComponentUpdate(nextProps, nextState) {
    if (this.state.items !== nextState.items) {
      return true
    }
    if (this.state.fields !== nextState.fields) {
      return true
    }
    return false
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
    
    getFieldStr: (kind, key) => {
      if (kind === 'type') {
        switch (key) {
        case 'C': return 'Character'
        case 'N': return 'Number'
        case 'D': return 'Date'
        case 'B': return 'Binary'
        default: return '' 
        }
      } else if (kind === 'yn') {
        switch (key) {
        case true: return 'Yes'
        case false: return 'No'
        default: return '' 
        }
      }
    },

    search: (param) => {
      let $this = this
      urmsc.ajax({
        type: 'GET',
        url: '/URM/data',
        data: param,
        success: function(list) {
          $this.setState({items: list})
        },
      })
    },
    
    viewFields: (record) => {
      let $this = this
      urmsc.ajax({
        type: 'GET',
        url: '/URM/data/field/' + record.id,
        success: function(fields) {
          $this.setState({fields: (fields ? fields : [])})
        }
      })
    }
  }

  render() {
    return (
      <div className="urm-list">
        <WrappedCopySearch  {...this.props} search={this.method.search} />
        <Table className="table-striped"
          dataSource={this.state.items} pagination={false} bordered
          size={"small"} scroll={{ y: 500 }} rowKey="id"
          onRow={(record, index) => {
            return {
              onClick: e => { this.method.viewFields(record) },
              onDoubleClick: e => { if (this.props.onDbClick) this.props.onDbClick(record) }
            }
          }
        }>
          <Table.Column title="ID" dataIndex="id" width="150px"/>
          <Table.Column title="Name" dataIndex="name" width="250px"/>
          <Table.Column title="Type" dataIndex="type" render={(val) => ( this.method.getTypeStr('sysType', val) )} />
          <Table.Column title="Register" dataIndex="regId"/>
          <Table.Column title="RegDate" dataIndex="regDate" render={(val) => ( moment(val).format('YYYY-MM-DD HH:mm') )} />
        </Table>
        
        <hr />
        
        <Table className="table-striped" rowClassName="editable-row"
          dataSource={this.state.fields} pagination={false} bordered
          size={"small"} scroll={{ y: 500 }} rowKey="sno">
          <Table.Column title="Index" dataIndex="sno" width="55px" align="center"/>
          <Table.Column title="ENG Name" dataIndex="engName" width="150px"/>
          <Table.Column title="KOR Name" dataIndex="name" width="150px"/>
          <Table.Column title="Type" dataIndex="type" width="110px" render={(val) => ( this.method.getFieldStr('type', val) )}/>
          <Table.Column title="DateFormat" dataIndex="dateFormat"/>
          <Table.Column title="Length" dataIndex="length"/>
          <Table.Column title="Nullable" dataIndex="nullable" width="75px" render={(val) => ( this.method.getFieldStr('yn', val) )}/>
          <Table.Column title="IsKey" dataIndex="keyYN" width="70px" render={(val) => ( this.method.getFieldStr('yn', val) )}/>
          <Table.Column title="IsSql" dataIndex="sqlYN" width="70px" render={(val) => ( this.method.getFieldStr('yn', val) )}/>
        </Table>
      </div>
    );
  }
}

const WrappedCopySearch = Form.create({name:'copy_search'})(CopySearch)
export default CopyList