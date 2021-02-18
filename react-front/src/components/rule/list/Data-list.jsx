import React from 'react'
import { Table, Button } from 'antd'
import { Form, Input, Select } from 'antd'
import moment from 'moment'

import RuleList, { RuleSearch } from './RuleList'
import SubModal from '@/components/SubModal'
import UsedData from './sub/UsedData'

import * as urmsc from '@/urm-utils'

const locale = urmsc.locale

class DataSearch extends RuleSearch {
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
            <Form.Item label={locale['label.dataId']}>{getFieldDecorator("id")(<Input size="small" className="search-id" />)}</Form.Item>
            <Form.Item label={locale['label.dataName']}>{getFieldDecorator("name")(<Input size="small" className="search-name" />)}</Form.Item>
            <Form.Item label={locale['label.dataType']}>
              {getFieldDecorator("type", {initialValue: ""})(<Select size="small" className="search-id">
                <Select.Option value="">ALL</Select.Option>
                {this.method.renderOpts("dataType")}
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

class DataList extends RuleList {
  method = {
    ...this.method,

    clickUsed: (id) => {
      let ref = this.refs.usedList
      let childState = {
        list: {dataId: {id: id}}
      }
      ref.setState({visible: true, childState: childState})
    }
  }

  render() {
    return (
      <div className="urm-list">
        <WrappedDataSearch  {...this.props} search={this.method.search} delete={this.method.clickDelete} canDelete={this.method.isPageDelete} />
        <Table className="table-striped"
            dataSource={this.state.items} pagination={false} bordered
            size="small" /*scroll={{ y: 500 }}*/ rowKey="id"
            rowSelection={this.rowSelection} onRow={this.onRow}>
          <Table.Column title={locale['label.id']} dataIndex="id" width="150px"/>
          <Table.Column title={locale['label.name']} dataIndex="name" ellipsis />
          <Table.Column title={locale['label.dataType']} dataIndex="type" width="100px" render={(val) => ( this.method.getTypeStr('dataType', val) )} />
          <Table.Column title={locale['label.registId']} dataIndex="regId" width="200px" ellipsis />
          <Table.Column title={locale['label.registDate']} dataIndex="regDate" width="200px" render={(val) => ( moment(val).format('YYYY-MM-DD HH:mm:ss') )} />
          {this.method.renderButton(
            <Table.Column className="operations" width="115px" render={(val) =>
              (<div>
                <Button icon="edit" onClick={e => { this.method.clickEdit(val.id) }} title={locale['label.modify']} />
                <Button icon="delete" type="danger" onClick={e => { this.method.clickDelete([val.id]) }} title={locale['label.delete']} disabled={!this.method.isPageDelete()} />
                <Button icon="interaction" onClick={e => { this.method.clickUsed(val.id) }} title="영향도" />
              </div>)}
            />
          )}
        </Table>
        
        <SubModal ref="usedList" width="900px" destroyOnClose>
          <UsedData key="list" isEditor={false} />
        </SubModal>
      </div>
    );
  }
}

const WrappedDataSearch = Form.create({name:'data_search'})(DataSearch)
export default DataList