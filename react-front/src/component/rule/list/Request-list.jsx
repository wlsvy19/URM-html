import React from 'react'
import { Table, Button, DatePicker } from 'antd'
import { Form, Input, Select } from 'antd'
//import { Resizable } from 'react-resizable'

import RuleListButton from './RuleListButton'
import {default as urmUtils} from '../../../urm-utils'

const KINDS = urmUtils.codeKey

class RequestSearch extends React.Component {
  componentDidMount() {
    this.props.search()
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
    const { RangePicker } = DatePicker
    const { getFieldDecorator } = this.props.form

    return (
      <div className="advanced-search-bar">
        <div className="row">
          <Form.Item label="Interface ID" colon={false}>{getFieldDecorator("interfaceId")(<Input size="small" className="search-id" />)}</Form.Item>
          <Form.Item label="Request ID" colon={false}>{getFieldDecorator("id")(<Input size="small"  className="search-id" />)}</Form.Item>
          <Form.Item label="Request Name" colon={false}>{getFieldDecorator("name")(<Input size="small" className="search-name" />)}</Form.Item>
          <Form.Item label="Type" colon={false}>
            {getFieldDecorator("interfaceType", {initialValue: ""})(<Select size={"small"} className="search-id">
              <Select.Option value="">ALL</Select.Option>
              {this.method.renderOpts("infType")}
            </Select>)}
          </Form.Item>
        </div>
        <div className="row">
          <Form.Item label="SendSystemID" colon={false}>{getFieldDecorator("sendSystemId")(<Input.Search size="small" readOnly className="search-id-icon" />)}</Form.Item>
          <Form.Item label="SourceBizCode" colon={false}>{getFieldDecorator("sendJobCodeId")(<Input.Search size="small" readOnly className="search-id-icon" />)}</Form.Item>
          <Form.Item label="ReceiveSystemID" colon={false}>{getFieldDecorator("rcvSystemId")(<Input.Search size="small" readOnly className="search-id-icon" />)}</Form.Item>
          <Form.Item label="TargetBizCode" colon={false}>{getFieldDecorator("rcvJobCodeId")(<Input.Search size="small" readOnly className="search-id-icon" />)}</Form.Item>
        </div>
        <div className="row">
          <Form.Item label="ProcessState" colon={false}>
            {getFieldDecorator('processStat', {initialValue: ""})(<Select size={"small"} style={{width: "150px"}}>
              <Select.Option value="">ALL</Select.Option>
              {this.method.renderOpts("procStat")}
            </Select>)}
          </Form.Item>
          <Form.Item label="ChangeState" colon={false}>
            {getFieldDecorator('chgStat', {initialValue: ""})(<Select size={"small"} className="search-id">
              <Select.Option value="">ALL</Select.Option>
              {this.method.renderOpts("chgStat")}
            </Select>)}
          </Form.Item>
          <Form.Item label="ChangeDate" colon={false}>{getFieldDecorator("chgDate")(<RangePicker size="small" style={{width: "220px"}} />)}</Form.Item>
          <Form.Item className="search-buttons">
            <Button onClick={this.method.clickSearch} icon="search" />
            <Button onClick={this.method.clickAdd} icon="plus" />
          </Form.Item>
        </div>
      </div>
    );
  }
}

class RequestList extends React.Component {
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
        <Table dataSource={this.state.items} pagination={false} bordered size={"small"} scroll={{ y: 500 }} className="table-striped">
          <Table.Column title="ID" dataIndex="id" width="130px"/>
          <Table.Column title="Name" dataIndex="name"/>
          <Table.Column title="Type" dataIndex="interfaceType" render={(val) => ( this.method.getTypeStr(KINDS.infType, val) )}/>
          <Table.Column title="ReqState" dataIndex="chgStat" render={(val) =>  ( this.method.getTypeStr(KINDS.chgStat, val) )}/>
          <Table.Column title="State" dataIndex="processStat" render={(val) => ( this.method.getTypeStr(KINDS.procStat, val) )}/>
          <Table.Column title="InterfaceID" dataIndex="interfaceId" width="120px"/>
          <Table.Column title="SendMethod" dataIndex="sendSystemType" width="100px" render={(val) => ( this.method.getTypeStr(KINDS.sysType, val) )}/>
          <Table.Column title="rcvMethod" dataIndex="rcvSystemType" width="100px" render={(val) => ( this.method.getTypeStr(KINDS.sysType, val) )}/>
          <Table.Column title="SendSystem" dataIndex="sendSystemId" width="110px"/>
          <Table.Column title="rcvSystem" dataIndex="rcvSystemId" width="110px"/>
          <Table.Column title="Operations" width="100px" render={(val) => (
            <RuleListButton edit={e => { this.method.clickEdit(val.id) }} />
          )}/>
        </Table>
      </div>
    );
  }
}

const WrappedRequestSearch = Form.create({name:'request_search'})(RequestSearch)
export default RequestList
export {WrappedRequestSearch}