import React from 'react'
import { Table, Pagination, Button } from 'antd'
import { Form, Input, Select, DatePicker, Checkbox } from 'antd'
import moment from 'moment'

import RuleList, { RuleSearch } from './RuleList'
import SubModal from '@/component/SubModal'
import RequestTransfer from './sub/RequestTransfer'
import SystemList from './System-list'
import BizList from '@/component/manage/list/BizCode-list'
import HistoryList from './RequestHistory-list'

import * as urmsc from '@/urm-utils'

const locale = urmsc.locale

class RequestSearch extends RuleSearch {
  method = {
    ...this.method,

    clickTransfer: e => {
      this.refs.trnEdit.setState({visible: true})
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

      let childState = {
        list: { onDbClick: callback }
      }
      ref.setState({visible: true, childState: childState})
    },

    setBizCode: (type) => {
      let ref = this.refs.bizList
      let form = this.props.form
      
      let callback = (biz) => {
        if (type === 'rcv') {
          form.setFieldsValue({rcvJobCodeId: biz.id})
        } else if (type === 'send') {
          form.setFieldsValue({sendJobCodeId: biz.id})
        }
        ref.setState({visible: false})
      }

      let childState = {
        list: { onDbClick: callback }
      }
      ref.setState({visible: true, childState: childState})
    },

    renderOpts: (key) => {
      return urmsc.getSubListByKey(this.props.codeList, 'kind', urmsc.CODEKEY[key])
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
            <Form.Item label={locale['label.interfaceId']}>{getFieldDecorator("interfaceId")(<Input size="small" className="search-id" />)}</Form.Item>
            <Form.Item label={locale['label.interfaceType']}>
              {getFieldDecorator("interfaceType", {initialValue: ""})(<Select size="small" className="search-id">
                <Select.Option value="">ALL</Select.Option>
                {this.method.renderOpts("infType")}
              </Select>)}
            </Form.Item>
            <Form.Item label={locale['label.requestId']}>{getFieldDecorator("id")(<Input size="small"  className="search-id" />)}</Form.Item>
            <Form.Item label={locale['label.requestName']}>{getFieldDecorator("name")(<Input size="small" className="search-name" />)}</Form.Item>
          </div>
          <div className="row">
            <Form.Item label={locale['label.sourceSystem']}>
              {getFieldDecorator("sendSystemId")(<Input size="small" readOnly allowClear className="search-id" onClick={e => { this.method.setSystem('send') }} />)}
            </Form.Item>
            <Form.Item label={locale['label.sndJobCode']}>
              {getFieldDecorator("sendJobCodeId")(<Input size="small" readOnly allowClear className="search-id" onClick={e => { this.method.setBizCode('send') }} />)}
            </Form.Item>
            <Form.Item label={locale['label.changeStatus']}>
              {getFieldDecorator('chgStat', {initialValue: ""})(<Select size="small" className="search-id">
                <Select.Option value="">ALL</Select.Option>
                {this.method.renderOpts("chgStat")}
              </Select>)}
            </Form.Item>
            <Form.Item label={locale['label.processStatus']}>
              {getFieldDecorator('processStat', {initialValue: ""})(<Select size="small" style={{width: "150px"}}>
                <Select.Option value="">ALL</Select.Option>
                {this.method.renderOpts("procStat")}
              </Select>)}
            </Form.Item>
          </div>
          <div className="row">
            <Form.Item label={locale['label.targetSystem']}>
              {getFieldDecorator("rcvSystemId")(<Input size="small" readOnly allowClear className="search-id" onClick={e => { this.method.setSystem('rcv') }} />)}
            </Form.Item>
            <Form.Item label={locale['label.rcvJobCode']}>
              {getFieldDecorator("rcvJobCodeId")(<Input size="small" readOnly allowClear className="search-id" onClick={e => { this.method.setBizCode('rcv') }} />)}
            </Form.Item>
            <Form.Item label={locale['label.lastChangeDate']}>{getFieldDecorator("chgDate", {
              initialValue: [moment().subtract(1, 'y'), moment()]
            })(<RangePicker size="small" style={{width: "220px"}} />)}</Form.Item>
            <div className="search-check">
              <Form.Item label="등록자">
                {getFieldDecorator("regId", {valuePropName: "checked", initialValue: this.props.canDelete()})(<Checkbox />)}
              </Form.Item>
              <Form.Item label="송신자">
                {getFieldDecorator("sendAdminId", {valuePropName: "checked", initialValue: this.props.canDelete()})(<Checkbox />)}
              </Form.Item>
              <Form.Item label="수신자">
                {getFieldDecorator("rcvAdminId", {valuePropName: "checked", initialValue: this.props.canDelete()})(<Checkbox />)}
              </Form.Item>
            </div>
            <Form.Item className="search-buttons">
              <Button icon="search" onClick={this.method.clickSearch} title={locale['label.search']} />
              <Button icon="plus" onClick={this.method.clickAdd} title={locale['label.add']} />
              <Button icon="switcher" onClick={this.method.clickTransfer} title="이관" />
              <Button icon="delete" type="danger" onClick={this.method.clickDelete} title={locale['label.delete']} disabled={!this.props.canDelete()} />
            </Form.Item>
          </div>
        </Form>

        <SubModal ref="sysList" width="1280px" bodyStyle={{height: 600, overflow: 'auto'}}>
          <SystemList key="list" path="system" codeList={this.props.codeList} onlySearch={true} />
        </SubModal>

        <SubModal ref="bizList" width="1240px">
          <BizList key="list" onlySearch={true} />
        </SubModal>

        <SubModal ref="trnEdit" width="1215px">
          <RequestTransfer codeList={this.props.codeList} authList={this.props.authList} />
        </SubModal>
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
        if (key === 'chgDate') {
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
        url: 'api/' + this.props.path,
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
    },
    
    viewHistory: (reqId) => {
      let childState = {
        list: { scparam: {id: reqId} }
      }
      this.refs.hisList.setState({visible: true, childState: childState})
    },
  }
  
  pageSizes = ['30', '50', '100']

  render() {
    const { paging } = this.state
    let form = null

    return (
      <div className="urm-list">
        <WrappedRequestSearch {...this.props} search={this.method.searchPage} delete={this.method.clickDelete} canDelete={this.method.isPageDelete}
          wrappedComponentRef={ref => { if (ref) form = ref.props.form }} />
        <Table className="table-striped"
            dataSource={this.state.items} pagination={false} bordered
            size="small" scroll={{ x: 1450, /*y: 700*/ }} rowKey="id" rowSelection={this.rowSelection}>
          <Table.Column title={locale['label.id']} dataIndex="id" width="145px" />
          <Table.Column title={locale['label.name']} dataIndex="name" width="180px" />
          <Table.Column title={locale['label.interfaceType']} dataIndex="interfaceType" render={(val) => ( this.method.getTypeStr('infType', val) )} />
          <Table.Column title={locale['label.changeStatus']} dataIndex="chgStat" render={(val) =>  ( this.method.getTypeStr('chgStat', val) )} />
          <Table.Column title={locale['label.processStatus']} dataIndex="processStat" render={(val) => ( this.method.getTypeStr('procStat', val) )} />
          <Table.Column title={locale['label.interfaceId']} dataIndex="interfaceId" width="120px"/>
          <Table.Column title={locale['label.sourceMethod']} dataIndex="sendSystemType" width="100px" render={(val) => ( this.method.getTypeStr('sysType', val) )}/>
          <Table.Column title={locale['label.targetMethod']} dataIndex="rcvSystemType" width="100px" render={(val) => ( this.method.getTypeStr('sysType', val) )}/>
          <Table.Column title={locale['label.sourceSystem']} dataIndex="sendSystem.name" width="110px"/>
          <Table.Column title={locale['label.targetSystem']} dataIndex="rcvSystem.name" width="110px"/>
          <Table.Column title={locale['label.lastChangeDate']} dataIndex="chgDate" width="150px" render={(val) => ( moment(val).format('YYYY-MM-DD HH:mm') )}/>/>
          <Table.Column className="operations" width="115px" render={(val) =>
            (<div>
              <Button icon="edit" onClick={e => { this.method.clickEdit(val.id) }} title={locale['label.modify']} />
              <Button icon="delete" type="danger" onClick={e => { this.method.clickDelete([val.id]) }} title={locale['label.delete']} disabled={!this.method.isPageDelete()} />
              <Button icon="history" onClick={e => { this.method.viewHistory([val.id]) }} title={locale['label.interfaceHistory']} />
            </div>)}
          />
        </Table>
        <Pagination defaultCurrent={paging.current} total={paging.total} className="urm-paging"
          showSizeChanger onShowSizeChange={(page, size) => { this.method.handlePageChange(page, size, form) }}
          pageSizeOptions={this.pageSizes} defaultPageSize={paging.size} onChange={(page, size) => { this.method.handlePageChange(page, size, form) }}/>

        <SubModal ref="hisList" width="1200px">
          <HistoryList key="list" path="request/history" codeList={this.props.codeList} />
        </SubModal>
      </div>
    );
  }
}

const WrappedRequestSearch = Form.create({name:'request_search'})(RequestSearch)
export default RequestList