import React from 'react'
import { Button, Table } from 'antd'
import { Form, Input, Select } from 'antd'
import moment from 'moment'

import * as urmsc from '@/urm-utils'

const locale = urmsc.locale

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
            <Form.Item label={locale['label.dataId']}>{getFieldDecorator("id")(<Input size="small" className="search-id" />)}</Form.Item>
            <Form.Item label={locale['label.dataName']}>{getFieldDecorator("name")(<Input size="small" className="search-name" />)}</Form.Item>
            <Form.Item label={locale['label.dataType']}>
              {getFieldDecorator("type", {initialValue: ""})(<Select size="small" className="search-id">
                <Select.Option value="">ALL</Select.Option>
                {this.method.renderOpts("dataType")}
              </Select>)}
            </Form.Item>
            <Form.Item className="search-buttons">
              <Button icon="search" onClick={this.method.clickSearch} title={locale['label.search']} />
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
        url: 'api/data',
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
        url: 'api/data/field/' + record.id,
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
          size="small" scroll={{ y: 300 }} rowKey="id"
          onRow={(record, index) => {
            return {
              onClick: e => { this.method.viewFields(record) },
              onDoubleClick: e => { if (this.props.onDbClick) this.props.onDbClick(record) }
            }
          }
        }>
          <Table.Column title={locale['label.id']} dataIndex="id" width="150px"/>
          <Table.Column title={locale['label.name']} dataIndex="name" width="250px"/>
          <Table.Column title={locale['label.dataType']} dataIndex="type" render={(val) => ( this.method.getTypeStr('dataType', val) )} />
          <Table.Column title={locale['label.registId']} dataIndex="regId"/>
          <Table.Column title={locale['label.registDate']} dataIndex="regDate" render={(val) => ( moment(val).format('YYYY-MM-DD HH:mm') )} />
        </Table>
        
        <hr />
        
        <Table className="table-striped"
          dataSource={this.state.fields} pagination={false} bordered
          size="small" scroll={{ y: 300 }} rowKey="sno">
          <Table.Column title={locale['label.index']} dataIndex="sno" width="65px" align="center"/>
          <Table.Column title={locale['label.fieldName']} dataIndex="engName" width="150px"/>
          <Table.Column title={locale['label.fieldLocalName']} dataIndex="name" width="150px"/>
          <Table.Column title={locale['label.fieldType']} dataIndex="type" width="110px" render={(val) => ( this.method.getFieldStr('type', val) )}/>
          <Table.Column title={locale['label.dFormat']} dataIndex="dateFormat"/>
          <Table.Column title={locale['label.len']} dataIndex="length"/>
          <Table.Column title={locale['label.nullable']} dataIndex="nullable" width="75px" render={(val) => ( this.method.getFieldStr('yn', val) )}/>
          <Table.Column title={locale['label.isKey']} dataIndex="keyYN" width="85px" render={(val) => ( this.method.getFieldStr('yn', val) )}/>
          <Table.Column title={locale['label.isSQL']} dataIndex="sqlYN" width="110px" render={(val) => ( this.method.getFieldStr('yn', val) )}/>
        </Table>
      </div>
    );
  }
}

const WrappedCopySearch = Form.create({name:'copy_search'})(CopySearch)
export default CopyList