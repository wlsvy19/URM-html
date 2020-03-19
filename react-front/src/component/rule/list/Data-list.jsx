import React from 'react'
import { Table, Button } from 'antd'
import { Form, Input, Select } from 'antd'
import moment from 'moment'

import RuleList, { RuleSearch } from './RuleList'
import ResizeableTitle from '../ResizeableTitle'
import SubModal from '../SubModal'
import UsedData from './sub/UsedData'

import * as urmsc from '../../../urm-utils'

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
              {this.method.renderButton(
                <div className="inline">
                  <Button icon="plus" onClick={this.method.clickAdd} />
                  <Button icon="delete" onClick={this.method.clickDelete} />
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
 state = {
    colWidth: [
      250, 275, 275, 275
    ],
  }

  components = {
    header: {
      cell: ResizeableTitle,
    },
  }
  
  method = {
    ...this.method,

    handleResize: index => (e, prop) => {
      console.log(prop)
      if (prop) {
      let { size } = prop
      this.setState(({ colWidth }) => {
        const nextColumns = [...colWidth]
        nextColumns[index] = size.width
        return { colWidth: nextColumns }
      })
      }
      return false
    },
    
    clickUsed: (id) => {
      let ref = this.refs.usedList
      let childState = {
        list: {scparam: {id: id}}
      }
      ref.setState({visible: true, childState: childState})
    }
  }

  render() {
    const { colWidth } = this.state

    return (
      <div className="urm-list">
        <WrappedDataSearch  {...this.props} search={this.method.search} delete={this.method.clickDelete} />
        <Table className="table-striped" components={this.components}
            dataSource={this.state.items} pagination={false} bordered
            size={"small"} scroll={{ y: 500 }} rowKey="id"
            rowSelection={this.rowSelection} onRow={this.onRow}>
          <Table.Column title="ID" dataIndex="id" width="150px"/>
          <Table.Column title="Name" dataIndex="name" width={colWidth[0]} />
          <Table.Column title="Type" dataIndex="type" width={colWidth[1]} render={(val) => ( this.method.getTypeStr('dataType', val) )} />
          <Table.Column title="Register" dataIndex="regId" width={colWidth[2]}/>
          <Table.Column title="RegDate" dataIndex="regDate" width={colWidth[3]} render={(val) => ( moment(val).format('YYYY-MM-DD HH:mm') )} />
          {this.method.renderButton(
            <Table.Column title="Operations" className="operations" width="120px" render={(val) =>
              (<div>
                <Button icon="edit" onClick={e => { this.method.clickEdit(val.id) }} />
                <Button icon="delete" onClick={e => { this.method.clickDelete([val.id]) }} />
                <Button icon="interaction" onClick={e => { this.method.clickUsed(val.id) }} />
              </div>)}
            />
          )}
        </Table>
        
        <SubModal ref="usedList" width="900px">
          <UsedData key="list" />
        </SubModal>
      </div>
    );
  }
}

const WrappedDataSearch = Form.create({name:'data_search'})(DataSearch)
export default DataList