import React from 'react'
import { Table, Pagination, Button, Popover } from 'antd'
import { Form, Input, Select, DatePicker, Checkbox } from 'antd'
import moment from 'moment'

import RuleList, { RuleSearch } from './RuleList'
import SubModal from '@/components/SubModal'
import RequestTransfer from './sub/RequestTransfer'
import SystemList from './System-list'
import BizList from '@/components/manage/list/BizCode-list'
import UserList from '@/components/manage/list/User-list'
import HistoryList from './RequestHistory-list'

import * as urmsc from '@/urm-utils'

const locale = urmsc.locale

class RequestSearch extends RuleSearch {
  state = {
    ...this.state,
    viewPop: false
  }

  componentDidMount() {
    this.props.search(this.method.makeParams())
    
    let $el = document.querySelectorAll('.advanced-search-bar input.ant-input')
    let _this = this
    $el.forEach((it) => {
      it.addEventListener('keydown', function(e) {
        if (e && e.keyCode === 13) {
          _this.props.search(_this.method.makeParams())
        }
      })
    })
  }

  method = {
    ...this.method,

    clickSearch: e => {
      this.props.search(this.method.makeParams())
    },

    makeParams: () => {
      let form = this.props.form
      let fields = form.getFieldsValue()
      let param = {}
      for (let key in fields) {
        if (key.endsWith('RegId') || key.endsWith('AdminId')) {
          continue
        }
        param[key] = fields[key]
      }
      param.regId = fields.cRegId ? 'checked true' : fields.sRegId
      param.sendAdminId = fields.cSendAdminId ? 'checked true' : fields.sSendAdminId
      param.rcvAdminId = fields.cRcvAdminId ? 'checked true' : fields.sRcvAdminId
      return param
    },

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

    setUser: (type, _this) => {
      let ref = _this.refs.usrList
      let form = _this.props.form
      
      let callback = (user) => {
        _this.setState({viewPop: true})
        if (type === 'rcv') {
          form.setFieldsValue({sRcvAdminId: user.id})
        } else if (type === 'send') {
          form.setFieldsValue({sSendAdminId: user.id})
        } else if (type === 'reg') {
          form.setFieldsValue({sRegId: user.id})
        }
        ref.setState({visible: false})
      }
      
      let childState = {
        list: { onDbClick: callback }
      }
      ref.setState({visible: true, childState: childState})
    },

    handleVisibleChange: visible => {
      this.setState({viewPop: visible})
    },

    renderOpts: (key) => {
      return urmsc.getSubListByKey(this.props.codeList, 'kind', urmsc.CODEKEY[key])
              .map((it) => <Select.Option key={it.code} value={it.code}>{it.name}</Select.Option>)
    },
    
    renderSearchUser: () => {
      const { getFieldDecorator } = this.props.form
      let auth = this.props.userInfo ? this.props.userInfo.auth : undefined
      let contents = <Form colon={false}>
        <div className="row">
          <Form.Item label="등록자">
            {getFieldDecorator("sRegId")(<Input size="small" style={{width: 135}} allowClear readOnly
              onClick={e => { this.method.setUser('reg', this) }} />)}
          </Form.Item>
          <Form.Item label="송신자" style={{marginLeft: 10}}>
            {getFieldDecorator("sSendAdminId")(<Input size="small" style={{width: 135}} allowClear readOnly
             onClick={e => { this.method.setUser('send', this) }} />)}
          </Form.Item>
          <Form.Item label="수신자" style={{marginLeft: 10}}>
            {getFieldDecorator("sRcvAdminId")(<Input size="small" style={{width: 135}} allowClear readOnly
             onClick={e => { this.method.setUser('rcv', this) }} />)}
          </Form.Item>
        </div>
      </Form>
      return auth === '0' &&
        <Popover placement="topRight" visible={this.state.viewPop} title="사용자 검색" trigger="click"
            content={contents} onVisibleChange={this.method.handleVisibleChange} className="urm-popover">
          <Button size="small" icon="usergroup-add" style={{width: 28}} title="사용자 검색" />
        </Popover>
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
            <Form.Item className="search-buttons">
              <Button size="small" onClick={this.method.clickTransfer}>이관</Button>
            </Form.Item>
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
            <Form.Item className="search-buttons">
              <Button size="small" onClick={this.method.clickAdd}>{locale['label.add']}</Button>
              <Button size="small" type="danger" onClick={this.method.clickDelete} disabled={!this.props.canDelete()}>{locale['label.delete']}</Button>
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
                {getFieldDecorator("cRegId", {valuePropName: "checked", initialValue: !this.props.canDelete()})(<Checkbox />)}
              </Form.Item>
              <Form.Item label="송신자">
                {getFieldDecorator("cSendAdminId", {valuePropName: "checked", initialValue: !this.props.canDelete()})(<Checkbox />)}
              </Form.Item>
              <Form.Item label="수신자">
                {getFieldDecorator("cRcvAdminId", {valuePropName: "checked", initialValue: !this.props.canDelete()})(<Checkbox />)}
              </Form.Item>
            </div>
            <Form.Item className="search-buttons">
              {this.method.renderSearchUser()}
              <Button size="small" onClick={this.method.clickSearch}>{locale['label.search']}</Button>
            </Form.Item>
          </div>
        </Form>

        <SubModal ref="sysList" width="1300px" bodyStyle={{maxHeight: 600, overflow: 'auto'}}>
          <SystemList key="list" path="system" codeList={this.props.codeList} onlySearch={true} />
        </SubModal>

        <SubModal ref="bizList" width="1240px" bodyStyle={{maxHeight: 600, overflow: 'auto'}}>
          <BizList key="list" onlySearch={true} />
        </SubModal>

        <SubModal ref="usrList" width="1080px" zIndex="1031" bodyStyle={{maxHeight: 600, overflow: 'auto'}}>
          <UserList key="list" path="user" authList={this.props.authList} onlySearch={true} />
        </SubModal>

        <SubModal ref="trnEdit" width="1215px" destroyOnClose>
          <RequestTransfer codeList={this.props.codeList} authList={this.props.authList} userInfo={this.props.userInfo} />
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
      
      let _this = this
      urmsc.ajax({
        type: 'GET',
        url: 'api/' + this.props.path,
        data: param,
        success: function(res) {
          _this.setState({
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
    
    handlePageChange: (page, size) => {
      let form = this.searchForm
      let paging = {current: page, size: size}
      this.method.searchPage(form.getFieldsValue(), paging)
    },
    
    viewHistory: (reqId) => {
      let childState = {
        list: { reqId: {id: reqId} }
      }
      this.refs.hisList.setState({visible: true, childState: childState})
    },
  }
  
  chgStatColor = (val) => {
    let color = {color: '#fff'}
    if (val === '1') {
      color = {color: '#0000FF'}
    } else if (val === '2') {
      color = {color: '#9900FF'}
    } else if (val === '3') {
      color = {color: '#FF0000'}
    } else {
      color = {color: '#990000'}
    }
    return color
  }
  
  pageSizes = ['15', '30', '50', '100']

  searchForm = null

  render() {
    const { paging } = this.state

    return (
      <div className="urm-list">
        <WrappedRequestSearch {...this.props} search={this.method.searchPage} delete={this.method.clickDelete} canDelete={this.method.isPageDelete}
          wrappedComponentRef={ref => { if (ref) this.searchForm = ref.props.form }} />
        <Table className="table-striped"
            dataSource={this.state.items} pagination={false} bordered
            size="small" scroll={{ x: 1450, /*y: 700*/ }} rowKey="id" rowSelection={this.rowSelection}>
          <Table.Column title={locale['label.id']} dataIndex="id" width="145px" />
          <Table.Column title={locale['label.changeStatus']} dataIndex="chgStat" width="115px" render={(val) =>  ( <span style={this.chgStatColor(val)}>{this.method.getTypeStr('chgStat', val)}</span> )} />
          <Table.Column title={locale['label.processStatus']} dataIndex="processStat" width="130px" render={(val) => ( this.method.getTypeStr('procStat', val) )} />
          <Table.Column title={locale['label.interfaceType']} dataIndex="interfaceType" width="80px" render={(val) => ( this.method.getTypeStr('infType', val) )} />
          <Table.Column title={locale['label.name']} dataIndex="name" ellipsis />
          <Table.Column title={locale['label.interfaceId']} dataIndex="interfaceId" width="135px"/>
          <Table.Column title={locale['label.sourceMethod']} dataIndex="sendSystemType" width="85px" render={(val) => ( this.method.getTypeStr('sysType', val) )}/>
          <Table.Column title={locale['label.targetMethod']} dataIndex="rcvSystemType" width="85px" render={(val) => ( this.method.getTypeStr('sysType', val) )}/>
          <Table.Column title={locale['label.sourceSystem']} dataIndex="sendSystem.name" width="110px" ellipsis />
          <Table.Column title={locale['label.targetSystem']} dataIndex="rcvSystem.name" width="110px" ellipsis />
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
          showSizeChanger onShowSizeChange={this.method.handlePageChange}
          pageSizeOptions={this.pageSizes} defaultPageSize={paging.size} onChange={this.method.handlePageChange}/>

        <SubModal ref="hisList" width="1200px" destroyOnClose>
          <HistoryList key="list" path="request/history" codeList={this.props.codeList} />
        </SubModal>
      </div>
    );
  }
}

const WrappedRequestSearch = Form.create({name:'request_search'})(RequestSearch)
export default RequestList