import React from 'react'
import { Table, Button, DatePicker } from 'antd'
import { Form, Input, Select } from 'antd'
//import { Resizable } from 'react-resizable'

import RuleList, { RuleSearch } from './RuleList'
import RuleListButton from './RuleListButton'
import {default as urmUtils} from '../../../urm-utils'

const KINDS = urmUtils.codeKey

class RequestSearch extends RuleSearch {
  method = {
    ...this.method,

    renderOpts: (key) => {
      return urmUtils.getSubListByKey(this.props.codeList, 'kind', KINDS[key])
              .map((it) => <Select.Option key={it.code} value={it.code}>{it.name}</Select.Option>)
    },
  }

  render() {
    const { RangePicker } = DatePicker
    const { getFieldDecorator } = this.props.form

    return (
      <div className="advanced-search-bar">
        <Form colon={false}>
          <div className="row">
            <Form.Item label="Interface ID">{getFieldDecorator("interfaceId")(<Input size="small" className="search-id" />)}</Form.Item>
            <Form.Item label="Type">
              {getFieldDecorator("interfaceType", {initialValue: ""})(<Select size={"small"} className="search-id">
                <Select.Option value="">ALL</Select.Option>
                {this.method.renderOpts("infType")}
              </Select>)}
            </Form.Item>
            <Form.Item label="Request ID">{getFieldDecorator("id")(<Input size="small"  className="search-id" />)}</Form.Item>
            <Form.Item label="Request Name">{getFieldDecorator("name")(<Input size="small" className="search-name" />)}</Form.Item>
          </div>
          <div className="row">
            <Form.Item label="SendSystemID">{getFieldDecorator("sendSystemId")(<Input.Search size="small" readOnly className="search-id" />)}</Form.Item>
            <Form.Item label="SourceBizCode">{getFieldDecorator("sendJobCodeId")(<Input.Search size="small" readOnly className="search-id" />)}</Form.Item>
            <Form.Item label="ProcessState">
              {getFieldDecorator('processStat', {initialValue: ""})(<Select size={"small"} style={{width: "150px"}}>
                <Select.Option value="">ALL</Select.Option>
                {this.method.renderOpts("procStat")}
              </Select>)}
            </Form.Item>
            <Form.Item label="ChangeState">
              {getFieldDecorator('chgStat', {initialValue: ""})(<Select size={"small"} className="search-id">
                <Select.Option value="">ALL</Select.Option>
                {this.method.renderOpts("chgStat")}
              </Select>)}
            </Form.Item>
          </div>
          <div className="row">
            <Form.Item label="ReceiveSystemID">{getFieldDecorator("rcvSystemId")(<Input.Search size="small" readOnly className="search-id" />)}</Form.Item>
            <Form.Item label="TargetBizCode">{getFieldDecorator("rcvJobCodeId")(<Input.Search size="small" readOnly className="search-id" />)}</Form.Item>
            <Form.Item label="ChangeDate">{getFieldDecorator("chgDate")(<RangePicker size="small" style={{width: "220px"}} />)}</Form.Item>
            <Form.Item className="search-buttons">
              <Button onClick={this.method.clickSearch} icon="search" />
              <Button onClick={this.method.clickAdd} icon="plus" />
            </Form.Item>
          </div>
        </Form>
      </div>
    );
  }
}

class RequestList extends RuleList {
  render() {
    /* const ResizableColumn = props => {
      const { onResize, width, ...restProps } = props
      if (!width) {
        return <th {...restProps} />
      }
      return (
        <Resizable width={width} height={0} onResize={onResize} draggableOpts={{enableUserSelectHack: false}}>
          <th {...restProps} />
        </Resizable>
      );
    } */
    
    return (
      <div className="urm-list">
        <Table className="table-striped"
          dataSource={this.state.items} pagination={false} bordered
          size={"small"} scroll={{ y: 500 }} rowKey="id">
          <Table.Column title="ID" dataIndex="id" width="130px"/>
          <Table.Column title="Name" dataIndex="name" width="180px"/>
          <Table.Column title="Type" dataIndex="interfaceType" render={(val) => ( this.method.getTypeStr(KINDS.infType, val) )}/>
          <Table.Column title="ReqState" dataIndex="chgStat" render={(val) =>  ( this.method.getTypeStr(KINDS.chgStat, val) )}/>
          <Table.Column title="State" dataIndex="processStat" render={(val) => ( this.method.getTypeStr(KINDS.procStat, val) )}/>
          <Table.Column title="InterfaceID" dataIndex="interfaceId" width="120px"/>
          <Table.Column title="SendMethod" dataIndex="sendSystemType" width="100px" render={(val) => ( this.method.getTypeStr(KINDS.sysType, val) )}/>
          <Table.Column title="rcvMethod" dataIndex="rcvSystemType" width="100px" render={(val) => ( this.method.getTypeStr(KINDS.sysType, val) )}/>
          <Table.Column title="SendSystem" dataIndex="sendSystem.name" width="110px"/>
          <Table.Column title="rcvSystem" dataIndex="rcvSystem.name" width="110px"/>
          <Table.Column title="Operations" width="100px" render={(val) =>
            (<RuleListButton edit={e => { this.method.clickEdit(val.id) }} />)} />
        </Table>
      </div>
    );
  }
}

const WrappedRequestSearch = Form.create({name:'request_search'})(RequestSearch)
export default RequestList
export {WrappedRequestSearch}