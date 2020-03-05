import React from 'react'
import { Table, Pagination, Button } from 'antd'
import { Form, Input, Select, DatePicker } from 'antd'
import moment from 'moment'

import RuleList, { RuleSearch } from './RuleList'
import RuleListButton from './RuleListButton'
import SubModal from '../SubModal'
import SystemList from '../list/System-list'
import BizList from '../../manage/list/BusinessCode-list'

import * as urmsc from '../../../urm-utils'

const KINDS = urmsc.CODEKEY

class RequestSearch extends RuleSearch {
  method = {
    ...this.method,

    renderOpts: (key) => {
      return urmsc.getSubListByKey(this.props.codeList, 'kind', KINDS[key])
              .map((it) => <Select.Option key={it.code} value={it.code}>{it.name}</Select.Option>)
    },
    
    setSystem: (type) => {
      let ref = this.refs.sysList
      let form = this.props.form
      
      let callback = (system) => {
        if (type === 'rcv') {
          form.setFieldsValue({rcvSystemId: system.id})
        } else if (type === 'send') {
          form.setFieldsValue({sendSystemId: system.id})
        }
        ref.setState({visible: false})
      }

      ref.setState({visible: true})
      ref.method.changeChildState({onDbClick: callback})
    },

    setBizCode: (type) => {
      let ref = this.refs.bizList
      let form = this.props.form
      
      urmsc.ajax({
        type: 'GET',
        url: '/URM/code/business',
        success: function(res) {
          let callback = (biz) => {
            if (type === 'rcv') {
              form.setFieldsValue({rcvJobCodeId: biz.id})
            } else if (type === 'send') {
              form.setFieldsValue({sendJobCodeId: biz.id})
            }
            ref.setState({visible: false})
          }
          
          ref.setState({visible: true})
          ref.method.changeChildState({items: res, onDbClick: callback})
        }
      })
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
            <Form.Item label="SendSystemID">
              {getFieldDecorator("sendSystemId")(<Input size="small" readOnly allowClear className="search-id" onClick={e => { this.method.setSystem('send') }} />)}
            </Form.Item>
            <Form.Item label="SourceBizCode">
              {getFieldDecorator("sendJobCodeId")(<Input size="small" readOnly allowClear className="search-id" onClick={e => { this.method.setBizCode('send') }} />)}
            </Form.Item>
            <Form.Item label="ChangeState">
              {getFieldDecorator('chgStat', {initialValue: ""})(<Select size={"small"} className="search-id">
                <Select.Option value="">ALL</Select.Option>
                {this.method.renderOpts("chgStat")}
              </Select>)}
            </Form.Item>
            <Form.Item label="ProcessState">
              {getFieldDecorator('processStat', {initialValue: ""})(<Select size={"small"} style={{width: "150px"}}>
                <Select.Option value="">ALL</Select.Option>
                {this.method.renderOpts("procStat")}
              </Select>)}
            </Form.Item>
          </div>
          <div className="row">
            <Form.Item label="ReceiveSystemID">
              {getFieldDecorator("rcvSystemId")(<Input size="small" readOnly allowClear className="search-id" onClick={e => { this.method.setSystem('rcv') }} />)}
            </Form.Item>
            <Form.Item label="TargetBizCode">
              {getFieldDecorator("rcvJobCodeId")(<Input size="small" readOnly allowClear className="search-id" onClick={e => { this.method.setBizCode('rcv') }} />)}
            </Form.Item>
            <Form.Item label="ChangeDate">{getFieldDecorator("chgDate", {
              initialValue: [moment().subtract(1, 'y'), moment()]
            })(<RangePicker size="small" style={{width: "220px"}} />)}</Form.Item>
            <Form.Item className="search-buttons row">
              <Button onClick={this.method.clickSearch} icon="search" />
              {this.method.renderButton(
                <div className="inline">
                  <Button onClick={this.method.clickAdd} icon="plus" />
                  <Button icon="delete" />
                </div>
              )}
            </Form.Item>
          </div>
        </Form>

        <SubModal ref="sysList" width="1110px"
          render={() => (<SystemList ref="child" path="system" codeList={this.props.codeList} />)}>
          <SystemList ref="child" path="system" codeList={this.props.codeList} onlySearch={true} />
        </SubModal>

        <SubModal ref="bizList" width="980px"
          render={() => (<BizList ref="child" />)} />
      </div>
    );
  }
}

class RequestList extends RuleList {
  constructor(props) {
    super(props)
    this.state.paging = {
      current: 1,
      size: 30,
      total: 1,
    }
  }
  
  method = {
    ...this.method,
    
    searchPage: (param, paging) => {
      if (!param) param = {}
      if (!paging) paging = this.state.paging
      
      /* parse moment for Date */
      for (let key in param) {
        if (key.endsWith('Date')) {
          let arr = [
            param[key][0].valueOf(),
            param[key][1].valueOf()
          ]
          param[key] = arr
        }
      }

      /* set page parameter */
      param = {
        ...param,
        page: paging.current,
        size: paging.size,
      }
      
      let $this = this
      urmsc.ajax({
        type: 'GET',
        url: '/URM/' + this.props.path,
        data: param,
        success: function(res) {
          $this.setState({
            items: res.list,
            paging: {
              current: res.curPage,
              size: paging.size,
              total: res.totalCount,
            }
          })
        },
      })
    },
    
    handlePageChange: (page, size, form) => {
      let paging = {current: page, size: size}
      this.method.searchPage(form.getFieldsValue(), paging)
    }
  }

  render() {
    const { paging } = this.state
    let form = null

    return (
      <div className="urm-list">
        <WrappedRequestSearch {...this.props} search={this.method.searchPage} wrappedComponentRef={ref => { if (ref) form = ref.props.form }} />
        <Table className="table-striped"
          dataSource={this.state.items} pagination={false} bordered
          size={"small"} scroll={{ y: 500 }} rowKey="id">
          <Table.Column title="ID" dataIndex="id" width="130px" />
          <Table.Column title="Name" dataIndex="name" width="180px" />
          <Table.Column title="Type" dataIndex="interfaceType" render={(val) => ( this.method.getTypeStr(KINDS.infType, val) )} />
          <Table.Column title="ReqState" dataIndex="chgStat" render={(val) =>  ( this.method.getTypeStr(KINDS.chgStat, val) )} />
          <Table.Column title="State" dataIndex="processStat" render={(val) => ( this.method.getTypeStr(KINDS.procStat, val) )} />
          <Table.Column title="InterfaceID" dataIndex="interfaceId" width="120px"/>
          <Table.Column title="SendMethod" dataIndex="sendSystemType" width="100px" render={(val) => ( this.method.getTypeStr(KINDS.sysType, val) )}/>
          <Table.Column title="rcvMethod" dataIndex="rcvSystemType" width="100px" render={(val) => ( this.method.getTypeStr(KINDS.sysType, val) )}/>
          <Table.Column title="SendSystem" dataIndex="sendSystem.name" width="110px"/>
          <Table.Column title="rcvSystem" dataIndex="rcvSystem.name" width="110px"/>
          <Table.Column title="LastUpdate" dataIndex="chgDate" width="150px" render={(val) => ( moment(val).format('YYYY-MM-DD HH:mm') )}/>/>
          <Table.Column title="Operations" className="operations" width="100px" render={(val) =>
            (<RuleListButton edit={e => { this.method.clickEdit(val.id) }} delete={e => { this.method.clickDelete([val.id]) }} />)} />
        </Table>
        <Pagination defaultCurrent={paging.current} total={paging.total} className="urm-paging"
          showSizeChanger onShowSizeChange={(page, size) => { this.method.handlePageChange(page, size, form) }}
          pageSizeOptions={['30', '50', '100']} defaultPageSize={paging.size} onChange={(page, size) => { this.method.handlePageChange(page, size, form) }}/>
      </div>
    );
  }
}

const WrappedRequestSearch = Form.create({name:'request_search'})(RequestSearch)
export default RequestList